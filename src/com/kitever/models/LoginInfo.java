package com.kitever.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginInfo implements Parcelable {

	public String id;
	public String mobile;
	public String status;
	public String uid;
	public String userlogin;
	public String balance;
	public String password;
	public String LoginType;

	@Override
	public String toString() {
		return "LoginInfo [id=" + id + ", mobile=" + mobile + ", status="
				+ status + ", uid=" + uid + ", userlogin=" + userlogin
				+ ", balance=" + balance + ", password=" + password
				+ ", LoginType=" + LoginType + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(mobile);
		dest.writeString(status);
		dest.writeString(uid);
		dest.writeString(userlogin);
		dest.writeString(balance);
		dest.writeString(password);
		dest.writeString(LoginType);
	}

}
