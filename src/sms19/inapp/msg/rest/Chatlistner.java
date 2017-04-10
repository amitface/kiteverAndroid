package sms19.inapp.msg.rest;

import android.content.Context;
import android.os.Bundle;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.MUCUser;

import sms19.inapp.msg.ChatFragment;
import sms19.inapp.msg.ContactFragment;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Slacktags;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.newproject.Home;
//import android.app.Notification;

public class Chatlistner {
    static String filePath = "";
    static String packetid = "";
    static Context ctx;
    public static PacketListener chatListner;

    public static void addPrecencelistner() {
        PacketFilter pfilter = new PacketTypeFilter(Presence.class);
        PacketListener precenseListner = new PacketListener() {
            public void processPacket(Packet packet) {
                Utils.printLog("presence call " + packet.toXML());

                MUCUser mucUser = (MUCUser) packet.getExtension("x",
                        "http://jabber.org/protocol/muc#user");
                String groipId = packet.getFrom();
                if (mucUser != null) {
                    String role = mucUser.getItem().getRole();
                    String reason = mucUser.getItem().getReason();
                    String jid = mucUser.getItem().getJid().split("/")[0];
                    String string = groipId.split("/")[0];
                    if (jid.equalsIgnoreCase(Home.mydetail.getRemote_jid())) {
                        if (reason.equalsIgnoreCase("kick from group")
                                || reason.equalsIgnoreCase("exit")) {

                            GlobalData.dbHelper.groupBlocknewfromDB(string);
                            if (SingleChatRoomFrgament.groupChatHandler != null) {
                                android.os.Message message = new android.os.Message();
                                Bundle bundle = new Bundle();
                                bundle.putInt("key", 2);
                                message.setData(bundle);

                                SingleChatRoomFrgament.groupChatHandler
                                        .sendMessage(message);
                            }
                        } else if (role.equalsIgnoreCase("none")) {
                            GlobalData.dbHelper.groupBlocknewfromDB(string);
                            if (SingleChatRoomFrgament.groupChatHandler != null) {
                                android.os.Message message = new android.os.Message();
                                Bundle bundle = new Bundle();
                                bundle.putInt("key", 2);
                                message.setData(bundle);

                                SingleChatRoomFrgament.groupChatHandler
                                        .sendMessage(message);
                            }
                        } else {
                            GlobalData.dbHelper.groupUnBlocknewfromDB(string);
                        }
                    }
                }

            }
        };
        GlobalData.connection.addPacketListener(precenseListner, pfilter);
    }

