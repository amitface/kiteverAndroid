package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class DeleteGroup {
//{"DeleteGroup":[{"Msg":"Delete Group Successfully"}]}
	//{"DeleteContactGroupRegistration":[{"Msg":"Delete Group Successfully"}]}
	private List<DelGroup> DeleteContactGroupRegistration;

	
	
	public static class DelGroup implements Serializable{

			private static final long serialVersionUID = 1L;
			
			private String Msg;
			private String Error;
			private String EmergencyType;
			private String EmergencyMessage;
			
			
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



	public List<DelGroup> getDeleteContactGroupRegistration() {
		return DeleteContactGroupRegistration;
	}



	public void setDeleteContactGroupRegistration(
			List<DelGroup> deleteContactGroupRegistration) {
		DeleteContactGroupRegistration = deleteContactGroupRegistration;
	}
	
}
