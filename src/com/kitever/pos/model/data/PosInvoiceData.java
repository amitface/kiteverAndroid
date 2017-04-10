package com.kitever.pos.model.data;

import java.util.List;

public class PosInvoiceData {

	private List<FetchInvoice> FetchInvoice;

	public List<FetchInvoice> getFetchInvoice() {
		return FetchInvoice;
	}

	public void setFetchInvoice(List<FetchInvoice> fetchInvoice) {
		FetchInvoice = fetchInvoice;
	}

	public PosInvoiceData(
			List<com.kitever.pos.model.data.FetchInvoice> fetchInvoice) {
		super();
		FetchInvoice = fetchInvoice;
	}
}
