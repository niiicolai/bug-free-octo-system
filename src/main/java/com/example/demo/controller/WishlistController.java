package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.demo.repository.WishlistRepository;
import com.example.demo.model.Wishlist;

@Controller
public class WishlistController {

	private WishlistRepository wishlistRepository;

	public WishlistController(WishlistRepository wishlistRepository) {
		this.wishlistRepository = wishlistRepository;
	}

    @GetMapping("/wishlists")
	public String index(Model model) {
		model.addAttribute("wishlists", wishlistRepository.findAll());
		return "wishlist/index";
	}

	@GetMapping("/wishlists/{id}")
	public String show(Model model, @PathVariable("id") long id) {
		model.addAttribute("wishlist", wishlistRepository.findOne(id));
		return "wishlist/show";
	}

	@GetMapping("/wishlists/new")
	public String instantiate(Model model) {
		model.addAttribute("wishlist", wishlistRepository.instantiate());
		return "wishlist/new";
	}

	@GetMapping("/wishlists/{id}/edit")
	public String edit(Model model, @PathVariable("id") long id) {
		model.addAttribute("wishlist", wishlistRepository.findOne(id));
		return "wishlist/edit";
	}

	@PostMapping("/wishlists")
	public String create(Model model, @RequestBody Wishlist wishlist, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("id", wishlist.getId());

		try {
			wishlistRepository.save(wishlist);
			return "redirect:show";
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getCause());
			return "redirect:instantiate";
		}
	}

	@PatchMapping("/wishlists")
	public String update(Model model, @RequestBody Wishlist wishlist, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("id", wishlist.getId());

		try {
			wishlistRepository.save(wishlist);
			return "redirect:show";
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getCause());
			return "redirect:edit";
		}		
	}
	
	@DeleteMapping("/wishlists")
	public String delete(Model model, @RequestBody Wishlist wishlist, RedirectAttributes redirectAttributes) {
		try {
			wishlistRepository.delete(wishlist.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getCause());
		}

		return "redirect:index";
	}
}