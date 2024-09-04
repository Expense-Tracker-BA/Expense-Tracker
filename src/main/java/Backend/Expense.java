package Backend;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

enum Category {
    Clothes,
    Food,
    Transport,
    Electronics,
}


public class Expense {
    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public LocalDate getExpense_date() {
        return expense_date;
    }

    public String getCategory() {
        return category.name();
    }

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

    private LocalDate ConvertToLocalDate(String date){
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            // Format the string to a LocalDate
            LocalDate newDate = LocalDate.parse(date, formatter);
            return newDate;
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
