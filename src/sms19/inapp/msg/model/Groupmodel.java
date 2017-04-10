package sms19.inapp.msg.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Groupmodel {
	
	
	private String groupname="",group_jid="",group_members="",created_me="";
	private byte[] user_pic=null;
	private String group_created_time="0";

	private ArrayList<Recentmodel> contactList=null;
	
	
	public String getGroupname() {
		return groupname;
	}

	public String getGroup_jid() {
		return group_jid;
	}

	public String getGroup_members() {
		return group_members;
	}

	public String getCreated_me() {
		return created_me;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public void setGroup_jid(String group_jid) {
		this.group_jid = group_jid;
	}

	public void setGroup_members(String group_members) {
		this.group_members = group_members;
	}

	public void setCreated_me(String created_me) {
		this.created_me = created_me;
	}

	public byte[] getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(byte[] user_pic) {
		this.user_pic = user_pic;
	}

	public ArrayList<Recentmodel> getContactList() {
		return contactList;
	}

	public void setContactList(ArrayList<Recentmodel> contactList) {
		this.contactList = contactList;
	}
	
	
	 public String getGroup_created_time() {
		return group_created_time;
	}

	public void setGroup_created_time(String group_created_time) {
		this.group_created_time = group_created_time;
	}
	
	
	


	@Override
	public String toString() {
		return "Groupmodel [groupname=" + groupname + ", group_jid="
				+ group_jid + ", group_members=" + group_members
				+ ", created_me=" + created_me + ", user_pic="
				+ Arrays.toString(user_pic) + ", group_created_time="
				+ group_created_time + ", contactList=" + contactList + "]";
	}





	public class CustomComparatorSortByRecentGroup implements Comparator<Groupmodel> {
	        @Override
	        public int compare(Groupmodel o1, Groupmodel o2) {
	        	
	            return Long.valueOf(o1.getGroup_created_time()).compareTo(Long.valueOf(o2.getGroup_created_time()));
	        }
	    }

	

}
