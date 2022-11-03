package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.repository.WishRepository;
import com.example.demo.repository.WishlistRepository;

public class WishReserver {
    
    private long wishId;
    private String fullname;
    private LocalDateTime createdAt;

    public WishReserver() {
    }

    public WishReserver(long wishId, String fullname, LocalDateTime createdAt) {
        this.wishId = wishId;
        this.fullname = fullname;
        this.createdAt = createdAt;
    }

    public long getWishId() {
        return wishId;
    }

    public void setWishId(long wishId) {
        this.wishId = wishId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean notAuthorizeExisting(WishlistRepository wishlistRepository, WishRepository wishRepository) {
        Wish wish = wishRepository.findOne(wishId);
        Wishlist list = wishlistRepository.findOne(wish.getWishlistId());
        return CustomUserDetails.AuthenticatedUser().getId() != list.getUserId();
    }
}
