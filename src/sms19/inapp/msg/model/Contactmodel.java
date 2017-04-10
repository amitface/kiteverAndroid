package sms19.inapp.msg.model;

import com.kitever.app.context.BaseApplicationContext;

import java.util.Arrays;
import java.util.Comparator;

import sms19.inapp.msg.constant.Utils;

public class Contactmodel {
    private String name = "";
    private String number = "";
    private String remote_jid = "";
    private String status = "";
    private String lastseen = "";
    private String status_custom_time = "";
    private int isGroup = 0;
    private String customStatus = "";
    private int isUserblock = 0;
    private byte[] avatar = null;
    private boolean isselected = false;
    private boolean isFromNative = false;
    private int isRegister = 0;
    private String userFromCommanGroup = "";
    private String fromPage = "";// 1 mean from ChatPage

    private int isStranger = 0;
    private int isAdmin = 0;
    private String row_id = "0";
    private String country_code = "";
    private String message_id = "";
    private String message_time = "";
    private String emailId = "";
    private String imageUrl = "";
    boolean isCheckConact = false;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public int getIsStranger() {
        return isStranger;
    }

    public void setIsStranger(int isStranger) {
        this.isStranger = isStranger;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getAvatar() {
		return avatar;
//        return null;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getRemote_jid() {
        return remote_jid;
    }

    public void setRemote_jid(String remote_jid) {
        this.remote_jid = remote_jid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsUserblock() {
        return isUserblock;
    }

    public void setIsUserblock(int isUserblock) {
        this.isUserblock = isUserblock;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCustomStatus() {
        return customStatus;
    }

    public void setCustomStatus(String customStatus) {
        this.customStatus = customStatus;
    }

    public boolean isFromNative() {
        return isFromNative;
    }

    public void setFromNative(boolean isFromNative) {
        this.isFromNative = isFromNative;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public int getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(int isRegister) {
        this.isRegister = isRegister;
    }

    public class CustomComparatorSortByName implements Comparator<Contactmodel> {
        @Override
        public int compare(Contactmodel o1, Contactmodel o2) {
            return o1.name.trim().compareToIgnoreCase(o2.name.trim());
        }
    }


    public class CustomComparatorSortByBeta implements Comparator<Contactmodel> {
        @Override
        public int compare(Contactmodel o1, Contactmodel o2) {
            return o2.name.trim().compareToIgnoreCase(o1.name.trim());
        }
    }


    public class CustomComparatorSortNumber implements Comparator<Contactmodel> {
        @Override
        public int compare(Contactmodel o1, Contactmodel o2) {


            return Double.valueOf(Utils.removeCountryCode(o1.number, BaseApplicationContext.baseContext)).compareTo(Double.valueOf(Utils.removeCountryCode(o2.number, BaseApplicationContext.baseContext)));

        }
    }


    public class CustomComparatorSortNumberDec implements Comparator<Contactmodel> {
        @Override
        public int compare(Contactmodel o1, Contactmodel o2) {
            return Double.valueOf(Utils.removeCountryCode(o2.number, BaseApplicationContext.baseContext)).compareTo(Double.valueOf(Utils.removeCountryCode(o1.number, BaseApplicationContext.baseContext)));
        }
    }


    public String getFromPage() {
        return fromPage;
    }

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }

    public String getUserFromCommanGroup() {
        return userFromCommanGroup;
    }

    public void setUserFromCommanGroup(String userFromCommanGroup) {
        this.userFromCommanGroup = userFromCommanGroup;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isCheckConact() {
        return isCheckConact;
    }

    public void setCheckConact(boolean isCheckConact) {
        this.isCheckConact = isCheckConact;
    }

    public String getRow_id() {
        return row_id;
    }

    public void setRow_id(String row_id) {
        this.row_id = row_id;
    }

    public String getStatus_custom_time() {
        return status_custom_time;
    }

    public void setStatus_custom_time(String status_custom_time) {
        this.status_custom_time = status_custom_time;
    }

    public String getMessage_id() {
        return message_id;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    @Override
    public String toString() {
        return "Contactmodel [name=" + name + ", number=" + number
                + ", remote_jid=" + remote_jid + ", status=" + status
                + ", lastseen=" + lastseen + ", status_custom_time="
                + status_custom_time + ", isGroup=" + isGroup
                + ", customStatus=" + customStatus + ", isUserblock="
                + isUserblock + ", avatar=" + Arrays.toString(avatar)
                + ", isselected=" + isselected + ", isFromNative="
                + isFromNative + ", isRegister=" + isRegister
                + ", userFromCommanGroup=" + userFromCommanGroup
                + ", fromPage=" + fromPage + ", isStranger=" + isStranger
                + ", isAdmin=" + isAdmin + ", row_id=" + row_id
                + ", country_code=" + country_code + ", message_id="
                + message_id + ", message_time=" + message_time
                + ", isCheckConact=" + isCheckConact + "]";
    }
}
