package com.example.demo.repository.CustomImplementation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class DatabaseRequest {

    private String sql; 

    private Map<String, Object> properties; 

    private List<String> exclude;
    
    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setProperty(String key, Object value) {
        if (properties == null)
            properties = new HashMap<>();

        properties.put(key, value);
    }

    public void setExclude(String ... varArgs) {
        this.exclude = new LinkedList<String>(Arrays.asList(varArgs));
    }

    public String getSql() {
        return sql;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public List<String> getExclude() {
        return exclude;
    }
}
