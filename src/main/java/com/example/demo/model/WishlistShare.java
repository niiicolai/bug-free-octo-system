package com.example.demo.model;

public class WishlistShare {
    
    private long wishlistId;
    private String uuid;

    public WishlistShare() {
    }

    public WishlistShare(long wishlistId, String uuid) {
        this.wishlistId = wishlistId;
        this.uuid = uuid;
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

}
