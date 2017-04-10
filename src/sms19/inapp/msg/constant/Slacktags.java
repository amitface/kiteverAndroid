package sms19.inapp.msg.constant;

import org.jivesoftware.smack.packet.PacketExtension;

public class Slacktags implements PacketExtension {

	// <slacktag xmlns="mysl:slacktag:slack" msgtype="type" ismantion="0"
	// username="Manish" userid="18" slacktime="125113"
	// slackmessageid="test_12_hgdhd" title="filetitle"
	// comment="file cmnt"
	// mentationsuser=", seprated string of userid">base64string if filetype
	// image or video</slacktag>

	String username;
	String userid;
	String msgtype;
	// String ismantion;
	/*String slacktime;
	String slackmessageid;*/
	String chattime;
	String chatmessageid;

	String title;
	String comment;
	String filesize;
	String mentationsuser; // __ seprated usernamestring
	String mentationschannel; // __ seprated channelnamestring
	String mentationsgroup; // __ seprated groupnamestring
	String mentationsuserids; // , seprated useridstring
	String mentationschannelids; // , seprated channelidstring
	String mentationsgroupids; // , seprated groupidstring
	String mentationsuserjids; // , seprated userjidstring
	String mentationschanneljids; // , seprated channeljidstring
	String mentationsgroupjids; // , seprated groupjidstring
	String mediathumb; // base 64 string if image or video
	
	
	/*<message type="chat" to="cud_220@54.254.193.216" from="cud_218@54.254.193.216/ed125bc9">
	<slacktag xmlns="mysl:slacktag:slack" username="cud" msgtype="normal" ismantion="0" userid="218" slacktime="1444916738215" 
	slackmessageid="cud_220-20990" title="" comment="" mentationsuser="" mentationschannel=""/><body> Dfgsd</body></message>*/
	
	
	/*(11:04) Neeraj Arora: for group
	(11:04) Neeraj Arora: <message type="groupchat" to="cud_483@conference.54.254.193.216" from="cud_220@54.254.193.216/MatriX">
	<body>dfgd</body><slacktag xmlns="mysl:slacktag:slack" username="cud1"
	msgtype="normal" ismantion="0" userid="220" slacktime="15102015071250944" 
	slackmessageid="cud_220_15102015071250944" title="" comment="" mentationsuser=""/></message>*/
	
	
	

	public Slacktags(String username, String userid, String msgtype,
			String slacktime, String slackmessageid) {

		this.username = username;
		this.userid = userid;
		this.msgtype = msgtype;
		// this.ismantion = ismantion;
		this.chattime = slacktime;
		this.chatmessageid = slackmessageid;

	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	// public String getIsmantion() {
	// return ismantion;
	// }
	//
	// public void setIsmantion(String ismantion) {
	// this.ismantion = ismantion;
	// }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSlacktime() {
		return chattime;
	}

	public void setSlacktime(String slacktime) {
		this.chattime = slacktime;
	}

	public String getSlackmessageid() {
		return chatmessageid;
	}

