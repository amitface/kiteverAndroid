package com.kitever.pos.model.data;

import java.util.ArrayList;

public class InsertUpdateFetchInvoiceSettingData {

	private String Status;

    private String Message;
    
    private String Header;
    
    private String Footer;
    
    private String OrderPrefix;

    private String TinNo;

    private String UserID;

    private String PanNo;   
    
    private String ServicetaxNo;

    private String InvoicePrefix;

    public InsertUpdateFetchInvoiceSettingData(String status, String message,
			String header, String footer, String orderPrefix, String tinNo,
			String userID, String panNo, String servicetaxNo,
			String invoicePrefix) {
		super();
		Status = status;
		Message = message;
		Header = header;
		Footer = footer;
		OrderPrefix = orderPrefix;
		TinNo = tinNo;
		UserID = userID;
		PanNo = panNo;
		ServicetaxNo = servicetaxNo;
		InvoicePrefix = invoicePrefix;
	}

	public String getOrderPrefix() {
		return OrderPrefix;
	}

	public void setOrderPrefix(String orderPrefix) {
		OrderPrefix = orderPrefix;
	}

	public String getTinNo() {
		return TinNo;
	}

	public void setTinNo(String tinNo) {
		TinNo = tinNo;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getPanNo() {
		return PanNo;
	}

	public void setPanNo(String panNo) {
		PanNo = panNo;
	}

	public String getServicetaxNo() {
		return ServicetaxNo;
	}

	public void setServicetaxNo(String servicetaxNo) {
		ServicetaxNo = servicetaxNo;
	}

	public String getInvoicePrefix() {
		return InvoicePrefix;
	}

	public void setInvoicePrefix(String invoicePrefix) {
		InvoicePrefix = invoicePrefix;
	}

	

    
//    public InsertUpdateFetchInvoiceSettingData(String status,String message,String Header, String Footer) {
////		super();
//		Status = status;
//		this.Header= Header;
//		this.Footer = Footer;
//		Message = message;
//	}
//    
    public String getHeader() {
		return Header;
	}

	public void setHeader(String header) {
		Header = header;
	}

	public String getFooter() {
		return Footer;
	}

	public void setFooter(String footer) {
		Footer = footer;
	}

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

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Message = "+Message+"]";
    }

    
    		
	    
}
