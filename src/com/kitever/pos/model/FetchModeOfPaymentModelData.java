package com.kitever.pos.model;

public class FetchModeOfPaymentModelData {

	String id, mode;

	public FetchModeOfPaymentModelData(String id, String mode) {
		super();
		this.id = id;
		this.mode = mode;
	}

	public String getId() {
		return id;
	}

	public String getMode() {
		return mode;
	}

}
