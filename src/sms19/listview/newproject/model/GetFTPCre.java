package sms19.listview.newproject.model;

import java.util.List;

public class GetFTPCre
{
//"GetFTPHostDetail":[{"HostName":"ftp.manjushaelastic.com","FtpUser":"test@manjushaelastic.com ","FtpPassword":"anything19","FtpUrl":"www.manjushaelastic.com/sms19","EmergencyType":"NO","EmergencyMessage":"NO"}]}//
		private List<GetFTPHostDetail> GetFTPHostDetail;
	
	public List<GetFTPHostDetail> getGetFTPHostDetail()
	{
		return GetFTPHostDetail;
	}


	public void setGetFTPHostDetail(List<GetFTPHostDetail> getFTPHostDetail)
	{
		GetFTPHostDetail = getFTPHostDetail;
	}
	
	public class GetFTPHostDetail
	{
	
		private String HostName;
		private String FtpUser;
		private String FtpPassword;
		private String FtpUrl;
		private String EmergencyType;
		private String EmergencyMessage;
		
		public String getHostName() 
		{
			return HostName;
		}

		public void setHostName(String hostName)
		{
			HostName = hostName;
		}

		public String getFtpUser() {
			return FtpUser;
		}

		public void setFtpUser(String ftpUser)
		{
			FtpUser = ftpUser;
		}

		public String getFtpPassword()
		{
			return FtpPassword;
		}

		public void setFtpPassword(String ftpPassword)
		{
			FtpPassword = ftpPassword;
		}

		public String getEmergencyMessage() 
		{
			return EmergencyMessage;
		}

		public void setEmergencyMessage(String emergencyMessage) 
		{
			EmergencyMessage = emergencyMessage;
		}

		public String getFtpUrl() {
			return FtpUrl;
		}

		public void setFtpUrl(String ftpUrl) 
		{
			FtpUrl = ftpUrl;
		}

		public String getEmergencyType()
		{
			return EmergencyType;
		}

		public void setEmergencyType(String emergencyType) 
		{
			EmergencyType = emergencyType;
		}
		
		
		
		
	}


	
}
