package com.kitever.pos.model.data;


public class CustomerOrderDetail
{
private String OrderDate;

private String ChequeDate;

private String orderno;

private String PayModeNo;

private String UserName;

private String TaxAmount;

private String BankName;

private String DiscountAmount;

private String BillAmount;

private String PayMode;

private String contactName;

private String OrderCode;

private String ContactID;

private String PaidAmount;

private String BalanceAmount;

private String OrderMasterID;

public String getOrderDate ()
{
return OrderDate;
}

public void setOrderDate (String OrderDate)
{
this.OrderDate = OrderDate;
}

public String getChequeDate ()
{
return ChequeDate;
}

public void setChequeDate (String ChequeDate)
{
this.ChequeDate = ChequeDate;
}

public String getOrderno ()
{
return orderno;
}

public void setOrderno (String orderno)
{
this.orderno = orderno;
}

public String getPayModeNo ()
{
return PayModeNo;
}

public void setPayModeNo (String PayModeNo)
{
this.PayModeNo = PayModeNo;
}

public String getUserName ()
{
return UserName;
}

public void setUserName (String UserName)
{
this.UserName = UserName;
}

public String getTaxAmount ()
{
return TaxAmount;
}

public void setTaxAmount (String TaxAmount)
{
this.TaxAmount = TaxAmount;
}

public String getBankName ()
{
return BankName;
}

public void setBankName (String BankName)
{
this.BankName = BankName;
}

public String getDiscountAmount ()
{
return DiscountAmount;
}

public void setDiscountAmount (String DiscountAmount)
{
this.DiscountAmount = DiscountAmount;
}

public String getBillAmount ()
{
return BillAmount;
}

public void setBillAmount (String BillAmount)
{
this.BillAmount = BillAmount;
}

public String getPayMode ()
{
return PayMode;
}

public void setPayMode (String PayMode)
{
this.PayMode = PayMode;
}

public String getContactName ()
{
return contactName;
}

public void setContactName (String contactName)
{
this.contactName = contactName;
}

public String getOrderCode ()
{
return OrderCode;
}

public void setOrderCode (String OrderCode)
{
this.OrderCode = OrderCode;
}

public String getContactID ()
{
return ContactID;
}

public void setContactID (String ContactID)
{
this.ContactID = ContactID;
}

public String getPaidAmount ()
{
return PaidAmount;
}

public void setPaidAmount (String PaidAmount)
{
this.PaidAmount = PaidAmount;
}

public String getBalanceAmount ()
{
return BalanceAmount;
}

public void setBalanceAmount (String BalanceAmount)
{
this.BalanceAmount = BalanceAmount;
}

public String getOrderMasterID ()
{
return OrderMasterID;
}

public void setOrderMasterID (String OrderMasterID)
{
this.OrderMasterID = OrderMasterID;
}

@Override
public String toString()
{
return "ClassPojo [OrderDate = "+OrderDate+", ChequeDate = "+ChequeDate+", orderno = "+orderno+", PayModeNo = "+PayModeNo+", UserName = "+UserName+", TaxAmount = "+TaxAmount+", BankName = "+BankName+", DiscountAmount = "+DiscountAmount+", BillAmount = "+BillAmount+", PayMode = "+PayMode+", contactName = "+contactName+", OrderCode = "+OrderCode+", ContactID = "+ContactID+", PaidAmount = "+PaidAmount+", BalanceAmount = "+BalanceAmount+", OrderMasterID = "+OrderMasterID+"]";
}
}
