package com.kitever.pos.model.data;

public class OrderDetailModelData {

	private String OrderDate;

    private String ChequeDate;

    private String orderno;

    private String PayModeNo;

    private String UserName;

    private String TaxAmount;

    private String BankName;

    private String DiscountAmount;

    private Double BillAmount;

    private String Email;

    private String PayMode;

    private String contactName;

    private String OrderCode;

    private String Mobile;

    private String ContactID;

    private String PaidAmount;

    private Double BalanceAmount;

    private String OrderMasterID;
    
    private String Combined;

    private String InvoiceCode;

    private boolean selected;

    public String getCombined() {
		return Combined;
	}

	public void setCombined(String combined) {
		Combined = combined;
	}

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

    public OrderDetailModelData(String orderDate, String chequeDate,
			String orderno, String payModeNo, String userName,
			String taxAmount, String bankName, String discountAmount,
			Double billAmount, String email, String payMode,
			String contactName, String orderCode, String mobile,
			String contactID, String paidAmount, Double balanceAmount,
			String orderMasterID) {
		super();
		OrderDate = orderDate;
		ChequeDate = chequeDate;
		this.orderno = orderno;
		PayModeNo = payModeNo;
		UserName = userName;
		TaxAmount = taxAmount;
		BankName = bankName;
		DiscountAmount = discountAmount;
		BillAmount = billAmount;
		Email = email;
		PayMode = payMode;
		this.contactName = contactName;
		OrderCode = orderCode;
		Mobile = mobile;
		ContactID = contactID;
		PaidAmount = paidAmount;
		BalanceAmount = balanceAmount;
		OrderMasterID = orderMasterID;
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

    public Double getBillAmount ()
    {
        return BillAmount;
    }

    public void setBillAmount (Double BillAmount)
    {
        this.BillAmount = BillAmount;
    }

    public String getEmail ()
    {
        return Email;
    }

    public void setEmail (String Email)
    {
        this.Email = Email;
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

    public String getMobile ()
    {
        return Mobile;
    }

    public void setMobile (String Mobile)
    {
        this.Mobile = Mobile;
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

    public Double getBalanceAmount ()
    {
        return BalanceAmount;
    }

    public void setBalanceAmount (Double BalanceAmount)
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

    public String getInvoiceCode() {
        return InvoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        InvoiceCode = invoiceCode;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [OrderDate = "+OrderDate+", ChequeDate = "+ChequeDate+", orderno = "+orderno+", PayModeNo = "+PayModeNo+", UserName = "+UserName+", TaxAmount = "+TaxAmount+", BankName = "+BankName+", DiscountAmount = "+DiscountAmount+", BillAmount = "+BillAmount+", Email = "+Email+", PayMode = "+PayMode+", contactName = "+contactName+", OrderCode = "+OrderCode+", Mobile = "+Mobile+", ContactID = "+ContactID+", PaidAmount = "+PaidAmount+", BalanceAmount = "+BalanceAmount+", OrderMasterID = "+OrderMasterID+"]";
    }
}