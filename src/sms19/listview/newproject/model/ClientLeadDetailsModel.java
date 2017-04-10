package sms19.listview.newproject.model;

import java.util.ArrayList;

/**
 * Created by android on 21/2/17.
 */

public class ClientLeadDetailsModel {

    private ArrayList<ClientLeadDetails> ClientLeadDetails;

    public ArrayList<ClientLeadDetails> getClientLeadDetails ()
    {
        return ClientLeadDetails;
    }

    public void setClientLeadDetails (ArrayList<ClientLeadDetails> ClientLeadDetails)
    {
        this.ClientLeadDetails = ClientLeadDetails;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ClientLeadDetails = "+ClientLeadDetails+"]";
    }
}