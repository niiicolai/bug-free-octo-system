package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.User;

@Controller
public class UserController {

	private UserRepository userRepository;
	private WishlistRepository wishlistRepository;

	public UserController(UserRepository userRepository, WishlistRepository wishlistRepository) {
		this.userRepository = userRepository;
		this.wishlistRepository = wishlistRepository;
	}

	@GetMapping("/users")
	public String show(Model model, @AuthenticationPrincipal CustomUserDetails authUser) {
		long id = authUser.getId();

		model.addAttribute("user", userRepository.findOne(id));
		model.addAttribute("wishlists", wishlistRepository.findWhere("user_id", id));
		return "user/show";
	}

	@GetMapping("/users/new")
	public String instantiate(Model model) {
		model.addAttribute("user", userRepository.instantiate());
		return "user/new";
	}

	@GetMapping("/users/edit")
	public String edit(Model model, @AuthenticationPrincipal CustomUserDetails authUser) {
		long id = authUser.getId();

		model.addAttribute("user", userRepository.findOne(id));
		return "user/edit";
	}

	@PostMapping("/users/create")
	public String create(Model model, User user, RedirectAttributes redirectAttributes) {
		try {
            user.encodePassword();
			user = userRepository.insert(user);
			
			return "redirect:users";
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:/users/new";
		}
	}

	@PatchMapping("/users")
	public String update(Model model, User user, RedirectAttributes redirectAttributes) {
		if (user.notAuthorizeExisting())
			return "redirect:/";
		
		try {
			user.encodePassword();
			userRepository.update(user);
			return "redirect:users";
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:/users/edit";
		}		
	}
	
	@DeleteMapping("/users")
	public String delete(Model model, User user, RedirectAttributes redirectAttributes) {
		if (user.notAuthorizeExisting())
			return "redirect:/";
		
		try {
			userRepository.delete("id", user.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return "redirect:/login";
	}
}
