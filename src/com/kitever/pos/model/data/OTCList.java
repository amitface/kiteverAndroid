package com.kitever.pos.model.data;

import java.util.ArrayList;
import java.util.List;

public class OTCList {
	private String Status;

    private String Message;

    private List<OTC> OTC;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getMessage ()
    {
        return Message;
    }

    public void setMessage (String Message)
    {
        this.Message = Message;
    }

    public ArrayList<OTC> getOTC ()
    {
        return (ArrayList<com.kitever.pos.model.data.OTC>) OTC;
    }

    public void setOTC (List<OTC> OTC)
    {
        this.OTC = OTC;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Message = "+Message+", OTC = "+OTC+"]";
    }
}
