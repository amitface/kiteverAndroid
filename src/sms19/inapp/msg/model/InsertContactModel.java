package sms19.inapp.msg.model;

import java.util.ArrayList;

/**
 * Created by android on 13/2/17.
 */

public class InsertContactModel {

    private ArrayList<InsertContacts> InsertContacts;

    public ArrayList<InsertContacts> getInsertContacts ()
    {
        return InsertContacts;
    }

    public void setInsertContacts (ArrayList<InsertContacts> InsertContacts)
    {
        this.InsertContacts = InsertContacts;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [InsertContacts = "+InsertContacts+"]";
    }

    public class InsertContacts
    {
        private String UserType;

        private String Contact_Mobile;

        private String User_ID;

        private String contact_email;

        private String contact_id;

        private String countryCode;

        private String Contact_DOB;

        private String Contact_Anniversary;

        private String ImageUrl;

        private String Contact_Name;

        private String CompanyName;
        private String Address;
        private String State;
        private String City;

        public String getUserType ()
        {
            return UserType;
        }

        public void setUserType (String UserType)
        {
            this.UserType = UserType;
        }

        public String getContact_Mobile ()
        {
            return Contact_Mobile;
        }

        public void setContact_Mobile (String Contact_Mobile)
        {
            this.Contact_Mobile = Contact_Mobile;
        }

        public String getUser_ID ()
        {
            return User_ID;
        }

        public void setUser_ID (String User_ID)
        {
            this.User_ID = User_ID;
        }

        public String getContact_email ()
    {
        return contact_email;
    }

        public void setContact_email (String contact_email)
        {
            this.contact_email = contact_email;
        }

        public String getContact_id ()
        {
            return contact_id;
        }

        public void setContact_id (String contact_id)
        {
            this.contact_id = contact_id;
        }

        public String getCountryCode ()
        {
            return countryCode;
        }

        public void setCountryCode (String countryCode)
        {
            this.countryCode = countryCode;
        }

        public String getContact_DOB ()
        {
            return Contact_DOB;
        }

        public void setContact_DOB (String Contact_DOB)
        {
            this.Contact_DOB = Contact_DOB;
        }

        public String getContact_Anniversary ()
        {
            return Contact_Anniversary;
        }

        public void setContact_Anniversary (String Contact_Anniversary)
        {
            this.Contact_Anniversary = Contact_Anniversary;
        }

        public String getImageUrl ()
        {
            return ImageUrl;
        }

        public void setImageUrl (String ImageUrl)
        {
            this.ImageUrl = ImageUrl;
        }

        public String getContact_Name ()
        {
            return Contact_Name;
        }

        public void setContact_Name (String Contact_Name)
        {
            this.Contact_Name = Contact_Name;
        }

        public String getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(String companyName) {
            CompanyName = companyName;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [UserType = "+UserType+", Contact_Mobile = "+Contact_Mobile+", User_ID = "+User_ID+", contact_email = "+contact_email+", contact_id = "+contact_id+", countryCode = "+countryCode+", Contact_DOB = "+Contact_DOB+", Contact_Anniversary = "+Contact_Anniversary+", ImageUrl = "+ImageUrl+", Contact_Name = "+Contact_Name+"]";
        }
    }

}
