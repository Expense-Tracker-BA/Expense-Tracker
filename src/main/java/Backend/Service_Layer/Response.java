package Backend.Service_Layer;

public class Response {

        public String ErrorMessage;
        public Response() { }
        public Response(String msg)
        {
            this.ErrorMessage = msg;
        }
        public boolean ErrorOccured ()
        {
            return ErrorMessage!=null;


        }

}
