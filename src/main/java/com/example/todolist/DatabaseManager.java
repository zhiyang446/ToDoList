package com.example.todolist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private Connection connection;
    public DatabaseManager() {
        try {
            // Loading JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Step 1 Provide the URL to the database with your own username and password
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist", "root", "zhiyang446");

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public boolean isConnected() {
        return connection != null;
    }
}