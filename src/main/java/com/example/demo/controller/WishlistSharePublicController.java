package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.model.Wish;
import com.example.demo.model.Wishlist;
import com.example.demo.model.WishlistShare;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.WishlistShareRepository;

import org.springframework.ui.Model;

@Controller
public class WishlistSharePublicController {

    private WishRepository wishRepository;
	private WishlistShareRepository wishlistShareRepository;
	private WishlistRepository wishlistRepository;
	private UserRepository userRepository;

    public WishlistSharePublicController(
		WishlistRepository wishlistRepository, 
        WishlistShareRepository wishlistShareRepository, 
		WishRepository wishRepository,
		UserRepository userRepository) {

		this.wishlistRepository = wishlistRepository;
		this.wishlistShareRepository = wishlistShareRepository;
        this.wishRepository = wishRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/wishlist-shares/{uuid}")
	public String show(Model model, @PathVariable("uuid") String uuid) {

        Iterable<WishlistShare> wishlistShares = wishlistShareRepository.findWhere("uuid", uuid);
		WishlistShare wishlistShare = wishlistShares.iterator().next();
		long wishlistId = wishlistShare.getWishlistId();
		Wishlist wishlist = wishlistRepository.findOne(wishlistId);
		User user = userRepository.findOne(wishlist.getUserId());

		model.addAttribute("canEdit", !CustomUserDetails.notAllowed(user.getId()));
		model.addAttribute("user", user);
        model.addAttribute("wishlistShare", wishlistShare);
		model.addAttribute("wishlist", wishlist);
		model.addAttribute("wishes", wishRepository.findWhere("wishlist_id", wishlistId));
		return "wishlist/show";
	}	

    @GetMapping("/wishlist-shares/{uuid}/wish/{id}")
	public String editReserve(Model model, @PathVariable("uuid") String uuid, @PathVariable("id") long id) {

		/* Don't allow to edit the wish without uuid and if the wish is already reserved. */
		Wish wish = wishRepository.findOne(id);
		Iterable<WishlistShare> wishlistShares = wishlistShareRepository.findWhere("uuid", uuid);
		WishlistShare wishlistShare = wishlistShares.iterator().next();
		if (wishlistShare.getWishlistId() != wish.getWishlistId() || wish.getReservedBy().length() > 0)
			return "redirect:/wishlist-shares/" + uuid;
		
		model.addAttribute("wishlistShare", wishlistShare);	
		model.addAttribute("wish", wish);
		return "wish/reserve";
	}

	@PatchMapping("/wishlist-shares/{uuid}/wish/{id}")
	public String updateReserve(Model model, Wish wish, @PathVariable("uuid") String uuid,
			RedirectAttributes redirectAttributes) {
		
		/* Don't allow to update the wish without uuid and if the wish is already reserved. */
		Wish currentWish = wishRepository.findOne(wish.getId());		
		Iterable<WishlistShare> wishlistShares = wishlistShareRepository.findWhere("uuid", uuid);
		WishlistShare wishlistShare = wishlistShares.iterator().next();
		if (wishlistShare.getWishlistId() != currentWish.getWishlistId() || currentWish.getReservedBy().length() > 0)
			return "redirect:/wishlist-shares/" + uuid;
		
		/* Don't allow to override the content or wishlist id */
		wish.setContent(currentWish.getContent());
		wish.setWishlistId(currentWish.getWishlistId());
		
		try {
			wishRepository.update(wish);
			return "redirect:/wishlist-shares/" + uuid;
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:/wishlist-shares/" + uuid + "/wish/" + wish.getId();
		}		
	}
}
