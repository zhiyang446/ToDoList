package com.example.todolist;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class GetAllTodoItemsTask extends Task<ObservableList<TodoItem>> {
    private final DatabaseManager databaseManager;

    public GetAllTodoItemsTask(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    protected ObservableList<TodoItem> call() throws Exception {
        return (ObservableList<TodoItem>) TodoItem.getAllFromDatabase(databaseManager);
    }
}
