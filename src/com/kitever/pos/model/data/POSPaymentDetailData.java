package com.kitever.pos.model.data;

import java.util.ArrayList;
import java.util.List;

public class POSPaymentDetailData {
//		private String Status;
//
//	    private String Message;

	    private List<PaymentDetail> PaymentDetail;
//
//	    public String getStatus ()
//	    {
//	        return Status;
//	    }
//
//	    public void setStatus (String Status)
//	    {
//	        this.Status = Status;
//	    }
//
//	    public String getMessage ()
//	    {
//	        return Message;
//	    }
//
//	    public void setMessage (String Message)
//	    {
//	        this.Message = Message;
//	    }

	    public ArrayList<PaymentDetail> getPaymentDetail ()
	    {
	        return  (ArrayList<com.kitever.pos.model.data.PaymentDetail>) PaymentDetail;
	    }

	    public void setCustomerOrderDetail (List<CustomerOrderDetail> CustomerOrderDetail)
	    {
	        this.PaymentDetail = PaymentDetail;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [Status = +Status+, Message = Message"+ PaymentDetail +"+PaymentDetail+]";
	    }
}
