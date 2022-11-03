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
import com.example.demo.model.Wish;

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
		Wish wish = wishRepository.findOne(id);

		if (wish.notAuthorizeExisting(wishlistRepository))
			return "redirect:/users";

		model.addAttribute("wish", wish);
		return "wish/edit";
	}

	@PostMapping("/wishes")
	public String create(Model model, Wish wish, RedirectAttributes redirectAttributes) {
		if (wish.notAuthorizeNew(wishlistRepository))
			return "redirect:/";
		
		try {
			wish = wishRepository.insert(wish);
			return String.format("redirect:wishlists/%d", wish.getWishlistId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:wishes/new";
		}
	}

	@PatchMapping("/wishes")
	public String update(Model model, Wish wish, RedirectAttributes redirectAttributes) {
		if (wish.notAuthorizeExisting(wishlistRepository))
			return "redirect:/";
		
		try {
			wishRepository.update(wish);
			return String.format("redirect:wishlists/%d", wish.getWishlistId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return String.format("redirect:wishes/%d/edit", wish.getId());
		}		
	}
	
	@DeleteMapping("/wishes")
	public String delete(Model model, Wish wish, RedirectAttributes redirectAttributes) {
		if (wish.notAuthorizeExisting(wishlistRepository))
			return "redirect:/";
		
		try {
			wishRepository.delete("id", wish.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return String.format("redirect:wishlists/%d", wish.getWishlistId());
	}
}
