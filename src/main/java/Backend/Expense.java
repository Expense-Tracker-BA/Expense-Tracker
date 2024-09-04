package Backend;

import java.time.LocalDate;
import java.util.Date;

enum Category {
    Clothes,
    Food,
    Transport,
    Electronics
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
        this.expense_date=LocalDate.parse(date);
        this.category=Category.valueOf(category);
    }
}
