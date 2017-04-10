package com.kitever.pos.model.data;

import java.util.ArrayList;
import java.util.List;

public class OrderList {
	 private String Status;

	    private String Message;

	    private List<OrderDetailModelData> Order;

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

	    public ArrayList<OrderDetailModelData> getOrder ()
	    {
	        return (ArrayList<OrderDetailModelData>) Order;
	    }

	    public void setOrder (List<OrderDetailModelData> Order)
	    {
	        this.Order = Order;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [Status = "+Status+", Message = "+Message+", Order = "+Order+"]";
	    }
}
