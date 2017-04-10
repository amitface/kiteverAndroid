package com.kitever.pos.model;

public class OrderDetailModelData {

	private String orderMasterID, orderCode, orderDate, userName, billAmount,
			taxAmount, discountAmount, paidAmount, balanceAmount, payMode,
			payModeNo, chequeDate, bankName, contactName;

	public OrderDetailModelData(String orderMasterID, String orderCode,
			String orderDate, String userName, String billAmount,
			String taxAmount, String discountAmount, String paidAmount,
			String balanceAmount, String payMode, String payModeNo,
			String chequeDate, String bankName, String contactName) {
		super();
		this.orderMasterID = orderMasterID;
		this.orderCode = orderCode;
		this.orderDate = orderDate;
		this.userName = userName;
		this.billAmount = billAmount;
		this.taxAmount = taxAmount;
		this.discountAmount = discountAmount;
		this.paidAmount = paidAmount;
		this.balanceAmount = balanceAmount;
		this.payMode = payMode;
		this.payModeNo = payModeNo;
		this.chequeDate = chequeDate;
		this.bankName = bankName;
		this.contactName = contactName;
	}

	public String getOrderMasterID() {
		return orderMasterID;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public String getUserName() {
		return userName;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public String getBalanceAmount() {
		return balanceAmount;
	}

	public String getPayMode() {
		return payMode;
	}

	public String getPayModeNo() {
		return payModeNo;
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public String getBankName() {
		return bankName;
	}

	public String getContactName() {
		return contactName;
	}

}
