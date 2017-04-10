package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;


public class FetchPackage
{
	private  List<PackageList> PackageDetails;
	public List<PackageList> getPackageDetails()
	{
	return PackageDetails;
	}

	public void setPackageDetails(List<PackageList> packageDetails)
	{
	PackageDetails = packageDetails;
	}
	
	public class PackageList implements Serializable
	{
		
//{"PackageDetails":[{"noofsms":10,"costofsms":1.0000},{"noofsms":50,"costofsms":5.0000},{"noofsms":100,"costofsms":11.0000},{"noofsms":500,"costofsms":55.0000},{"noofsms":1000,"costofsms":110.0000}]}
	// Response: {"PackageDetails":[{"noofsms":10,"CostOfSms":1.0000},{"noofsms":50,"CostOfSms":5.0000},{"noofsms":100,"CostOfSms":11.0000},{"noofsms":500,"CostOfSms":55.0000},{"noofsms":1000,"CostOfSms":110.0000}]}
	
	private static final long serialVersionUID = 1L;
	private String noofsms;
	private String CostOfSms;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getNoofsms() {
		return noofsms;
	}
	public void setNoofsms(String noofsms) {
		this.noofsms = noofsms;
	}
	public String getCostOfSms() {
		return CostOfSms;
	}
	public void setCostOfSms(String costOfSms) {
		CostOfSms = costOfSms;
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