	public void setSlackmessageid(String slackmessageid) {
		this.chatmessageid = slackmessageid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMentationsuser() {
		return mentationsuser;
	}

	public void setMentationsuser(String mentationsuser) {
		this.mentationsuser = mentationsuser;
	}

	public String getMentationschannel() {
		return mentationschannel;
	}

	public void setMentationschannel(String mentationschannel) {
		this.mentationschannel = mentationschannel;
	}

	public String getMentationsgroup() {
		return mentationsgroup;
	}

	public void setMentationsgroup(String mentationsgroup) {
		this.mentationsgroup = mentationsgroup;
	}

	public String getMentationsuserids() {
		return mentationsuserids;
	}

	public void setMentationsuserids(String mentationsuserids) {
		this.mentationsuserids = mentationsuserids;
	}

	public String getMentationschannelids() {
		return mentationschannelids;
	}

	public void setMentationschannelids(String mentationschannelids) {
		this.mentationschannelids = mentationschannelids;
	}

	public String getMentationsgroupids() {
		return mentationsgroupids;
	}

	public void setMentationsgroupids(String mentationsgroupids) {
		this.mentationsgroupids = mentationsgroupids;
	}

	public String getMentationsuserjids() {
		return mentationsuserjids;
	}

	public void setMentationsuserjids(String mentationsuserjids) {
		this.mentationsuserjids = mentationsuserjids;
	}

	public String getMentationschanneljids() {
		return mentationschanneljids;
	}

	public void setMentationschanneljids(String mentationschanneljids) {
		this.mentationschanneljids = mentationschanneljids;
	}

	public String getMentationsgroupjids() {
		return mentationsgroupjids;
	}

	public void setMentationsgroupjids(String mentationsgroupjids) {
		this.mentationsgroupjids = mentationsgroupjids;
	}

	public String getMediathumb() {
		return mediathumb;
	}

	public void setMediathumb(String mediathumb) {
		this.mediathumb = mediathumb;
	}

	@Override
	public String getElementName() {
		// TODO Auto-generated method stub
		return "chattag";
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return "mysl:chattag:chat";
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		StringBuilder localStringBuilder = new StringBuilder();
		

		localStringBuilder.append("<").append(getElementName())
				.append(" xmlns=\"").append(getNamespace()).append("\"");

		localStringBuilder.append(" username=\"");
		localStringBuilder.append(getUsername());
		localStringBuilder.append("\"");

		localStringBuilder.append(" userid=\"");
		localStringBuilder.append(getUserid());
		localStringBuilder.append("\"");

		localStringBuilder.append(" msgtype=\"");
		localStringBuilder.append(getMsgtype());
		localStringBuilder.append("\"");

		// localStringBuilder.append(" ismantion=\"");
		// localStringBuilder.append(getUsername());
		// localStringBuilder.append("\"");

		localStringBuilder.append(" chattime=\"");
		localStringBuilder.append(getSlacktime());
		localStringBuilder.append("\"");

		localStringBuilder.append(" chatmessageid=\"");
		localStringBuilder.append(getSlackmessageid());
		localStringBuilder.append("\"");

		if (getTitle() != null) {

			localStringBuilder.append(" title=\"");
			localStringBuilder.append(getTitle());
			localStringBuilder.append("\"");
		}
		if (getComment() != null && !getComment().isEmpty()) {
			localStringBuilder.append(" comment=\"");
			localStringBuilder.append(getComment());
			localStringBuilder.append("\"");
		}
		if (getFilesize() != null) {

			localStringBuilder.append(" filesize=\"");
			localStringBuilder.append(getFilesize());
			localStringBuilder.append("\"");
		}

		if (getMentationsuser() != null) {
			localStringBuilder.append(" mentationsuser=\"");
			localStringBuilder.append(getMentationsuser());
			localStringBuilder.append("\"");
		}
		if (getMentationschannel() != null) {
			localStringBuilder.append(" mentationschannel=\"");
			localStringBuilder.append(getMentationschannel());
			localStringBuilder.append("\"");
		}
		if (getMentationsgroup() != null) {
			localStringBuilder.append(" mentationsgroup=\"");
			localStringBuilder.append(getMentationsgroup());
			localStringBuilder.append("\"");
		}
		if (getMentationsuserids() != null) {
			localStringBuilder.append(" mentationsuserids=\"");
			localStringBuilder.append(getMentationsuserids());
			localStringBuilder.append("\"");
		}
		if (getMentationsuserjids() != null) {
			localStringBuilder.append(" mentationsuserjids=\"");
			localStringBuilder.append(getMentationsuserjids());
			localStringBuilder.append("\"");
		}
		if (getMentationschannelids() != null) {
			localStringBuilder.append(" mentationschannelids=\"");
			localStringBuilder.append(getMentationschannelids());
			localStringBuilder.append("\"");
		}
		if (getMentationschanneljids() != null) {
			localStringBuilder.append(" mentationschanneljids=\"");
			localStringBuilder.append(getMentationschanneljids());
			localStringBuilder.append("\"");
		}
		if (getMentationsgroupids() != null) {
			localStringBuilder.append(" mentationsgroupids=\"");
			localStringBuilder.append(getMentationsgroupids());
			localStringBuilder.append("\"");
		}
		if (getMentationsgroupjids() != null) {
			localStringBuilder.append(" mentationsgroupjids=\"");
			localStringBuilder.append(getMentationsgroupjids());
			localStringBuilder.append("\"");
		}

		localStringBuilder.append(">");
		if (getMediathumb() != null) {
			localStringBuilder.append(getMediathumb());
		}

		localStringBuilder.append("</").append(getElementName()).append(">");
		return localStringBuilder.toString();
	}

}
