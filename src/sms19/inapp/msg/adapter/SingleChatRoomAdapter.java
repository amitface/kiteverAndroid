package sms19.inapp.msg.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.asynctask.MessageDeleteAsyncTask;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.imoze.EmojiconTextView;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Downloadfilelistner;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

public class SingleChatRoomAdapter extends BaseAdapter implements
        OnCompletionListener, OnClickListener {

    private Activity activity;
    private ArrayList<Chatmodel> chathistorylist;
    private LayoutInflater inflater;
    private View.OnClickListener clickListener;

    private boolean playflag = false;
    private MediaPlayer mPlayer = null;
    private OnLongClickListener longClickListener = null;
    Runnable runSeekBar = null;
    private Handler mHandler = new Handler();

    public static boolean isForwardData = false;
    public static String pathToForwardData;
    public static String typeOfData;
    public static String msgToSend;

    public SingleChatRoomAdapter(Activity activity,
                                 ArrayList<Chatmodel> chathistorylist) {

        this.activity = activity;
        this.chathistorylist = chathistorylist;

    }

    @Override
    public int getCount() {

        if (chathistorylist == null) {
            chathistorylist = new ArrayList<Chatmodel>();
        }

        return chathistorylist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        View convertView = view;
        SingleChatRoomAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.chat_adapter_lay_new, parent, false);
            holder = new ViewHolder();

            holder.attch_video_progressAync = (ProgressBar) convertView
                    .findViewById(R.id.attch_video_progress2);
            holder.attch_audio_progress2 = (ProgressBar) convertView
                    .findViewById(R.id.attch_audio_progress2);
            holder.attch_image_progress2 = (ProgressBar) convertView
                    .findViewById(R.id.attch_image_progress2);

            holder.seek_bar = (SeekBar) convertView.findViewById(R.id.seek_bar);
            holder.seek_bar_mine = (SeekBar) convertView
                    .findViewById(R.id.seek_bar_mine);

            holder.retry_btn = (TextView) convertView
                    .findViewById(R.id.retry_btn_video);
            holder.retry_btn_audio = (TextView) convertView
                    .findViewById(R.id.retry_btn_audio);
            holder.retry_btn_image = (TextView) convertView
                    .findViewById(R.id.retry_btn_image);

            holder.kit_logo = (ImageView) convertView
                    .findViewById(R.id.kit_logo);
            holder.mReceiverAttchVideobtnPlay = (ImageView) convertView
                    .findViewById(R.id.attch_videobtn);
            holder.date_seperator = (TextView) convertView
                    .findViewById(R.id.date_seperator);
            holder.frndchatrow = (LinearLayout) convertView
                    .findViewById(R.id.frndchatrow);
            holder.mychatrow = (LinearLayout) convertView
                    .findViewById(R.id.mychatrow);
            holder.frndname = (TextView) convertView
                    .findViewById(R.id.frndname);
            holder.frndchatmsg = (EmojiconTextView) convertView
                    .findViewById(R.id.frndchatmsg);
            holder.myname = (TextView) convertView.findViewById(R.id.myname);
            holder.mychatmsg = (EmojiconTextView) convertView
                    .findViewById(R.id.mychatmsg);

            holder.tick_imgview = (ImageView) convertView
                    .findViewById(R.id.tick_imgview);

//            holder.frndpic = (ImageView) convertView.findViewById(R.id.frndpic);
//            holder.mypic = (ImageView) convertView.findViewById(R.id.mypic);
            holder.attch_img = (ImageView) convertView
                    .findViewById(R.id.attch_img);
            holder.myattch_img = (ImageView) convertView
                    .findViewById(R.id.myattch_img);
            holder.downloadbtn = (ImageView) convertView
                    .findViewById(R.id.downloadbtn);
            holder.mydownloadbtn = (ImageView) convertView
                    .findViewById(R.id.mydownloadbtn);
            holder.attch_videoimg = (ImageView) convertView
                    .findViewById(R.id.attch_videoimg);
            holder.attch_myvideoimg = (ImageView) convertView
                    .findViewById(R.id.attch_myvideoimg);
            holder.frnd_img_file = (RelativeLayout) convertView
                    .findViewById(R.id.frnd_img_file);
            holder.frnd_video_file = (RelativeLayout) convertView
                    .findViewById(R.id.frnd_video_file);
            holder.frnd_audio_file = (RelativeLayout) convertView
                    .findViewById(R.id.frnd_audio_file);
            holder.my_img_file = (RelativeLayout) convertView
                    .findViewById(R.id.my_img_file);
            holder.my_video_file = (RelativeLayout) convertView
                    .findViewById(R.id.my_video_file);
            holder.my_audio_file = (RelativeLayout) convertView
                    .findViewById(R.id.my_audio_file);
            holder.attch_img_progress = (ProgressBar) convertView
                    .findViewById(R.id.attch_img_progress);
            holder.attch_myimg_progress = (ProgressBar) convertView
                    .findViewById(R.id.attch_myimg_progress);
            holder.attch_video_progress = (ProgressBar) convertView
                    .findViewById(R.id.attch_video_progress);
            holder.attch_myvideo_progress = (ProgressBar) convertView
                    .findViewById(R.id.attch_myvideo_progress);
            holder.audio_progress = (ProgressBar) convertView
                    .findViewById(R.id.audio_progress);
            holder.myaudio_progress = (ProgressBar) convertView
                    .findViewById(R.id.myaudio_progress);
            holder.audioplay = (TextView) convertView
                    .findViewById(R.id.audioplay);
            holder.myaudioplay = (TextView) convertView
                    .findViewById(R.id.myaudioplay);
            holder.audiostop = (TextView) convertView
                    .findViewById(R.id.audiostop);
            holder.myaudiostop = (TextView) convertView
                    .findViewById(R.id.myaudiostop);
            holder.mychat_msgdate = (TextView) convertView
                    .findViewById(R.id.mychat_msgdate);
            holder.mychat_msgtime = (TextView) convertView
                    .findViewById(R.id.mychat_msgtime);
            holder.frndchat_msgdate = (TextView) convertView
                    .findViewById(R.id.frndchat_msgdate);
            holder.frndchat_msgtime = (TextView) convertView
                    .findViewById(R.id.frndchat_msgtime);

            holder.broadcast_icon_layout = (LinearLayout) convertView
                    .findViewById(R.id.broadcast_icon_layout);
            holder.InInboxMsgCount = (TextView) convertView
                    .findViewById(R.id.inbox_msg_count);
            holder.MailCount = (TextView) convertView
                    .findViewById(R.id.inbox_mail_count);
            holder.inAppMsgCount = (TextView) convertView
                    .findViewById(R.id.inapp_msg_count);
            holder.inbox_icon = (ImageView) convertView
                    .findViewById(R.id.inbox_icon);
            holder.mail_inbox_icon = (ImageView) convertView
                    .findViewById(R.id.mail_inbox_icon);

            holder.myaudioplay.setTag(holder.seek_bar_mine);
            holder.myaudioplay.setTag(R.id.myaudio_progress,
                    holder.myaudio_progress);
            holder.audioplay.setTag(holder.seek_bar);
            holder.audioplay.setTag(R.id.audio_progress, holder.audio_progress);

            // holder.my_audio_file .setTag(holder.seek_bar_mine);


            holder.retry_btn.setOnClickListener(clickListener);
            holder.retry_btn_audio.setOnClickListener(clickListener);
            holder.retry_btn_image.setOnClickListener(clickListener);

            holder.retry_btn.setTag(position);
            holder.retry_btn_audio.setTag(position);
            holder.retry_btn_image.setTag(position);


            holder.mychatrow.setTag(holder.attch_myvideo_progress);
            holder.mychatrow.setTag(R.id.attch_myimg_progress,
                    holder.attch_myimg_progress);
            holder.mychatrow.setTag(R.id.myaudio_progress,
                    holder.myaudio_progress);

            holder.frndchatrow.setTag(R.id.attch_video_progress2,
                    holder.attch_video_progressAync);
            holder.frndchatrow.setTag(R.id.attch_audio_progress2,
                    holder.attch_audio_progress2);
            holder.frndchatrow.setTag(R.id.attch_image_progress2,
                    holder.attch_image_progress2);
            holder.date_seperator.setTag(position);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.seek_bar_mine.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                return true;
            }
        });

        holder.seek_bar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                return true;
            }
        });

        holder.downloadbtn.setTag(holder);

        final Chatmodel model = chathistorylist.get(position);

        holder.audioplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SeekBar bar = (SeekBar) v.getTag();

                ProgressBar myaudio_progress = (ProgressBar) v
                        .getTag(R.id.audio_progress);
                if (myaudio_progress != null) {
                    if (myaudio_progress.getVisibility() != View.VISIBLE) {
                        if (playflag) {
                            startPlaying(model.getMediapath(), bar);

                        } else {
                            stopPlaying();
                            startPlaying(model.getMediapath(), bar);
                        }
                    }
                }
            }
        });

        holder.myaudioplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SeekBar bar = (SeekBar) v.getTag();

                ProgressBar myaudio_progress = (ProgressBar) v
                        .getTag(R.id.myaudio_progress);

                if (myaudio_progress != null) {
                    if (myaudio_progress.getVisibility() != View.VISIBLE) {
                        if (playflag) {
                            startPlaying(model.getMediapath(), bar);
                        } else {
                            stopPlaying();
                            startPlaying(model.getMediapath(), bar);
                        }
                    }
                }
            }
        });

        holder.audiostop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (playflag) {
                    stopPlaying();
                } else {

                }
            }
        });
        holder.myaudiostop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (playflag) {
                    stopPlaying();
                } else {

                }
            }
        });
        holder.downloadbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ViewHolder holder = (ViewHolder) v.getTag();
                senMediatochatAccordingUI(holder, model.getMediatype(), false,
                        model, true);
                try {
                    Downloadandsetmedia(holder, model);
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });

        holder.frndchatrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressBar allThreeMixPrg = null;

                ProgressBar attch_myvideo_progress = (ProgressBar) v
                        .getTag(R.id.attch_video_progress2);
                ProgressBar attch_myimg_progress = (ProgressBar) v
                        .getTag(R.id.attch_image_progress2);
                ProgressBar myaudio_progress = (ProgressBar) v
                        .getTag(R.id.attch_audio_progress2);
                String type = chathistorylist.get(position).getMediatype();
                if (type.equals(GlobalData.Imagefile)) {
                    allThreeMixPrg = attch_myimg_progress;
                } else if (type.equals(GlobalData.Videofile)) {
                    allThreeMixPrg = attch_myvideo_progress;
                } else if (type.equals(GlobalData.Audiofile)) {
                    allThreeMixPrg = myaudio_progress;
                }
                if (allThreeMixPrg != null) {
                    if (allThreeMixPrg.getVisibility() != View.VISIBLE) {
                        showimgandVideo(position);
                    }
                }

            }
        });
        holder.mychatrow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ProgressBar allThreeMixPrg = null;

                ProgressBar attch_myvideo_progress = (ProgressBar) v.getTag();
                ProgressBar attch_myimg_progress = (ProgressBar) v
                        .getTag(R.id.attch_myimg_progress);
                ProgressBar myaudio_progress = (ProgressBar) v
                        .getTag(R.id.myaudio_progress);
                String type = chathistorylist.get(position).getMediatype();
                if (type.equals(GlobalData.Imagefile)) {
                    allThreeMixPrg = attch_myimg_progress;
                } else if (type.equals(GlobalData.Videofile)) {
                    allThreeMixPrg = attch_myvideo_progress;
                } else if (type.equals(GlobalData.Audiofile)) {
                    allThreeMixPrg = myaudio_progress;
                }
                if (allThreeMixPrg != null) {
                    if (allThreeMixPrg.getVisibility() != View.VISIBLE) {
                        showimgandVideo(position);
                    }
                }

            }
        });

        // holder.frndchatrow.setOnLongClickListener(longClickListener);
        holder.frndchatrow.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String type = chathistorylist.get(position).getMediatype();
                String rowid = chathistorylist.get(position).getMessagerowid();
                String txt = "";
                String msg_time = chathistorylist.get(position).getMsgtime();

                if (type != null && type.trim().length() != 0) {
                    txt = "";
                } else {
                    txt = chathistorylist.get(position).getChatmessage();
                }
                String recent_previous_last_msg = "", recent_previous_last_msgtime = "";
                if (position > 0) {
                    recent_previous_last_msg = chathistorylist
                            .get(position - 1).getChatmessage();
                    recent_previous_last_msgtime = chathistorylist.get(
                            position - 1).getMsgDateTimeCombine();
                }
                showcopy_delete_dia(txt, rowid, position,
                        recent_previous_last_msg, recent_previous_last_msgtime,
                        msg_time, type);

                return true;
            }
        });

        if (chathistorylist.get(position).getSeperator_line().length() > 0) {
            holder.date_seperator.setVisibility(View.VISIBLE);
            holder.date_seperator.setText(chathistorylist.get(position)
                    .getSeperator_line());
        } else
            holder.date_seperator.setVisibility(View.GONE);

        if (model.isMine()) {

            Utils.setdosisboldFont(holder.myname, activity);
            Utils.setmyriadproFont(holder.mychatmsg, activity);

            holder.frndchatrow.setVisibility(View.GONE);
            holder.mychatrow.setVisibility(View.VISIBLE);

            holder.myname.setText(SingleChatRoomFrgament.myModel.getName());
            holder.mychat_msgdate.setText(model.getMsgDate());
            holder.mychat_msgtime.setText(model.getMsgTime());

            if (model.getMediatype() != null
                    && model.getMediatype().trim().length() != 0) {
                holder.mychat_msgtime.setVisibility(View.VISIBLE);
                holder.mychatmsg.setVisibility(View.GONE);
                try {
                    setUIaccordingmessage(holder, model.getMediatype(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    senMediatochatAccordingUI(holder, model.getMediatype(),
                            true, model, true);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                if (model.getMediatype().equals(GlobalData.Imagefile)) {
                    if (model.getIsretry() == 1) {
                        holder.retry_btn_image.setVisibility(View.VISIBLE);
                    } else {
                        holder.retry_btn_image.setVisibility(View.GONE);
                    }
                } else if (model.getMediatype().equals(GlobalData.Videofile)) {
                    if (model.getIsretry() == 1) {
                        holder.retry_btn.setVisibility(View.VISIBLE);
                    } else {
                        holder.retry_btn.setVisibility(View.GONE);
                    }
                } else if (model.getMediatype().equals(GlobalData.Audiofile)) {
                    if (model.getIsretry() == 1) {
                        holder.retry_btn_audio.setVisibility(View.VISIBLE);
                    } else {
                        holder.retry_btn_audio.setVisibility(View.GONE);
                    }
                }

            } else {
                holder.retry_btn.setVisibility(View.GONE);
                try {
                    setUIaccordingmessage(holder, "", true);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                holder.mychatmsg.setVisibility(View.VISIBLE);
                String msg = com.kitever.utils.Emojione.shortnameToUnicode(
                        model.getChatmessage(), true);
//                holder.mychat_msgtime.setVisibility(View.GONE);
                holder.mychat_msgtime.setText(model.getMsgTime());
                holder.mychatmsg.setText(msg);


				/*
                 * if (holder.mychatmsg.getText().toString() != null &&
				 * holder.mychatmsg.getText().toString().length() > 0) { String
				 * upperString = holder.mychatmsg.getText()
				 * .toString().substring(0, 1).toUpperCase() +
				 * holder.mychatmsg.getText().toString() .substring(1);
				 * holder.mychatmsg.setText(upperString); }
				 */
            }

            if (SingleChatRoomFrgament.isgroup == 0) {
                holder.tick_imgview.setVisibility(View.VISIBLE);

                if (model.getSent_msg_success().equals("1")) {
                    holder.tick_imgview.setImageResource(R.drawable.sent);
                }
                if (model.getSent_msg_success().equals("0")) {
                    holder.tick_imgview.setImageResource(R.drawable.time_icon);
                }

                if (model.getDeliver_msg_success().equals("1")) {
                    holder.tick_imgview.setImageResource(R.drawable.delivered);
                }
                if (model.getRead_msg_success().equals("1")) {
                    holder.tick_imgview.setImageResource(R.drawable.read);
                }
                if (model.getSent_msg_success().equals("2")) {
                    holder.tick_imgview.setImageResource(R.drawable.sent);
                }
            }

            if (SingleChatRoomFrgament.isgroup == 2) {

                if (model.getIsBroadCast() == 3 || model.getIsBroadCast() == 2) {

                    holder.broadcast_icon_layout.setVisibility(View.VISIBLE);

                    holder.inAppMsgCount.setText(model.getInApMsgCount() + "");
                    holder.InInboxMsgCount
                            .setText(model.getInInboxCount() + "");
                    holder.MailCount.setText(model.getMail_count() + "");
                    holder.inbox_icon.setVisibility(View.GONE);
                    holder.mail_inbox_icon.setVisibility(View.GONE);

                } else {
                    holder.broadcast_icon_layout.setVisibility(View.GONE);
                }
                // if(model.getIsBroadCast()!=0){
                if (model.getExpiry_time() != null
                        && model.getMsgtime() != null
                        && model.getMsgtime().length() > 0
                        && model.getExpiry_time().length() > 0
                        && model.getIsBroadCast() != 3) {
                    Utils.expiryTimedifference(model.getExpiry_time(),
                            model.getMsgtime(), model.getMessage_id(), 2,
                            model.getMsg_packetid());
                    // }
                }

            } else if (SingleChatRoomFrgament.isgroup == 0) {
                if (model.getIsBroadCast() == 3) {
                    // if(GlobalData.MESSAGE_STATUS.equalsIgnoreCase(model.getMessageStatus().toString().trim())){
                    holder.inbox_icon.setVisibility(View.VISIBLE);
                    // }
                } else {
                    holder.inbox_icon.setVisibility(View.GONE);
                }

                if (model.getIsBroadCast() != 0) {
                    if (model.getExpiry_time() != null
                            && model.getMsgtime() != null
                            && model.getMsgtime().length() > 0
                            && model.getExpiry_time().length() > 0
                            && model.getIsBroadCast() != 3) {
                        Utils.expiryTimedifference(model.getExpiry_time(),
                                model.getMsgtime(), model.getMessage_id(), 0,
                                model.getMsg_packetid());
                    }
                }

            } else {

                holder.broadcast_icon_layout.setVisibility(View.GONE);
            }

        } else {

            Utils.setdosisboldFont(holder.frndname, activity);
            Utils.setmyriadproFont(holder.frndchatmsg, activity);

            if (model.getSent_msg_success().equals("4")) {
                holder.kit_logo.setVisibility(View.VISIBLE);
            } else if (model.getMediatype() != null
                    && model.getMediatype().trim().length() != 0
                    && model.getMediatype().equalsIgnoreCase("SMS")) {
                holder.kit_logo.setVisibility(View.VISIBLE);
            } else {
                holder.kit_logo.setVisibility(View.GONE);
            }

            holder.mychatrow.setVisibility(View.GONE);
            holder.frndchatrow.setVisibility(View.VISIBLE);

            holder.frndchat_msgdate.setText(model.getMsgDate());
            holder.frndchat_msgtime.setText(model.getMsgTime());
            if (SingleChatRoomFrgament.isgroup == 1) {
                String remoteid = model.getRemote_userid();

//                Contactmodel model2 = GlobalData.dbHelper
//                        .getDisplaynameforGroupuser(remoteid);
                Contactmodel model2 = GlobalData.dbHelper
                        .getDisplaynameforGroupuser(chathistorylist.get(position).getRemote_userid());

                if (model2 != null) {
                    if (model2.getName() != null) {
                        if (!model2.getName().equalsIgnoreCase("")) {
                            holder.frndname.setText(model2.getName());
                        } else {
                            holder.frndname.setText(model2.getNumber());
                        }
                    }

                } else {
                    if (!remoteid.equalsIgnoreCase("")) {
                        holder.frndname.setText(remoteid.split("@")[0]);
                    }
                }
            } else if (SingleChatRoomFrgament.isgroup == 0) {
                holder.frndname.setVisibility(View.GONE);
            } else {

                holder.frndname.setText(SingleChatRoomFrgament.frndname);

            }

            if (model.getMediatype() != null
                    && model.getMediatype().trim().length() != 0) {

                holder.frndchatmsg.setVisibility(View.GONE);
                holder.frndchat_msgtime.setVisibility(View.VISIBLE);

                try {
                    setUIaccordingmessage(holder, model.getMediatype(), false);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                if (model.getStatus().equals(
                        GlobalData.fileSendingordownloading)) {
                    holder.attch_img_progress.setVisibility(View.VISIBLE);

                } else {
                    holder.attch_img_progress.setVisibility(View.GONE);
                    if (new File(model.getMediapath()).exists()) {
                        senMediatochatAccordingUI(holder, model.getMediatype(),
                                false, model, true);
                    } else {
                        senMediatochatAccordingUI(holder, model.getMediatype(),
                                false, model, false);
                    }
                }

            } else {

                setUIaccordingmessage(holder, "", false);
                holder.frndchatmsg.setVisibility(View.VISIBLE);
//                holder.frndchat_msgtime.setVisibility(View.GONE);
                holder.frndchat_msgtime.setText(model.getMsgTime());

                holder.frndchatmsg.setText(model.getChatmessage());

                if (holder.frndchatmsg.getText().toString() != null
                        && holder.frndchatmsg.getText().toString().length() > 0) {
                    String upperString = holder.frndchatmsg.getText()
                            .toString().substring(0, 1).toUpperCase()
                            + holder.frndchatmsg.getText().toString()
                            .substring(1);
                    holder.frndchatmsg.setText(upperString);
                }
            }

        }

        holder.mychatrow.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                String type = chathistorylist.get(position).getMediatype();
                String rowid = chathistorylist.get(position).getMessagerowid();
                String msg_time = "";
                msg_time = chathistorylist.get(position).getMsgtime();
                String recent_previous_last_msg = "", recent_previous_last_msgtime = "";
                if (position > 0) {
                    recent_previous_last_msg = chathistorylist
                            .get(position - 1).getChatmessage();
                    recent_previous_last_msgtime = chathistorylist.get(
                            position - 1).getMsgDateTimeCombine();

                }
                if (type != null && type.trim().length() != 0) {
                    showcopy_delete_dia("", rowid, position,
                            recent_previous_last_msg,
                            recent_previous_last_msgtime, msg_time, type);
                } else {
                    showcopy_delete_dia(chathistorylist.get(position)
                                    .getChatmessage(), rowid, position,
                            recent_previous_last_msg,
                            recent_previous_last_msgtime, msg_time, type);
                }
                return true;
            }
        });

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private static class ViewHolder {

        EmojiconTextView frndchatmsg, mychatmsg;
        TextView frndname, myname, frndchat_msgdate, frndchat_msgtime,
                mychat_msgdate, mychat_msgtime;
        ProgressBar attch_video_progressAync, attch_audio_progress2,
                attch_image_progress2;
        ImageView mReceiverAttchVideobtnPlay, kit_logo;
        SeekBar seek_bar, seek_bar_mine;

        ImageView frndpic, mypic, attch_img, myattch_img, attch_videoimg,
                attch_myvideoimg, downloadbtn, mydownloadbtn, tick_imgview;
        ProgressBar audio_progress, myaudio_progress, attch_img_progress,
                attch_myimg_progress, attch_video_progress,
                attch_myvideo_progress;

        LinearLayout frndchatrow, mychatrow;
        RelativeLayout frnd_img_file, frnd_video_file, frnd_audio_file,
                my_img_file, my_video_file, my_audio_file;
        TextView date_seperator, audioplay, audiostop, myaudioplay,
                myaudiostop;
        LinearLayout broadcast_icon_layout;
        TextView inAppMsgCount, InInboxMsgCount, MailCount;
        TextView retry_btn, retry_btn_audio, retry_btn_image;
        ImageView inbox_icon,mail_inbox_icon;
    }

    private boolean dataExisted = true;

    public void showcopy_delete_dia(final String txt, final String rowid,
                                    final int position, final String updateRecentLastMsg,
                                    final String msgtime, final String msg_timeExact, final String type) {

        final String dataPath = chathistorylist.get(position).getMediapath();
        final Dialog copy_delete_dia = new Dialog(activity);
        // country_dia.setCancelable(false);
        copy_delete_dia.setContentView(R.layout.copy_delete_dialoglay);
        copy_delete_dia.setTitle("Choose Option");
        final TextView copybtn = (TextView) copy_delete_dia
                .findViewById(R.id.copybtn);
        TextView dltbtn = (TextView) copy_delete_dia.findViewById(R.id.dltbtn);
        TextView share_tbtn = (TextView) copy_delete_dia
                .findViewById(R.id.share_tbtn);
        TextView forwardBtn = (TextView) copy_delete_dia.findViewById(R.id.forward_tbtn);

        if (type.equalsIgnoreCase("I")) {
            Bitmap finalBitmap = Utils.decodeFile(dataPath, GlobalData.fileorignalWidth,
                    GlobalData.fileorignalheight);
            if (finalBitmap == null) {
                dataExisted = false;
            }
        } else {
            Bitmap finalBitmap = ThumbnailUtils.createVideoThumbnail(dataPath,
                    MediaStore.Images.Thumbnails.MINI_KIND);
            if (finalBitmap == null) {
                dataExisted = false;
            }
        }
        if (txt.trim().length() == 0) {
            copybtn.setVisibility(View.GONE);
        } else {
            copybtn.setVisibility(View.VISIBLE);
//			forwardBtn.setVisibility(View.GONE);
        }
        copybtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                copy(txt);
                copy_delete_dia.dismiss();
                Toast.makeText(activity, "message copied.", Toast.LENGTH_SHORT)
                        .show();

            }
        });
        dltbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // GlobalData.dbHelper.fetchSingleRow(rowid);
                if (position == 0) {
                    GlobalData.dbHelper.updateContactmsgData(
                            SingleChatRoomFrgament.remote_jid,
                            updateRecentLastMsg, msgtime);
                } else if (position == chathistorylist.size() - 1) {
                    GlobalData.dbHelper.updateContactmsgData(
                            SingleChatRoomFrgament.remote_jid,
                            updateRecentLastMsg, msgtime);
                }

                GlobalData.dbHelper.deleteSinglemsgrow(rowid);
                chathistorylist.remove(position);
                SingleChatRoomFrgament.mListView.setSelection(chathistorylist
                        .size() - 1);

                copy_delete_dia.dismiss();
                Toast.makeText(activity, "message deleted.", Toast.LENGTH_SHORT)
                        .show();

                MessageDeleteAsyncTask asyncTask = new MessageDeleteAsyncTask(
                        msg_timeExact, SingleChatRoomFrgament.isgroup);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    asyncTask.execute();
                }

            }
        });
        share_tbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if ((txt.trim().length() > 0)) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, txt);
                    sendIntent.putExtra("data_type", type);
                    sendIntent.setType("text/plain");
                    activity.startActivity(Intent
                            .createChooser(sendIntent, txt));
                } else {

                    if (dataExisted) {
                        Intent intent = shareImage(activity, dataPath, "image");
                        intent.putExtra("data_type", type);
                        intent.putExtra("share_path", dataPath);
                        activity.startActivity(intent);

                    } else {
                        dataExisted = true;
                        Toast.makeText(activity, "Data not existed .", Toast.LENGTH_LONG).show();
                    }
                }
                copy_delete_dia.dismiss();
            }
        });
        forwardBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isForwardData = true;
                pathToForwardData = dataPath;
                typeOfData = type;
                if (txt.length() > 0) {
                    msgToSend = txt;
                }
                if (dataExisted || txt.trim().length() > 0) {
                    Intent intent = new Intent(activity,
                            InAppMessageActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                } else {
                    dataExisted = true;
                    Toast.makeText(activity, "Data not existed .", Toast.LENGTH_LONG).show();
                }
                copy_delete_dia.dismiss();
            }
        });
        copy_delete_dia.show();
    }


    @SuppressWarnings("deprecation")
    public Intent shareImage(Context context, String pathToImage,
                             String mimeType) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra("from_share", true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        else
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        shareIntent.setType("image/*");

        // For a file in shared storage. For data in private storage, use a
        // ContentProvider.
        File f = new File(pathToImage);
        Uri uri = Uri.fromFile(f);// context.getFileStreamPath(f.getPath())
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return shareIntent;
    }

    public void showimgandVideo(int position) {
        String type = chathistorylist.get(position).getMediatype();
        if (type != null && type.trim().length() != 0) {
            if (new File(chathistorylist.get(position).getMediapath()).exists()) {
                // String filepath = new File(chathistorylist.get(position)
                // .getMediapath()).getAbsolutePath();
                if (type.equals(GlobalData.Imagefile)
                        || type.equals(GlobalData.Videofile)) {
                    String mimeType = getMimeType(chathistorylist.get(position)
                            .getMediapath());
                    Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
                    mediaIntent.setDataAndType(Uri.fromFile(new File(
                                    chathistorylist.get(position).getMediapath())),
                            mimeType);
                    activity.startActivity(mediaIntent);
                } else if (type.equals(GlobalData.Locationfile)) {
                    showLocationonMap(chathistorylist.get(position)
                            .getChatmessage());
                }

            } else {
                Toast.makeText(activity, "re-try",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("NewApi")
    public void copy(String txt) {

        if (txt != null && txt.trim().length() != 0) {
            String selectedText = txt;
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(selectedText);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData
                        .newPlainText("WordKeeper", selectedText);
                clipboard.setPrimaryClip(clip);
            }
        }
    }

    public void showLocationonMap(String latlng) {
        String[] latlngarray = latlng.split(",");
        float latitude = Float.parseFloat(latlngarray[0]);
        float longitude = Float.parseFloat(latlngarray[1]);
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude,
                longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");

        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(uri));
                activity.startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(activity, "Please install a maps application",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getMimeType(String url) {
        String extension = url.substring(url.lastIndexOf("."));
        String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                mimeTypeMap);
        return mimeType;
    }

    public void setUIaccordingmessage(ViewHolder holder, String Type,
                                      boolean mine) {

        try {
            if (Type.equals(GlobalData.Imagefile)) {
                if (mine) {
                    holder.my_img_file.setVisibility(View.VISIBLE);
                    holder.my_video_file.setVisibility(View.GONE);
                    holder.my_audio_file.setVisibility(View.GONE);
                } else {
                    holder.frnd_img_file.setVisibility(View.VISIBLE);
                    holder.frnd_video_file.setVisibility(View.GONE);
                    holder.frnd_audio_file.setVisibility(View.GONE);
                }

            } else if (Type.equals(GlobalData.Videofile)) {
                if (mine) {
                    holder.my_img_file.setVisibility(View.GONE);
                    holder.my_video_file.setVisibility(View.VISIBLE);
                    holder.my_audio_file.setVisibility(View.GONE);
                } else {
                    holder.frnd_img_file.setVisibility(View.GONE);
                    holder.frnd_video_file.setVisibility(View.VISIBLE);
                    holder.frnd_audio_file.setVisibility(View.GONE);
                }
            } else if (Type.equals(GlobalData.Audiofile)) {
                if (mine) {
                    holder.my_img_file.setVisibility(View.GONE);
                    holder.my_video_file.setVisibility(View.GONE);
                    holder.my_audio_file.setVisibility(View.VISIBLE);
                } else {
                    holder.frnd_img_file.setVisibility(View.GONE);
                    holder.frnd_video_file.setVisibility(View.GONE);
                    holder.frnd_audio_file.setVisibility(View.VISIBLE);
                }
            } else if (Type.equals(GlobalData.Locationfile)) {
                if (mine) {
                    holder.my_img_file.setVisibility(View.VISIBLE);
                    holder.my_video_file.setVisibility(View.GONE);
                    holder.my_audio_file.setVisibility(View.GONE);
                } else {
                    holder.frnd_img_file.setVisibility(View.VISIBLE);
                    holder.frnd_video_file.setVisibility(View.GONE);
                    holder.frnd_audio_file.setVisibility(View.GONE);
                }
            } else {
                if (mine) {
                    holder.my_img_file.setVisibility(View.GONE);
                    holder.my_video_file.setVisibility(View.GONE);
                    holder.my_audio_file.setVisibility(View.GONE);

                } else {
                    holder.frnd_img_file.setVisibility(View.GONE);
                    holder.frnd_video_file.setVisibility(View.GONE);
                    holder.frnd_audio_file.setVisibility(View.GONE);
                    holder.downloadbtn.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void senMediatochatAccordingUI(final ViewHolder holder, String Type,
                                          boolean mine, final Chatmodel model, boolean download) {

        try {
            if (Type.equals(GlobalData.Imagefile)) {

                if (mine) {
                    if (model.getOrignalbitmap() != null) {
                        if (model.getStatus().equals(
                                GlobalData.fileSendingordownloading)) {
                            holder.attch_myimg_progress
                                    .setVisibility(View.VISIBLE);
                        } else {
                            holder.attch_myimg_progress
                                    .setVisibility(View.GONE);
                        }

                        holder.myattch_img.setImageBitmap(model
                                .getOrignalbitmap());

                    } else {

                        holder.attch_myimg_progress.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            public void run() {
                                if (new File(model.getMediapath()).exists()) {

                                    final Bitmap img = BitmapFactory
                                            .decodeFile(model.getMediapath());
                                    model.setOrignalbitmap(img);
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            holder.attch_myimg_progress
                                                    .setVisibility(View.GONE);
                                            if (img != null) {
                                                holder.myattch_img
                                                        .setImageBitmap(img);

                                            }

                                        }
                                    });

                                } else {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            holder.attch_myimg_progress
                                                    .setVisibility(View.GONE);
                                        }
                                    });
                                }

                            }

                        }).start();
                    }

                } else {

                    if (download) {

                        if (model.getOrignalbitmap() != null) {

                            holder.attch_img_progress.setVisibility(View.GONE);
                            holder.downloadbtn.setVisibility(View.GONE);
                            holder.attch_img.setImageBitmap(model
                                    .getOrignalbitmap());

                        } else {

                            holder.attch_img_progress
                                    .setVisibility(View.VISIBLE);
                            holder.downloadbtn.setVisibility(View.GONE);
                            new Thread(new Runnable() {
                                public void run() {
                                    if (new File(model.getMediapath()).exists()) {

                                        final Bitmap img = BitmapFactory
                                                .decodeFile(model
                                                        .getMediapath());
                                        model.setOrignalbitmap(img);
                                        activity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                holder.attch_img_progress
                                                        .setVisibility(View.GONE);
                                                if (img != null) {
                                                    holder.attch_img
                                                            .setImageBitmap(img);
                                                }
                                            }
                                        });

                                    } else {
                                        activity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                holder.attch_img_progress
                                                        .setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }

                            }).start();
                        }
                    } else {

                        holder.attch_img_progress.setVisibility(View.GONE);
                        holder.downloadbtn.setVisibility(View.VISIBLE);

                        if (model.getThmbbitmap() != null) {

                            holder.attch_img.setImageBitmap(model
                                    .getThmbbitmap());
                        } else {
                            if (model.getMediathmb() != null) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Bitmap bit = BitmapFactory.decodeByteArray(
                                                model.getMediathmb(), 0,
                                                model.getMediathmb().length);

                                        model.setThmbbitmap(bit);
                                        if (bit != null) {
                                            activity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    holder.attch_img
                                                            .setImageBitmap(model
                                                                    .getThmbbitmap());
                                                }
                                            });

                                        }
                                    }
                                }).start();

                            }
                        }

                    }

                }

            } else if (Type.equals(GlobalData.Videofile)) {
                if (mine) {

                    if (model.getOrignalbitmap() != null) {
                        if (model.getStatus().equals(
                                GlobalData.fileSendingordownloading)) {
                            holder.attch_myvideo_progress
                                    .setVisibility(View.VISIBLE);
                        } else {
                            holder.attch_myvideo_progress
                                    .setVisibility(View.GONE);

                        }
                        holder.attch_myvideoimg.setImageBitmap(model
                                .getOrignalbitmap());
                    } else {
                        holder.attch_myvideo_progress
                                .setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            public void run() {
                                if (new File(model.getMediapath()).exists()) {

                                    final Bitmap img = ThumbnailUtils.createVideoThumbnail(
                                            model.getMediapath(),
                                            MediaStore.Images.Thumbnails.MINI_KIND);
                                    model.setOrignalbitmap(img);
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            holder.attch_myvideo_progress
                                                    .setVisibility(View.GONE);
                                            if (img != null) {
                                                holder.attch_myvideoimg
                                                        .setImageBitmap(img);
                                            }
                                        }
                                    });

                                } else {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            holder.attch_myvideo_progress
                                                    .setVisibility(View.GONE);
                                        }
                                    });
                                }

                            }

                        }).start();
                    }

                } else {
                    if (download) {
                        holder.attch_video_progress.setVisibility(View.GONE);
                        holder.downloadbtn.setVisibility(View.GONE);
                        holder.mReceiverAttchVideobtnPlay
                                .setVisibility(View.VISIBLE);
                        if (model.getOrignalbitmap() != null) {

                            final Bitmap img = ThumbnailUtils
                                    .createVideoThumbnail(
                                            model.getMediapath(),
                                            MediaStore.Images.Thumbnails.MINI_KIND);
                            model.setOrignalbitmap(img);
                            holder.attch_videoimg.setImageBitmap(img);

                        } else {

                            holder.attch_video_progress
                                    .setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                public void run() {
                                    if (new File(model.getMediapath()).exists()) {

                                        final Bitmap img = ThumbnailUtils.createVideoThumbnail(
                                                model.getMediapath(),
                                                MediaStore.Images.Thumbnails.MINI_KIND);

                                        // model.setOrignalbitmap(img);
                                        activity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                try {
                                                    holder.attch_video_progress
                                                            .setVisibility(View.GONE);
                                                    if (img != null) {
                                                        model.setOrignalbitmap(img);
                                                        holder.attch_videoimg
                                                                .setImageBitmap(img);

                                                    }
                                                } catch (Exception e) {

                                                    e.printStackTrace();
                                                }

                                            }
                                        });

                                    } else {
                                        activity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                holder.attch_video_progress
                                                        .setVisibility(View.GONE);
                                            }
                                        });
                                    }

                                }

                            }).start();

                        }

                    } else {
                        holder.attch_video_progress.setVisibility(View.VISIBLE);
                        holder.downloadbtn.setVisibility(View.VISIBLE);

                        if (model.getThmbbitmap() != null) {
                            holder.attch_videoimg.setImageBitmap(model
                                    .getThmbbitmap());
                        } else {
                            if (model.getMediathmb() != null) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Bitmap bit = BitmapFactory.decodeByteArray(
                                                model.getMediathmb(), 0,
                                                model.getMediathmb().length);

                                        model.setThmbbitmap(bit);
                                        if (bit != null) {
                                            activity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    holder.attch_videoimg
                                                            .setImageBitmap(model
                                                                    .getThmbbitmap());
                                                }
                                            });
                                        }
                                    }
                                }).start();

                            }
                        }

                    }

                }
            } else if (Type.equals(GlobalData.Audiofile)) {
                if (mine) {
                    if (model.getStatus().equals(
                            GlobalData.fileSendingordownloading)) {
                        holder.myaudio_progress.setVisibility(View.VISIBLE);

                    } else {
                        holder.myaudio_progress.setVisibility(View.GONE);

                    }

                } else {
                    if (download) {
                        holder.downloadbtn.setVisibility(View.GONE);
                        holder.audio_progress.setVisibility(View.GONE);
                        Utils.setMediatoChat(activity, model.getMediapath(),
                                model.getMediaUrl(), model.getMediatype(),
                                holder.attch_img, holder.audio_progress);
                    } else {
                        holder.downloadbtn.setVisibility(View.VISIBLE);
                        holder.audio_progress.setVisibility(View.GONE);

                    }

                }
            } else if (Type.equals(GlobalData.Locationfile)) {
                if (mine) {

                    if (model.getOrignalbitmap() != null) {
                        if (model.getStatus().equals(
                                GlobalData.fileSendingordownloading)) {
                            holder.attch_myimg_progress
                                    .setVisibility(View.VISIBLE);

                        } else {
                            holder.attch_myimg_progress
                                    .setVisibility(View.GONE);

                        }
                        holder.myattch_img.setImageBitmap(model
                                .getOrignalbitmap());
                    } else {
                        holder.attch_myimg_progress.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            public void run() {
                                if (new File(model.getMediapath()).exists()) {

                                    final Bitmap img = BitmapFactory
                                            .decodeFile(model.getMediapath());
                                    model.setOrignalbitmap(img);
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            holder.attch_myimg_progress
                                                    .setVisibility(View.GONE);
                                            if (img != null) {
                                                holder.myattch_img
                                                        .setImageBitmap(img);

                                            }

                                        }
                                    });

                                } else {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            holder.attch_myimg_progress
                                                    .setVisibility(View.GONE);
                                        }
                                    });
                                }

                            }

                        }).start();
                    }

                } else {
                    if (download) {

                        if (model.getOrignalbitmap() != null) {

                            holder.attch_img_progress.setVisibility(View.GONE);
                            holder.downloadbtn.setVisibility(View.GONE);

                            holder.attch_img.setImageBitmap(model
                                    .getOrignalbitmap());
                        } else {
                            holder.attch_img_progress
                                    .setVisibility(View.VISIBLE);
                            holder.downloadbtn.setVisibility(View.GONE);
                            new Thread(new Runnable() {
                                public void run() {
                                    if (new File(model.getMediapath()).exists()) {

                                        final Bitmap img = BitmapFactory
                                                .decodeFile(model
                                                        .getMediapath());
                                        model.setOrignalbitmap(img);
                                        activity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                holder.attch_img_progress
                                                        .setVisibility(View.GONE);
                                                if (img != null) {
                                                    holder.attch_img
                                                            .setImageBitmap(img);

                                                }

                                            }
                                        });

                                    } else {
                                        activity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                holder.attch_img_progress
                                                        .setVisibility(View.GONE);
                                            }
                                        });
                                    }

                                }

                            }).start();
                        }

                    } else {
                        holder.downloadbtn.setVisibility(View.VISIBLE);
                        holder.attch_img_progress.setVisibility(View.GONE);
                        if (model.getThmbbitmap() != null) {

                            holder.attch_img.setImageBitmap(model
                                    .getThmbbitmap());
                        } else {
                            if (model.getMediathmb() != null) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        Bitmap bit = BitmapFactory.decodeByteArray(
                                                model.getMediathmb(), 0,
                                                model.getMediathmb().length);

                                        model.setThmbbitmap(bit);
                                        if (bit != null) {
                                            activity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    holder.attch_img
                                                            .setImageBitmap(model
                                                                    .getThmbbitmap());
                                                }
                                            });

                                        }
                                    }
                                }).start();

                            }
                        }

                    }

                }
            }

			/*
             * if (SingleChatRoomFrgament.isgroup == 0) {
			 * holder.tick_imgview.setVisibility(View.VISIBLE); if
			 * (model.getSent_msg_success().equals("1")) {
			 * holder.tick_imgview.setImageResource(R.drawable.sent); } if
			 * (model.getSent_msg_success().equals("0")) { holder.tick_imgview
			 * .setImageResource(R.drawable.time_icon); }
			 *
			 * if (model.getDeliver_msg_success().equals("1")) {
			 * holder.tick_imgview.setImageResource(R.drawable.delivered); } if
			 * (model.getRead_msg_success().equals("1")) {
			 * holder.tick_imgview.setImageResource(R.drawable.read); } }
			 */
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void startPlaying(String filepath, final SeekBar bar) {

        try {

            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer = null;
                }
            }

            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(filepath);
                mPlayer.prepare();
                bar.setMax(mPlayer.getDuration());
                mPlayer.setOnCompletionListener(this); // Important
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer arg0) {

                        mPlayer.start();
                        playflag = true;
                        runSeekBar = new Runnable() {

                            @Override
                            public void run() {
                                if (bar != null) {
                                    seekUpdation(bar, mPlayer);
                                }
                            }
                        };

                        seekUpdation(bar, mPlayer);

                    }
                });

            } catch (IOException e) {

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void seekUpdation(SeekBar seek_bar, MediaPlayer player) {
        if (playflag) {

            seek_bar.setProgress(player.getCurrentPosition());
            Handler seekHandler = new Handler();
            seekHandler.postDelayed(runSeekBar, 1000);

        } else {
            if (seek_bar != null) {
                seek_bar.setProgress(0);
            }
        }
    }

    public void Downloadandsetmedia(final ViewHolder holder,
                                    final Chatmodel model) {
        model.setStatus(GlobalData.fileSendingordownloading);
        holder.downloadbtn.setVisibility(View.GONE);
        if (model.getMediatype().equals(GlobalData.Imagefile)
                || model.getMediatype().equals(GlobalData.Locationfile)) {
            holder.attch_img_progress.setVisibility(View.VISIBLE);
            holder.attch_image_progress2.setVisibility(View.VISIBLE);
        } else if (model.getMediatype().equals(GlobalData.Videofile)) {
            holder.attch_video_progress.setVisibility(View.GONE);
            holder.downloadbtn.setVisibility(View.GONE);
            holder.attch_video_progressAync.setVisibility(View.VISIBLE);
        } else if (model.getMediatype().equals(GlobalData.Audiofile)) {
            holder.audio_progress.setVisibility(View.VISIBLE);
            holder.attch_audio_progress2.setVisibility(View.VISIBLE);
        }

        // holder.attch_myvideo_progress.setVisibility(View.VISIBLE);
        // holder.attch_video_progressAync.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean bFlag = false;
                SingleChatRoomFrgament frgament = SingleChatRoomFrgament
                        .newInstance("");
                try {
                    URL fileurl = new URL(model.getMediaUrl());

                    URLConnection ucon = fileurl.openConnection();

                    final int fileLength = ucon.getContentLength();

                    InputStream is = ucon.getInputStream();
                    BufferedInputStream inStream = new BufferedInputStream(is,
                            1024 * 5);
                    FileOutputStream outStream = new FileOutputStream(model
                            .getMediapath());
                    byte[] buff = new byte[5 * 1024];

                    // Read bytes (and store them) until there is nothing more
                    // to
                    // read(-1)
                    int len;
                    long total = 0;

                    if (frgament != null) {
                        holder.attch_myvideo_progress.setMax(100);
                        holder.attch_video_progressAync.setMax(100);
                        holder.attch_image_progress2.setMax(100);
                        holder.attch_audio_progress2.setMax(100);

                        holder.attch_video_progressAync.setProgress(0);
                        holder.attch_image_progress2.setProgress(0);
                        holder.attch_audio_progress2.setProgress(0);

                        // holder.attch_video_progressAync.setProgress(0);
                        /*
						 * if (model.getMediatype().equals(GlobalData.Imagefile)
						 * ||
						 * model.getMediatype().equals(GlobalData.Locationfile))
						 * { holder.attch_image_progress2.setMax(100);
						 *
						 * } else if
						 * (model.getMediatype().equals(GlobalData.Videofile)) {
						 * holder.attch_video_progressAync.setMax(100);
						 *
						 *
						 * } else if
						 * (model.getMediatype().equals(GlobalData.Audiofile)) {
						 * holder.attch_audio_progress2.setMax(100); }
						 */
                    }

                    while ((len = inStream.read(buff)) != -1) {

                        total += len;
                        final long total2 = total;
                        if (frgament != null) {

                            mHandler.post(new Runnable() {
                                public void run() {
                                    holder.attch_video_progressAync
                                            .setProgress((int) (total2 * 100 / fileLength));
                                    holder.attch_image_progress2
                                            .setProgress((int) (total2 * 100 / fileLength));
                                    holder.attch_audio_progress2
                                            .setProgress((int) (total2 * 100 / fileLength));

                                }
                            });
                        }

                        outStream.write(buff, 0, len);
                        total++;
                    }

                    outStream.flush();
                    outStream.close();
                    inStream.close();
                    bFlag = true;

                } catch (Exception e) {
                    bFlag = false;
                    new File(model.getMediapath()).delete();
                    e.printStackTrace();

                }

                frgament = SingleChatRoomFrgament.newInstance("");
                if (frgament != null) {
                    if (bFlag) {
                        model.setOrignalbitmap(BitmapFactory.decodeFile(model
                                .getMediapath()));
                        setToUi(holder, model, true);
                    } else {
                        new File(model.getMediapath()).delete();
                        model.setOrignalbitmap(null);
                        setToUi(holder, model, false);
                    }
                }

            }
        }).start();

    }

    public void setToUi(final ViewHolder holder, final Chatmodel model,
                        final boolean success) {

        activity.runOnUiThread(new Runnable() {
            public void run() {
                SingleChatRoomFrgament frgament = SingleChatRoomFrgament
                        .newInstance("");
                if (frgament != null) {
                    if (model.getMediatype().equals(GlobalData.Imagefile)
                            || model.getMediatype().equals(
                            GlobalData.Locationfile)) {

                        holder.attch_image_progress2.setVisibility(View.GONE);

                    } else if (model.getMediatype()
                            .equals(GlobalData.Videofile)) {

                        holder.attch_video_progressAync
                                .setVisibility(View.GONE);

                    } else if (model.getMediatype()
                            .equals(GlobalData.Audiofile)) {

                        holder.audio_progress.setVisibility(View.VISIBLE);
                        holder.attch_audio_progress2.setVisibility(View.GONE);

                    }
                }

                model.setStatus(GlobalData.fileSendordownloadSuccess);

                if (success) {

                    holder.downloadbtn.setVisibility(View.GONE);

                    if (model.getMediatype().equals(GlobalData.Imagefile)
                            || model.getMediatype().equals(
                            GlobalData.Locationfile)) {
                        holder.attch_img_progress.setVisibility(View.GONE);
                        holder.attch_img.setImageBitmap(model
                                .getOrignalbitmap());

                        try {
                            Utils.saveImageInGallery(activity,
                                    model.getMediapath());
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    } else if (model.getMediatype()
                            .equals(GlobalData.Videofile)) {
                        holder.attch_video_progress.setVisibility(View.GONE);
                        holder.mReceiverAttchVideobtnPlay
                                .setVisibility(View.VISIBLE);
                        final Bitmap img = ThumbnailUtils.createVideoThumbnail(
                                model.getMediapath(),
                                MediaStore.Images.Thumbnails.MINI_KIND);

                        if (img != null) {
                            holder.attch_videoimg.setImageBitmap(img);
                        } else {
                            holder.attch_videoimg.setImageBitmap(model
                                    .getThmbbitmap());
                        }

                        // holder.attch_videoimg.setImageBitmap(model.getOrignalbitmap());

                        try {
                            Utils.saveImageInGallery(activity,
                                    model.getMediapath());
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    } else if (model.getMediatype()
                            .equals(GlobalData.Audiofile)) {
                        holder.audio_progress.setVisibility(View.GONE);
                    }
                } else {

                    holder.downloadbtn.setVisibility(View.VISIBLE);
                    holder.attch_img_progress.setVisibility(View.GONE);
                    holder.attch_video_progress.setVisibility(View.GONE);
                    holder.audio_progress.setVisibility(View.GONE);
                }
            }
        });
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ArrayList<Chatmodel> getChatArrayList() {
        return chathistorylist;
    }

    public void setChatListArrayList(ArrayList<Chatmodel> chatArrayList) {
        this.chathistorylist = chatArrayList;

    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            playflag = false;
            seekUpdation(null, null);
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopPlaying();
    }

    public void Downloadandsetmedia2(Chatmodel model) {
        model.setStatus("R");

        Utils.downloadfileandsave(model.getMediaUrl(), model.getMediapath(),
                model.getMessagerowid(),/* model.getDashboardID() */"",
                new Downloadfilelistner() {

                    @Override
                    public void downloadsuccess(String rowid,
                                                String dasboardjson) {

                        if (chathistorylist != null) {

                            // GlobalData.dbHelper.up

                            for (int i = 0; i < chathistorylist.size(); i++) {
                                Chatmodel modal = chathistorylist.get(i);
                                if (modal.getMessagerowid().equals(rowid)) {
                                    chathistorylist.get(i).setStatus("S");

                                    if (!dasboardjson.isEmpty()) {
                                        // chathistorylist.get(i).setJSON(dasboardjson);
                                    }
                                    break;
                                }
                            }
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void downloadfail(String rowid, String dasboardjson) {

                        if (chathistorylist != null) {
                            for (int i = 0; i < chathistorylist.size(); i++) {
                                Chatmodel modal = chathistorylist.get(i);
                                if (modal.getMessagerowid().equals(rowid)) {
                                    chathistorylist.get(i).setStatus("S");
                                    if (!dasboardjson.isEmpty()) {
                                        // chathistorylist.get(i).setJSON(dasboardjson);
                                    }
                                    break;
                                }
                            }
                            notifyDataSetChanged();
                        }
                    }
                });
        notifyDataSetChanged();

    }

    public OnLongClickListener getLongClickListener() {
        return longClickListener;
    }

    public void setLongClickListener(OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }

    public void setmPlayer(MediaPlayer mPlayer) {
        this.mPlayer = mPlayer;
    }

}