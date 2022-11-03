package com.example.demo.repository.CustomImplementation;

import java.util.LinkedList;
import java.util.Map;

import com.example.demo.repository.CustomImplementation.QueryFormatters.MysqlQueryFormatter;

import groovyjarjarantlr4.v4.parse.ANTLRParser.prequelConstruct_return;

public abstract class CrudRepository<T> {

    /*
     * The query formatter instance.
     */
    private QueryFormatter queryFormatter;

    private boolean setPrimaryKeyOnCreate;

    /*
     * The CrudRepository constructor requires
     * a table name as parameter which the query formatter
     * uses to create SQL queries.
     */
    public CrudRepository(String table, String primaryKeyName, boolean setPrimaryKeyOnCreate) {
        
        this.setPrimaryKeyOnCreate = setPrimaryKeyOnCreate;

        /*
         * Defines the type of query formatter.
         * Change the type to use another query formatter.  
         */
        this.queryFormatter = new MysqlQueryFormatter(table, primaryKeyName);
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

    protected abstract Iterable<T> instantiateCollectionWithRelation(LinkedList<Map<String, Object>> resultList);

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
    public T insert(T entity) throws Exception {     
        Map<String, Object> properties = properties(entity);
        String exclude = (!setPrimaryKeyOnCreate ? queryFormatter.getPrimaryKey() : null);
        DatabaseRequest request = new DatabaseRequest();
        request.setProperties(properties);
        request.setExclude(exclude);        
        request.setSql(queryFormatter.insert(properties, request.getExclude()));
        
        DatabaseResponse response = Database.executeUpdate(request);
        if (response.error()) throw new Exception(response.getMessage());
        
        return last();
    }

    public T update(T entity) throws Exception {
        Map<String, Object> properties = properties(entity);
        String primaryKeyColumn = queryFormatter.getPrimaryKey();
        Object primaryKey = properties.get(primaryKeyColumn);
        DatabaseRequest request = new DatabaseRequest();
        request.setProperties(properties);
        request.setExclude(primaryKeyColumn);
        request.setSql(queryFormatter.update(properties, primaryKey));
           
        DatabaseResponse response = Database.executeUpdate(request);
        if (response.error()) throw new Exception(response.getMessage());

        return findOne(primaryKey);
    }

    /*
     * Returns an entity with the
     * specified primary key. 
     */
    public T findOne(Object primaryKey) {
        DatabaseRequest request = new DatabaseRequest();
        request.setProperty("primaryKey", primaryKey);
        request.setSql(queryFormatter.findOne());
        DatabaseResponse response = Database.executeQuery(request);

        if (response.getFirstResult() != null)
            return instantiate(response.getFirstResult());
        else
            return instantiate();
    }

    /*
     * Returns all entities. 
     */
    public Iterable<T> findAll() {
        DatabaseRequest request = new DatabaseRequest();
        request.setSql(queryFormatter.findAll());
        DatabaseResponse response = Database.executeQuery(request);

        return instantiateCollection(response.getResultList());
    }

    /*
     * Returns all entities by where clause. 
     */
    public Iterable<T> findWhere(String column, Object value) {
        DatabaseRequest request = new DatabaseRequest();
        request.setProperty(column, value);
        request.setSql(queryFormatter.findWhere(column));
        DatabaseResponse response = Database.executeQuery(request);

        return instantiateCollection(response.getResultList());
    }

     /*
     * Returns all entities by where clause with a collection. 
     */
    public Iterable<T> findWhereJoin(String column, Object value, String referencesTable, String foreignKeyColumn) {
        DatabaseRequest request = new DatabaseRequest();
        request.setProperty(column, value);
        request.setSql(queryFormatter.findWhereJoin(column, referencesTable, foreignKeyColumn));
        DatabaseResponse response = Database.executeQuery(request);

        return instantiateCollectionWithRelation(response.getResultList());
    }

    /*
     * Returns the number of entities. 
     */
    public Long count() {
        DatabaseRequest request = new DatabaseRequest();
        request.setSql(queryFormatter.count());
        DatabaseResponse response = Database.executeQuery(request);

        return (long)response.getResultList().getFirst().get("count");
    }

    /*
     * Returns the number of entities. 
     */
    public Long countWhereJoin(String column, Object value, String referencesTable, String foreignKeyColumn) {
        DatabaseRequest request = new DatabaseRequest();
        request.setProperty(column, value);
        request.setSql(queryFormatter.countWhereJoin(column, referencesTable, foreignKeyColumn));
        DatabaseResponse response = Database.executeQuery(request);
        return (long)response.getResultList().getFirst().get("count");
    }

    /*
     * Delete an entity with the 
     * specified primary key. 
     */
    public void delete(String column, Object primaryKey) throws Exception {
        DatabaseRequest request = new DatabaseRequest();
        request.setProperty(column, primaryKey);
        request.setSql(queryFormatter.delete());
        DatabaseResponse response = Database.executeUpdate(request);

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
        DatabaseRequest request = new DatabaseRequest();
        request.setSql(queryFormatter.last());
        DatabaseResponse response = Database.executeQuery(request);
        if (response.getResultList() != null && !response.getResultList().isEmpty())
            return instantiate(response.getResultList().getFirst());
        else
            return instantiate();
    }
}
