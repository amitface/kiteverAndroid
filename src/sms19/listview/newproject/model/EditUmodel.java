package sms19.listview.newproject.model;

import java.util.List;

public class EditUmodel
{
	private List<updatep>UpdateUserProfile;
	public List<updatep> getUpdateUserProfile()
	{
		return UpdateUserProfile;
	}
	public void setUpdateUserProfile(List<updatep> updateUserProfile) 
	{
		UpdateUserProfile = updateUserProfile;
	}
	
	public class updatep
	{
	private String Msg;
	

	public String getMsg() 
	{
		return Msg;
	}

	public void setMsg(String msg) 
	{
		Msg = msg;
	}

	
	}
	
}
