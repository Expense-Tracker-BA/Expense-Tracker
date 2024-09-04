package com.example.expensetracker;

import Backend.Controller;
import Backend.Expense;
import DAL.DAL_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        //Controller.getInstance().AddExpense(LocalDate.now().toString(), "something", 40.5, "Food");
    }

    public static void main(String[] args) {
        launch();
    }
}