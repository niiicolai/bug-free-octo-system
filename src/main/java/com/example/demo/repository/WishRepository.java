package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import com.example.demo.model.Wish;

@Repository
public class WishRepository extends CrudRepository<Wish> {
    
    @Override
    public Wish instantiate() {
        return new Wish();
    }

    @Override
    protected Wish instantiate(ResultSet resultSet) throws SQLException {
        Wish wish = instantiate();
        while (resultSet.next()) {
            wish.setId(resultSet.getLong("id"));
            wish.setContent(resultSet.getString("content"));
        }
        return wish;
    }

    @Override
    protected Iterable<Wish> instantiateCollection(ResultSet resultSet) throws SQLException {
        LinkedList<Wish> collection = new LinkedList<Wish>();
        while (resultSet.next()) {
            Wish wish = new Wish();
            wish.setId(resultSet.getLong("id"));
            wish.setContent(resultSet.getString("content"));
            collection.add(wish); 
        }
        return collection;
    }

    @Override
    protected Map<String, Object> values(Wish entity) {
        HashMap<String, Object> collection = new HashMap<String, Object>();
        collection.put("id", entity.getId());
        collection.put("content", entity.getContent());
        return collection; 
    }

    @Override
    protected String table() {
        return "Wishes";
    }
}
