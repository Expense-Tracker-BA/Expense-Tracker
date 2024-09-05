package Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ExpenseTrackerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ExpenseTrackerApplication.class.getResource("ExpenseTracker-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("Expense Tracker!");
        stage.setScene(scene);
        stage.show();
        //ExtractInDateRange Test:
        //ResponseT<List<Expense>> shit=Service_Controller.GetInstance().ExtractInDateRange("01-09-1990","01-10-2005");
        //System.out.println(shit.ErrorMessage);
        //ExtractByDescription Test:
        //Service_Controller.GetInstance().ExtractByDescription("test");
        //UpdateExpense Test:
      // ResponseT<String> shit= Service_Controller.GetInstance().UpdateExpense(11,"new_test",69.69,"01-01-2000","Clothes");
      // System.out.println(shit.ErrorMessage);
        //RemoveExpense Test:
//        ResponseT<String> shit= Service_Controller.GetInstance().RemoveExpense(69);
//         System.out.println(shit.ErrorMessage);

        try {
            //AddExpense Test:
             //Controller.getInstance().AddExpense("04-09-2024", "socks", 1.5, "Clothes");

            //ExtractByCategory Test:
            /* List<String> categories = new LinkedList<>();
            categories.add("Food");
            categories.add("Clothes");
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