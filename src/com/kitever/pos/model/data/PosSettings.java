package com.kitever.pos.model.data;

import java.util.ArrayList;
import java.util.List;

public class PosSettings {

	private List<InvoiceSetting> InvoiceSetting;

	

	public PosSettings(
			List<com.kitever.pos.model.data.InvoiceSetting> invoiceSetting) {
		super();
		InvoiceSetting = invoiceSetting;
	}

	public ArrayList<InvoiceSetting> getInvoiceSetting() {
		return (ArrayList<com.kitever.pos.model.data.InvoiceSetting>) InvoiceSetting;
	}

	public void setInvoiceSetting(List<InvoiceSetting> invoiceSetting) {
		InvoiceSetting = invoiceSetting;
	}
	
	@Override
	public String toString() {
		return "PosSettings [InvoiceSetting=" + InvoiceSetting + "]";
	}

}
