package com.example.expensetracker;

import Backend.Service_Layer.ResponseT;
import Backend.Service_Layer.Service_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        //ExtractInDateRange Test:
        //Service_Controller.GetInstance().ExtractInDateRange("01-09-1990","01-10-2005");
        //ExtractByDescription Test:
        //Service_Controller.GetInstance().ExtractByDescription("test");
        //UpdateExpense Test:
      // ResponseT<String> shit= Service_Controller.GetInstance().UpdateExpense(69,"new_test",69.69,"01-01-2000","Clothes");
      // System.out.println(shit.ErrorMessage);
        //RemoveExpense Test:
//        ResponseT<String> shit= Service_Controller.GetInstance().RemoveExpense(69);
//         System.out.println(shit.ErrorMessage);

        try {
            //AddExpense Test:
            /* Controller.getInstance().AddExpense("04-09-2024", "something", 57.4, "Transport"); */

            //ExtractByDate Test:
            /* List<Expense> expenses = Controller.getInstance().ExtractByDate("04-09-2024");
            for(int i = 0; i < expenses.size(); i++) {
                System.out.println(expenses.get(i).GetExpense());
            } */

            //ExtractByCategory Test:
            /* List<String> categories = new LinkedList<>();
            categories.add("Food");
            categories.add("Transport");
            List<Expense> expenses = Controller.getInstance().ExtractByCategory(categories);
            for(int i = 0; i < expenses.size(); i++) {
                System.out.println(expenses.get(i).GetExpense());
            } */

            //ExtractByCost test:
            /* List<Expense> expenses = Controller.getInstance().ExtractByCost(0.0, 57.4);
            for(int i = 0; i < expenses.size(); i++) {
                System.out.println(expenses.get(i).GetExpense());
            } */
        }
        catch(Exception e){

        }
    }

    public static void main(String[] args) {
        launch();
    }
}