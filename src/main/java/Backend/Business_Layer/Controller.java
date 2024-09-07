package Backend.Business_Layer;
import Backend.Service_Layer.ResponseT;
import DAL.DAL_Controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private double curr_sum;
    private static Controller instance = new Controller();
    public static Controller getInstance(){
        return instance;
    }
    private List<Expense> list_of_expenses;


    private Controller()
    {
        curr_sum=0;
        list_of_expenses=null;
    }

    public String AddExpense(String date, String description, double cost, String category) throws Exception {
        try{
            if (check_if_valid_date_format(date)) {
                Expense expense = new Expense(description, cost, date, category);
                DAL_Controller.getInstance().InsertExpense(expense);
                return "Added Expense successfully";
            }
            else{
                throw new Exception("please enter a valid date format DD-MM-YYYY");
            }
        }
        catch(Exception e){
            throw e;
        }
    }


    public List<Expense> ExtractByCategory(List<String> categories) throws SQLException {
        try{
            //TODO:: a drop list in the frontend
            return DAL_Controller.getInstance().GetExpenseByCategory(categories);
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Expense> ExtractByCost(double lower_cost, double upper_cost) throws Exception {
        try{
            if(lower_cost <= upper_cost) {
                return DAL_Controller.getInstance().GetExpenseByCost(lower_cost, upper_cost);
            }
            else{
                throw new Exception("lower cost can't be larger that the upper cost");
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    public double Get_current_sum()
    {
        return this.curr_sum;

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

        try {
            if (this.list_of_expenses == null) {

                this.list_of_expenses = DAL_Controller.getInstance().ExtractByDescription(description);
                Update_total_cost();
                return this.list_of_expenses;

            } else {
                List<Expense> filtered_list = new ArrayList<>();
                for (Expense expense : this.list_of_expenses) {
                        if (expense.getDescription().contains(description))
                        {

                            filtered_list.add(expense);
                        }
                }

                this.list_of_expenses=filtered_list;
                Update_total_cost();
                return filtered_list;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void Update_total_cost() {
        this.curr_sum=0;
        for(Expense expense:this.list_of_expenses)
        {
            this.curr_sum=this.curr_sum+expense.getCost();
        }
    }


    public String UpdateExpense(Integer ID,String description, String cost, String date, String category) throws Exception {
        try{
            if (!check_if_valid_date_format(date)){
                throw new Exception("please enter a valid date format DD-MM-YYYY");
            }
            if (!check_if_valid_numeric_input(cost)){
                throw new Exception("please enter numeric cost only");
            }

            String converted_date=Date_string_converter(date);
            DAL_Controller.getInstance().UpdateExpense(ID,description, Double.parseDouble(cost), converted_date, category);
            return "updated expense successfully";
        }
        catch(Exception e){
            throw e;
        }
    }

    private boolean check_if_valid_numeric_input(String input) {
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        return p.matcher(input).matches();
    }

    public String RemoveExpense(String id) throws Exception {
        try{
            if (!check_if_valid_numeric_input(id))
            {
                throw  new Exception("please enter numeric ID only");
            }

            DAL_Controller.getInstance().RemoveExpense(Integer.parseInt(id));
            return "Deleted expense successfully";
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Expense> ExtractAll() throws SQLException {
        try{
            this.list_of_expenses=null;
            this.curr_sum=0;
            List<Expense> expenses=DAL_Controller.getInstance().ExtractAll();
            for (Expense expense : expenses) {
                    this.curr_sum=this.curr_sum+expense.getCost();
            }
            return expenses;

        }
        catch(Exception e){
            throw e;
        }
    }

    public Expense ExtractByID(String id) throws Exception {
        try{
            String regex = "[0-9]+";

            // Compile the ReGex
            Pattern p = Pattern.compile(regex);


            if (!p.matcher(id).matches())
            {
                throw  new Exception("please enter numeric ID only");
            }



            return DAL_Controller.getInstance().ExtractByID(Integer.parseInt(id));
        }
        catch(Exception e){
            throw e;
        }

    }
}
