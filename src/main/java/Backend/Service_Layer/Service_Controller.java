package Backend.Service_Layer;

import Backend.Business_Layer.Controller;
import Backend.Expense;

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

    public Response UpdateExpense(String description,double cost,String date, String category)
    {
        try
        {
            Controller.getInstance().UpdateExpense(description,cost,date, category);
            Response response =new Response("updated expense successfully");
            return response;
        }
        catch (Exception e)
        {
            return new Response(e.getMessage());
        }
    }

    public Response RemoveExpense()// remove by what?
    {
        try
        {
            //Controller.getInstance().RemoveExpense(description,cost,date, category);
            Response response =new Response("removed expense successfully");
            return response;
        }
        catch (Exception e)
        {
            return new Response(e.getMessage());
        }
    }

}
