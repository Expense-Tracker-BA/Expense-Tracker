package Backend.Business_Layer;
import Backend.Expense;
import Backend.Service_Layer.ResponseT;
import DAL.DAL_Controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

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

    public List<Expense> ExtractByCategory(List<String> categories) throws SQLException {
        try{
            return DAL_Controller.getInstance().GetExpenseByCategory(categories);
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Expense> ExtractByCost(double lower_cost, double upper_cost) throws SQLException {
        try{
            return DAL_Controller.getInstance().GetExpenseByCost(lower_cost, upper_cost);
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Expense> ExtractInDateRange(String lower_date, String upper_date) throws Exception {
        try{
            if (check_if_valid_date_format(lower_date) && check_if_valid_date_format(upper_date) ) {
                String converted_lower_Date = Date_string_converter(lower_date);
                String converted_upper_Date = Date_string_converter(upper_date);
                return DAL_Controller.getInstance().GetExpenseInDateRange(converted_lower_Date, converted_upper_Date);
            }
            else
            {
                throw new Exception("please enter a valid date format DD-MM-YYYY");
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    private boolean check_if_valid_date_format(String date) {
        Pattern p = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
        return p.matcher(date).matches();

    }

    //converts from the format DD-MM-YYYY to YYYY-MM-DD
    private String Date_string_converter(String date)
    {
        DateTimeFormatter output = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter input = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate inputnewDate = LocalDate.parse(date, input);
        String desired_date_format=inputnewDate.format(output);
        return desired_date_format;
    }

    public List<Expense> ExtractByDescription(String description) throws Exception {
        try{
            List<Expense> test=DAL_Controller.getInstance().ExtractByDescription(description);
            for(Expense exp : test)
            {
                System.out.println(exp.getDescription());
            }
            return DAL_Controller.getInstance().ExtractByDescription(description);
        }
        catch(Exception e){
            throw e;
        }
    }

    public String UpdateExpense(Integer ID,String description, double cost, String date, String category) throws Exception {
        try{
            String converted_date=Date_string_converter(date);
            DAL_Controller.getInstance().UpdateExpense(ID,description, cost, converted_date, category);
            return "updated expense successfully";
        }
        catch(Exception e){
            throw e;
        }
    }

    public String RemoveExpense(Integer id) throws Exception {
        try{

            DAL_Controller.getInstance().RemoveExpense(id);
            return "Deleted expense successfully";
        }
        catch(Exception e){
            throw e;
        }
    }
}
