
package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class FetchGroupDetailsInbox {
	
	List<GroupDetailsInbox> GroupDetails;

	public List<GroupDetailsInbox> getGroupDetails() {
		return GroupDetails;
	}

	public void setGroupDetails(List<GroupDetailsInbox> groupDetails) {
		GroupDetails = groupDetails;
	}
	
	//get size of group added into list
		public int getListSize()
		{
			int i=GroupDetails.size();
			return i;
		}
	
	public class GroupDetailsInbox implements Serializable{
		
		private static final long serialVersionUID = 1L;
			
		private String group_name;
		private String Column1;
		private String EmergencyType;
		private String EmergencyMessage;
	
		public String getColumn1() {
			return Column1;
		}
		public void setColumn1(String column1) {
			Column1 = column1;
		}
		public String getGroup_name() {
			return group_name;
		}
		public void setGroup_name(String group_name) {
			this.group_name = group_name;
		}
		public String getEmergencyType() {
			return EmergencyType;
		}
		public void setEmergencyType(String emergencyType) {
			EmergencyType = emergencyType;
		}
		public String getEmergencyMessage() {
			return EmergencyMessage;
		}
		public void setEmergencyMessage(String emergencyMessage) {
			EmergencyMessage = emergencyMessage;
		}
	
		
	}
	

}
