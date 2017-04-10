package com.kitever.pos.model.data;

public class FetchProductModelData {

	private String categoryName, brandName, productName, productID,
			perUnitPrice, units, userID, cUG, taxApplied,lastQuantity;
	private String productImage,priceWithTax;
	private int AvailableStock = 0;
	private int position;
	public FetchProductModelData(String categoryName, String brandName,
			String productName, String productID, String perUnitPrice,
			String units, String userID, String cUG, String taxApplied, String productImage, String priceWithTax, int position,int AvailableStock) {
		super();
		this.categoryName = categoryName;
		this.brandName = brandName;
		this.productName = productName;
		this.productID = productID;
		this.perUnitPrice = perUnitPrice;
		this.units = units;
		this.userID = userID;
		this.cUG = cUG;
		this.taxApplied = taxApplied;
		this.position=position;
		this.productImage=productImage;
		this.priceWithTax=priceWithTax;
		this.lastQuantity="0";
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

	public String getUserID() {
		return userID;
	}

	public String getcUG() {
		return cUG;
	}

	public String getTaxApplied() {
		return taxApplied;
	}
	
	public String getproductImage()
	{
		return productImage;
	}
	
	public String getpriceWithTax()
	{
		return priceWithTax;
	}
	
	public Integer getPosition() {
		return position;
	}
	
	public void  SetlastQuantity(String lastQuantity)
	{
		
		this.lastQuantity=(lastQuantity=="")?"0":lastQuantity;
	}
	
	public String getlastQuantity()
	{
		return lastQuantity;
	}

	public int getAvailableStock() {
		return AvailableStock;
	}

	public void setAvailableStock(int availableStock) {
		AvailableStock = availableStock;
	}
}
