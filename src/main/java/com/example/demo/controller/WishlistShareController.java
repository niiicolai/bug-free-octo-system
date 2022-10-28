package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.model.Wish;
import com.example.demo.model.Wishlist;
import com.example.demo.model.WishlistShare;
import com.example.demo.repository.WishRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.WishlistShareRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class WishlistShareController {

    private WishRepository wishRepository;
    private WishlistRepository wishlistRepository;
	private WishlistShareRepository wishlistShareRepository;
	private UserRepository userRepository;

	public WishlistShareController(
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

    @PostMapping("/wishlist-shares")
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

    @DeleteMapping("/wishlist-shares")
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

	@GetMapping("/wishlist-shares-reserve/{uuid}/wish/{id}")
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

	@PatchMapping("/wishlist-shares-reserve/{uuid}/wish/{id}")
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
			return "redirect:/wishlist-shares/" + uuid + "/reserve-wish/" + wish.getId();
		}		
	}

	private boolean blockModifyingOtherWishlists(long wishlistId) {
		Wishlist wishlist = wishlistRepository.findOne(wishlistId);
		return CustomUserDetails.notAllowed(wishlist.getUserId());
	}
}
