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
import com.example.demo.repository.WishRepository;
import com.example.demo.model.Wishlist;

@Controller
public class WishlistController {

	private WishlistRepository wishlistRepository;
	private WishRepository wishRepository;

	public WishlistController(WishlistRepository wishlistRepository, WishRepository wishRepository) {
		this.wishlistRepository = wishlistRepository;
		this.wishRepository = wishRepository;
	}

	@GetMapping("/wishlists/{id}")
	public String show(Model model, @PathVariable("id") long id) {
		model.addAttribute("wishlist", wishlistRepository.findOne(id));
		model.addAttribute("wishes", wishRepository.findWhere("wishlist_id", id));
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
	public String create(Model model, Wishlist wishlist, RedirectAttributes redirectAttributes) {
		try {
			wishlist = wishlistRepository.save(wishlist);
			return "redirect:wishlists/" + wishlist.getId();
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:wishlists/new";
		}
	}

	@PatchMapping("/wishlists")
	public String update(Model model, Wishlist wishlist, RedirectAttributes redirectAttributes) {
		try {
			System.out.println(wishlist);
			wishlistRepository.save(wishlist);
			return "redirect:wishlists/" + wishlist.getId();
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:wishlists/" + wishlist.getId() + "/edit";
		}		
	}
	
	@DeleteMapping("/wishlists")
	public String delete(Model model, Wishlist wishlist, RedirectAttributes redirectAttributes) {
		try {
			wishlistRepository.delete(wishlist.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return "redirect:users/" + wishlist.getUserId();
	}
}