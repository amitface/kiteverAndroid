package com.kitever.pos.model.data;

public class PosCategoryModelData {
	private String categoryID;
	private String categoryCode;
	private String categoryName;
	private String parentCategory;
	private String ParentCategoryID;
	private String type;
	private String description;
	private String createdDate;
	private String userID;
	private String isActive;


	public PosCategoryModelData(String categoryID, String categoryCode,
			String categoryName, String parentCategory,String ParentCategoryID, String type,
			String description, String createdDate, String userID,
			String isActive) {
		super();
		this.categoryID = categoryID;
		this.categoryCode = categoryCode;
		this.categoryName = categoryName;
		this.parentCategory = parentCategory;
		this.ParentCategoryID=ParentCategoryID;
		this.type = type;
		this.description = description;
		this.createdDate = createdDate;
		this.userID = userID;
		this.isActive = isActive;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getParentCategory() {
		return parentCategory;
	}

	public String getParentCategoryID(){return ParentCategoryID;}
	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getUserID() {
		return userID;
	}

	public String getIsActive() {
		return isActive;
	}
	
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