    public static void addChatlistner(Context context) {
        ctx = context;

        PacketFilter filter = new MessageTypeFilter(Message.Type.chat);

        chatListner = new PacketListener() {
            public void processPacket(final Packet packet) {

                Utils.printLog("message call " + packet.toXML());

                final Message message = (Message) packet;
                final String remote_jidNew = StringUtils
                        .parseBareAddress(message.getFrom());
//				Utils.showCustomNotification(ctx,message.getBody(),"kitever");
                if (!GlobalData.dbHelper.isContactBlock(remote_jidNew)) {
                    /*file__http://nowconnect.in/1199/Kitever_1487685548031.jpg__I*/
                    /*file__http://nowconnect.in/1199/Kitever_1487685548031.jpg__I*/
                    if (message.getBody() != null) {
                        if (message.getPacketID() == null) {
                            packetid = "";
                        }
                        if (!packetid.equalsIgnoreCase(message.getPacketID())) {
                            packetid = message.getPacketID();
                            final String remote_jid = StringUtils
                                    .parseBareAddress(message.getFrom());

                            Utils.printLog("Received message: "
                                    + message.toXML());

                            new Thread(new Runnable() {
                                public void run() {
                                    // long rowid = 0;

                                    if (GlobalData.dbHelper != null) {

                                        if (!GlobalData.dbHelper
                                                .userIsBlock(remote_jid)) {

                                            if (!GlobalData.dbHelper
                                                    .checkcontactisAlreadyexist(remote_jid)) {
                                                addNewUserentryinContact(remote_jid);
                                            }
                                            String msgtime = String.valueOf(System
                                                    .currentTimeMillis());

                                            Slacktags slcktg = null;
                                            try {

                                                slcktg = (Slacktags) packet
                                                        .getExtension(
                                                                "chattag",
                                                                "mysl:chattag:chat");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            if (slcktg != null) {
                                                if (!slcktg.getSlacktime()
                                                        .equalsIgnoreCase("")) {

                                                    msgtime = slcktg
                                                            .getSlacktime()
                                                            .trim();
                                                }
                                            }

                                            String packetid2 = message
                                                    .getPacketID();

                                            if (message
                                                    .getBody()
                                                    .startsWith(
                                                            SingleChatRoomFrgament.sendfilefixString)) {

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

                                                filePath = Utils
                                                        .getfilePath(filetype);

                                                long rowid = GlobalData.dbHelper
                                                        .addchatFileToMessagetable(
                                                                remote_jid,
                                                                filePath,
                                                                filetype,
                                                                remote_jid,
                                                                "S", fileurl,
                                                                location,
                                                                mediathmb,
                                                                msgtime,
                                                                packetid2, "",
                                                                false);// set
                                                // blank
                                                // msgPacket
                                                // id
                                                // and
                                                // msgsendstatus
                                                if (rowid != 0 && rowid != -1) {
                                                    GlobalData.dbHelper
                                                            .addorupdateRecentTable(
                                                                    remote_jid,
                                                                    String.valueOf(rowid));
                                                    GlobalData.dbHelper
                                                            .updateContactmsgWithIsRegisterData(
                                                                    remote_jid,
                                                                    Utils.updatemessage(filetype)
                                                                            + " Received",
                                                                    msgtime);
                                                }

                                                SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                                                        .newInstance("");
                                                if (chatRoomFrgament != null) {

                                                    if (SingleChatRoomFrgament.remote_jid
                                                            .equalsIgnoreCase(remote_jid)) {
                                                        addmessgaeToUi(message
                                                                        .getBody(),
                                                                rowid, msgtime,
                                                                filePath,
                                                                packetid2);
                                                    } else {
                                                        GlobalData.dbHelper
                                                                .updateUnreadmsgCount(remote_jid);
                                                    }

                                                } else {
                                                    GlobalData.dbHelper
                                                            .updateUnreadmsgCount(remote_jid);
                                                }

                                                if (GlobalData.OnHomefrag) {

                                                    if (message
                                                            .getBody()
                                                            .startsWith(
                                                                    SingleChatRoomFrgament.sendfilefixString)) {
                                                        addmessgaeToRecentUi(
                                                                remote_jid,
                                                                Utils.updatemessage(filetype)
                                                                        + " Received",
                                                                msgtime);
                                                    } else {
                                                        addmessgaeToRecentUi(
                                                                remote_jid,
                                                                message.getBody(),
                                                                msgtime);

                                                    }

                                                }

                                            } else {

                                                long rowid = GlobalData.dbHelper
                                                        .addchatToMessagetable(
                                                                remote_jid,
                                                                message.getBody(),
                                                                remote_jid,
                                                                msgtime,
                                                                packetid2, "",
                                                                "0");// not set
                                                // packet
                                                // id
                                                // because
                                                // its
                                                // friend
                                                // msg
                                                if (rowid != 0 && rowid != -1) {
                                                    GlobalData.dbHelper
                                                            .addorupdateRecentTable(
                                                                    remote_jid,
                                                                    String.valueOf(rowid));
                                                    GlobalData.dbHelper
                                                            .updateContactmsgWithIsRegisterData(
                                                                    remote_jid,
                                                                    message.getBody(),
                                                                    msgtime);

                                                }

                                                SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                                                        .newInstance("");
                                                if (chatRoomFrgament != null) {
                                                    if (SingleChatRoomFrgament.remote_jid
                                                            .equalsIgnoreCase(remote_jid)) {
                                                        addmessgaeToUi(message
                                                                        .getBody(),
                                                                rowid, msgtime,
                                                                filePath,
                                                                packetid2);
                                                    } else {
                                                        GlobalData.dbHelper
                                                                .updateUnreadmsgCount(remote_jid);
                                                    }
                                                } else {
                                                    GlobalData.dbHelper
                                                            .updateUnreadmsgCount(remote_jid);
                                                }

                                                if (GlobalData.OnHomefrag) {
                                                    if (message
                                                            .getBody()
                                                            .startsWith(
                                                                    SingleChatRoomFrgament.sendfilefixString)) {
                                                        String messagearray[] = message
                                                                .getBody()
                                                                .split("__");
                                                        String filetype = messagearray[2];
                                                        addmessgaeToRecentUi(
                                                                remote_jid,
                                                                Utils.updatemessage(filetype)
                                                                        + " Received",
                                                                msgtime);
                                                    } else {
                                                        addmessgaeToRecentUi(
                                                                remote_jid,
                                                                message.getBody(),
                                                                msgtime);
                                                    }
                                                }

                                            }

                                            if (ctx != null) {
                                                if (!GlobalData.silent) {
                                                    Utils.playselectedTones(
                                                            ctx,
                                                            GlobalData.msgToneuri);
                                                    if (Utils.isAppIsInBackground(ctx)) {
                                                        Contactmodel model = GlobalData.dbHelper.getUserDetails(remote_jid);
                                                        String name = "";
                                                        name = model.getName();
                                                        if (name == null || name.equalsIgnoreCase("")) {
                                                            name = model.getName();
                                                        }
                                                        Utils.showCustomNotification(ctx, message.getBody(), name, remote_jid);
                                                    }

                                                }
                                                if (GlobalData.vibrate) {
                                                    Utils.vibrateplay(ctx);
                                                }
                                            }
                                        }
                                    }
                                }

                            }).start();
                        }
                    }
                }
            }
        };

        GlobalData.connection.addPacketListener(chatListner, filter);

        // ReceiveFile(GlobalData.connection);
        addPrecencelistner();
    }

    public static void addmessgaeToUi(String message, long rowid,
                                      String msgTime, String filepath, String packetId) {
        /*
         * if (message.startsWith(SingleChatRoomFrgament.sendfilefixString)) {
		 * String messagearray[] = message.split("__"); String filetype =
		 * messagearray[2];
		 * 
		 * filePath = Utils.getfilePath(filetype); }
		 */
        if (SingleChatRoomFrgament.ChatupdateHandler != null) {
            android.os.Message msg = SingleChatRoomFrgament.ChatupdateHandler
                    .obtainMessage();// comment m
            Bundle b = new Bundle();
            b.putString("message", message);
            b.putString("senderid", "");
            b.putString("filePath", filepath);
            b.putString("msgtime", msgTime);
            b.putString("packetId", packetId);
            b.putString("rowid", String.valueOf(rowid));
            b.putByteArray("picdata", null);

            msg.setData(b);
            SingleChatRoomFrgament.ChatupdateHandler.sendMessage(msg);// comment
            // m
        }
    }

    public static void addmessgaeToRecentUi(String remoteid, String message,
                                            String msgTime) {
        if (ChatFragment.RecentupdateHandler != null) {
            android.os.Message msg = ChatFragment.RecentupdateHandler
                    .obtainMessage();// comment m
            Bundle b = new Bundle();
            b.putString("remoteid", remoteid);
            b.putString("msg", message);
            b.putString("msgtime", msgTime);
            msg.setData(b);// comment m
            if (ChatFragment.RecentupdateHandler != null) {
                ChatFragment.RecentupdateHandler.sendMessage(msg);// comment m
            }
        }
    }

    public static void addNewUserentryinContact(String jid) {
        byte[] byteArray = null;
        String picname = "";
        String status = "0";
        String remote_jid = "";
        if (GlobalData.roster == null) {
            GlobalData.roster = GlobalData.connection.getRoster();
            GlobalData.roster.setSubscriptionMode(SubscriptionMode.accept_all);
        }

        Presence presence = GlobalData.roster.getPresence(jid);
        String custom_status = presence.getStatus();

        if (presence.getType().equals(Presence.Type.available)) {
            status = "1";
        }

        remote_jid = jid;
        Utils.printLog("new entry " + jid + " : " + presence.toString());
        // String time = String.valueOf(System.currentTimeMillis());
        //
        // Contactmodel newcontact = GlobalData.dbHelper.NewentryupdateorAdd(
        // remote_jid, status, byteArray, picname, time);

        Contactmodel newcontact = GlobalData.dbHelper.NewentryupdateorAdd(
                remote_jid, status, byteArray, picname, custom_status);

        if (GlobalData.OnContactsfrag) {
            if (ContactFragment.contactlist != null
                    && ContactFragment.UserupdateHandler != null) {

                ContactFragment.contactlist.add(newcontact);// comment m

                android.os.Message msg = ContactFragment.UserupdateHandler
                        .obtainMessage();// comment m
                Bundle b = new Bundle();
                b.putString("newentry", "1");
                msg.setData(b);// comment m

                ContactFragment.UserupdateHandler.sendMessage(msg);
                // comment m
            }
        }
        // ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
        // new VCardProvider());
        // SmackConfiguration.setPacketReplyTimeout(300000);
        //
        // try {
        // VCard vCard = new VCard();
        // vCard.load(GlobalData.connection, jid);
        // Utils.printLog("new User vcard load successfully");
        //
        // byteArray = vCard.getAvatar();
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // Utils.printLog("new User vcard load excption");
        // }

    }

}
