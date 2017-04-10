package com.kitever.pos.model;

public class FetchOrderDetailModelData {

	private String orderMasterID, orderDate, orderCode, contactName,
			billAmount, taxAmount, discountAmount, paidAmount, balanceAmount,
			payMode, payModeNo, chequeDate, bankName, status;

	public FetchOrderDetailModelData(String orderMasterID, String orderDate,
			String orderCode, String contactName, String billAmount,
			String taxAmount, String discountAmount, String paidAmount,
			String balanceAmount, String payMode, String payModeNo,
			String chequeDate, String bankName, String status) {
		super();
		this.orderMasterID = orderMasterID;
		this.orderDate = orderDate;
		this.orderCode = orderCode;
		this.contactName = contactName;
		this.billAmount = billAmount;
		this.taxAmount = taxAmount;
		this.discountAmount = discountAmount;
		this.paidAmount = paidAmount;
		this.balanceAmount = balanceAmount;
		this.payMode = payMode;
		this.payModeNo = payModeNo;
		this.chequeDate = chequeDate;
		this.bankName = bankName;
		this.status = status;
	}

	public String getOrderMasterID() {
		return orderMasterID;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public String getContactName() {
		return contactName;
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

	public String getStatus() {
		return status;
	}

}
