package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import com.example.demo.model.User;
import com.example.demo.repository.CustomImplementation.CrudRepository;

@Repository
public class UserRepository extends CrudRepository<User> {

    public UserRepository() {
        /*
         * Specify the user table.
         */
        super("Users");
    }

    /*
     * Instantiates an empty user instance,
     * and returns it.
     */
    @Override
    public User instantiate() {
        return new User();
    }

    /*
     * Instantiates a new user instance based on a Map collection,
     * and returns it.
     */
    @Override
    protected User instantiate(Map<String, Object> result) {
        User user = instantiate();
        long id = ((Number)result.get("id")).longValue();
        user.setId(id);
        user.setEmail((String)result.get("email"));
        return user;
    }

    /*
     * Instantiates a collection of users based on a LinkedList
     * containing Map Collections, and returns the collection
     * as an iterable object of entities.
     */
    @Override
    protected Iterable<User> instantiateCollection(LinkedList<Map<String, Object>> resultList) {
        LinkedList<User> collection = new LinkedList<User>();
        for (var result : resultList) {
            collection.add(instantiate(result));
        }
        return collection;
    }

    /*
     * Returns a user's properties as a Map collection.
     */
    @Override
    protected Map<String, Object> properties(User entity) {
        HashMap<String, Object> collection = new HashMap<String, Object>();
        collection.put("id", entity.getId());
        collection.put("email", entity.getEmail());
        return collection; 
    }
}
