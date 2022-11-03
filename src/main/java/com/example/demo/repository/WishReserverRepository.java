package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.demo.model.WishReserver;
import com.example.demo.repository.CustomImplementation.CrudRepository;

@Repository
public class WishReserverRepository extends CrudRepository<WishReserver> {

    public WishReserverRepository() {
        /*
         * Specify the wish reservers table.
         */
        super("wish_reservers", "wish_id", true);
    }

    /*
     * Instantiates an empty wish reserver instance,
     * and returns it.
     */
    @Override
    public WishReserver instantiate() {
        return new WishReserver();
    }

    public static WishReserver create(Map<String, Object> result) {
        WishReserver wishReserver = new WishReserver();

        Object wishId = result.get("wish_id");
        long parsedWishId = wishId == null ? 0 : ((Number)wishId).longValue();
        wishReserver.setWishId(parsedWishId);
        wishReserver.setFullname((String)result.get("fullname"));
        wishReserver.setCreatedAt((LocalDateTime)result.get("created_at"));
        return wishReserver;
    }

    /*
     * Instantiates a new wish reserver instance based on a Map collection,
     * and returns it.
     */
    @Override
    protected WishReserver instantiate(Map<String, Object> result) {
        return WishReserverRepository.create(result);
    }

    /*
     * Instantiates a collection of wish reservers based on a LinkedList
     * containing Map Collections, and returns the collection
     * as an iterable object of entities.
     */
    @Override
    protected Iterable<WishReserver> instantiateCollection(LinkedList<Map<String, Object>> resultList) {
        LinkedList<WishReserver> collection = new LinkedList<>();
        for (var result : resultList) {
            collection.add(instantiate(result));
        }
        return collection;
    }

    @Override
    protected Iterable<WishReserver> instantiateCollectionWithRelation(LinkedList<Map<String, Object>> resultList) {
        return new LinkedList<WishReserver>();
    }

    /*
     * Returns a wish reserver's properties as a Map collection.
     */
    @Override
    protected Map<String, Object> properties(WishReserver entity) {
        HashMap<String, Object> collection = new HashMap<>();
        collection.put("wish_id", entity.getWishId());
        collection.put("fullname", entity.getFullname());
        return collection; 
    }
}
