package sms19.listview.newproject.model;

import java.util.ArrayList;

/**
 * Created by android on 6/2/17.
 */

public class FetchSenderModel {


    private ArrayList <FetchSenderIDs> FetchSenderIDs;

    public ArrayList <FetchSenderIDs> getFetchSenderIDs() {
        return FetchSenderIDs;
    }

    public void setFetchSenderIDs(ArrayList <FetchSenderIDs> FetchSenderIDs) {
        this.FetchSenderIDs = FetchSenderIDs;
    }

    @Override
    public String toString() {
        return "ClassPojo [FetchSenderIDs = " + FetchSenderIDs + "]";
    }
}