package com.example.todolist;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TodoListApp extends Application {
    private Stage primaryStage;
    private ListView<String> todoListView;
    private ObservableList<String> todoItems;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("To-Do List");

        // 初始化待办事项列表
        todoItems = FXCollections.observableArrayList();

        // 创建主页面
        Scene mainScene = createMainScene();

        // 默认显示主页面
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private Scene createMainScene() {
        // 顶部标题
        Label titleLabel = new Label("To-Do List");
        titleLabel.setStyle("-fx-font-size: 20px;");

        // 中间主体 - 待办事项列表
        todoListView = new ListView<>();
        todoListView.setPrefHeight(300);
        todoListView.setItems(todoItems);

        // 底部按钮
        Button addTodoButton = new Button("New To-Do list");
        addTodoButton.setOnAction(e -> primaryStage.setScene(createAddTodoScene()));

        HBox bottomBar = new HBox(addTodoButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setStyle("-fx-background-color: #f0f0f0;");

        // 设置按钮
        Button settingsButton = new Button("Setting");
        settingsButton.setOnAction(e -> primaryStage.setScene(createSettingsScene()));
        HBox settingsBar = new HBox(settingsButton);
        settingsBar.setAlignment(Pos.CENTER_RIGHT);
        settingsBar.setPadding(new Insets(10));
        settingsBar.setStyle("-fx-background-color: #f0f0f0;");

        // 主页布局
        VBox mainLayout = new VBox(titleLabel, settingsBar, todoListView, bottomBar);
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(10));

        return new Scene(mainLayout, 400, 500);
    }

    private Scene createAddTodoScene() {
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
            String todoItem = "Content: " + content + ", Category: " + category + ", Time: " + date + ", Priority: " + priority;
            todoItems.add(todoItem);
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

    private Scene createSettingsScene() {
        // 顶部标题
        Label titleLabel = new Label("Setting");
        titleLabel.setStyle("-fx-font-size: 20px;");

        // 主体内容
        CheckBox themeCheckBox = new CheckBox("Switching theme colours");
        Slider fontSizeSlider = new Slider(10, 20, 14);
        ComboBox<String> sortingComboBox = new ComboBox<>();
        sortingComboBox.getItems().addAll("By Name", "By Time", "By Priority");

        Button clearButton = new Button("Clear of completed item");
        clearButton.setOnAction(e -> todoItems.clear());

        VBox settingsLayout = new VBox(
                new Label("Theme Colour:"),
                themeCheckBox,
                new Label("Font Size:"),
                fontSizeSlider,
                new Label("Sort by:"),
                sortingComboBox,
                clearButton
        );
        settingsLayout.setSpacing(10);
        settingsLayout.setPadding(new Insets(10));

        // 底部按钮
        Button saveButton = new Button("Save Setting");
        saveButton.setOnAction(e -> primaryStage.setScene(createMainScene()));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(createMainScene()));

        HBox bottomBar = new HBox(saveButton, backButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setStyle("-fx-background-color: #f0f0f0;");

        // 设置页面布局
        VBox settingsSceneLayout = new VBox(titleLabel, settingsLayout, bottomBar);
        settingsSceneLayout.setSpacing(10);
        settingsSceneLayout.setPadding(new Insets(10));

        return new Scene(settingsSceneLayout, 400, 500);
    }
}
