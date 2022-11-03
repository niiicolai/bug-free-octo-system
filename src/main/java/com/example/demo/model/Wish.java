package com.example.demo.model;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.CustomImplementation.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public class Wish {
    
    private long id;
    private long wishlistId;
    private String content;
    private LocalDateTime createdAt;
    private WishReserver wishReserver;

    public Wish() {
    }

    public Wish(long id, long wishlistId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }

    public long getId() {
        return this.id;
    }

    public void setWishlistId(long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public long getWishlistId() {
        return this.wishlistId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setWishReserver(WishReserver wishReserver) {
        this.wishReserver = wishReserver;
    }

    public WishReserver getWishReserver() {
        return this.wishReserver;
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
