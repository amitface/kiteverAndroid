package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class ChangePasswordModel {

	private List<Change> Changepassword;

	public List<Change> getChangepassword() {
		return Changepassword;
	}

	public void setChangepassword(List<Change> changepassword) {
		Changepassword = changepassword;
	}
	
	public class Change implements Serializable{

		private static final long serialVersionUID = 1L;
		
		String Msg;
		String Error;

		public String getMsg() {
			return Msg;
		}

		public void setMsg(String msg) {
			Msg = msg;
		}

		public String getError() {
			return Error;
		}

		public void setError(String error) {
			Error = error;
		}
		
		
		
	}
	
}
