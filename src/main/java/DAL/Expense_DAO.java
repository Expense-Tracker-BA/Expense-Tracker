package DAL;
import java.sql.*;

public class Expense_DAO {
    private String table_name="Expenses";
    private String connection_string;


    public Expense_DAO(String connection_string)
    {
        this.connection_string=connection_string;
        create_table(connection_string);

    }

    private void create_table(String connection_string) {

        String sql = "CREATE TABLE IF NOT EXISTS Expenses (\n"
                + " ID integer PRIMARY KEY AUTOINCREMENT,\n"
                + " description text,\n"
                + " Date text,\n"
                + " Cost float,\n"
                + " Category text\n"
                + ");";




        try{
            Connection conn = DriverManager.getConnection(connection_string);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println("shit");

        }

    }

}
