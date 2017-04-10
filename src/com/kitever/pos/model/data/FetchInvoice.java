package com.kitever.pos.model.data;


public class FetchInvoice {
    private String Contact_Mobile;

    private Long OrderDate;

    private String InvoiceID;

    private String ChequeDate;

    private String PayModeNo;

    private String UserID;

    private String TaxAmount;

    private String BankName;

    private String DiscountAmount;

    private String CustomerName;

    private String InvoiceCode;

    private String BillAmount;

    private String PayMode;
    private String ContactID;


    private String contactName;

    private String OrderCode;

    private String PaidAmount;

    private String BalanceAmount;

    private String OrderMasterID;

    private String EmailID;

    public String getContact_Mobile() {
        return Contact_Mobile;
    }

    public void setContact_Mobile(String Contact_Mobile) {
        this.Contact_Mobile = Contact_Mobile;
    }

    public Long getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Long OrderDate) {
        this.OrderDate = OrderDate;
    }

    public String getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(String InvoiceID) {
        this.InvoiceID = InvoiceID;
    }

    public String getChequeDate() {
        return ChequeDate;
    }

    public void setChequeDate(String ChequeDate) {
        this.ChequeDate = ChequeDate;
    }

    public String getPayModeNo() {
        return PayModeNo;
    }

    public void setPayModeNo(String PayModeNo) {
        this.PayModeNo = PayModeNo;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(String TaxAmount) {
        this.TaxAmount = TaxAmount;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String BankName) {
        this.BankName = BankName;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String DiscountAmount) {
        this.DiscountAmount = DiscountAmount;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getInvoiceCode() {
        return InvoiceCode;
    }

    public void setInvoiceCode(String InvoiceCode) {
        this.InvoiceCode = InvoiceCode;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(String BillAmount) {
        this.BillAmount = BillAmount;
    }

    public String getPayMode() {
        return PayMode;
    }

    public void setPayMode(String PayMode) {
        this.PayMode = PayMode;
    }

    public String getContactID() {
        return ContactID;
    }

    public void setContactID(String contactID) {
        ContactID = contactID;
    }


    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String OrderCode) {
        this.OrderCode = OrderCode;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(String PaidAmount) {
        this.PaidAmount = PaidAmount;
    }

    public String getBalanceAmount() {
        return BalanceAmount;
    }

    public void setBalanceAmount(String BalanceAmount) {
        this.BalanceAmount = BalanceAmount;
    }

    public String getOrderMasterID() {
        return OrderMasterID;
    }

    public void setOrderMasterID(String OrderMasterID) {
        this.OrderMasterID = OrderMasterID;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String EmailID) {
        this.EmailID = EmailID;
    }

    @Override
    public String toString() {
        return "ClassPojo [Contact_Mobile = " + Contact_Mobile + ", OrderDate = " + OrderDate + ", InvoiceID = " + InvoiceID + ", ChequeDate = " + ChequeDate + ", PayModeNo = " + PayModeNo + ", UserID = " + UserID + ", TaxAmount = " + TaxAmount + ", BankName = " + BankName + ", DiscountAmount = " + DiscountAmount + ", CustomerName = " + CustomerName + ", InvoiceCode = " + InvoiceCode + ", BillAmount = " + BillAmount + ", PayMode = " + PayMode + ", contactName = " + contactName + ", OrderCode = " + OrderCode + ", PaidAmount = " + PaidAmount + ", BalanceAmount = " + BalanceAmount + ", OrderMasterID = " + OrderMasterID + ", EmailID = " + EmailID + "]";
    }
}

