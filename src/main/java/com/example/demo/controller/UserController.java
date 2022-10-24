package com.example.demo.controller;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;

@Controller
public class UserController {

	private UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

    @GetMapping("/users")
	public String index(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "index";
	}

	@GetMapping("/users/{id}")
	public String show(Model model, @PathVariable("id") long id) {
		model.addAttribute("user", userRepository.findOne(id));
		return "show";
	}

	@GetMapping("/users/new")
	public String instantiate(Model model) {
		model.addAttribute("user", userRepository.instantiate());
		return "user/new";
	}

	@GetMapping("/users/{id}/edit")
	public String edit(Model model, @PathVariable("id") long id) {
		model.addAttribute("user", userRepository.findOne(id));
		return "edit";
	}

	@PostMapping("/users")
	public String create(Model model, @RequestBody User user, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("id", user.getId());

		try {
			userRepository.save(user);
			return "redirect:show";
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getCause());
			return "redirect:instantiate";
		}
	}

	@PatchMapping("/users")
	public String update(Model model, @RequestBody User user, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("id", user.getId());

		try {
			userRepository.save(user);
			return "redirect:show";
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getCause());
			return "redirect:edit";
		}		
	}
	
	@DeleteMapping("/users")
	public String delete(Model model, @RequestBody User user, RedirectAttributes redirectAttributes) {
		try {
			userRepository.delete(user.getId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getCause());
		}

		return "redirect:index";
	}
}
