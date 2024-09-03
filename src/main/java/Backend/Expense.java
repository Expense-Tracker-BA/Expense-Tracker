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
    private String ID;
    private String description;
    private float cost;
    private LocalDate expense_date;
    private Category category;

    public Expense (String description,float cost,String date,String category,String id)
    {
        this.ID=id;
        this.description=description;
        this.cost=cost;
        this.expense_date=LocalDate.parse(date);
        this.category=Category.valueOf(category);
    }


}
