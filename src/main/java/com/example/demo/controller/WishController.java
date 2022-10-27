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

import com.example.demo.repository.WishRepository;
import com.example.demo.model.Wish;

@Controller
public class WishController {

	private WishRepository wishRepository;

	public WishController(WishRepository wishRepository) {
		this.wishRepository = wishRepository;
	}

	@GetMapping("/wishes/new")
	public String instantiate(Model model) {
		model.addAttribute("wish", wishRepository.instantiate());
		return "wish/new";
	}

	@GetMapping("/wishes/{id}/edit")
	public String edit(Model model, @PathVariable("id") long id) {
		model.addAttribute("wish", wishRepository.findOne(id));
		return "wish/edit";
	}

	@PostMapping("/wishes")
	public String create(Model model, Wish wish, RedirectAttributes redirectAttributes) {
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
		try {
			wishRepository.delete("id", wish.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return "redirect:wishlists/" + wish.getWishlistId();
	}
}
