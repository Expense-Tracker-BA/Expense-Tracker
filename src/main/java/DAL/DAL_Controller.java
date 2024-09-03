package DAL;
import java.sql.*;

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
//        try {
//            // The newInstance() call is a work around for some
//            // broken Java implementations
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//        } catch (Exception ex) {
//            System.out.println("wtf");
//            // handle the error
//        }
        this.expenses_table=new Expense_DAO(connection_string);
    }

}
