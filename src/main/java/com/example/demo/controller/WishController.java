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
    
	
}
