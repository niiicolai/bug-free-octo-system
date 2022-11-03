package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Wish;
import com.example.demo.model.WishReserver;
import com.example.demo.model.WishlistShare;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishRepository;
import com.example.demo.repository.WishReserverRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.WishlistShareRepository;

@Controller
public class WishReserverController {

    private WishRepository wishRepository;
	private WishlistShareRepository wishlistShareRepository;
    private WishlistRepository wishlistRepository;
    private WishReserverRepository wishReserverRepository;

    public WishReserverController(
        WishlistShareRepository wishlistShareRepository,
		WishlistRepository wishlistRepository, 
		WishRepository wishRepository,
        WishReserverRepository wishReserverRepository) {

		this.wishlistShareRepository = wishlistShareRepository;
        this.wishRepository = wishRepository;
        this.wishReserverRepository = wishReserverRepository;
        this.wishlistRepository = wishlistRepository;
	}
    
    @GetMapping("/wish-reservers/{uuid}/wish/{id}")
	public String instantiate(Model model, @PathVariable("uuid") String uuid, @PathVariable("id") long id) {

		/* Don't allow to edit the wish without uuid and if the wish is already reserved. */
		Wish wish = wishRepository.findOne(id);
		Iterable<WishlistShare> wishlistShares = wishlistShareRepository.findWhere("uuid", uuid);
		WishlistShare wishlistShare = wishlistShares.iterator().next();
		if (wishlistShare.getWishlistId() != wish.getWishlistId())// || wish.getReservedBy().length() > 0)
			return String.format("redirect:/wishlist-shares/%s", uuid);
		
		model.addAttribute("wishlistShare", wishlistShare);	
		model.addAttribute("wish", wish);
        model.addAttribute("wishReserver", wishReserverRepository.findOne(wish.getId()));
		return "wish_reserver/new";
	}

	@PostMapping("/wish-reservers/{uuid}/wish/{id}")
	public String create(Model model, WishReserver wishReserver, @PathVariable("uuid") String uuid,
			RedirectAttributes redirectAttributes) {
		
		/* Don't allow to update the wish without uuid and if the wish is already reserved. */
		Wish currentWish = wishRepository.findOne(wishReserver.getWishId());		
		Iterable<WishlistShare> wishlistShares = wishlistShareRepository.findWhere("uuid", uuid);
		WishlistShare wishlistShare = wishlistShares.iterator().next();
		if (wishlistShare.getWishlistId() != currentWish.getWishlistId())// || currentWish.getReservedBy().length() > 0)
			return String.format("redirect:/wishlist-shares/%s", uuid);
		
		try {
			wishReserverRepository.insert(wishReserver);
			return String.format("redirect:/wishlist-shares/%s", uuid);
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
			return String.format("redirect:/wish-reservers/%s/wish/%d", uuid, currentWish.getId());
		}		
	}

    @DeleteMapping("/edit-wish-reservers")
	public String delete(Model model, WishReserver wishReserver, RedirectAttributes redirectAttributes) {
		if (wishReserver.notAuthorizeExisting(wishlistRepository, wishRepository))
			return "redirect:/";
		
        Wish currentWish = wishRepository.findOne(wishReserver.getWishId());	

		try {
			wishReserverRepository.delete("wish_id", wishReserver.getWishId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return String.format("redirect:wishlists/%d", currentWish.getWishlistId());
	}
}
