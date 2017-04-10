package com.kitever.pos.model.data;

public class FetchUnitModelData {

	private String iD, unitName, convertionGroup;

	public FetchUnitModelData(String iD, String unitName, String convertionGroup) {
		super();
		this.iD = iD;
		this.unitName = unitName;
		this.convertionGroup = convertionGroup;
	}

	public String getiD() {
		return iD;
	}

	public String getUnitName() {
		return unitName;
	}

	public String getConvertionGroup() {
		return convertionGroup;
	}

}
