package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import com.example.demo.model.Wish;
import com.example.demo.repository.CustomImplementation.CrudRepository;

@Repository
public class WishRepository extends CrudRepository<Wish> {

    public WishRepository() {
        /*
         * Specify the wishes table.
         */
        super("Wishes", "id", false);
    }

    /*
     * Instantiates an empty wish instance,
     * and returns it.
     */
    @Override
    public Wish instantiate() {
        return new Wish();
    }

    /*
     * Instantiates a new wish instance based on a Map collection,
     * and returns it.
     */
    @Override
    protected Wish instantiate(Map<String, Object> result) {
        Wish wish = instantiate();
        wish.setId(((Number)result.get("id")).longValue());
        wish.setContent((String)result.get("content"));
        wish.setWishlistId(((Number)result.get("wishlist_id")).longValue());
        wish.setReservedBy((String)result.get("reserved_by"));
        return wish;
    }

    /*
     * Instantiates a collection of wishes based on a LinkedList
     * containing Map Collections, and returns the collection
     * as an iterable object of entities.
     */
    @Override
    protected Iterable<Wish> instantiateCollection(LinkedList<Map<String, Object>> resultList) {
        LinkedList<Wish> collection = new LinkedList<Wish>();
        for (var result : resultList) {
            collection.add(instantiate(result));
        }
        return collection;
    }

    /*
     * Returns a wish's properties as a Map collection.
     */
    @Override
    protected Map<String, Object> properties(Wish entity) {
        HashMap<String, Object> collection = new HashMap<String, Object>();
        collection.put("id", entity.getId());
        collection.put("content", entity.getContent());
        collection.put("wishlist_id", entity.getWishlistId());
        collection.put("reserved_by", entity.getReservedBy());
        return collection; 
    }
}
