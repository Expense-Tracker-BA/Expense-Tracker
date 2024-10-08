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


    public List<Expense> ExtractByCategory(List<String> categories) throws Exception {
        if(categories == null || categories.isEmpty()){
            throw new Exception("Please select at least one category!");
        }
        try{
            if (this.list_of_expenses == null) {
                this.list_of_expenses = DAL_Controller.getInstance().GetExpenseByCategory(categories);
                Update_total_cost();
                return this.list_of_expenses;
            } else {
                List<Expense> filtered_list = new ArrayList<>();
                for (Expense expense : this.list_of_expenses) {
                    if (categories.contains(expense.getCategory()))
                    {
                        filtered_list.add(expense);
                    }
                }

                this.list_of_expenses=filtered_list;
                Update_total_cost();
                return filtered_list;
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    private Double convertToDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    public List<Expense> ExtractByCost(String lower_cost, String upper_cost) throws Exception {
        double lowerCost = 0;
        double upperCost = 0;
        try {
            lowerCost = convertToDouble(lower_cost);
            upperCost = convertToDouble(upper_cost);
        }
        catch(Exception e){
            throw new Exception("You should only enter numeric values!");
        }

        if(!(lowerCost <= upperCost)) {
            throw new Exception("lower cost can't be larger that the upper cost");
        }

        try{
            if (this.list_of_expenses == null) {
                this.list_of_expenses = DAL_Controller.getInstance().GetExpenseByCost(lowerCost, upperCost);
                Update_total_cost();
                return this.list_of_expenses;

            } else {
                List<Expense> filtered_list = new ArrayList<>();
                for (Expense expense : this.list_of_expenses) {
                    if (expense.getCost() >= lowerCost && expense.getCost() <= upperCost)
                    {
                        filtered_list.add(expense);
                    }
                }

                this.list_of_expenses=filtered_list;
                Update_total_cost();
                return filtered_list;
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
        String converted_lower_Date = "";
        String converted_upper_Date = "";

        try{
            if (check_if_valid_date_format(lower_date) && check_if_valid_date_format(upper_date)) {
                converted_lower_Date = Date_string_converter(lower_date);
                converted_upper_Date = Date_string_converter(upper_date);
            }
            else{
                throw new Exception("please enter a valid date format DD-MM-YYYY");
            }

            if (this.list_of_expenses == null) {
                this.list_of_expenses = DAL_Controller.getInstance().GetExpenseInDateRange(converted_lower_Date, converted_upper_Date);
                Update_total_cost();
                return this.list_of_expenses;

            } else {
                List<Expense> filtered_list = new ArrayList<>();
                LocalDate upperDate = ConvertToLocalDate(upper_date);
                LocalDate lowerDate = ConvertToLocalDate(lower_date);
                for (Expense expense : this.list_of_expenses) {
                    if ((expense.getExpense_date().isAfter(lowerDate) || (expense.getExpense_date().isEqual(lowerDate))) &&
                            (expense.getExpense_date().isBefore(upperDate) || expense.getExpense_date().isEqual(upperDate)))
                    {
                        filtered_list.add(expense);
                    }
                }

                this.list_of_expenses=filtered_list;
                Update_total_cost();
                return filtered_list;
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    private LocalDate ConvertToLocalDate(String date){
        // Define the date format

        DateTimeFormatter output = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter input = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            // Format the string to a LocalDate

            LocalDate inputnewDate = LocalDate.parse(date, input);
            String desired_date_format=inputnewDate.format(output);
            LocalDate outputdate=LocalDate.parse(desired_date_format, output);

            return outputdate;
        }
        catch (Exception e) {
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
        try
        {
            double d =Double.parseDouble(input);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
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
