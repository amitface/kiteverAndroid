
package sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class CartModel {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Product")
    private List<sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart.Product> mProduct;
    @SerializedName("Status")
    private Boolean mStatus;

    public CartModel(List<Product> mProduct) {
        this.mProduct = mProduct;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String Message) {
        mMessage = Message;
    }

    public List<sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart.Product> getProduct() {
        return mProduct;
    }

    public void setProduct(List<sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart.Product> Product) {
        mProduct = Product;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean Status) {
        mStatus = Status;
    }

}
