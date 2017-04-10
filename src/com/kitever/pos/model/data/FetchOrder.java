package com.kitever.pos.model.data;

public class FetchOrder
{
private Double BillAmount;

private String Quantity;

private String ProductName;

private Double DiscountAmount;

private Double TaxAmount;

public Double getTaxAmount() {
	return TaxAmount;
}

public void setTaxAmount(Double taxAmount) {
	TaxAmount = taxAmount;
}

public Double getOTCAmount() {
	return OTCAmount;
}

public void setOTCAmount(Double oTCAmount) {
	OTCAmount = oTCAmount;
}

public Boolean getIsInclusiveTax() {
	return isInclusiveTax;
}

public void setIsInclusiveTax(Boolean isInclusiveTax) {
	this.isInclusiveTax = isInclusiveTax;
}

public String getTaxApplied() {
	return TaxApplied;
}

public void setTaxApplied(String taxApplied) {
	TaxApplied = taxApplied;
}

public String getOTCApplied() {
	return OTCApplied;
}

public void setOTCApplied(String oTCApplied) {
	OTCApplied = oTCApplied;
}


private Double OTCAmount;

private Boolean isInclusiveTax;

private String TaxApplied;

private String OTCApplied;


public String getQuantity() {
	return Quantity;
}

public void setQuantity(String quantity) {
	Quantity = quantity;
}

public String getProductName() {
	return ProductName;
}

public void setProductName(String productName) {
	ProductName = productName;
}

public Double getDiscountAmount() {
	return DiscountAmount;
}

public void setDiscountAmount(Double discountAmount) {
	DiscountAmount = discountAmount;
}



public Double getBillAmount ()
{
return BillAmount;
}

public void setBillAmount (Double BillAmount)
{
this.BillAmount = BillAmount;
}


@Override
public String toString()
{
return "ClassPojo [BillAmount = "+BillAmount+", Quantity = "+Quantity+", ProductName = "+ProductName+", DiscountAmount = "+DiscountAmount+"]";
}
}