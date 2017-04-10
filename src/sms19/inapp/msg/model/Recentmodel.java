package sms19.inapp.msg.model;

import java.util.Comparator;

public class Recentmodel {
	private byte[] userpic = null;
	private String displayname = "";
	private String customStatus = "";
	private String lastmsg = "";
	private String lastmsg_t = "";
	private int unreadcount = 0;
	private String remote_jid = "";
	private String status = "";
	private int isgroup = 0;
	private String usernumber = "";
	private int isStranger = 0;
	private int isUserblock=0;



	private int isRegister=0;
	private int totalMessageCount=0;
	private String userPicUrl=null;

	//Amit
	private String sent_msg_success = "";
	private String deliver_msg_success = "";
	private String read_msg_success = "";
	private boolean mine = false;

	public boolean getMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public String getSent_msg_success() {
		return sent_msg_success;
	}

	public void setSent_msg_success(String sent_msg_success) {
		this.sent_msg_success = sent_msg_success;
	}

	public String getDeliver_msg_success() {
		return deliver_msg_success;
	}

	public void setDeliver_msg_success(String deliver_msg_success) {
		this.deliver_msg_success = deliver_msg_success;
	}

	public String getRead_msg_success() {
		return read_msg_success;
	}

	public void setRead_msg_success(String read_msg_success) {
		this.read_msg_success = read_msg_success;
	}
	//Amit Over

	public String getUserPicUrl() {
		return userPicUrl;
	}

	public void setUserPicUrl(String userPicUrl) {
		this.userPicUrl = userPicUrl;
	}

	public int getIsStranger() {
		return isStranger;
	}

	public void setIsStranger(int isStranger) {
		this.isStranger = isStranger;
	}

	public String getUsernumber() {
		return usernumber;
	}

	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}

	public int getIsgroup() {
		return isgroup;
	}

	public void setIsgroup(int isgroup) {
		this.isgroup = isgroup;
	}

	public int getUnreadcount() {
		return unreadcount;
	}

	public void setUnreadcount(int unreadcount) {
		this.unreadcount = unreadcount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemote_jid() {
		return remote_jid;
	}

	public void setRemote_jid(String remote_jid) {
		this.remote_jid = remote_jid;
	}

	public byte[] getUserpic() {
		return userpic;
	}

	public void setUserpic(byte[] userpic) {
		this.userpic = userpic;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getLastmsg() {
		return lastmsg;
	}

	public int getIsUserblock() {
		return isUserblock;
	}

	public void setIsUserblock(int isUserblock) {
		this.isUserblock = isUserblock;
	}

	public void setLastmsg(String lastmsg) {
		this.lastmsg = lastmsg;
	}

	public String getLastmsg_t() {
		return lastmsg_t;
	}

	public void setLastmsg_t(String lastmsg_t) {
		this.lastmsg_t = lastmsg_t;
	}

	public String getCustomStatus() {
		return customStatus;
	}

	public void setCustomStatus(String customStatus) {
		this.customStatus = customStatus;
	}
	 public class CustomComparatorSortByName implements Comparator<Recentmodel> {
	        @Override
	        public int compare(Recentmodel o1, Recentmodel o2) {
	            return o1.displayname.trim().compareToIgnoreCase(o2.displayname.trim());
	        }
	    }
	 
	 public class CustomComparatorSortByNameDec implements Comparator<Recentmodel> {
	        @Override
	        public int compare(Recentmodel o1, Recentmodel o2) {
	            return o2.displayname.trim().compareToIgnoreCase(o1.displayname.trim());
	        }
	    }
	 
	 public class CustomComparatorSortByRecent implements Comparator<Recentmodel> {
	        @Override
	        public int compare(Recentmodel o1, Recentmodel o2) {
	            return o2.getLastmsg_t().compareTo(o1.getLastmsg_t());
	        }
	    }
	 public class CustomComparatorSortByRecentDec implements Comparator<Recentmodel> {
	        @Override
	        public int compare(Recentmodel o1, Recentmodel o2) {
	            return o1.getLastmsg_t().compareTo(o2.getLastmsg_t());
	        }
	    }
	 
	 public class CustomComparatorSortByUnRecent implements Comparator<Recentmodel> {
	        @Override
	        public int compare(Recentmodel o1, Recentmodel o2) {
	            return o1.getLastmsg_t().compareTo(o2.getLastmsg_t());
	        }
	    }
	 
	 
	 public class CustomComparatorSortByMostContected implements Comparator<Recentmodel> {
	        @Override
	        public int compare(Recentmodel o1, Recentmodel o2) {
	        	
	            return o2.getTotalMessageCount()-o1.getTotalMessageCount();
	        }
	    }
	 public class CustomComparatorSortByMostContectedDec implements Comparator<Recentmodel> {
	        @Override
	        public int compare(Recentmodel o1, Recentmodel o2) {
	            return o2.getLastmsg_t().compareTo(o1.getLastmsg_t());
	        }
	    }
	 

	public int getIsRegister() {
		return isRegister;
	}

	public void setIsRegister(int isRegister) {
		this.isRegister = isRegister;
	}

	public int getTotalMessageCount() {
		return totalMessageCount;
	}

	public void setTotalMessageCount(int totalMessageCount) {
		this.totalMessageCount = totalMessageCount;
	}
	
}
