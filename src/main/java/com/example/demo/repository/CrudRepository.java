package com.example.demo.repository;

import java.sql.*;
import java.util.Map;

public abstract class CrudRepository<T> {

    public abstract T instantiate();

    protected abstract T instantiate(ResultSet resultSet) throws SQLException;
    protected abstract Iterable<T> instantiateCollection(ResultSet resultSet) throws SQLException;
    protected abstract Map<String, Object> values(T entity);
    protected abstract String table();

    public T save(T entity) {        
        try {
            Map<String, Object> values = values(entity);
            long id = (long)values.get("id");

            PreparedStatement statement;
            if (exists(id)) {
                statement = CrudService.prepareCreateStatement(values, table());
            } else {
                statement = CrudService.prepareUpdateStatement(values, table());
            }
            statement.executeUpdate();
            return findOne(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T findOne(long primaryKey) {
        try {
            String sql = String.format("SELECT * FROM %s WHERE id = ?", table());
            PreparedStatement statement = CrudService.prepareStatement(sql);
            statement.setLong(1, primaryKey);
            ResultSet resultSet = statement.executeQuery();
            return instantiate(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Iterable<T> findAll() {
        try {
            String sql = String.format("SELECT * FROM %s", table());
            PreparedStatement statement = CrudService.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            return instantiateCollection(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long count() {
        long count = 0;
        try {
            String sql = String.format("SELECT Count(*) FROM %s", table());
            PreparedStatement statement = CrudService.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) 
                count = resultSet.getLong("count");

            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(long primaryKey) {
        try {
            String sql = String.format("DELETE FROM %s WHERE id = ?", table());
            PreparedStatement statement = CrudService.prepareStatement(sql);
            statement.setLong(1, primaryKey);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(long primaryKey) {
        return findOne(primaryKey) != null;
    }
}
