package com.kitever.pos.model.data;

import java.util.ArrayList;

public class POSInvoicePopUp {
    
	private ArrayList<ReceiptDetail> ReceiptDetail;
	
	public ArrayList<ReceiptDetail> getReceiptDetail ()
	{
		return ReceiptDetail;
	}
	
	public void setReceiptDetail (ArrayList<ReceiptDetail> ReceiptDetail)
	{
		this.ReceiptDetail = ReceiptDetail;
	}
	
	@Override
	public String toString()
	{
		return "ClassPojo [ReceiptDetail = "+ReceiptDetail+"]";
	}
}

