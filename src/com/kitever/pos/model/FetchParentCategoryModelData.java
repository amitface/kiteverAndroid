package com.kitever.pos.model;

public class FetchParentCategoryModelData {

	private String categoryID, categoryName;

	public FetchParentCategoryModelData(String categoryID, String categoryName) {
		super();
		this.categoryID = categoryID;
		this.categoryName = categoryName;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

}
