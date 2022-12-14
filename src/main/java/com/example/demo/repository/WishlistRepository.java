package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

import com.example.demo.model.Wishlist;
import com.example.demo.repository.CustomImplementation.CrudRepository;

@Repository
public class WishlistRepository extends CrudRepository<Wishlist> {

    public WishlistRepository() {
        /*
         * Specify the wishlists table.
         */
        super("Wishlists", "id", false);
    }

    /*
     * Instantiates an empty wishlist instance,
     * and returns it.
     */
    @Override
    public Wishlist instantiate() {
        return new Wishlist();
    }

    /*
     * Instantiates a new wishlist instance based on a Map collection,
     * and returns it.
     */
    @Override
    protected Wishlist instantiate(Map<String, Object> result) {
        Wishlist wishlist = instantiate();
        wishlist.setId(((Number)result.get("id")).longValue());
        wishlist.setTitle((String)result.get("title"));
        wishlist.setUserId(((Number)result.get("user_id")).longValue());
        wishlist.setCreatedAt((LocalDateTime)result.get("created_at"));
        return wishlist;
    }

    /*
     * Instantiates a collection of wishlists based on a LinkedList
     * containing Map Collections, and returns the collection
     * as an iterable object of entities.
     */
    @Override
    protected Iterable<Wishlist> instantiateCollection(LinkedList<Map<String, Object>> resultList) {
        LinkedList<Wishlist> collection = new LinkedList<Wishlist>();
        for (var result : resultList) {
            collection.add(instantiate(result));
        }
        return collection;
    }

    @Override
    protected Iterable<Wishlist> instantiateCollectionWithRelation(LinkedList<Map<String, Object>> resultList) {
        return new LinkedList<Wishlist>();
    }

    /*
     * Returns a wishlist's properties as a Map collection.
     */
    @Override
    protected Map<String, Object> properties(Wishlist entity) {
        HashMap<String, Object> collection = new HashMap<String, Object>();
        collection.put("id", entity.getId());
        collection.put("title", entity.getTitle());
        collection.put("user_id", entity.getUserId());
        return collection; 
    }
}
