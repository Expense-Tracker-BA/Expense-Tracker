package Backend;
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

    public List<Expense> ExtractByDate(String date) throws SQLException {
        try{
            return DAL_Controller.getInstance().GetExpenseByDate(date);
        }
        catch(Exception e){
            throw e;
        }
    }
}
