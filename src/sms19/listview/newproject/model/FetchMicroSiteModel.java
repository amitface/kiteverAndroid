package sms19.listview.newproject.model;

import java.util.ArrayList;

/**
 * Created by android on 16/2/17.
 */


public class FetchMicroSiteModel
{
    private ArrayList<MicrositeDetails> MicrositeDetails;

    public ArrayList<MicrositeDetails> getMicrositeDetails ()
    {
        return MicrositeDetails;
    }

    public void setMicrositeDetails (ArrayList<MicrositeDetails> MicrositeDetails)
    {
        this.MicrositeDetails = MicrositeDetails;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [MicrositeDetails = "+MicrositeDetails+"]";
    }
}