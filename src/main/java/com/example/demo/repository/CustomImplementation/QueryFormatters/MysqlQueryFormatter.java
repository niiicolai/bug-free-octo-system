package com.example.demo.repository.CustomImplementation.QueryFormatters;

import java.util.Map;

import com.example.demo.repository.CustomImplementation.QueryFormatter;

public class MysqlQueryFormatter extends QueryFormatter {

    public MysqlQueryFormatter(String table) {
        super(table);
    }

    /*
     * Returns a SQL statement used to insert entities.
     */
    @Override
    public String insert(Map<String, Object> entityProperties) {
        return String.format("INSERT INTO %s (%s) VALUES (%s)", table, 
            formattedInsertKeys(entityProperties),
            formattedInsertValues(entityProperties));
    }

    /*
     * Returns a SQL statement used to update entities.
     */
    @Override
    public String update(Map<String, Object> entityProperties, long id) {
        return String.format("UPDATE %s SET %s WHERE id = %d", table,
            formattedUpdateKeys(entityProperties), id);
    }

    /*
     * Returns a SQL statement used to return an entity by id.
     */
    @Override
    public String findOne() {
        return String.format("SELECT * FROM %s WHERE id = ?", table);
    }

    /*
     * Returns a SQL statement used to return all entities.
     */
    @Override
    public String findAll() {
        return String.format("SELECT * FROM %s", table);
    }

    /*
     * Returns a SQL statement used to return all entities
     * where a column is equal to a value.
     */
    @Override
    public String findWhere(String column) {
        return String.format("SELECT * FROM %s WHERE %s = ?", table, column);
    }

    /*
     * Returns a SQL statement used to return the number of all entities.
     */
    @Override
    public String count() {
        return String.format("SELECT Count(*) FROM %s", table);
    }

    /*
     * Returns a SQL statement used to delete an entity by id.
     */
    @Override
    public String delete() {
        return String.format("DELETE FROM %s WHERE id = ?", table);
    }

    /*
     * Returns a SQL statement used to return the last created entity.
     */
    @Override
    public String last() {
        return String.format("SELECT * FROM %s ORDER BY ID DESC LIMIT 1", table);
    }
}
