package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.sql.SQLDataException;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.model.User;

@Controller
public class UserController {

	private UserRepository userRepository;
	private WishlistRepository wishlistRepository;

	public UserController(UserRepository userRepository, WishlistRepository wishlistRepository) {
		this.userRepository = userRepository;
		this.wishlistRepository = wishlistRepository;
	}

	@RequestMapping(value = {"/users", "/"}, method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "user/index";
	}

	@GetMapping("/users/{id}")
	public String show(Model model, @PathVariable("id") long id) {
		model.addAttribute("user", userRepository.findOne(id));
		model.addAttribute("wishlists", wishlistRepository.findWhere("user_id", id));
		return "user/show";
	}

	@GetMapping("/users/new")
	public String instantiate(Model model) {
		model.addAttribute("user", userRepository.instantiate());
		return "user/new";
	}

	@GetMapping("/users/{id}/edit")
	public String edit(Model model, @PathVariable("id") long id) {
		model.addAttribute("user", userRepository.findOne(id));
		return "user/edit";
	}

	@PostMapping("/users")
	public String create(Model model, User user, RedirectAttributes redirectAttributes) {
		try {
			user = userRepository.insert(user);
			return "redirect:users/" + user.getId();
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:users/new";
		}
	}

	@PatchMapping("/users")
	public String update(Model model, User user, RedirectAttributes redirectAttributes) {
		try {
			userRepository.update(user);
			return "redirect:users/" + user.getId();
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:users/"  + user.getId() + "/edit";
		}		
	}
	
	@DeleteMapping("/users")
	public String delete(Model model, User user, RedirectAttributes redirectAttributes) {
		try {
			userRepository.delete("id", user.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return "redirect:users";
	}
}
