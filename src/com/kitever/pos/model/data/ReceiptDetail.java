package com.kitever.pos.model.data;


public class ReceiptDetail
{
private String ReceiptNo;

private String InvoiceCode;

private String InvoiceNo;

private Long ReceiptDate;

private String ReceiptCode;

private String PayMode;

private String ChequeDate;

private String Remarks;

private String ChequeNo;

private String UserID;

private String PaidAmount;

private String BankName;

public String getReceiptNo ()
{
return ReceiptNo;
}

public void setReceiptNo (String ReceiptNo)
{
this.ReceiptNo = ReceiptNo;
}

public String getInvoiceCode ()
{
return InvoiceCode;
}

public void setInvoiceCode (String InvoiceCode)
{
this.InvoiceCode = InvoiceCode;
}

public String getInvoiceNo ()
{
return InvoiceNo;
}

public void setInvoiceNo (String InvoiceNo)
{
this.InvoiceNo = InvoiceNo;
}

public Long getReceiptDate ()
{
return ReceiptDate;
}

public void setReceiptDate (Long ReceiptDate)
{
this.ReceiptDate = ReceiptDate;
}

public String getReceiptCode ()
{
return ReceiptCode;
}

public void setReceiptCode (String ReceiptCode)
{
this.ReceiptCode = ReceiptCode;
}

public String getPayMode ()
{
return PayMode;
}

public void setPayMode (String PayMode)
{
this.PayMode = PayMode;
}

public String getChequeDate ()
{
return ChequeDate;
}

public void setChequeDate (String ChequeDate)
{
this.ChequeDate = ChequeDate;
}

public String getRemarks ()
{
return Remarks;
}

public void setRemarks (String Remarks)
{
this.Remarks = Remarks;
}

public String getChequeNo ()
{
return ChequeNo;
}

public void setChequeNo (String ChequeNo)
{
this.ChequeNo = ChequeNo;
}

public String getUserID ()
{
return UserID;
}

public void setUserID (String UserID)
{
this.UserID = UserID;
}

public String getPaidAmount ()
{
return PaidAmount;
}

public void setPaidAmount (String PaidAmount)
{
this.PaidAmount = PaidAmount;
}

public String getBankName ()
{
return BankName;
}

public void setBankName (String BankName)
{
this.BankName = BankName;
}

@Override
public String toString()
{
return "ClassPojo [ReceiptNo = "+ReceiptNo+", InvoiceCode = "+InvoiceCode+", InvoiceNo = "+InvoiceNo+", ReceiptDate = "+ReceiptDate+", ReceiptCode = "+ReceiptCode+", PayMode = "+PayMode+", ChequeDate = "+ChequeDate+", Remarks = "+Remarks+", ChequeNo = "+ChequeNo+", UserID = "+UserID+", PaidAmount = "+PaidAmount+", BankName = "+BankName+"]";
}
}
