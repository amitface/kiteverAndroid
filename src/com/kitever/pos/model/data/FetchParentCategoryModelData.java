package com.kitever.pos.model.data;

public class FetchParentCategoryModelData {

	private String categoryID;
	private String categoryName;
	private String Type;

	public FetchParentCategoryModelData(String categoryID, String categoryName, String type) {
		this.categoryID = categoryID;
		this.categoryName = categoryName;
		this.Type = type;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getType() {		return Type;	}

}
