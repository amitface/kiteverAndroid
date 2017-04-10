package com.kitever.sendsms;

import java.util.ArrayList;

import com.kitever.models.ContactInfo;

public class SendSmsContactsSingleton {
	
	private static SendSmsContactsSingleton mInstance;
	
	private ArrayList<ContactInfo> contactInfos;
	
	private ArrayList<ContactInfo> emailInfos;
	
	private SendSmsContactsSingleton(){
		
	}
	
	public static synchronized SendSmsContactsSingleton getInstance(){
		if(mInstance==null){
			mInstance = new SendSmsContactsSingleton();
		}
		return mInstance;
	}

	public ArrayList<ContactInfo> getEmailInfos() {
		return emailInfos;
	}

	public void setEmailInfos(ArrayList<ContactInfo> emailInfos) {
		this.emailInfos = emailInfos;
	}

	public ArrayList<ContactInfo> getContactInfos() {
		return contactInfos;
	}

	public void setContactInfos(ArrayList<ContactInfo> contactInfos) {
		this.contactInfos = contactInfos;
	}
	
}
