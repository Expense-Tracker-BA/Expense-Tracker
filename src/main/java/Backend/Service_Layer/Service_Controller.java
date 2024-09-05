package Backend.Service_Layer;

import Backend.Business_Layer.Controller;
import Backend.Business_Layer.Expense;

import java.util.List;

public class Service_Controller {


    private static Service_Controller instance;
    private Service_Controller()
    {

    }

    public static Service_Controller GetInstance()
    {
        if(instance==null)
        {
            instance=new Service_Controller();
        }
        return instance;
    }

    public ResponseT<String> AddExpense(String date, String description, double cost, String category){
        try
        {
            ResponseT<String> response = ResponseT.FromValue(Controller.getInstance().AddExpense(date, description, cost, category));
            return response;
        }
        catch (Exception e)
        {
            return ResponseT.FromError(e.getMessage());
        }
    }

    public ResponseT<List<Expense>> ExtractByCategory(List<String> categories){
        try
        {
            ResponseT<List<Expense>> response =ResponseT.FromValue(Controller.getInstance().ExtractByCategory(categories));
            return response;
        }
        catch (Exception e)
        {
            return ResponseT.FromError(e.getMessage());
        }
    }

    public ResponseT<List<Expense>> ExtractByCost(double lower_cost, double upper_cost){
        try
        {
            ResponseT<List<Expense>> response =ResponseT.FromValue(Controller.getInstance().ExtractByCost(lower_cost, upper_cost));
            return response;
        }
        catch (Exception e)
        {
            return ResponseT.FromError(e.getMessage());
        }
    }

    public ResponseT<List<Expense>> ExtractInDateRange(String lower_date, String upper_date)
    {
        try
        {
            ResponseT<List<Expense>> response =ResponseT.FromValue(Controller.getInstance().ExtractInDateRange(lower_date,upper_date));
            return response;
        }
        catch (Exception e)
        {
            return ResponseT.FromError(e.getMessage());
        }
    }

    public ResponseT<List<Expense>> ExtractByDescription(String description)
    {
        try
        {
            ResponseT<List<Expense>> response =ResponseT.FromValue(Controller.getInstance().ExtractByDescription(description));
            return response;
        }
        catch (Exception e)
        {
            return ResponseT.FromError(e.getMessage());
        }
    }

    public ResponseT<String> UpdateExpense(Integer ID,String description,double cost,String date, String category)
    {
        try
        {
            ResponseT<String> response=ResponseT.FromValue(Controller.getInstance().UpdateExpense(ID,description,cost,date, category));
            return response;
        }
        catch (Exception e)
        {
            return ResponseT.FromError(e.getMessage());
        }
    }

    public ResponseT<String> RemoveExpense(Integer ID)
    {
        try
        {
            ResponseT<String> response=ResponseT.FromValue(Controller.getInstance().RemoveExpense(ID));
            return response;
        }
        catch (Exception e)
        {
            return ResponseT.FromError(e.getMessage());
        }
    }

}
