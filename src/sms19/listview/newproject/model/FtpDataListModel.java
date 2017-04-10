package sms19.listview.newproject.model;

public class FtpDataListModel {
	
	private String dataName;
	private String size;
	private String dataType;
	private String messageBody;

	public FtpDataListModel(String dataName, String dataType,String messageBody) {
		// TODO Auto-generated constructor stub
		this.dataName=dataName;
		this.dataType=dataType;
		this.messageBody=messageBody;
	}

	public FtpDataListModel(String dataName, String dataType,String messageBody,String size) {
		// TODO Auto-generated constructor stub
		this.dataName=dataName;
		this.dataType=dataType;
		this.messageBody=messageBody;
		this.size=size;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

}
