package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
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
        super("Wishlists");
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
        long id = ((Number)result.get("id")).longValue();
        wishlist.setId(id);
        wishlist.setTitle((String)result.get("title"));
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

    /*
     * Returns a wishlist's properties as a Map collection.
     */
    @Override
    protected Map<String, Object> properties(Wishlist entity) {
        HashMap<String, Object> collection = new HashMap<String, Object>();
        collection.put("id", entity.getId());
        collection.put("title", entity.getTitle());
        return collection; 
    }
}
