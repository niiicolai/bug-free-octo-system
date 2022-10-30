package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.Wishlist;
import com.example.demo.model.WishlistShare;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.WishlistShareRepository;

@Controller
public class WishlistShareController {

    private WishlistRepository wishlistRepository;
	private WishlistShareRepository wishlistShareRepository;

	public WishlistShareController(
		WishlistRepository wishlistRepository, 
        WishlistShareRepository wishlistShareRepository) {

		this.wishlistRepository = wishlistRepository;
		this.wishlistShareRepository = wishlistShareRepository;
	}

    @PostMapping("/edit-wishlist-shares")
	public String create(Model model, WishlistShare wishlistShare, RedirectAttributes redirectAttributes) {
		Wishlist wishlist = wishlistRepository.findOne(wishlistShare.getWishlistId());
		
		if (blockModifyingOtherWishlists(wishlist.getId()))
			return "redirect:/users";

        try {
			wishlistShare = wishlistShareRepository.insert(wishlistShare);
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

        return "redirect:wishlists/" + wishlistShare.getWishlistId();
	}

    @DeleteMapping("/edit-wishlist-shares")
	public String delete(Model model, WishlistShare wishlistShare, RedirectAttributes redirectAttributes) {
		Wishlist wishlist = wishlistRepository.findOne(wishlistShare.getWishlistId());
		
		if (blockModifyingOtherWishlists(wishlist.getId()))
			return "redirect:/users";

		try {
			wishlistShareRepository.delete("wishlist_id", wishlistShare.getWishlistId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return "redirect:wishlists/" + wishlistShare.getWishlistId();
	}

	private boolean blockModifyingOtherWishlists(long wishlistId) {
		Wishlist wishlist = wishlistRepository.findOne(wishlistId);
		return CustomUserDetails.notAllowed(wishlist.getUserId());
	}
}
