package Backend.Service_Layer;

public class ResponseT<T> extends Response{
    private final  T Value;
    private ResponseT(T value, String msg)
    {
        this.Value = value;
        super.ErrorMessage=msg;
    }



    public static <T> ResponseT<T> FromValue(T value)
    {
        return new ResponseT(value, null);
    }

    public static <T> ResponseT<T> FromError(String msg)
    {
        return new ResponseT(null, msg);
    }
}

