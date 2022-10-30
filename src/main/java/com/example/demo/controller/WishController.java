package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.WishRepository;
import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.Wish;
import com.example.demo.model.Wishlist;

@Controller
public class WishController {

	private WishRepository wishRepository;
	private WishlistRepository wishlistRepository;

	public WishController(WishRepository wishRepository, WishlistRepository wishlistRepository) {
		this.wishRepository = wishRepository;
		this.wishlistRepository = wishlistRepository;
	}

	@GetMapping("/wishes/new")
	public String instantiate(Model model) {
		model.addAttribute("wish", wishRepository.instantiate());
		return "wish/new";
	}

	@GetMapping("/wishes/{id}/edit")
	public String edit(Model model, @PathVariable("id") long id) {
		if (blockModifyingOthers(id))
			return "redirect:/users";

		model.addAttribute("wish", wishRepository.findOne(id));
		return "wish/edit";
	}

	@PostMapping("/wishes")
	public String create(Model model, Wish wish, RedirectAttributes redirectAttributes) {
		if (blockTransferingOthers(wish.getWishlistId()))
			return "redirect:/users";
		
		try {
			wish = wishRepository.insert(wish);
			return "redirect:wishlists/" + wish.getWishlistId();
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:wishes/new";
		}
	}

	@PatchMapping("/wishes")
	public String update(Model model, Wish wish, RedirectAttributes redirectAttributes) {
		if (blockModifyingOthers(wish.getId()) || blockTransferingOthers(wish.getWishlistId()))
			return "redirect:/users";

		try {
			wishRepository.update(wish);
			return "redirect:wishlists/" + wish.getWishlistId();
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:wishes/"  + wish.getId() + "/edit";
		}		
	}
	
	@DeleteMapping("/wishes")
	public String delete(Model model, Wish wish, RedirectAttributes redirectAttributes) {
		if (blockModifyingOthers(wish.getId()))
			return "redirect:/users";

		try {
			wishRepository.delete("id", wish.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return "redirect:wishlists/" + wish.getWishlistId();
	}

	private boolean blockModifyingOthers(long wishId) {
		Wish wish = wishRepository.findOne(wishId);
		return blockTransferingOthers(wish.getWishlistId());
	}

	private boolean blockTransferingOthers(long wishlistId) {
		Wishlist wishlist = wishlistRepository.findOne(wishlistId);
		return CustomUserDetails.notAllowed(wishlist.getUserId());
	}
}
