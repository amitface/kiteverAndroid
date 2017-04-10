package com.kitever.pos.model.data;

import java.util.List;

public class FetchOrderForId {
	private List<FetchOrder> FetchOrder;

	public List<FetchOrder> getFetchOrder() {
		return FetchOrder;
	}

	public void setFetchOrder(List<FetchOrder> FetchOrder) {
		this.FetchOrder = FetchOrder;
	}

	public FetchOrderForId(List<FetchOrder> FetchOrder) {
		super();
		this.FetchOrder = FetchOrder;
	}
}
