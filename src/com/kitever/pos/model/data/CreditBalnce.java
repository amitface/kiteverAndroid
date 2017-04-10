package com.kitever.pos.model.data;


public class CreditBalnce
{
private String Contact_Email;

private String Contact_Mobile;

private String InvoiceID;

private String InvoiceDate;

private String PaymentDate;

private String Contact_Name;

private String DiscountAmount;

private String ReceiptNo;

private String BillAmount;

private String contactID;

private String MerchantId;

private String PaidAmount;

private String BalanceAmount;

private String Combined;

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

public String getInvoiceID ()
{
  return InvoiceID;
}

public void setInvoiceID (String InvoiceID)
{
  this.InvoiceID = InvoiceID;
}

public String getInvoiceDate ()
{
  return InvoiceDate;
}

public void setInvoiceDate (String InvoiceDate)
{
  this.InvoiceDate = InvoiceDate;
}

public String getPaymentDate ()
{
  return PaymentDate;
}

public void setPaymentDate (String PaymentDate)
{
  this.PaymentDate = PaymentDate;
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

public String getReceiptNo ()
{
  return ReceiptNo;
}

public void setReceiptNo (String ReceiptNo)
{
  this.ReceiptNo = ReceiptNo;
}

public String getBillAmount ()
{
  return BillAmount;
}

public void setBillAmount (String BillAmount)
{
  this.BillAmount = BillAmount;
}

public String getContactID ()
{
  return contactID;
}

public void setContactID (String contactID)
{
  this.contactID = contactID;
}

public String getMerchantId ()
{
  return MerchantId;
}

public void setMerchantId (String MerchantId)
{
  this.MerchantId = MerchantId;
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

@Override
public String toString()
{
  return "ClassPojo [Contact_Email = "+Contact_Email+", Contact_Mobile = "+Contact_Mobile+", InvoiceID = "+InvoiceID+", InvoiceDate = "+InvoiceDate+", PaymentDate = "+PaymentDate+", Contact_Name = "+Contact_Name+", DiscountAmount = "+DiscountAmount+", ReceiptNo = "+ReceiptNo+", BillAmount = "+BillAmount+", contactID = "+contactID+", MerchantId = "+MerchantId+", PaidAmount = "+PaidAmount+", BalanceAmount = "+BalanceAmount+"]";
}
}

