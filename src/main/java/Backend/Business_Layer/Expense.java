package Backend.Business_Layer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

enum Category {
    Clothes,
    Food,
    Transport,
    Electronics
}


public class Expense {
    public Integer getID() { return ID;}
    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public LocalDate getExpense_date() {
        return expense_date;
    }
    public String getExpense_date_string() {


        DateTimeFormatter output = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String desired_date_format=this.expense_date.format(output);
        return desired_date_format;
    }

    public String getCategory() {
        return category.name();
    }

    private Integer ID;
    private String description;
    private double cost;
    private LocalDate expense_date;
    private Category category;

    public Expense (String description, double cost, String date, String category)
    {
        this.description=description;
        this.cost=cost;

        // Define the date format
        this.expense_date = ConvertToLocalDate(date);

        this.category=Category.valueOf(category);
    }

    public Expense (Integer ID,String description, double cost, String date, String category,boolean flag_from_database)
    {
        this.description=description;
        this.cost=cost;

        // Define the date format
        this.expense_date = ConvertToLocalDate_from_database(date);

        this.category=Category.valueOf(category);
        this.ID=ID;
    }

    private LocalDate ConvertToLocalDate_from_database(String date) {
        // Define the date format

        DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter output = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            // Format the string to a LocalDate

            LocalDate inputdate = LocalDate.parse(date, input);
            String desired_date_format=inputdate.format(output);
            LocalDate outputdate=LocalDate.parse(desired_date_format, output);

            return outputdate;
        }
        catch (Exception e) {
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

    public String GetExpense(){
        return "Description: " + description + ", Cost: " + cost + ", Date: " + expense_date + ", Category: " + category + ".\n";
    }


    // TODO:: Make a Set Expense as well in the future - to update an existing expense? (or in the DB instead?)
}
