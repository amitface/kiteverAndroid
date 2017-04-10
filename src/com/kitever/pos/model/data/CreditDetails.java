package com.kitever.pos.model.data;


public class CreditDetails
{
private String InvoiceNo;

private String InvoiceCode;

private String Contact_Email;

private String Contact_Mobile;

private String BillAmount;

private String OrderDate;

private String contactID;

private String OrderID;

private String PaidAmount;

private String Contact_Name;

private String DiscountAmount;

private String BalanceAmount;

private String Combined;

private String OrderCode;

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		InvoiceNo = invoiceNo;
	}

	public String getInvoiceCode() {
		return InvoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		InvoiceCode = invoiceCode;
	}


public String getOrderCode() {
	return OrderCode;
}

public void setOrderCode(String orderCode) {
	OrderCode = orderCode;
}

public String getCombined() {
	return Combined;
}

public void setCombined(String combined) {
	Combined = combined;
}

public String getContact_Email ()
{
return Contact_Email;
}

public void setContact_Email (String Contact_Email)
{
this.Contact_Email = Contact_Email;
}

public String getContact_Mobile ()
{
return Contact_Mobile;
}

public void setContact_Mobile (String Contact_Mobile)
{
this.Contact_Mobile = Contact_Mobile;
}

public String getBillAmount ()
{
return BillAmount;
}

public void setBillAmount (String BillAmount)
{
this.BillAmount = BillAmount;
}

public String getOrderDate ()
{
return OrderDate;
}

public void setOrderDate (String OrderDate)
{
this.OrderDate = OrderDate;
}

public String getContactID ()
{
return contactID;
}

public void setContactID (String contactID)
{
this.contactID = contactID;
}

public String getOrderID ()
{
return OrderID;
}

public void setOrderID (String OrderID)
{
this.OrderID = OrderID;
}

public String getPaidAmount ()
{
return PaidAmount;
}

public void setPaidAmount (String PaidAmount)
{
this.PaidAmount = PaidAmount;
}

public String getContact_Name ()
{
return Contact_Name;
}

public void setContact_Name (String Contact_Name)
{
this.Contact_Name = Contact_Name;
}

public String getDiscountAmount ()
{
return DiscountAmount;
}

public void setDiscountAmount (String DiscountAmount)
{
this.DiscountAmount = DiscountAmount;
}

public String getBalanceAmount ()
{
return BalanceAmount;
}

public void setBalanceAmount (String BalanceAmount)
{
this.BalanceAmount = BalanceAmount;
}

@Override
public String toString()
{
return "ClassPojo [Contact_Email = "+Contact_Email+", Contact_Mobile = "+Contact_Mobile+", BillAmount = "+BillAmount+", OrderDate = "+OrderDate+", contactID = "+contactID+", OrderID = "+OrderID+", PaidAmount = "+PaidAmount+", Contact_Name = "+Contact_Name+", DiscountAmount = "+DiscountAmount+", BalanceAmount = "+BalanceAmount+"]";
}
}
