package sms19.listview.newproject.model;

/**
 * Created by android on 6/2/17.
 */

public class FetchSenderIDs {
    private String Sender_Name;

    private String Sender_ID;

    public String getSender_Name() {
        return Sender_Name;
    }

    public void setSender_Name(String Sender_Name) {
        this.Sender_Name = Sender_Name;
    }

    public String getSender_ID() {
        return Sender_ID;
    }

    public void setSender_ID(String Sender_ID) {
        this.Sender_ID = Sender_ID;
    }

    @Override
    public String toString() {
        return "ClassPojo [Sender_Name = " + Sender_Name + ", Sender_ID = " + Sender_ID + "]";
    }
}