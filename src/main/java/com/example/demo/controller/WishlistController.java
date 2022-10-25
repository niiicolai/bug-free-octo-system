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

    
}