package sms19.inapp.msg.rest;

import android.os.Bundle;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

public class GroupchatInvitation implements InvitationListener {

    @Override
    public void invitationReceived(Connection conn, final String room,
                                   final String inviter, final String reason, String password,
                                   final Message message) {
        // TODO Auto-generated method stub

        Utils.printLog("Group chat invitation received: from " + inviter);
        if (!GlobalData.dbHelper.checkGroupisAlreadyexsit(room)) {
            new Thread(new Runnable() {
                public void run() {

                    // String groupname = room.split("@")[0];
                    // groupname = groupname.split("\\+")[0];
                    String groupname = reason;
                    byte[] byteArray = null;
                    if (message != null) {
                        String grouppic = message.getBody();
                        if (grouppic != null && grouppic.trim().length() != 0) {
                            byteArray = Utils.decodeToImage(grouppic);
                        }
                    }
                    ArrayList<Contactmodel> selectedmemberlist = new ArrayList<Contactmodel>();

                    String msgtime = Utils.currentTimeStap();

                    GlobalData.dbHelper.addnewGroupinDB(room, groupname,
                            byteArray, selectedmemberlist, false, msgtime);

                    try {
                        if (GlobalData.connection != null
                                && GlobalData.connection.isConnected()) {
                            final MultiUserChat mucjoin = new MultiUserChat(GlobalData.connection, room);

                            DiscussionHistory history = new DiscussionHistory();
                            history.setMaxStanzas(0);

                            try {
                                mucjoin.join(ConstantFields.mydetail.getRemote_jid(), null, history, SmackConfiguration.getPacketReplyTimeout());
                            } catch (Exception e) {
                                // TODO Auto-generated catch block

                                e.printStackTrace();
                                /*	mucjoin.join(
										ConstantFields.mydetail.getRemote_jid(),
										null, history,
										SmackConfiguration.getPacketReplyTimeout());*/

                            }
                            if (mucjoin != null && mucjoin.isJoined()) {

                                Collection<Affiliate> members = mucjoin.getOwners();
                                if (members.size() > 0) {
                                    Iterator<Affiliate> ids = members.iterator();
                                    while (ids.hasNext()) {
                                        String id = ids.next().getJid();
                                        if (!id.equals(ConstantFields.mydetail
                                                .getRemote_jid())) {
                                            Contactmodel model = new Contactmodel();
                                            model.setRemote_jid(id);
                                            selectedmemberlist.add(model);
                                        }
                                    }
                                }

                                GlobalData.dbHelper.updateGroupinDB(room,
                                        selectedmemberlist);
                                final ArrayList<Contactmodel> filtrlist = GlobalData.dbHelper
                                        .editContactforGroup(selectedmemberlist);

                                new Thread(new Runnable() {
                                    public void run() {
                                        for (int i = 0; i < filtrlist.size(); i++) {
                                            GlobalData.dbHelper
                                                    .DownloadVcardandupdateContact(filtrlist
                                                            .get(i).getRemote_jid());
                                        }
                                    }
                                }).start();

                            }
                            //if (!GlobalData.dbHelper.checkGroupisAlreadyexsit(room)) {
                            addFirstmsgtogroup(room, "Hello...!!!", inviter);
                            //}

                        }
                    } catch (XMPPException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {

            //	if(!GlobalData.dbHelper.groupIsBlockNew(room)){
			/*SingleChatRoomFrgament chatRoomFrgament=SingleChatRoomFrgament.newInstance("");
				if(chatRoomFrgament!=null){

				}*/
            GlobalData.dbHelper.groupUnBlocknewfromDB(room);
            initeGroupChat(room);
            addgroupToUi();
            //}

        }
    }


    public static void addgroupToUi() {

        if (SingleChatRoomFrgament.groupChatHandler != null) {
            android.os.Message message = new android.os.Message();
            Bundle bundle = new Bundle();
            bundle.putInt("key", 1);
            message.setData(bundle);

            SingleChatRoomFrgament.groupChatHandler.sendMessage(message);
        }
        // }
    }


    public MultiUserChat initeGroupChat(String groip_jid) {
        MultiUserChat muc = null;

        if (GlobalData.connection != null && GlobalData.connection.isConnected()) {
            if (InAppMessageActivity.myModel != null) {
                muc = new MultiUserChat(GlobalData.connection, groip_jid);
                try {
                    DiscussionHistory history = new DiscussionHistory();
                    history.setMaxStanzas(0);
                    try {
                        muc.join(InAppMessageActivity.myModel.getRemote_jid(), null, history, SmackConfiguration.getPacketReplyTimeout());
                    } catch (XMPPException e) {
                        e.printStackTrace();
                        muc = null;
                    }

                } catch (Exception e) {
                    muc = null;
                    e.printStackTrace();
                }
            }
        }

        return muc;

    }

    public void addFirstmsgtogroup(String G_remote_jid, String message,
                                   String sendby) {
        String msgtime = /*String.valueOf(System.currentTimeMillis())*/Utils.currentTimeStap();
        long rowid = GlobalData.dbHelper.addchatToMessagetable(G_remote_jid,
                message, sendby, msgtime, "", "", "0");//not set packet id because its friend msg
        if (rowid != 0 && rowid != -1) {
            GlobalData.dbHelper.addorupdateRecentTable(G_remote_jid,
                    String.valueOf(rowid));
            GlobalData.dbHelper.updateContactmsgData(G_remote_jid, message,
                    msgtime);
        }

        // if (ctx != null) {
        // if (!GlobalData.silent) {
        // Utils.playselectedTones(ctx,
        // GlobalData.msgToneuri);
        // }
        // if (GlobalData.vibrate) {
        // Utils.vibrateplay(ctx);
        // }
        // }
        if (GlobalData.OnHomefrag) {

            GroupchatListner.addmessgaeToRecentUi(G_remote_jid, message,
                    msgtime);

        }

        //ChatFragment chatFragment=ChatFragment.newInstance("");

		/*if(chatFragment!=null){
			chatFragment.refreshChatAdapter();
		}*/


        GlobalData.dbHelper.updateUnreadmsgCount(G_remote_jid);
    }
}
