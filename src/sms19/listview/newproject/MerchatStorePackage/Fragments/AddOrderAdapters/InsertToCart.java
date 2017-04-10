package sms19.listview.newproject.MerchatStorePackage.Fragments.AddOrderAdapters;

import sms19.listview.newproject.MerchatStorePackage.Model.Product;

/**
 * Created by android on 5/4/17.
 */

public interface InsertToCart {
    void insertItem(Product product, Integer Quantity);
    void insertCartItem(String productId, Integer Quantity);
    void updateCartItem(String json);
}
