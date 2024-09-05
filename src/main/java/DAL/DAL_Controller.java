package DAL;
import Backend.Expense;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the LocalDate to a string
        String formattedDate = date.format(formatter);
        return formattedDate;
    }

    public void InsertExpense(Expense expense) throws SQLException {
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
        catch (Exception e) {
            throw e;
        }
    }

    public List<Expense> GetExpenseByDate(String date) throws SQLException{
        try {
            List<Expense> expenses = new LinkedList<>();
            String sql = "SELECT * FROM Expenses WHERE Date = ?";
            Connection conn = DriverManager.getConnection(connection_string);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Set the date parameter
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                expenses.add(new Expense(rs.getString("description"), rs.getDouble("Cost"),
                       rs.getString("Date"), rs.getString("Category"),true));
            }
            pstmt.close();
            conn.close();
            return expenses;
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Expense> GetExpenseByCategory(List<String> categories) throws SQLException {
        try {
            List<Expense> expenses = new LinkedList<>();
            String sql = "SELECT * FROM Expenses WHERE Category = ?";
            Connection conn = DriverManager.getConnection(connection_string);
            //Foreach category
            for(int i = 0; i < categories.size(); i++) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                // Set the category parameter
                pstmt.setString(1, categories.get(i));
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    expenses.add(new Expense(rs.getString("description"), rs.getDouble("Cost"),
                            rs.getString("Date"), rs.getString("Category"),true));
                }
                pstmt.close();
            }
            conn.close();
            return expenses;
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Expense> GetExpenseByCost(double lower_cost, double upper_cost) throws SQLException {
        try {
            List<Expense> expenses = new LinkedList<>();
            String sql = "SELECT * FROM Expenses WHERE Cost BETWEEN ? AND ?";
            Connection conn = DriverManager.getConnection(connection_string);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Set the cost parameters
            pstmt.setDouble(1, lower_cost);
            pstmt.setDouble(2, upper_cost);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                expenses.add(new Expense(rs.getString("description"), rs.getDouble("Cost"),
                        rs.getString("Date"), rs.getString("Category"),true));
            }
            pstmt.close();
            conn.close();
            return expenses;
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Expense> GetExpenseInDateRange(String lower_date, String upper_date) throws Exception {
        try {
            List<Expense> expenses = new LinkedList<>();
            String sql = "SELECT * FROM Expenses WHERE Date BETWEEN ? AND ?";
            Connection conn = DriverManager.getConnection(connection_string);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, lower_date);
            pstmt.setString(2, upper_date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                expenses.add(new Expense(rs.getString("description"), rs.getDouble("Cost"),
                        rs.getString("Date"), rs.getString("Category"),true));
            }
            pstmt.close();
            conn.close();
            return expenses;
        }
        catch(Exception e){
            throw e;
        }
    }

    public List<Expense> ExtractByDescription(String description) throws SQLException {
        try {
            List<Expense> expenses = new LinkedList<>();
            String sql = "SELECT * FROM Expenses WHERE description LIKE '%"+description+"%'";
            Connection conn = DriverManager.getConnection(connection_string);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                expenses.add(new Expense(rs.getString("description"), rs.getDouble("Cost"),
                        rs.getString("Date"), rs.getString("Category"),true));
            }
            pstmt.close();
            conn.close();
            return expenses;
        }
        catch(Exception e){
            throw e;
        }
    }

    public void UpdateExpense(Integer id, String description, double cost, String date, String category) throws Exception {
        try {
            String sql = "UPDATE Expenses SET description = ? , Date = ? , Cost = ? , Category = ?  WHERE ID = ?;";
            Connection conn = DriverManager.getConnection(connection_string);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, description);
            pstmt.setDouble(2, cost);
            pstmt.setString(3, date);
            pstmt.setString(4, category);
            pstmt.setInt(5, id);
           int num_of_changed_rows= pstmt.executeUpdate();
           if (num_of_changed_rows==0)
           {
               throw new Exception("no Expense with the provided ID: "+id);
           }

            pstmt.close();
            conn.close();

        }
        catch(Exception e){
            throw e;
        }

    }

    public void RemoveExpense(Integer id) throws Exception {
        try {
            String sql = "DELETE FROM Expenses WHERE ID = ?;";
            Connection conn = DriverManager.getConnection(connection_string);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int num_of_changed_rows= pstmt.executeUpdate();
            if (num_of_changed_rows==0)
            {
                throw new Exception("no Expense with the provided ID: "+id);
            }

            pstmt.close();
            conn.close();

        }
        catch(Exception e){
            throw e;
        }
    }
}
