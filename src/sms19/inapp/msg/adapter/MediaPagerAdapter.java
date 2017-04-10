package sms19.inapp.msg.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kitever.android.R;

import java.io.File;
import java.util.ArrayList;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

public class MediaPagerAdapter extends PagerAdapter {

    int NumberOfPages = 5;
    ArrayList<Chatmodel> chathistorylist = null;

    int[] res = {
            android.R.drawable.ic_dialog_alert,
            android.R.drawable.ic_menu_camera,
            android.R.drawable.ic_menu_compass,
            android.R.drawable.ic_menu_directions,
            android.R.drawable.ic_menu_gallery};
    int[] backgroundcolor = {
            0xFF101010,
            0xFF202020,
            0xFF303030,
            0xFF404040,
            0xFF505050};

    private Activity activity;


    public MediaPagerAdapter(Activity activity, ArrayList<Chatmodel> chathistorylist) {
        this.activity = activity;
        this.chathistorylist = chathistorylist;
    }

    @Override
    public int getCount() {
        return chathistorylist.size();
    }

    @Override
    public float getPageWidth(int position) {
        return (0.3f);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        View view = activity.getLayoutInflater().inflate(R.layout.row_media_gallery, null, false);

        final Chatmodel model = chathistorylist.get(position);

        ImageView imageview = (ImageView) view.findViewById(R.id.imageview);

        ImageView mPlayBtn = (ImageView) view.findViewById(R.id.play_btn);
        ImageView mAudioBtn = (ImageView) view.findViewById(R.id.audio);


        imageview.setTag(position);
        mAudioBtn.setTag(position);
        if (model.isMine()) {


            final Bitmap img = BitmapFactory.decodeFile(model.getMediapath());


            if (img != null) {
                imageview.setImageBitmap(img);
            }

            if (model.getMediatype() != null
                    && model.getMediatype().trim().length() != 0) {


                senMediatochatAccordingUI(imageview, mPlayBtn, model.getMediatype(), true, model, true, mAudioBtn);

            } else {


            }

        } else {


            if (SingleChatRoomFrgament.isgroup == 1) {

                String remoteid = model.getRemote_userid();
                Contactmodel model2 = GlobalData.dbHelper.getDisplaynameforGroupuser(remoteid);
                if (model2 != null) {

                }
            }

            if (model.getMediatype() != null && model.getMediatype().trim().length() != 0) {


                if (model.getStatus().equals(GlobalData.fileSendingordownloading)) {


                } else {

                    if (new File(model.getMediapath()).exists()) {

                        senMediatochatAccordingUI(imageview, mPlayBtn, model.getMediatype(), false, model, true, mAudioBtn);
                    } else {
                        senMediatochatAccordingUI(imageview, mPlayBtn, model.getMediatype(), false, model, false, mAudioBtn);
                    }
                }

            }


            if (model.getMediathmb() != null && !model.getMediatype().equals(GlobalData.Audiofile)) {
                Bitmap bit = BitmapFactory.decodeByteArray(
                        model.getMediathmb(), 0,
                        model.getMediathmb().length);

                if (bit != null) {
                    imageview.setImageBitmap(bit);
                }
            }

        }

        imageview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int pos = (Integer) v.getTag();
                showimgandVideo(pos);
            }
        });
        mAudioBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int pos = (Integer) v.getTag();
                showimgandVideo(pos);
            }
        });


        container.addView(view, 0);
        return view;
    }


    public void showimgandVideo(int position) {
        String type = chathistorylist.get(position).getMediatype();
        if (type != null && type.trim().length() != 0) {
            if (new File(chathistorylist.get(position).getMediapath()).exists()) {
                // String filepath = new File(chathistorylist.get(position)
                // .getMediapath()).getAbsolutePath();
                if (type.equals(GlobalData.Imagefile) || type.equals(GlobalData.Videofile) || type.equals(GlobalData.Audiofile)) {
                    String mimeType = getMimeType(chathistorylist.get(position)
                            .getMediapath());
                    Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
                    mediaIntent.setDataAndType(Uri.fromFile(new File(
                                    chathistorylist.get(position).getMediapath())),
                            mimeType);
                    activity.startActivity(mediaIntent);
                } else if (type.equals(GlobalData.Locationfile)) {
                    /*showLocationonMap(chathistorylist.get(position)
							.getChatmessage());*/
                }

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

    public void senMediatochatAccordingUI(final ImageView myattch_img, final ImageView mPlayBtn, String Type,
                                          boolean mine, final Chatmodel model, boolean download, final ImageView audioImage) {
        if (Type.equals(GlobalData.Imagefile)) {

            mPlayBtn.setVisibility(View.GONE);

            if (mine) {
                if (model.getOrignalbitmap() != null) {
                    if (model.getStatus().equals(
                            GlobalData.fileSendingordownloading)) {


                    } else {


                    }
                    myattch_img.setImageBitmap(model.getOrignalbitmap());
                } else {

                    new Thread(new Runnable() {
                        public void run() {
                            if (new File(model.getMediapath()).exists()) {

                                final Bitmap img = BitmapFactory
                                        .decodeFile(model.getMediapath());
                                model.setOrignalbitmap(img);
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {

                                        if (img != null) {
                                            myattch_img
                                                    .setImageBitmap(img);

                                        }

                                    }
                                });

                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {

                                    }
                                });
                            }

                        }

                    }).start();
                }

            } else {

                if (download) {

                    if (model.getOrignalbitmap() != null) {


                        myattch_img.setImageBitmap(model
                                .getOrignalbitmap());
                    } else {

                        new Thread(new Runnable() {
                            public void run() {
                                if (new File(model.getMediapath()).exists()) {

                                    final Bitmap img = BitmapFactory
                                            .decodeFile(model.getMediapath());
                                    model.setOrignalbitmap(img);
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {

                                            if (img != null) {
                                                myattch_img
                                                        .setImageBitmap(img);

                                            }

                                        }
                                    });

                                } else {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {

                                        }
                                    });
                                }

                            }

                        }).start();
                    }

                } else {

                    if (model.getThmbbitmap() != null) {

                        myattch_img.setImageBitmap(model.getThmbbitmap());
                    } else {
                        if (model.getMediathmb() != null) {
                            new Thread(new Runnable() {
                                public void run() {
                                    Bitmap bit = BitmapFactory.decodeByteArray(
                                            model.getMediathmb(), 0,
                                            model.getMediathmb().length);

                                    model.setThmbbitmap(bit);
                                    if (bit != null) {
                                        activity.runOnUiThread(
                                                new Runnable() {
                                                    public void run() {
                                                        myattch_img
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
            final Bitmap img = ThumbnailUtils.createVideoThumbnail(
                    model.getMediapath(),
                    MediaStore.Images.Thumbnails.MINI_KIND);
            model.setOrignalbitmap(img);

            activity.runOnUiThread(new Runnable() {
                public void run() {

                    if (img != null) {
                        mPlayBtn.setVisibility(View.VISIBLE);
                        myattch_img.setImageBitmap(img);
                    }

                }
            });

        } else if (Type.equals(GlobalData.Audiofile)) {


            activity.runOnUiThread(new Runnable() {
                public void run() {
                    myattch_img.setVisibility(View.GONE);
                    mPlayBtn.setVisibility(View.GONE);
                    audioImage.setVisibility(View.VISIBLE);


                }
            });

        }


    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}