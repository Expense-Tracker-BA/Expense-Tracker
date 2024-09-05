package Backend.Business_Layer;
import Backend.Expense;
import Backend.Service_Layer.ResponseT;
import DAL.DAL_Controller;

import java.sql.SQLException;
import java.util.List;

public class Controller {
    private static Controller instance = new Controller();
    public static Controller getInstance(){
        return instance;
    }


    public Controller()
    {

    }

    public void AddExpense(String date, String description, double cost, String category) throws SQLException {
        try{
            Expense expense = new Expense(description, cost, date, category);
            DAL_Controller.getInstance().InsertExpense(expense);
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Backend.Expense> ExtractByDate(String date) throws SQLException {
        try{
            return DAL_Controller.getInstance().GetExpenseByDate(date);
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Backend.Expense> ExtractByCategory(List<String> categories) throws SQLException {
        try{
            return DAL_Controller.getInstance().GetExpenseByCategory(categories);
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Backend.Expense> ExtractByCost(double lower_cost, double upper_cost) throws SQLException {
        try{
            return DAL_Controller.getInstance().GetExpenseByCost(lower_cost, upper_cost);
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Backend.Expense> ExtractInDateRange(String lower_date, String upper_date) throws Exception {
        throw new Exception ("impleemnbet me");
    }

    public List<Backend.Expense> ExtractByDescription(String description) throws Exception {
        throw new Exception ("impleemnbet me");
    }

    public void UpdateExpense(String description, double cost, String date, String category) throws Exception {
        throw new Exception ("impleemnbet me");
    }
}
