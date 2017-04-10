package com.kitever.pos.model.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreditModelData {

	
	    private String Status;

	    private String Message;

	    private List<CreditDetails> CreditDetails;

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

	    public ArrayList<CreditDetails> getCreditDetails ()
	    {
	        return (ArrayList<com.kitever.pos.model.data.CreditDetails>) CreditDetails;
	    }

	    public void setCreditDetails (List<CreditDetails> CreditDetails)
	    {
	        this.CreditDetails = CreditDetails;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [Status = "+Status+", Message = "+Message+", CreditDetails = "+Arrays.toString(CreditDetails.toArray())+"]";
	    }
	
}
