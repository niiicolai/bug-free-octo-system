package com.example.demo.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.Wish;
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
		Wishlist wishlist = wishlistRepository.findOne(id);
		List<Wish> wishes = (List<Wish>) wishRepository.findWhereJoin("wishlist_id", id, "wish_reservers", "wish_id");
		double reservedCount = wishRepository.countWhereJoin("wishlist_id", id, "wish_reservers", "wish_id");

		if (wishlist.notAuthorizeExisting(wishlistRepository))
			return "redirect:/users";
		
		model.addAttribute("canEdit", true);	
		model.addAttribute("wishlist", wishlist);
		model.addAttribute("wishlistShare", wishlistShareRepository.findOne(id));
		model.addAttribute("wishes", wishes);
		model.addAttribute("reservePercent", (int)(reservedCount/wishes.size()*100));
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
		Wishlist wishlist = wishlistRepository.findOne(id);

		if (wishlist.notAuthorizeExisting(wishlistRepository))
			return "redirect:/users";

		model.addAttribute("wishlist", wishlist);
		return "wishlist/edit";
	}

	@PostMapping("/wishlists")
	public String create(Model model, Wishlist wishlist, RedirectAttributes redirectAttributes) {
		if (wishlist.notAuthorizeNew())
			return "redirect:/";
		
		try {
			wishlist = wishlistRepository.insert(wishlist);
			return String.format("redirect:wishlists/%d", wishlist.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:wishlists/new";
		}
	}

	@PatchMapping("/wishlists")
	public String update(Model model, Wishlist wishlist, RedirectAttributes redirectAttributes) {
		if (wishlist.notAuthorizeExisting(wishlistRepository))
			return "redirect:/";
		
		try {
			wishlistRepository.update(wishlist);
			return String.format("redirect:wishlists/%d", wishlist.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return String.format("redirect:wishlists/%d/edit", wishlist.getId());
		}		
	}
	
	@DeleteMapping("/wishlists")
	public String delete(Model model, Wishlist wishlist, RedirectAttributes redirectAttributes) {
		if (wishlist.notAuthorizeExisting(wishlistRepository))
			return "redirect:/";
		
		try {
			wishlistRepository.delete("id", wishlist.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return "redirect:/users";
	}
}