package com.example.demo.repository;

import java.sql.*;
import java.util.Map;

public class CrudService {

    private static final String URL = "jdbc:mysql://localhost/test";
    private static final String USERNAME = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private static Connection connection;

    private static Connection createConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        connection = CrudService.createConnection();
        return connection.prepareStatement(sql);
    }

    public static PreparedStatement prepareCreateStatement(Map<String, Object> values, String table) throws SQLException {
        int i = 0;
        StringBuilder formattedKeys = new StringBuilder("");
        StringBuilder formattedValues = new StringBuilder("");
        for (String key : values.keySet()) {
            if (key != "id") {
                String format = (i < values.size()-1 ? "%s, " : "%s");
                formattedKeys.append(String.format(format, key));
                formattedValues.append(String.format(format, "?"));
            }
            
            i++;
        }

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", table, 
            formattedKeys.toString(), formattedValues.toString());

        PreparedStatement statement = CrudService.prepareStatement(sql);
        setStatementValues(statement, values);

        return statement;
    }

    public static PreparedStatement prepareUpdateStatement(Map<String, Object> values, String table) throws SQLException {
        int i = 0;
        StringBuilder builder = new StringBuilder("");
        for (String key : values.keySet()) {
            if (key != "id") {
                String format = (i < values.size()-1 ? "%s = ?, " : "%s = ?");
                builder.append(String.format(format, key));
            }

            i++;
        }

        long id = (long)values.get("id");
        String sql = String.format("UPDATE %s SET %s WHERE id = %d", table, builder.toString(), id);
        PreparedStatement statement = CrudService.prepareStatement(sql);
        setStatementValues(statement, values);

        return statement;
    }

    private static void setStatementValues(PreparedStatement statement, Map<String, Object> values) throws SQLException {
        int i = 1;
        for (String key : values.keySet()) {
            if (key != "id") {
                statement.setObject(i, values.get(key));
                i++;
            }
        }
    }

    public static void closeConnection() throws SQLException {
        if (connection == null) return;
        connection.close();
    }

    public static long lastInsertId(String table) throws SQLException {
        if (connection == null) 
            throw new SQLException("No connection was found");
            
        String sql = String.format("SELECT id FROM %s ORDER BY ID DESC LIMIT 1", table);
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        long id = 0;
        
        while (resultSet.next()) {
            id = (long)resultSet.getInt("id");
        }        
        
        connection.close();
        return id;
    }
}
