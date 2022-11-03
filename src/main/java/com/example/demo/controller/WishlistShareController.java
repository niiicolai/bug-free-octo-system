package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.model.Wish;
import com.example.demo.model.Wishlist;
import com.example.demo.model.WishlistShare;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.WishlistShareRepository;

@Controller
public class WishlistShareController {

    private WishRepository wishRepository;
	private WishlistShareRepository wishlistShareRepository;
    private WishlistRepository wishlistRepository;
    private UserRepository userRepository;

	public WishlistShareController(
        WishlistShareRepository wishlistShareRepository,
		WishlistRepository wishlistRepository, 
		WishRepository wishRepository,
        UserRepository userRepository) {

		this.wishlistShareRepository = wishlistShareRepository;
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
	}

	@GetMapping("/wishlist-shares/{uuid}")
	public String show(Model model, @PathVariable("uuid") String uuid) {

        Iterable<WishlistShare> wishlistShares = wishlistShareRepository.findWhere("uuid", uuid);
		WishlistShare wishlistShare = wishlistShares.iterator().next();
		long wishlistId = wishlistShare.getWishlistId();
		List<Wish> wishes = (List<Wish>) wishRepository.findWhereJoin("wishlist_id", wishlistId, "wish_reservers", "wish_id");
		double reservedCount = wishRepository.countWhereJoin("wishlist_id", wishlistId, "wish_reservers", "wish_id");
		Wishlist wishlist = wishlistRepository.findOne(wishlistId);
		User user = userRepository.findOne(wishlist.getUserId());
		boolean canEdit = CustomUserDetails.AuthenticatedUser() != null &&
						  CustomUserDetails.AuthenticatedUser().getId() == user.getId();

		model.addAttribute("canEdit", canEdit);
		model.addAttribute("user", user);
        model.addAttribute("wishlistShare", wishlistShare);
		model.addAttribute("wishlist", wishlist);
		model.addAttribute("wishes", wishes);
		model.addAttribute("reservePercent", (int)(reservedCount/wishes.size()*100));
		return "wishlist/show";
	}	

    @PostMapping("/edit-wishlist-shares")
	public String create(Model model, WishlistShare wishlistShare, RedirectAttributes redirectAttributes) throws Exception {
        if (wishlistShare.notAuthorizeNew(wishlistRepository))
			return "redirect:/";

			wishlistShare = wishlistShareRepository.insert(wishlistShare);

		return String.format("redirect:wishlists/%d", wishlistShare.getWishlistId());
	}

    @DeleteMapping("/edit-wishlist-shares")
	public String delete(Model model, WishlistShare wishlistShare, RedirectAttributes redirectAttributes) {
		if (wishlistShare.notAuthorizeExisting(wishlistRepository))
			return "redirect:/";
		
		try {
			wishlistShareRepository.delete("wishlist_id", wishlistShare.getWishlistId());
		} catch (Exception e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}

		return String.format("redirect:wishlists/%d", wishlistShare.getWishlistId());
	}
}
