package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.demo.repository.WishlistShareRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishRepository;
import com.example.demo.model.WishlistShare;
import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.Wishlist;

@Controller
public class WishlistController {

	private WishlistShareRepository wishlistShareRepository;
	private WishlistRepository wishlistRepository;
	private WishRepository wishRepository;
	private UserRepository userRepository;

	public WishlistController(
		WishlistRepository wishlistRepository, 
		WishRepository wishRepository, 
		WishlistShareRepository wishlistShareRepository,
		UserRepository userRepository) {

		this.wishlistRepository = wishlistRepository;
		this.wishRepository = wishRepository;
		this.wishlistShareRepository = wishlistShareRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/wishlists/{id}")
	public String show(Model model, @PathVariable("id") long id) {
		boolean cannotEdit = blockModifyingOthers(id);
		if (cannotEdit)
			return "redirect:/users";
		
		Wishlist wishlist = wishlistRepository.findOne(id);
		model.addAttribute("canEdit", !cannotEdit);	
		model.addAttribute("wishlist", wishlist);
		model.addAttribute("wishlistShare", wishlistShareRepository.findOne(id));
		model.addAttribute("wishes", wishRepository.findWhere("wishlist_id", id));
		model.addAttribute("user", userRepository.findOne(wishlist.getUserId()));
		return "wishlist/show";
	}

	@GetMapping("/wishlists/new")
	public String instantiate(Model model, @AuthenticationPrincipal CustomUserDetails authUser) {
		model.addAttribute("userId", authUser.getId());
		model.addAttribute("wishlist", wishlistRepository.instantiate());
		return "wishlist/new";
	}

	@GetMapping("/wishlists/{id}/edit")
	public String edit(Model model, @PathVariable("id") long id) {
		if (blockModifyingOthers(id))
			return "redirect:/users";

		model.addAttribute("wishlist", wishlistRepository.findOne(id));
		return "wishlist/edit";
	}

	@PostMapping("/wishlists")
	public String create(Model model, Wishlist wishlist, RedirectAttributes redirectAttributes) {
		if (blockTransferingOthers(wishlist.getUserId()))
			return "redirect:/users";

		try {
			wishlist = wishlistRepository.insert(wishlist);
			return "redirect:wishlists/" + wishlist.getId();
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:wishlists/new";
		}
	}

	@PatchMapping("/wishlists")
	public String update(Model model, Wishlist wishlist, RedirectAttributes redirectAttributes) {
		if (blockModifyingOthers(wishlist.getId()) || blockTransferingOthers(wishlist.getUserId()))
			return "redirect:/users";
		
		try {
			wishlistRepository.update(wishlist);
			return "redirect:wishlists/" + wishlist.getId();
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:wishlists/" + wishlist.getId() + "/edit";
		}		
	}
	
	@DeleteMapping("/wishlists")
	public String delete(Model model, Wishlist wishlist, RedirectAttributes redirectAttributes) {
		if (blockModifyingOthers(wishlist.getId()))
			return "redirect:/users";
		
		try {
			wishlistRepository.delete("id", wishlist.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return "redirect:/users";
	}

	private boolean blockModifyingOthers(long wishlistId) {
		Wishlist wishlist = wishlistRepository.findOne(wishlistId);
		return blockTransferingOthers(wishlist.getUserId());
	}

	private boolean blockTransferingOthers(long userId) {
		return CustomUserDetails.notAllowed(userId);
	}
}