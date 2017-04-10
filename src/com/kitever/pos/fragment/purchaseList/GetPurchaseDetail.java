
package com.kitever.pos.fragment.purchaseList;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetPurchaseDetail {

    @SerializedName("BillAmount")
    private Double mBillAmount;
    @SerializedName("IsTaxIncluded")
    private Boolean mIsTaxIncluded;
    @SerializedName("ProductName")
    private String mProductName;
    @SerializedName("Quantity")
    private Long mQuantity;
    @SerializedName("TaxAmount")
    private Double mTaxAmount;
    @SerializedName("TaxApplied")
    private String mTaxApplied;

    public Double getBillAmount() {
        return mBillAmount;
    }

    public void setBillAmount(Double BillAmount) {
        mBillAmount = BillAmount;
    }

    public Boolean getIsTaxIncluded() {
        return mIsTaxIncluded;
    }

    public void setIsTaxIncluded(Boolean IsTaxIncluded) {
        mIsTaxIncluded = IsTaxIncluded;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String ProductName) {
        mProductName = ProductName;
    }

    public Long getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Long Quantity) {
        mQuantity = Quantity;
    }

    public Double getTaxAmount() {
        return mTaxAmount;
    }

    public void setTaxAmount(Double TaxAmount) {
        mTaxAmount = TaxAmount;
    }

    public String getTaxApplied() {
        return mTaxApplied;
    }

    public void setTaxApplied(String TaxApplied) {
        mTaxApplied = TaxApplied;
    }

}
