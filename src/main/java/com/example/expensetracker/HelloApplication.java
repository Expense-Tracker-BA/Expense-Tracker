package com.example.expensetracker;

import Backend.Business_Layer.Controller;
import Backend.Expense;
import DAL.DAL_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        try {
            //Controller.getInstance().AddExpense("04-09-2024", "something", 57.4, "Transport");
            List<Expense> expenses = Controller.getInstance().ExtractByDate("04-09-2024");
            for(int i = 0; i < expenses.size(); i++) {
                System.out.println(expenses.get(i).GetExpense());
            }
        }
        catch(Exception e){

        }
    }

    public static void main(String[] args) {
        launch();
    }
}