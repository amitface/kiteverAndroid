package sms19.listview.newproject.model;

/**
 * Created by android on 24/2/17.
 */

public class ContactRegistration {
    private String UserType;
    private String Msg;
    private String Error;

    public String getUserType ()
    {
        return UserType;
    }

    public void setUserType (String UserType)
    {
        this.UserType = UserType;
    }

    public String getMsg ()
    {
        return Msg;
    }

    public void setMsg (String Msg)
    {
        this.Msg = Msg;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [UserType = "+UserType+", Msg = "+Msg+"]";
    }
}
