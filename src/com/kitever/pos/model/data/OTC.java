package com.kitever.pos.model.data;


public class OTC
{
private String Amount;

private String OTCID;

private String IsActive;

private String OTC;

private String OTCSetting;

private String Code;

public String getAmount ()
{
return Amount;
}

public void setAmount (String Amount)
{
this.Amount = Amount;
}

public String getOTCID ()
{
return OTCID;
}

public void setOTCID (String OTCID)
{
this.OTCID = OTCID;
}

public String getIsActive ()
{
return IsActive;
}

public void setIsActive (String IsActive)
{
this.IsActive = IsActive;
}

public String getOTC ()
{
return OTC;
}

public void setOTC (String OTC)
{
this.OTC = OTC;
}

public String getOTCSetting ()
{
return OTCSetting;
}

public void setOTCSetting (String OTCSetting)
{
this.OTCSetting = OTCSetting;
}

public String getCode ()
{
return Code;
}

public void setCode (String Code)
{
this.Code = Code;
}

@Override
public String toString()
{
return "ClassPojo [Amount = "+Amount+", OTCID = "+OTCID+", IsActive = "+IsActive+", OTC = "+OTC+", OTCSetting = "+OTCSetting+", Code = "+Code+"]";
}
}


