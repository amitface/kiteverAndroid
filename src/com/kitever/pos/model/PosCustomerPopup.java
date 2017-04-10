package com.kitever.pos.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class PosCustomerPopup {


    private String Name;
    private String Mobile;
    private String Email;
    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Order")
    private ArrayList<Order> mOrder;
    @SerializedName("Status")
    private String mStatus;


    public void setMessage(String Message) {
        mMessage = Message;
    }

    public void setOrder(ArrayList<com.kitever.pos.model.Order> Order) {
        mOrder = Order;
    }

    public void setStatus(String Status) {
        mStatus = Status;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public ArrayList<com.kitever.pos.model.Order> getOrder() {
        return mOrder;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getEmail() {
        return Email;
    }

    public String getmMessage() {
        return mMessage;
    }

    public List<Order> getmOrder() {
        return mOrder;
    }

    public String getMessage() {
        return mMessage;
    }
}
