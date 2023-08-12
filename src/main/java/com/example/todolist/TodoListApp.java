package com.example.todolist;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class TodoListApp extends Application {
    private Stage primaryStage;
    private TableView<TodoItem> tableView;
    private ObservableList<TodoItem> todoItems;
    private DatabaseManager databaseManager;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("To-Do List");

        databaseManager = new DatabaseManager(); // Initialize properly
        TodoItem todoItem = new TodoItem(databaseManager);

        tableView = new TableView<>();
        tableView.setItems(todoItems);

        // Create main scene
        Scene mainScene = createMainScene();

        // Set main scene
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private Scene createMainScene() {
        // Top title label
        Label titleLabel = new Label("To-Do List");
        titleLabel.setStyle("-fx-font-size: 20px;");

        // TableView setup
        this.tableView = new TableView<>();
        this.tableView.setPrefHeight(300);
        this.tableView.setItems(todoItems);

        TableColumn<TodoItem, String> contentColumn = new TableColumn<>("Content");
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

        TableColumn<TodoItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<TodoItem, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<TodoItem, String> priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        TableColumn<TodoItem, Void> updateColumn = new TableColumn<>("Update");
        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setStyle("-fx-background-color: #4285f4; -fx-text-fill: white; -fx-font-size: 12px;");
                updateButton.setOnAction(event -> {
                    TodoItem todoItem = getTableView().getItems().get(getIndex());
                    primaryStage.setScene(createUpdateTodoScene(todoItem));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });
        updateColumn.setPrefWidth(80);

        TableColumn<TodoItem, Void> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px;");
                deleteButton.setOnAction(event -> {
                    TodoItem todoItem = getTableView().getItems().get(getIndex());
                    TodoItem.deleteFromDatabase(todoItem);
                    todoItems.remove(todoItem); // Remove the item from the list
                    TodoListApp.this.tableView.refresh(); // Refresh the table view
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        deleteColumn.setPrefWidth(80);

        this.tableView.getColumns().addAll(contentColumn, categoryColumn, timeColumn, priorityColumn, updateColumn, deleteColumn);


        // Bottom buttons
        Button addTodoButton = new Button("New To-Do list");
        addTodoButton.setOnAction(e -> primaryStage.setScene(createAddTodoScene()));

        HBox bottomBar = new HBox(addTodoButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setStyle("-fx-background-color: #f0f0f0;");

        // Main layout
        VBox mainLayout = new VBox(titleLabel, this.tableView, bottomBar);
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(10));

        return new Scene(mainLayout, 600, 400);
    }

    private Scene createAddTodoScene() {
        databaseManager = new DatabaseManager();
        // 顶部标题
        Label titleLabel = new Label("New To-Do");
        titleLabel.setStyle("-fx-font-size: 20px;");

        // 中间主体
        TextField todoContentTextField = new TextField();
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Work", "Learn", "Life");

        DatePicker datePicker = new DatePicker();

        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll("Low", "Medium", "High");

        VBox addTodoLayout = new VBox(
                new Label("Content:"),
                todoContentTextField,
                new Label("Category:"),
                categoryComboBox,
                new Label("Time:"),
                datePicker,
                new Label("Priority:"),
                priorityComboBox
        );
        addTodoLayout.setSpacing(10);
        addTodoLayout.setPadding(new Insets(10));

        // 底部按钮
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String content = todoContentTextField.getText();
            String category = categoryComboBox.getValue();
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            String priority = priorityComboBox.getValue();

            try {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement("INSERT INTO todos (content, category, date, priority) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, content);
                preparedStatement.setString(2, category);
                preparedStatement.setDate(3, Date.valueOf(date));
                preparedStatement.setString(4, priority);

                preparedStatement.executeUpdate();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            primaryStage.setScene(createMainScene());
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(createMainScene()));

        HBox bottomBar = new HBox(saveButton, backButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setStyle("-fx-background-color: #f0f0f0;");

        // 新增事项页面布局
        VBox addTodoSceneLayout = new VBox(titleLabel, addTodoLayout, bottomBar);
        addTodoSceneLayout.setSpacing(10);
        addTodoSceneLayout.setPadding(new Insets(10));

        return new Scene(addTodoSceneLayout, 400, 500);
    }
    private Scene createUpdateTodoScene(TodoItem todoItem) {
        databaseManager = new DatabaseManager();

        // Top title label
        Label titleLabel = new Label("Update To-Do");
        titleLabel.setStyle("-fx-font-size: 20px;");

        // Center layout for the "Update To-Do" scene
        VBox updateTodoLayout = new VBox(titleLabel);

        // Content input
        TextField todoContentTextField = new TextField();
        todoContentTextField.setText(todoItem.getContent());
        updateTodoLayout.getChildren().addAll(new Label("Content:"), todoContentTextField);

        // Category selection
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Work", "Learn", "Life");
        categoryComboBox.setValue(todoItem.getCategory());
        updateTodoLayout.getChildren().addAll(new Label("Category:"), categoryComboBox);

        // Date picker
        DatePicker datePicker = new DatePicker();
        if (!todoItem.getDate().isEmpty()) {
            datePicker.setValue(LocalDate.parse(todoItem.getDate()));
        }
        updateTodoLayout.getChildren().addAll(new Label("Time:"), datePicker);

        // Priority selection
        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll("Low", "Medium", "High");
        priorityComboBox.setValue(todoItem.getPriority());
        updateTodoLayout.getChildren().addAll(new Label("Priority:"), priorityComboBox);

        updateTodoLayout.setSpacing(10);
        updateTodoLayout.setPadding(new Insets(10));

        // Bottom buttons

        Button backButton = new Button("Back");
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String content = todoContentTextField.getText();
            String category = categoryComboBox.getValue();
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            String priority = priorityComboBox.getValue();

            try  {
                PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement("UPDATE todos SET content=?, category=?, date=?, priority=? WHERE id=?");
                preparedStatement.setString(1, content);
                preparedStatement.setString(2, category);
                preparedStatement.setDate(3, Date.valueOf(date));
                preparedStatement.setString(4, priority);
                preparedStatement.executeUpdate();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        backButton.setOnAction(e -> primaryStage.setScene(createMainScene()));

        HBox bottomBar = new HBox(saveButton, backButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setStyle("-fx-background-color: #f0f0f0;");
        updateTodoLayout.getChildren().add(bottomBar);

        // Update scene layout
        VBox updateTodoSceneLayout = new VBox(updateTodoLayout);
        updateTodoSceneLayout.setSpacing(10);
        updateTodoSceneLayout.setPadding(new Insets(10));

        // Return the scene
        return new Scene(updateTodoSceneLayout, 400, 500);
    }
}