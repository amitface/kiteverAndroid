package com.kitever.sendsms.fragments;

import java.util.ArrayList;

import sms19.listview.newproject.model.FetchGroupDetails.GroupDetails;
import sms19.listview.newproject.model.FetchSenderIDs;

public interface SmsMailInterface {
	void ChangeFragment(ArrayList<GroupDetails> groupNameTagList,
						ArrayList<String> groupsArrayList);
	void setSendersId(ArrayList<FetchSenderIDs> sendersId);
	void Backpressed();
}
