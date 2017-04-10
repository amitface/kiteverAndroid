package com.kitever.pos.model.data;

public class ProductModelData {
	private String categoryName;
	private String brandName;
	private String productName;
	private String productID;
	private String perUnitPrice;
	private String units;
	private String productImage;
	private String isActive;
	private String description;
    private String taxApplied;
    private String PriceWithTax;
    private String AvailableStock;

	public ProductModelData(String categoryName, String brandName,
			String productName, String productID, String perUnitPrice,
			String units, String productImage, String isActive,String description, String taxApplied, String PriceWithTax, String AvailableStock) {
		super();
		this.categoryName = categoryName;
		this.brandName = brandName;
		this.productName = productName;
		this.productID = productID;
		this.perUnitPrice = perUnitPrice;
		this.units = units;
		this.productImage = productImage;
		this.isActive = isActive;
		this.description=description;
		this.taxApplied=taxApplied;
		this.PriceWithTax=PriceWithTax;
		this.AvailableStock=AvailableStock;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductID() {
		return productID;
	}

	public String getPerUnitPrice() {
		return perUnitPrice;
	}

	public String getUnits() {
		return units;
	}

	public String getProductImage() {
		return productImage;
	}

	public String getIsActive() {
		return isActive;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getTaxApplied()
	{
		return taxApplied;
	}
	
	public String getPriceWithTax()
	{
		return PriceWithTax;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getAvailableStock() {
		return AvailableStock;
	}

	public void setAvailableStock(String availableStock) {
		AvailableStock = availableStock;
	}
}
