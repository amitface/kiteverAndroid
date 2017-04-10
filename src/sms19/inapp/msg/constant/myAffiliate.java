package sms19.inapp.msg.constant;

import org.jivesoftware.smackx.packet.MUCAdmin;
import org.jivesoftware.smackx.packet.MUCOwner;

public class myAffiliate {
	private String jid;
	private String affiliation;
	private String role;
	private String nick;

	myAffiliate(MUCOwner.Item paramItem) {
		this.jid = paramItem.getJid();
		this.affiliation = paramItem.getAffiliation();
		this.role = paramItem.getRole();
		this.nick = paramItem.getNick();
	}

	myAffiliate(MUCAdmin.Item paramItem) {
		this.jid = paramItem.getJid();
		this.affiliation = paramItem.getAffiliation();
		this.role = paramItem.getRole();
		this.nick = paramItem.getNick();
	}

	public String getJid() {
		return this.jid;
	}

	public String getAffiliation() {
		return this.affiliation;
	}

	public String getRole() {
		return this.role;
	}

	public String getNick() {
		return this.nick;
	}
}
