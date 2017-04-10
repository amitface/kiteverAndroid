package sms19.inapp.msg.model;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.util.Comparator;

public class Chatmodel {
    private byte[] pic = null;
    private String name = "";
    private String chatmessage = "";
    private boolean mine = false;
    private String remote_userid = "";

    private String mediatype = "";
    private String mediapath = "";
    private String mediaUrl = "";
    private String status = "S";
    private byte[] mediathmb = null;
    private byte[] mediathmb2 = null;
    private String messagerowid = "";
    private String msgtime = "";
    private String msgDate = "";
    private String msgTime = "";
    private String msgDateTimeCombine = "";
    private Bitmap Orignalbitmap = null;
    private Bitmap thmbbitmap = null;
    private String seperator_line = "";

    private String msg_packetid = "";
    private String sent_msg_success = "";
    private String deliver_msg_success = "";
    private String read_msg_success = "";
    private int isofflinemsg = 0;
    private boolean fileIsUploaded = false;
    private String row_offlineId = "";
    private int isretry = 0;
    private int isBroadCast = 0;
    private int inApMsgCount = 0;


    private int inInboxCount = 0;
    private int mail_count = 0;
    private String broadCastTime = "0";
    private String expiry_time = "0";
    private String message_id = "0";
    private String MessageStatus = "";


    @Override
    public String toString() {
        final Gson gson = new Gson();
        return gson.toJson(this);
    }


    public Bitmap getOrignalbitmap() {
        return Orignalbitmap;
    }

    public void setOrignalbitmap(Bitmap orignalbitmap) {
        Orignalbitmap = orignalbitmap;
    }

    public Bitmap getThmbbitmap() {
        return thmbbitmap;
    }

    public void setThmbbitmap(Bitmap thmbbitmap) {
        this.thmbbitmap = thmbbitmap;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(String msgtime) {
        this.msgtime = msgtime;
    }

    public String getMessagerowid() {
        return messagerowid;
    }

    public void setMessagerowid(String messagerowid) {
        this.messagerowid = messagerowid;
    }

    public byte[] getMediathmb() {
        return mediathmb;
    }

    public void setMediathmb(byte[] mediathmb) {
        this.mediathmb = mediathmb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getMediapath() {
        return mediapath;
    }

    public void setMediapath(String mediapath) {
        this.mediapath = mediapath;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getRemote_userid() {
        return remote_userid;
    }

    public void setRemote_userid(String remote_userid) {
        this.remote_userid = remote_userid;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

	/*public byte[] getPic() {
        return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}*/

    public int getMail_count() {
        return mail_count;
    }

    public void setMail_count(int mail_count) {
        this.mail_count = mail_count;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatmessage() {
        return chatmessage;
    }

    public void setChatmessage(String chatmessage) {
        this.chatmessage = chatmessage;
    }

    public String getMsgDateTimeCombine() {
        return msgDateTimeCombine;
    }

    public void setMsgDateTimeCombine(String msgDateTimeCombine) {
        this.msgDateTimeCombine = msgDateTimeCombine;
    }

    public String getSeperator_line() {
        return seperator_line;
    }

    public void setSeperator_line(String seperator_line) {
        this.seperator_line = seperator_line;
    }


    public String getMsg_packetid() {
        return msg_packetid;
    }

    public void setMsg_packetid(String msg_packetid) {
        this.msg_packetid = msg_packetid;
    }

    public String getSent_msg_success() {
        return sent_msg_success;
    }

    public void setSent_msg_success(String sent_msg_success) {
        this.sent_msg_success = sent_msg_success;
    }

    public String getDeliver_msg_success() {
        return deliver_msg_success;
    }

    public void setDeliver_msg_success(String deliver_msg_success) {
        this.deliver_msg_success = deliver_msg_success;
    }

    public String getRead_msg_success() {
        return read_msg_success;
    }

    public void setRead_msg_success(String read_msg_success) {
        this.read_msg_success = read_msg_success;
    }

    public int getIsofflinemsg() {
        return isofflinemsg;
    }

    public void setIsofflinemsg(int isofflinemsg) {
        this.isofflinemsg = isofflinemsg;
    }

/*	public byte[] getMediathmb2() {
		return mediathmb2;
	}

	public void setMediathmb2(byte[] mediathmb2) {
		this.mediathmb2 = mediathmb2;
	}*/

    public boolean isFileIsUploaded() {
        return fileIsUploaded;
    }

    public void setFileIsUploaded(boolean fileIsUploaded) {
        this.fileIsUploaded = fileIsUploaded;
    }


    public int getIsBroadCast() {
        return isBroadCast;
    }

    public int getInApMsgCount() {
        return inApMsgCount;
    }

    public int getInInboxCount() {
        return inInboxCount;
    }

    public void setIsBroadCast(int isBroadCast) {
        this.isBroadCast = isBroadCast;
    }

    public void setInApMsgCount(int inApMsgCount) {
        this.inApMsgCount = inApMsgCount;
    }

    public void setInInboxCount(int inInboxCount) {
        this.inInboxCount = inInboxCount;
    }


    public class CustomComparatorSortByTime implements Comparator<Chatmodel> {
        @Override
        public int compare(Chatmodel o1, Chatmodel o2) {
            return Long.valueOf(o1.getMsgtime()).compareTo(Long.valueOf(o2.getMsgtime()));
        }
    }


    public String getRow_offlineId() {
        return row_offlineId;
    }

    public void setRow_offlineId(String row_offlineId) {
        this.row_offlineId = row_offlineId;
    }

    public int getIsretry() {
        return isretry;
    }

    public void setIsretry(int isretry) {
        this.isretry = isretry;
    }

    public String getBroadCastTime() {
        return broadCastTime;
    }

    public void setBroadCastTime(String broadCastTime) {
        this.broadCastTime = broadCastTime;
    }

    public String getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(String expiry_time) {
        this.expiry_time = expiry_time;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessageStatus() {
        return MessageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        MessageStatus = messageStatus;
    }


}
