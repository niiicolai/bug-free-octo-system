package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.repository.WishRepository;

@Controller
public class WishController {

	private WishRepository wishRepository;

	public WishController(WishRepository wishRepository) {
		this.wishRepository = wishRepository;
	}
    

}
