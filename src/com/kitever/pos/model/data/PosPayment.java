package com.kitever.pos.model.data;

import java.util.ArrayList;
import java.util.List;

public class PosPayment {
	private String Status;

    private String Message;

    private List<CreditBalnce> CreditBalnce;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getMessage ()
    {
        return Message;
    }

    public void setMessage (String Message)
    {
        this.Message = Message;
    }

    public ArrayList<CreditBalnce> getCreditBalnce ()
    {
        return (ArrayList<com.kitever.pos.model.data.CreditBalnce>) CreditBalnce;
    }

    public void setCreditBalnce (List<CreditBalnce> CreditBalnce)
    {
        this.CreditBalnce = CreditBalnce;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Message = "+Message+", CreditBalnce = "+CreditBalnce+"]";
    }
}
