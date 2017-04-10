package sms19.inapp.msg.rest;

public interface Downloadfilelistner {

	void downloadsuccess(String rowid, String dasboardjson);

	void downloadfail(String rowid, String dasboardjson);

}
