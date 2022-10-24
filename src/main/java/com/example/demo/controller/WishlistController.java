package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.repository.WishlistRepository;

@Controller
public class WishlistController {

	private WishlistRepository wishlistRepository;

	public WishlistController(WishlistRepository wishlistRepository) {
		this.wishlistRepository = wishlistRepository;
	}

    
}