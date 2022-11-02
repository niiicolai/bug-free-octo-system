package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import com.example.demo.model.WishlistShare;
import com.example.demo.repository.CustomImplementation.CrudRepository;

@Repository
public class WishlistShareRepository extends CrudRepository<WishlistShare> {

    public WishlistShareRepository() {
        /*
         * Specify the wishlist shares table.
         */
        super("Wishlist_shares", "wishlist_id", true);
    }

    /*
     * Instantiates an empty wishlist share instance,
     * and returns it.
     */
    @Override
    public WishlistShare instantiate() {
        return new WishlistShare();
    }

    /*
     * Instantiates a new wishlist share instance based on a Map collection,
     * and returns it.
     */
    @Override
    protected WishlistShare instantiate(Map<String, Object> result) {
        WishlistShare wishlistShare = instantiate();
        wishlistShare.setWishlistId(((Number)result.get("wishlist_id")).longValue());
        wishlistShare.setUuid((String)result.get("uuid"));
        return wishlistShare;
    }

    /*
     * Instantiates a collection of wishlist shares based on a LinkedList
     * containing Map Collections, and returns the collection
     * as an iterable object of entities.
     */
    @Override
    protected Iterable<WishlistShare> instantiateCollection(LinkedList<Map<String, Object>> resultList) {
        LinkedList<WishlistShare> collection = new LinkedList<WishlistShare>();
        for (var result : resultList) {
            collection.add(instantiate(result));
        }
        return collection;
    }

    /*
     * Returns a wishlist shares's properties as a Map collection.
     */
    @Override
    protected Map<String, Object> properties(WishlistShare entity) {
        HashMap<String, Object> collection = new HashMap<String, Object>();
        collection.put("wishlist_id", entity.getWishlistId());
        return collection; 
    }
}
