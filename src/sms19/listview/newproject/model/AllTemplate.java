package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class AllTemplate
{
	private List<TempAll> UserAllTemplateListDetail;
	public List<TempAll> getUserAllTemplateListDetail() 
	{
		return UserAllTemplateListDetail;
	}

	public void setUserAllTemplateListDetail(List<TempAll> userAllTemplateListDetail)
{
		UserAllTemplateListDetail = userAllTemplateListDetail;
	}
	public class TempAll implements Serializable
	{
		
	//{"UserAllTemplateListDetail":[{"template_id":17805,"m_id":10043,"user_id":13110404,"template_title":"India","template":"is good"},	
	
	private static final long serialVersionUID = 1L;
	
	private String template_id;
	private String user_id;
	private String template;
	private String template_title;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getTemplate_title() {
		return template_title;
	}
	public void setTemplate_title(String template_title) {
		this.template_title = template_title;
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
