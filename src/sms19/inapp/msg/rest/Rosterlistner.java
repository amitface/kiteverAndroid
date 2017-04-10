package sms19.inapp.msg.rest;

import java.util.Collection;

import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.util.StringUtils;

import sms19.inapp.msg.ChatFragment;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

import android.os.Bundle;
import android.os.Message;

public class Rosterlistner implements RosterListener {

    @Override
    public void entriesAdded(Collection<String> remote_jids) {
        // TODO Auto-generated method stub
        Utils.printLog(remote_jids.toString());
        if (GlobalData.roster == null) {
            GlobalData.roster = GlobalData.connection.getRoster();
            GlobalData.roster.setSubscriptionMode(SubscriptionMode.accept_all);
        }
        for (String remote_jid : remote_jids) {
            RosterEntry entry = GlobalData.roster.getEntry(remote_jid);
            if (entry != null && entry.getUser() != null && entry.getUser().length() > 0) {
                Presence subscribe = new Presence(Presence.Type.subscribe);
                subscribe.setTo(entry.getUser());
                GlobalData.connection.sendPacket(subscribe);
//                addedEntryinDBandloadvcard(entry);
            }
        }
//        downloadvcardandsaveinDB(remote_jids);
    }

    @Override
    public void entriesDeleted(Collection<String> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void entriesUpdated(Collection<String> arg0) {
        // TODO Auto-generated method stub
        Utils.printLog("entry is updated." + arg0.toString());
    }

    @Override
    public void presenceChanged(Presence presence) {
        // TODO Auto-generated method stub
        String status = "";
        String picname = "";
        byte[] byteArray = null;
        String[] remote = presence.getFrom().split("/");
        String remote_jid = remote[0];

        if (presence.getType().equals(Presence.Type.available)) {
            status = "1";
        } else {
            status = "0";
        }


        String lastseenansrow = GlobalData.dbHelper.updateContactdata(remote_jid, status, byteArray, picname, 1, true);


        if (presence.getStatus() != null) {
            Utils.printLog("status==> " + presence.toXML());
            status = presence.getStatus();
            status = StringUtils.unescapeNode(status);
            GlobalData.dbHelper.updateCustomStatusOfContact(remote_jid, status);
            Utils.printLog(" status updated in database");


        }

        Utils.printLog("Row effected : " + lastseenansrow + " for " + remote_jid);

        try {
            downloadvcardandsaveinDBOfSingleUser(remote_jid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (presence.getType().equals(Presence.Type.available)) {
            status = "1";

        } else {
            status = "0";
        }

        if (GlobalData.OnHomefrag) {

            ChatFragment chatFragment = ChatFragment.newInstance("");
            if (chatFragment != null) {
                if (ChatFragment.UserupStatusHandler != null) {
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("updatestatus", status);
                    b.putString("remoteid", remote_jid);
                    msg.setData(b);
                    ChatFragment.UserupStatusHandler.sendMessage(msg);// comment m
                }
            }


        }
        if (SingleChatRoomFrgament.ON_SINGLE_CHAT_PAGE) {

            SingleChatRoomFrgament singleChatRoomFrgament = SingleChatRoomFrgament.newInstance("");
            if (singleChatRoomFrgament != null) {
                if (remote_jid.equalsIgnoreCase(singleChatRoomFrgament.getRemote_jid())) {
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("onlineofline", "1");
                    msg.setData(b);
                    SingleChatRoomFrgament.onLineOfflineHandler.sendMessage(msg);// comment m

                }
            }
        }
    }
    //}


    public static int retrieveState_mode(Mode userMode, boolean isOnline) {
        int userState = 0;
        /** 0 for offline, 1 for online, 2 for away,3 for busy*/
        if (userMode == Mode.dnd) {
            userState = 2;
        } else if (userMode == Mode.away) {
            userState = 4;
        } else if (userMode == Mode.xa) {
            userState = 3;
        } else if (isOnline) {
            userState = 1;
        }
        return userState;
    }


    public void addedEntryinDBandloadvcard(final RosterEntry newentry) {

        new Thread(new Runnable() {
            public void run() {

                byte[] byteArray = null;
                String picname = "";
                String status = "";
                String remote_jid = "";
                Presence presence = GlobalData.roster.getPresence(newentry
                        .getUser());

                try {
                    retrieveState_mode(presence.getMode(), presence.isAvailable());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String stringStatus = presence.getStatus();

                if (presence.getType().equals(Presence.Type.available)) {
                    status = "1";
                } else {
                    status = "0";
                }
                if (newentry != null && newentry.getUser() != null && newentry.getUser().length() > 0) {
                    remote_jid = newentry.getUser();
                    Utils.printLog("new entry " + newentry.getUser() + " : " + presence.toString());


                    Contactmodel newcontact = GlobalData.dbHelper.NewentryupdateorAdd(remote_jid, status, byteArray, picname, stringStatus);
                }

            }
        }).start();
    }

    public void downloadvcardandsaveinDB(final Collection<String> rosterEntries) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (String remote_jid : rosterEntries) {
                    RosterEntry entry = GlobalData.roster.getEntry(remote_jid);
                    if (entry != null && entry.getUser() != null && entry.getUser().length() > 0) {
                        String remote_id = entry.getUser();
                        GlobalData.dbHelper.DownloadVcardandupdateContact(remote_id);

                        ChatFragment chatFragment = ChatFragment.newInstance("");
                        if (chatFragment != null) {
                            chatFragment.refreshChatAdapter();
                        }
                    }
                }
            }
        }).start();
    }

    public void downloadvcardandsaveinDBOfSingleUser(final String remote_jid) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                byte[] byteArray = null;
                if (!remote_jid.equalsIgnoreCase("")) {
                    RosterEntry entry = GlobalData.roster.getEntry(remote_jid);
                    if (entry != null && entry.getUser() != null && entry.getUser().length() > 0) {
                        String remote_id = entry.getUser();
                        byteArray = GlobalData.dbHelper.DownloadVcardandupdateContact(remote_id);
                        ChatFragment chatFragment = ChatFragment.newInstance("");
                        if (chatFragment != null) {
                            //	chatFragment.refreshChatAdapter();

                            if (GlobalData.romote_jid.equalsIgnoreCase(remote_jid)) {
                                try {
                                    GlobalData.dbHelper.updateUserDatainDb(remote_jid, "", byteArray);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            if (ChatFragment.UserUpdateImageHandler != null) {
                                if (byteArray != null) {
                                    if (GlobalData.OnHomefrag) {
                                        Message msg = new Message();
                                        Bundle b = new Bundle();
                                        b.putByteArray("imagedata", byteArray);
                                        b.putString("remoteid", remote_jid);
                                        msg.setData(b);
                                        ChatFragment.UserUpdateImageHandler.sendMessage(msg);// comment m
                                    }

                                }
                            }
                        }


                    }

                }


            }
        }).start();
    }

}
