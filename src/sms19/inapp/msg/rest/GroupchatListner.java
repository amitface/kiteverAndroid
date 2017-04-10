package sms19.inapp.msg.rest;

import android.content.Context;
import android.os.Bundle;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import sms19.inapp.msg.ChatFragment;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Slacktags;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;


public class GroupchatListner {
    static String filePath = "";
    static String packetid = "";
    static Context ctx;

    public static void addGroupChatlistner(Context context) {
        ctx = context;
        PacketFilter gc_filter = new MessageTypeFilter(Message.Type.groupchat);

        GlobalData.connection.addPacketListener(new PacketListener() {
            public void processPacket(final Packet packet) {

                Utils.printLog("Received --- " + packet.toXML());

                final Message message = (Message) packet;


                String subject = "";

                if (message.getBody("custom_subject") != null)

                    subject = message.getBody("custom_subject");
                String remote_jid = StringUtils.parseBareAddress(message
                        .getFrom());
                if (!GlobalData.dbHelper.checkcontactisAlreadyexist(remote_jid)) {
                    Chatlistner.addNewUserentryinContact(remote_jid);
                }
                if (subject != null)
                    Utils.printLog("packetlistener called Message subject is="
                            + subject);


                if (subject.equals(GlobalData.subject_gourp_name_changed)) {

                    if (message.getPacketID() == null) {
                        packetid = "";
                    }
                    if (!packetid.equalsIgnoreCase(message.getPacketID())) {
                        packetid = message.getPacketID();

                        final String G_remote_jid = StringUtils
                                .parseBareAddress(message.getFrom());
                        final String sendby = StringUtils.parseResource(message.getFrom());

                        GlobalData.dbHelper.updateGroupNameInDB(
                                G_remote_jid, message.getBody());

                        Utils.printLog("group name updated");
                    }

                    if (GlobalData.OnHomefrag) {

                        ChatFragment chatFragment = ChatFragment.newInstance("");
                        if (chatFragment != null) {
                            android.os.Message msg = new android.os.Message();
                            Bundle b = new Bundle();
                            b.putString("statusset", "1");

                            msg.setData(b);
                            ChatFragment.UpdateUserImageAndName.sendMessage(msg);// comment m
                        }

                    }
                    Utils.refreshImageAndStatus(remote_jid);

                } else if (subject
                        .equals(GlobalData.subject_gourp_icon_changed)) {
                    Utils.printLog("group icon changed packet recieved");
                    if (message.getPacketID() == null) {
                        packetid = "";
                    }
                    if (packetid == null) {
                        packetid = "";
                    }
                    if (!packetid.equalsIgnoreCase(message.getPacketID())) {
                        packetid = message.getPacketID();
                        final String G_remote_jid = StringUtils
                                .parseBareAddress(message.getFrom());
                        final String sendby = StringUtils
                                .parseResource(message.getFrom());
                        String grouppic = message.getBody();
                        byte[] byteArray = null;
                        if (grouppic != null
                                && grouppic.trim().length() != 0) {
                            byteArray = Utils.decodeToImage(grouppic);
                        }
                        if (byteArray != null) {
                            GlobalData.dbHelper.updateGroupIconInDB(
                                    G_remote_jid, byteArray);

                            Utils.printLog("group icon updated");
                        }
                    }

                    if (GlobalData.OnHomefrag) {

                        ChatFragment chatFragment = ChatFragment.newInstance("");
                        if (chatFragment != null) {
                            android.os.Message msg = new android.os.Message();
                            Bundle b = new Bundle();
                            b.putString("statusset", "1");

                            msg.setData(b);
                            ChatFragment.UpdateUserImageAndName.sendMessage(msg);// comment m
                        }

                    }

                    Utils.refreshImageAndStatus(remote_jid);


                } else {


                    if (message.getBody() != null) {
                        if (message.getPacketID() == null) {
                            packetid = "";
                        }
                        if (!packetid.equalsIgnoreCase(message.getPacketID())) {
                            packetid = message.getPacketID();
                            sms19.inapp.msg.constant.Utils.printLog("Group chat message: "
                                    + message.getFrom() + " " + message.getBody());
                            final String G_remote_jid = StringUtils
                                    .parseBareAddress(message.getFrom());


                            if (!GlobalData.dbHelper.groupIsBlockNew(G_remote_jid)) {
                                //	if(!GlobalData.dbHelper.groupIsBlockNew(G_remote_jid)){

                                final String sendby = StringUtils.parseResource(message.getFrom());
                                Utils.printLog("Received message: " + G_remote_jid + "," + message.getBody() + "," + sendby);
                                if (!sendby.equals(ConstantFields.mydetail.getRemote_jid())) {

                                    new Thread(new Runnable() {
                                        public void run() {
                                            //	long rowid = 0;


                                            String msgtime = String.valueOf(System
                                                    .currentTimeMillis());


                                            Slacktags slcktg = null;
                                            try {

                                                slcktg = (Slacktags) packet.getExtension("chattag", "mysl:chattag:chat");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            if (slcktg != null) {
                                                if (!slcktg.getSlacktime().equalsIgnoreCase("")) {

                                                    msgtime = slcktg.getSlacktime().trim();
                                                }
                                            }

                                            boolean isMessageTime = GlobalData.dbHelper.alredyExistMessageTime(msgtime);

                                            if (!isMessageTime) {

                                                if (message.getBody().startsWith(SingleChatRoomFrgament.sendfilefixString)) {//comment m

										/*	String messagearray[] = message
                                                    .getBody().split("__");
											String filetype = messagearray[2];
											String location = "";
											String mediathmb = "";
											if (!filetype.equals(GlobalData.Audiofile)) {
												mediathmb = messagearray[3];
											} //comment m
											if (filetype
													.equals(GlobalData.Locationfile)) {
												location = messagearray[4];
											}*/
                                                    String messagearray[] = message
                                                            .getBody().split("__");
                                                    String filetype = messagearray[2];
                                                    // [file,
                                                    // http://nowconnect.in/27/636027220419092854^63.28
                                                    // KB^tmp1039117058425403754_1466260775179.jpeg,
                                                    // I]
                                                    String location = "";
                                                    String mediathmb = "";

                                                    if (filetype
                                                            .equals(GlobalData.Locationfile)) {
                                                        try {
                                                            location = messagearray[3];
                                                        } catch (Exception exception) {
                                                            exception
                                                                    .printStackTrace();
                                                            Utils.printLog("array oofb exp==\n"
                                                                    + exception
                                                                    .getMessage());
                                                        }

                                                    }


                                                    String fileurl = messagearray[1];


                                                    filePath = Utils.getfilePath(filetype);


                                                    long rowid = GlobalData.dbHelper
                                                            .addchatFileToMessagetable(
                                                                    G_remote_jid, filePath,
                                                                    filetype, sendby, "S",
                                                                    fileurl, location,
                                                                    mediathmb, msgtime, "", "", false);//set blank msgPacket id and msgsendstatus
                                                    if (rowid != 0 && rowid != -1) {
                                                        GlobalData.dbHelper
                                                                .addorupdateRecentTable(
                                                                        G_remote_jid,
                                                                        String.valueOf(rowid));
                                                        GlobalData.dbHelper.updateContactmsgData(
                                                                G_remote_jid,
                                                                Utils.updatemessage(filetype)
                                                                        + " Received",
                                                                msgtime);

                                                    }


                                                    SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament.newInstance("");
                                                    if (chatRoomFrgament != null) {

                                                        if (SingleChatRoomFrgament.remote_jid
                                                                .equalsIgnoreCase(G_remote_jid)) {// comment m

                                                            addmessgaeToUi(message.getBody(),
                                                                    sendby, rowid, msgtime, filePath);
                                                        } else {
                                                            GlobalData.dbHelper
                                                                    .updateUnreadmsgCount(G_remote_jid);
                                                        }

                                                    } else if (GlobalData.OnHomefrag) {
                                                        if (message.getBody().startsWith(
                                                                SingleChatRoomFrgament.sendfilefixString)) {// comment m
                                                            String messagearray2[] = message
                                                                    .getBody().split("__");
                                                            String filetype2 = messagearray2[2];
                                                            addmessgaeToRecentUi(
                                                                    G_remote_jid,
                                                                    Utils.updatemessage(filetype2)
                                                                            + " Received",
                                                                    msgtime);

                                                        } else {
                                                            addmessgaeToRecentUi(G_remote_jid,
                                                                    message.getBody(), msgtime);

                                                        }

                                                        GlobalData.dbHelper
                                                                .updateUnreadmsgCount(G_remote_jid);
                                                    } else {
                                                        GlobalData.dbHelper
                                                                .updateUnreadmsgCount(G_remote_jid);
                                                    }


                                                } else {

                                                    long rowid = GlobalData.dbHelper
                                                            .addchatToMessagetable(
                                                                    G_remote_jid,
                                                                    message.getBody(),
                                                                    sendby, msgtime, "", "", "0");//not set packet id because its friend msg
                                                    if (rowid != 0 && rowid != -1) {
                                                        GlobalData.dbHelper.addorupdateRecentTable(G_remote_jid,
                                                                String.valueOf(rowid));
                                                        GlobalData.dbHelper.updateContactmsgData(G_remote_jid,
                                                                message.getBody(),
                                                                msgtime);
                                                    }


                                                    SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament.newInstance("");
                                                    if (chatRoomFrgament != null) {

                                                        if (SingleChatRoomFrgament.remote_jid
                                                                .equalsIgnoreCase(G_remote_jid)) {// comment m

                                                            addmessgaeToUi(message.getBody(),
                                                                    sendby, rowid, msgtime, filePath);
                                                        } else {
                                                            GlobalData.dbHelper
                                                                    .updateUnreadmsgCount(G_remote_jid);
                                                        }

                                                    } else if (GlobalData.OnHomefrag) {
                                                        if (message.getBody().startsWith(
                                                                SingleChatRoomFrgament.sendfilefixString)) {// comment m
                                                            String messagearray[] = message
                                                                    .getBody().split("__");
                                                            String filetype = messagearray[2];
                                                            addmessgaeToRecentUi(
                                                                    G_remote_jid,
                                                                    Utils.updatemessage(filetype)
                                                                            + " Received",
                                                                    msgtime);

                                                        } else {
                                                            addmessgaeToRecentUi(G_remote_jid,
                                                                    message.getBody(), msgtime);

                                                        }

                                                        GlobalData.dbHelper
                                                                .updateUnreadmsgCount(G_remote_jid);
                                                    } else {
                                                        GlobalData.dbHelper
                                                                .updateUnreadmsgCount(G_remote_jid);
                                                    }
                                                }

                                                if (ctx != null) {
                                                    if (!GlobalData.silent) {
                                                        Utils.playselectedTones(ctx,
                                                                GlobalData.msgToneuri);
                                                        if (Utils.isAppIsInBackground(ctx)) {
                                                            Contactmodel model = GlobalData.dbHelper.getUserDetails(G_remote_jid);
                                                            String name = "";
                                                            name = model.getName();
                                                            if (name == null || name.equalsIgnoreCase("")) {
                                                                name = model.getName();
                                                            }
                                                            Utils.showCustomNotification(ctx, message.getBody(), model.getName(), G_remote_jid);
                                                        }
                                                    }
                                                    if (GlobalData.vibrate) {
                                                        Utils.vibrateplay(ctx);
                                                    }
                                                }


                                            }

                                        }


                                    }).start();
                                }
                            }


                        }


                    }
                }
				
				/*if(Home.chatPrefs!=null){
				Utils.saveGroupLeaveTm(Home.chatPrefs);
				}
				*/


            }
        }, gc_filter);
    }

    public static void addmessgaeToUi(String message, String sendby,
                                      long rowid, String msgTime, String filString) {

        if (SingleChatRoomFrgament.ChatupdateHandler != null) {
            android.os.Message msg = SingleChatRoomFrgament.ChatupdateHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("message", message);
            b.putString("senderid", sendby);
            b.putString("filePath", filString);
            b.putString("msgtime", msgTime);
            b.putString("rowid", String.valueOf(rowid));
            // b.putByteArray("picdata", model.getAvatar());

            msg.setData(b);
            SingleChatRoomFrgament.ChatupdateHandler.sendMessage(msg);
        }
        // }
    }

    public static void addmessgaeToRecentUi(String remoteid, String message,
                                            String msgTime) {
        if (ChatFragment.RecentupdateHandler != null) {
            android.os.Message msg = ChatFragment.RecentupdateHandler.obtainMessage();// comment m
            Bundle b = new Bundle();
            b.putString("remoteid", remoteid);
            b.putString("msg", message);
            b.putString("msgtime", msgTime);

            msg.setData(b);// comment m
            ChatFragment.RecentupdateHandler.sendMessage(msg);// comment m
        }
    }

}
