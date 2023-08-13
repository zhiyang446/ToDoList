package com.example.todolist;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TodoItem {
    private static int id;
    private static DatabaseManager databaseManager;
    private String content;
    private String category;
    private String date;
    private String priority;

    public TodoItem(int id, DatabaseManager databaseManager, String content, String category, String date, String priority) {
        TodoItem.id = id;
        TodoItem.databaseManager = new DatabaseManager();
        this.content = content;
        this.category = category;
        this.date = date;
        this.priority = priority;
    }

    public TodoItem(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
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
                items.add(new TodoItem(id, databaseManager, content, category, date, priority));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public static void deleteFromDatabase(TodoItem item) {
        try  {
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement("DELETE FROM todos WHERE id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTime(String date) {
        this.date = date;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
