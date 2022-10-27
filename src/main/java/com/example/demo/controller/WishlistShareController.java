package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.demo.model.WishlistShare;
import com.example.demo.repository.WishRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.WishlistShareRepository;

@Controller
public class WishlistShareController {

    private WishRepository wishRepository;
    private WishlistRepository wishlistRepository;
	private WishlistShareRepository wishlistShareRepository;

	public WishlistShareController(WishlistRepository wishlistRepository, 
        WishlistShareRepository wishlistShareRepository, WishRepository wishRepository) {
		this.wishlistRepository = wishlistRepository;
		this.wishlistShareRepository = wishlistShareRepository;
        this.wishRepository = wishRepository;
	}

    @GetMapping("/wishlist-shares/{uuid}")
	public String show(Model model, @PathVariable("uuid") String uuid) {

        Iterable<WishlistShare> wishlistShares = wishlistShareRepository.findWhere("uuid", uuid);
		WishlistShare wishlistShare = wishlistShares.iterator().next();
        long id = wishlistShare.getWishlistId();
        model.addAttribute("wishlistShare", wishlistShare);
		model.addAttribute("wishlist", wishlistRepository.findOne(id));
		model.addAttribute("wishes", wishRepository.findWhere("wishlist_id", id));
		return "wishlist/show";
	}

    @PostMapping("/wishlist-shares")
	public String create(Model model, WishlistShare wishlistShare, RedirectAttributes redirectAttributes) {

        try {
			wishlistShare = wishlistShareRepository.insert(wishlistShare);
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

        return "redirect:wishlists/" + wishlistShare.getWishlistId();
	}

    @DeleteMapping("/wishlist-shares")
	public String delete(Model model, WishlistShare wishlistShare, RedirectAttributes redirectAttributes) {
		try {
			wishlistShareRepository.delete("wishlist_id", wishlistShare.getWishlistId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return "redirect:wishlists/" + wishlistShare.getWishlistId();
	}
}
