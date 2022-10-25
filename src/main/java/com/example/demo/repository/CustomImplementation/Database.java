package com.example.demo.repository.CustomImplementation;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Database {

    /*
     * Database driver protocol, host, and database name.
     */
    private static final String URI = "jdbc:mysql://localhost/test";

    /*
     * Database username.
     * Defined locally in system environment variables.
     */
    private static final String USERNAME = System.getenv("DB_USERNAME");

    /*
     * Database password.
     * Defined locally in system environment variables.
     */
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    /*
     * Create and returns a new connection based uri, username & password.
     */
    private static Connection createConnection() throws SQLException
    {
        return DriverManager.getConnection(URI, USERNAME, PASSWORD);
    }    

    /*
     * Parse the content of a ResultSet and returns it as a LinkedList
     * of Map collections.
     */
    private static LinkedList<Map<String, Object>> parseResultSet(ResultSet resultSet) throws SQLException {
        LinkedList<Map<String, Object>> resultList = new LinkedList<>();  
        ResultSetMetaData metaData = resultSet.getMetaData();

        int k = metaData.getColumnCount();
            
        while(resultSet.next()) {
                
            HashMap<String, Object> result = new HashMap<>();
            for (int j = 1; j < k + 1; j++) {
                String columnName = metaData.getColumnName(j);
                Object value = resultSet.getObject(columnName);
                result.put(columnName, value);
            }
                
            resultList.add(result);
        }

        return resultList;
    }

    /*
     * Map (not excluded) properties to a prepared statement.
     */
    private static void setStatementValues(PreparedStatement statement, Map<String, Object> properties, List<String> exclude) throws SQLException {
        if (exclude == null) 
            exclude = new LinkedList<String>();

        if (properties != null) {
            int i = 1;
            for (String key : properties.keySet()) {
                if (!exclude.contains(key)) {
                    statement.setObject(i, properties.get(key));
                    i++;
                }
            }
        }        
    } 

    /*
     * Call executeUpdate on a prepared statement based on a sql statement, sql properties, 
     * and the optional exclude parameter.
     */
    public static void executeUpdate(String sql, Map<String, Object> properties, List<String> exclude) {
        try {
            Connection connection = createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setStatementValues(preparedStatement, properties, exclude);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    /*
     * Call executeQuery on a prepared statement based on a sql statement, sql properties, 
     * and the optional exclude parameter.
     * Returns a LinkedList with a Map Collection with the result.
     */
    public static LinkedList<Map<String, Object>> executeQuery(String sql, Map<String, Object> properties, List<String> exclude) {
        LinkedList<Map<String, Object>> resultList = new LinkedList<>();        
        System.out.println("executeQuery");
        try {
            Connection connection = createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setStatementValues(preparedStatement, properties, exclude);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultList = parseResultSet(resultSet);

            connection.close();
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.getStackTrace();
        }

        return resultList;
    }
}
