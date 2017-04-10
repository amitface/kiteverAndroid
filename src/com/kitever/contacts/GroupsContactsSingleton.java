package com.kitever.contacts;

import java.util.ArrayList;

import com.kitever.models.ContactInfo;

public class GroupsContactsSingleton {
	
	private static GroupsContactsSingleton mInstance;

	private ArrayList<ContactInfo> contactInfos;

	private GroupsContactsSingleton() {

	}

	public static synchronized GroupsContactsSingleton getInstance() {
		if (mInstance == null) {
			mInstance = new GroupsContactsSingleton();
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
