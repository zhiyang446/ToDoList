module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.todolist to javafx.fxml;
    exports com.example.todolist;
}