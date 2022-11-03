package com.example.demo.repository.CustomImplementation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public abstract class QueryFormatter {

    /*
     * The table used in the SQL statements.
     */
    protected String table;

    /*
     * The column name of the primary key used 
     * in the SQL statements.
     */
    protected String primaryKeyName;

    
    public QueryFormatter(String table, String primaryKeyName) {
        this.table = table;
        this.primaryKeyName = primaryKeyName;
    }

    /*
     * Returns a SQL statement used to insert entities.
     */
    public abstract String insert(Map<String, Object> values, List<String> exclude);

    /*
     * Returns a SQL statement used to update entities.
     */
    public abstract String update(Map<String, Object> values, Object primaryKey);

    /*
     * Returns a SQL statement used to return an entity 
     * by a primary key.
     */
    public abstract String findOne();

    /*
     * Returns a SQL statement used to return all entities.
     */
    public abstract String findAll();

    /*
     * Returns a SQL statement used to return all entities
     * where a column is equal to a value.
     */
    public abstract String findWhere(String column);

    /*
     * Returns a SQL statement used to return all entities
     * where a column is equal to a value,
     * and joins another table based on a foreign key.
     */
    public abstract String findWhereJoin(String column, String referencesTable, String foreignKeyColumn);
    

    /*
     * Returns a SQL statement used to return the number of all entities.
     */
    public abstract String count();

    public abstract String countWhereJoin(String column, String referencesTable, String foreignKeyColumn);

    /*
     * Returns a SQL statement used to delete an entity
     * by a primary key.
     */
    public abstract String delete();

    /*
     * Returns a SQL statement used to return the last created entity.
     */
    public abstract String last();

    /*
     * Returns the name of the primary key
     */
    public String getPrimaryKey() {
        return primaryKeyName;
    }

    /*
     * Returns a string of formatted column names based on an entity's properties.
     * Meant to be used by the insert method.
     *  
     */
    protected String formattedInsertKeys(Map<String, Object> properties, List<String> exclude) {
        int i = 0;
        StringBuilder builder = new StringBuilder("");
        for (String key : properties.keySet()) {
            if (!exclude.contains(key)) {
                String format = (i < properties.size()-1 ? "%s, " : "%s");
                builder.append(String.format(format, key));
            }

            i++;
        }
        return builder.toString();
    }

    /*
     * Returns a string of formatted question marks based on an entity's properties.
     * Meant to be used by the insert method.
     *  
     */
    protected String formattedInsertValues(Map<String, Object> properties, List<String> exclude) {
        int i = 0;
        StringBuilder builder = new StringBuilder("");
        for (String key : properties.keySet()) {
            if (!exclude.contains(key)) {
                String format = (i < properties.size()-1 ? "%s, " : "%s");
                builder.append(String.format(format, "?"));
            }

            i++;
        }
        return builder.toString();
    }

    /*
     * Returns a string of formatted keys based on an entity's properties.
     * Meant to be used by the update method.
     *  
     */
    protected String formattedUpdateKeys(Map<String, Object> properties) {
        int i = 0;
        StringBuilder builder = new StringBuilder("");
        for (String key : properties.keySet()) {
            if (key != this.primaryKeyName) {
                String format = (i < properties.size()-1 ? "%s = ?, " : "%s = ?");
                builder.append(String.format(format, key));
            }

            i++;
        }
        return builder.toString();
    }
}


