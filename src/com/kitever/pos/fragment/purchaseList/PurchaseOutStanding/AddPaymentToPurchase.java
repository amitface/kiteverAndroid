
package com.kitever.pos.fragment.purchaseList.PurchaseOutStanding;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class AddPaymentToPurchase {

    @SerializedName("Balance")
    private Double mBalance = 0.0;
    @SerializedName("BillAmount")
    private Double mBillAmount = 0.0;
    @SerializedName("id")
    private Long mId;
    @SerializedName("PaidAmount")
    private Double mPaidAmount = 0.0;

    public Double getBalance() {
        return mBalance;
    }

    public void setBalance(Double Balance) {
        mBalance = Balance;
    }

    public Double getBillAmount() {
        return mBillAmount;
    }

    public void setBillAmount(Double BillAmount) {
        mBillAmount = BillAmount;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Double getPaidAmount() {
        return mPaidAmount;
    }

    public void setPaidAmount(Double PaidAmount) {
        mPaidAmount = PaidAmount;
    }

}
