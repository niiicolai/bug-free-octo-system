package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import com.example.demo.model.Wishlist;

@Repository
public class WishlistRepository extends CrudRepository<Wishlist> {

    @Override
    public Wishlist instantiate() {
        return new Wishlist();
    }

    @Override
    protected Wishlist instantiate(ResultSet resultSet) throws SQLException {
        Wishlist wishlist = instantiate();
        while (resultSet.next()) {
            wishlist.setTitle(resultSet.getString("title"));
        }
        return wishlist;
    }

    @Override
    protected Iterable<Wishlist> instantiateCollection(ResultSet resultSet) throws SQLException {
        LinkedList<Wishlist> collection = new LinkedList<Wishlist>();
        while (resultSet.next()) {
            Wishlist wishlist = instantiate(resultSet);
            collection.add(wishlist); 
        }
        return collection;
    }

    @Override
    protected Map<String, Object> values(Wishlist entity) {
        HashMap<String, Object> collection = new HashMap<String, Object>();
        collection.put("id", entity.getId());
        collection.put("title", entity.getTitle());
        return collection; 
    }

    @Override
    protected String table() {
        return "Wishlists";
    }
}
