package com.kitever.pos.model.data;

import java.io.Serializable;


@SuppressWarnings("serial")
public class FetchSelectedProductModelData implements Serializable {

    private String categoryName, brandName, productName, productID,
            perUnitPrice, units, userID, qty, taxApplied, pricewithTax, productImage;
    private int AvailableStock = 0;
    public FetchSelectedProductModelData(String categoryName, String brandName,
                                         String productName, String productID, String perUnitPrice,
                                         String units, String userID, String qty, String taxApplied, String pricewithTax, String productImage,int AvailableStock) {
        super();
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.productName = productName;
        this.productID = productID;
        this.perUnitPrice = perUnitPrice;
        this.units = units;
        this.userID = userID;
        this.qty = qty;
        this.taxApplied = taxApplied;
        this.pricewithTax = pricewithTax;
        this.productImage = productImage;
        this.AvailableStock = AvailableStock;
    }

    public FetchSelectedProductModelData() {

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

    public String getQuantity() {
        return qty;
    }

    public String getTaxApplied() {
        return taxApplied;
    }

    public String getpricewithTax() {
        return pricewithTax;
    }

    public String getproductImage() {
        return productImage;
    }

    public int getAvailableStock() {
        return AvailableStock;
    }

    public void setAvailableStock(int availableStock) {
        AvailableStock = availableStock;
    }

    /*public FetchSelectedProductModelData(Parcel in) {
		String[] data= new String[10];
		this.categoryName =data[0];
		this.brandName = data[1];
		this.productName = data[2];
		this.productID = data[3];
		this.perUnitPrice = data[4];
		this.units = data[5];
		this.userID = data[6];
		this.qty = data[7];
		this.taxApplied = data[8];
		this.pricewithTax=data[9];
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[]{this.categoryName,this.brandName ,this.productName,this.productID,this.perUnitPrice,this.units,this.userID ,this.qty,this.taxApplied,this.pricewithTax});
	}
	
	public static final Parcelable.Creator<FetchSelectedProductModelData> CREATOR= new Parcelable.Creator<FetchSelectedProductModelData>() {
			@Override
			public FetchSelectedProductModelData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new FetchSelectedProductModelData(source);  //using parcelable constructor
			}
	
			@Override
			public FetchSelectedProductModelData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new FetchSelectedProductModelData[size];
			}
		};
*/
}
