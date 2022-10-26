package com.example.demo.repository.CustomImplementation;

import java.util.LinkedList;
import java.util.Map;

import com.example.demo.repository.CustomImplementation.QueryFormatters.MysqlQueryFormatter;

public abstract class CrudRepository<T> {

    /*
     * The query formatter instance.
     */
    private QueryFormatter queryFormatter;

    /*
     * The CrudRepository constructor requires
     * a table name as parameter which the query formatter
     * uses to create SQL queries.
     */
    public CrudRepository(String table) {
        
        /*
         * Defines the type of query formatter.
         * Change the type to use another query formatter.  
         */
        this.queryFormatter = new MysqlQueryFormatter(table);
    }

    /*
     * Instantiates an empty instance,
     * and returns it.
     */
    public abstract T instantiate();

    /*
     * Instantiates a new entity instance based on a Map collection,
     * and returns it.
     */
    protected abstract T instantiate(Map<String, Object> result);

    /*
     * Instantiates a collection of entities based on a LinkedList
     * containing Map Collections, and returns the collection
     * as an iterable object of entities.
     */
    protected abstract Iterable<T> instantiateCollection(LinkedList<Map<String, Object>> resultList);

    /*
     * Returns an entity's properties as a Map collection.
     */
    protected abstract Map<String, Object> properties(T entity);

    /*
     * IF entity.id == 0 : 
     *      Insert an entity by the specified values, 
     *      and returns the last created entity.
     * otherwise : 
     *      Update an entity by the specified values, 
     *      and returns the updated entity.
     */
    public T save(T entity) throws Exception {        
        Map<String, Object> properties = properties(entity);
        long id = (long)properties.get("id");

        if (id == 0) {
            String sql = queryFormatter.insert(properties);                                    
            DatabaseResponse response = Database.executeUpdate(sql, properties, QueryFormatter.exclude("id"));
            if (response.error()) throw new Exception(response.getMessage());
            return last();
        } else {
            String sql = queryFormatter.update(properties, id);
            DatabaseResponse response = Database.executeUpdate(sql, properties, QueryFormatter.exclude("id"));
            if (response.error()) throw new Exception(response.getMessage());

            return findOne(id);
        }
    }

    /*
     * Returns an entity with the
     * specified primary key. 
     */
    public T findOne(long primaryKey) {
        Map<String, Object> properties = QueryFormatter.property("id", primaryKey);
        String sql = queryFormatter.findOne();
        DatabaseResponse response = Database.executeQuery(sql, properties, null);
        return instantiate(response.getResultList().getFirst());
    }

    /*
     * Returns all entities. 
     */
    public Iterable<T> findAll() {
        String sql = queryFormatter.findAll();
        DatabaseResponse response = Database.executeQuery(sql, null, null);
        return instantiateCollection(response.getResultList());
    }

    /*
     * Returns all entities by where clause. 
     */
    public Iterable<T> findWhere(String column, Object value) {
        Map<String, Object> properties = QueryFormatter.property(column, value);
        String sql = queryFormatter.findWhere(column);
        DatabaseResponse response = Database.executeQuery(sql, properties, null);        
        return instantiateCollection(response.getResultList());
    }

    /*
     * Returns the number of entities. 
     */
    public Long count() {
        String sql = queryFormatter.count();
        DatabaseResponse response = Database.executeQuery(sql, null, null);
        return (long)response.getResultList().getFirst().get("count");
    }

    /*
     * Delete an entity with the 
     * specified primary key. 
     */
    public void delete(long primaryKey) throws Exception {
        Map<String, Object> properties = QueryFormatter.property("id", primaryKey);
        String sql = queryFormatter.delete();
        DatabaseResponse response = Database.executeUpdate(sql, properties, null);
        if (response.error()) throw new Exception(response.getMessage());
    }

    /*
     * Returns true if an entity with the 
     * specified primary key exist. 
     */
    public boolean exists(long primaryKey) {
        return findOne(primaryKey) != null;
    }

    /*
     * Returns the last entity created.
     */
    public T last() {
        String sql = queryFormatter.last();
        DatabaseResponse response = Database.executeQuery(sql, null, null);
        return instantiate(response.getResultList().getFirst());
    }
}
