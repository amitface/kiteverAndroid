package com.kitever.contacts;


import java.util.ArrayList;

import com.kitever.models.ContactInfo;

public class ContactsSingleton {
	
	private static ContactsSingleton mInstance;
	
	private ArrayList<ContactInfo> contactInfos;
	
	private ContactsSingleton(){
		
	}
	
	public static synchronized ContactsSingleton getInstance(){
		if(mInstance==null){
			mInstance = new ContactsSingleton();
		}
		return mInstance;
	}

	public ArrayList<ContactInfo> getContactInfos() {
		return contactInfos;
	}

	public void setContactInfos(ArrayList<ContactInfo> contactInfos) {
		this.contactInfos = contactInfos;
	}
	
}
