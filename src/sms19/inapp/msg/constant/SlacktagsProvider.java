package sms19.inapp.msg.constant;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class SlacktagsProvider implements PacketExtensionProvider {

	public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
			throws Exception {
		String username = paramXmlPullParser.getAttributeValue("", "username");
		String userid = paramXmlPullParser.getAttributeValue("", "userid");
		String msgtype = paramXmlPullParser.getAttributeValue("", "msgtype");
		String slacktime = paramXmlPullParser
				.getAttributeValue("", "chattime");
		String slackmessageid = paramXmlPullParser.getAttributeValue("",
				"chatmessageid");

		Slacktags localObject1 = new Slacktags(username, userid, msgtype,
				slacktime, slackmessageid);

		String title = paramXmlPullParser.getAttributeValue("", "title");
		title = (null == title || "".equals(title) ? null : title);
		localObject1.setTitle(title);

		String comment = paramXmlPullParser.getAttributeValue("", "comment");
		comment = (null == comment || "".equals(comment) ? null : comment);
		localObject1.setComment(comment);

		String filesize = paramXmlPullParser.getAttributeValue("", "filesize");
		filesize = (null == filesize || "".equals(filesize) ? null : filesize);
		localObject1.setFilesize(filesize);

		String mentationsuser = paramXmlPullParser.getAttributeValue("",
				"mentationsuser");
		mentationsuser = (null == mentationsuser || "".equals(mentationsuser) ? null
				: mentationsuser);
		localObject1.setMentationsuser(mentationsuser);

		String mentationschannel = paramXmlPullParser.getAttributeValue("",
				"mentationschannel");
		mentationschannel = (null == mentationschannel
				|| "".equals(mentationschannel) ? null : mentationschannel);
		localObject1.setMentationschannel(mentationschannel);

		String mentationsgroup = paramXmlPullParser.getAttributeValue("",
				"mentationsgroup");
		mentationsgroup = (null == mentationsgroup
				|| "".equals(mentationsgroup) ? null : mentationsgroup);
		localObject1.setMentationsgroup(mentationsgroup);

		String mentationsuserids = paramXmlPullParser.getAttributeValue("",
				"mentationsuserids");
		mentationsuserids = (null == mentationsuserids
				|| "".equals(mentationsuserids) ? null : mentationsuserids);
		localObject1.setMentationsuserids(mentationsuserids);

		String mentationschannelids = paramXmlPullParser.getAttributeValue("",
				"mentationschannelids");
		mentationschannelids = (null == mentationschannelids
				|| "".equals(mentationschannelids) ? null
				: mentationschannelids);
		localObject1.setMentationschannelids(mentationschannelids);

		String mentationsgroupids = paramXmlPullParser.getAttributeValue("",
				"mentationsgroupids");
		mentationsgroupids = (null == mentationsgroupids
				|| "".equals(mentationsgroupids) ? null : mentationsgroupids);
		localObject1.setMentationsgroupids(mentationsgroupids);

		String mentationsuserjids = paramXmlPullParser.getAttributeValue("",
				"mentationsuserjids");
		mentationsuserjids = (null == mentationsuserjids
				|| "".equals(mentationsuserjids) ? null : mentationsuserjids);
		localObject1.setMentationsuserjids(mentationsuserjids);

		String mentationschanneljids = paramXmlPullParser.getAttributeValue("",
				"mentationschanneljids");
		mentationschanneljids = (null == mentationschanneljids
				|| "".equals(mentationschanneljids) ? null
				: mentationschanneljids);
		localObject1.setMentationschanneljids(mentationschanneljids);

		String mentationsgroupjids = paramXmlPullParser.getAttributeValue("",
				"mentationsgroupjids");
		mentationsgroupjids = (null == mentationsgroupjids
				|| "".equals(mentationsgroupjids) ? null : mentationsgroupjids);
		localObject1.setMentationsgroupjids(mentationsgroupjids);

		String thmb = paramXmlPullParser.nextText();

		thmb = "".equals(thmb) ? null : thmb;
		localObject1.setMediathumb(thmb);

		return localObject1;
	}

}
