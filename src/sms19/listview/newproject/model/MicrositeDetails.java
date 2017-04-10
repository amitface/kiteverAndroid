package sms19.listview.newproject.model;

/**
 * Created by android on 16/2/17.
 */

public class MicrositeDetails {
    private String Ptype;

    private String Pagename;

    private String SiteLeads;

    private String MicroPageName;

    public String getMicroPageName() {
        return MicroPageName;
    }

    public void setMicroPageName(String microPageName) {
        MicroPageName = microPageName;
    }

    public MicrositeDetails(String ptype, String pagename, String siteLeads, String shortUrl, String siteVisits, String column1) {
        Ptype = ptype;
        Pagename = pagename;
        SiteLeads = siteLeads;
        ShortUrl = shortUrl;
        SiteVisits = siteVisits;
        Column1 = column1;
    }

    private String ShortUrl;

    private String SiteVisits;

    public String getColumn1() {
        return Column1;
    }

    public void setColumn1(String column1) {
        Column1 = column1;
    }

    private String Column1;

    public String getPtype() {
        return Ptype;
    }

    public void setPtype(String Ptype) {
        this.Ptype = Ptype;
    }

    public String getPagename() {
        return Pagename;
    }

    public void setPagename(String Pagename) {
        this.Pagename = Pagename;
    }

    public String getSiteLeads() {
        return SiteLeads;
    }

    public void setSiteLeads(String SiteLeads) {
        if (SiteLeads != null)
            this.SiteLeads = SiteLeads;
        else
            this.SiteLeads = "0";
    }

    public String getShortUrl() {
        return ShortUrl;
    }

    public void setShortUrl(String ShortUrl) {
        this.ShortUrl = ShortUrl;
    }

    public String getSiteVisits() {
        return SiteVisits;
    }

    public void setSiteVisits(String SiteVisits) {
        if (SiteLeads != null)
        this.SiteVisits = SiteVisits;
        else
            this.SiteVisits = "0";
    }

    @Override
    public String toString() {
        return "ClassPojo [Ptype = " + Ptype + ", Pagename = " + Pagename + ", SiteLeads = " + SiteLeads + ", ShortUrl = " + ShortUrl + ", SiteVisits = " + SiteVisits + "]";
    }
}