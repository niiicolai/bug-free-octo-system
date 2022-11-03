package com.example.demo.repository.CustomImplementation.QueryFormatters;

import java.util.Map;
import java.util.LinkedList;
import java.util.List;

import com.example.demo.repository.CustomImplementation.QueryFormatter;

public class MysqlQueryFormatter extends QueryFormatter {

    public MysqlQueryFormatter(String table, String primaryKeyName) {
        super(table, primaryKeyName);
    }

    /*
     * Returns a SQL statement used to insert entities.
     */
    @Override
    public String insert(Map<String, Object> entityProperties, List<String> exclude) {
        return String.format("INSERT INTO %s (%s) VALUES (%s)", table, 
            formattedInsertKeys(entityProperties, exclude),
            formattedInsertValues(entityProperties, exclude));
    }

    /*
     * Returns a SQL statement used to update entities.
     */
    @Override
    public String update(Map<String, Object> entityProperties, Object primaryKey) {
        return String.format("UPDATE %s SET %s WHERE %s = %s", table,
            formattedUpdateKeys(entityProperties), primaryKeyName, primaryKey.toString());
    }

    /*
     * Returns a SQL statement used to return an entity by id.
     */
    @Override
    public String findOne() {
        return String.format("SELECT * FROM %s WHERE %s = ?", table, primaryKeyName);
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
     * Returns a SQL statement used to return all entities
     * where a column is equal to a value.
     */
    @Override
    public String findWhereJoin(String column, String referencesTable, String foreignKeyColumn) {
        return String.format("SELECT * FROM %s LEFT JOIN %s ON %s.%s=%s.%s WHERE %s.%s=?", 
        table, 
        referencesTable, 
        table, 
        primaryKeyName, 
        referencesTable, 
        foreignKeyColumn,
        table,
        column);
    }

    /*
     * Returns a SQL statement used to return the number of all entities.
     */
    @Override
    public String count() {
        return String.format("SELECT Count(*) as count FROM %s", table);
    }

    @Override
    public String countWhereJoin(String column, String referencesTable, String foreignKeyColumn) {
        return String.format("SELECT Count(*) as count FROM %s INNER JOIN %s ON %s.%s=%s.%s WHERE %s.%s=?", 
        table, 
        referencesTable, 
        table, 
        primaryKeyName, 
        referencesTable, 
        foreignKeyColumn,
        table,
        column);
    }

    /*
     * Returns a SQL statement used to delete an entity by id.
     */
    @Override
    public String delete() {
        return String.format("DELETE FROM %s WHERE %s = ?", table, primaryKeyName);
    }

    /*
     * Returns a SQL statement used to return the last created entity.
     */
    @Override
    public String last() {
        return String.format("SELECT * FROM %s ORDER BY created_at DESC LIMIT 1", table);
    }
}
