package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.CustomImplementation.CrudRepository;

public class WishlistShare {
    
    private long wishlistId;
    private String uuid;
    private LocalDateTime createdAt;

    public WishlistShare() {
    }

    public WishlistShare(long wishlistId, String uuid, LocalDateTime createdAt) {
        this.wishlistId = wishlistId;
        this.uuid = uuid;
        this.createdAt = createdAt;
    }

    public long getWishlistId() {
        return this.wishlistId;
    }

    public void setWishlistId(long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean notAuthorizeNew(WishlistRepository repository) {
        return notAuthorizeExisting(repository);
    }

    public boolean notAuthorizeExisting(WishlistRepository repository) {
        Wishlist list = repository.findOne(wishlistId);
        return CustomUserDetails.AuthenticatedUser().getId() != list.getUserId();
    }
}
