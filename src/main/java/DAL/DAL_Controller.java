package DAL;
import Backend.Expense;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DAL_Controller {
    private Expense_DAO expenses_table;
    private static DAL_Controller instance=new DAL_Controller();
    private String connection_string;


    public static DAL_Controller getInstance(){
        return instance;
    }

    private DAL_Controller()
    {
        this.connection_string="jdbc:sqlite:Expenses_Tracker.db";
        this.expenses_table=new Expense_DAO(connection_string);
    }
    private String LocalDateConverter(LocalDate date){
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Format the LocalDate to a string
        String formattedDate = date.format(formatter);
        return formattedDate;
    }

    public void InsertExpense(Expense expense) {
        String sql = "INSERT INTO Expenses(description, Date, Cost, Category) VALUES(?,?,?,?)";

        try{
            Connection conn = DriverManager.getConnection(connection_string);
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, expense.getDescription());
            pstmt.setString(2,LocalDateConverter(expense.getExpense_date()));
            pstmt.setDouble(3,expense.getCost());
            pstmt.setString(4,expense.getCategory());

            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        }
        catch (SQLException e) {

        }
    }
}
