package sms19.listview.newproject.model;

import java.util.ArrayList;

/**
 * Created by android on 24/2/17.
 */

public class ContactRegistrationModel {
    private ArrayList<ContactRegistration> ContactRegistration;

    public ArrayList<ContactRegistration> getContactRegistration ()
    {
        return ContactRegistration;
    }

    public void setContactRegistration (ArrayList<ContactRegistration> ContactRegistration)
    {
        this.ContactRegistration = ContactRegistration;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ContactRegistration = "+ContactRegistration+"]";
    }
}
