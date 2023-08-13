package com.example.todolist;

import javafx.scene.control.Alert;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TodoItem {
    public int id;
    public static DatabaseManager databaseManager;
    private String content;
    private String category;
    private String date;
    private String priority;

    public TodoItem(Integer id, String content, String category, String date, String priority) {
        this.id = id;
        TodoItem.databaseManager = new DatabaseManager();
        this.content = content;
        this.category = category;
        this.date = date;
        this.priority = priority;
    }

    public static List<TodoItem> getAllFromDatabase(DatabaseManager databaseManager) {
        List<TodoItem> items = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement("SELECT * FROM todos");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String content = resultSet.getString("content");
                String category = resultSet.getString("category");
                String date = resultSet.getString("date");
                String priority = resultSet.getString("priority");
                items.add(new TodoItem(id,content, category, date, priority));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public static void deleteFromDatabase(DatabaseManager databaseManager, TodoItem todoItem) {
        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement("DELETE FROM todos WHERE id=?");
            preparedStatement.setInt(1, todoItem.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateInDatabase(DatabaseManager databaseManager, TodoItem updatedItem) {
        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "UPDATE todos SET content=?, category=?, date=?, priority=? WHERE id=?"
            );
            preparedStatement.setString(1, updatedItem.getContent());
            preparedStatement.setString(2, updatedItem.getCategory());
            preparedStatement.setDate(3, Date.valueOf(updatedItem.getDate()));
            preparedStatement.setString(4, updatedItem.getPriority());
            preparedStatement.setInt(5, updatedItem.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertIntoDatabase(DatabaseManager databaseManager, TodoItem newItem) {
        try {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement("INSERT INTO todos (content, category, date, priority) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, newItem.getContent());
            preparedStatement.setString(2, newItem.getCategory());
            preparedStatement.setDate(3, Date.valueOf(newItem.getDate()));
            preparedStatement.setString(4, newItem.getPriority());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }
}
