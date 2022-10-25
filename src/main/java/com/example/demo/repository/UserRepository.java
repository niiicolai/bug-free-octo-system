package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import com.example.demo.model.User;

@Repository
public class UserRepository extends CrudRepository<User> {

    @Override
    public User instantiate() {
        return new User();
    }

    @Override
    protected User instantiate(ResultSet resultSet) throws SQLException {
        User user = instantiate();
        while (resultSet.next()) {
            user.setId(resultSet.getLong("id"));
            user.setEmail(resultSet.getString("email"));
        }
        return user;
    }

    @Override
    protected Iterable<User> instantiateCollection(ResultSet resultSet) throws SQLException {
        LinkedList<User> collection = new LinkedList<User>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setEmail(resultSet.getString("email"));
            collection.add(user); 
        }
        return collection;
    }

    @Override
    protected Map<String, Object> values(User entity) {
        HashMap<String, Object> collection = new HashMap<String, Object>();
        collection.put("id", entity.getId());
        collection.put("email", entity.getEmail());
        return collection; 
    }

    @Override
    protected String table() {
        return "Users";
    }
}
