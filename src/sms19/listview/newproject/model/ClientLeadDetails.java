package sms19.listview.newproject.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android on 21/2/17.
 */


public class ClientLeadDetails implements Parcelable {
    private String Source;

    private String Email;

    private Long Date;

    private String Address;

    private String Mobile;

    private String Remarks;

    private String FirstName;

    private String Zip;

    private String LastName;

    private String City;

    private String cname;

    public String getSource() {
        return Source;
    }

    public void setSource(String Source) {
        this.Source = Source;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public Long getDate() {
        return Date;
    }

    public void setDate(Long Date) {
        this.Date = Date;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String Zip) {
        this.Zip = Zip;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "ClassPojo [Source = " + Source + ", Email = " + Email + ", Date = " + Date + ", Address = " + Address + ", Mobile = " + Mobile + ", Remarks = " + Remarks + ", FirstName = " + FirstName + ", Zip = " + Zip + ", LastName = " + LastName + ", City = " + City + ", cname = " + cname + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Source);
        dest.writeString(this.Email);
        dest.writeValue(this.Date);
        dest.writeString(this.Address);
        dest.writeString(this.Mobile);
        dest.writeString(this.Remarks);
        dest.writeString(this.FirstName);
        dest.writeString(this.Zip);
        dest.writeString(this.LastName);
        dest.writeString(this.City);
        dest.writeString(this.cname);
    }

    public ClientLeadDetails() {
    }

    protected ClientLeadDetails(Parcel in) {
        this.Source = in.readString();
        this.Email = in.readString();
        this.Date = (Long) in.readValue(Long.class.getClassLoader());
        this.Address = in.readString();
        this.Mobile = in.readString();
        this.Remarks = in.readString();
        this.FirstName = in.readString();
        this.Zip = in.readString();
        this.LastName = in.readString();
        this.City = in.readString();
        this.cname = in.readString();
    }

    public static final Parcelable.Creator<ClientLeadDetails> CREATOR = new Parcelable.Creator<ClientLeadDetails>() {
        @Override
        public ClientLeadDetails createFromParcel(Parcel source) {
            return new ClientLeadDetails(source);
        }

        @Override
        public ClientLeadDetails[] newArray(int size) {
            return new ClientLeadDetails[size];
        }
    };
}

