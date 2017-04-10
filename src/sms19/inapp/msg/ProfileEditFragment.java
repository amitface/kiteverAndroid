package sms19.inapp.msg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.janmuller.android.simplecropimage.CropImage;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;

public class ProfileEditFragment extends Fragment implements OnClickListener {


    private static ProfileEditFragment addName_1;
    private InAppMessageActivity homeActivity;
    private String mLastTitle = "";
    private String mCurrentPhotoPath = "";
    private int LAST_HIDE_MENU = 0;
    private EditText mGroupNameEdt;
    private Button mSaveBtn;
    private ImageView mProfileImage;
    private TextView mEdtiImageTextBtn;
    private GroupProfile groupProfile;
    public static int RESULT_LOAD_IMAGE = 1;
    public static int CAMERA_REQUEST = 1888;
    public static Uri mCapturedImageURI = null;
    public static String grouppicPath = "";
    static Bitmap finalBitmap = null;

    public static Handler onLineOfflineHandler = null;

    public final String TAG = "MainActivity";

    public final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    public final int REQUEST_CODE_GALLERY = 0x1;
    public final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private File mFileTemp;


    public static ProfileEditFragment getInstance() {

        return addName_1;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (InAppMessageActivity) getActivity();

        ConstantFlag.TAB_BACK_HANDLE_FRAGMENT = "2";

        homeActivity.groupActionBarControlIsVisual();

        LAST_HIDE_MENU = ConstantFields.HIDE_MENU;

        homeActivity.getLayout_name_status().setVisibility(View.GONE);
        homeActivity.getmActionBarImage().setVisibility(View.GONE);
        ConstantFields.HIDE_MENU = 3;
        homeActivity.invalidateOptionMenuItem();
        homeActivity.getActionbar_title().setVisibility(View.VISIBLE);
        mLastTitle = homeActivity.getActionbar_title().getText().toString();
        homeActivity.getActionbar_title().setText("EditGroup");

        groupProfile = GroupProfile.newInstance();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addName_1 = this;
        View v = inflater.inflate(R.layout.edit_group_profile, container, false);

        Init(v);
        setOnLineOfflineHandler();

        return v;
    }

    private void Init(View view) {

        mGroupNameEdt = (EditText) view.findViewById(R.id.group_name);
        mSaveBtn = (Button) view.findViewById(R.id.saveBtn);
        mProfileImage = (ImageView) view.findViewById(R.id.image);
        mEdtiImageTextBtn = (TextView) view.findViewById(R.id.edtImageBtn);

        mGroupNameEdt.setText(groupProfile.getGroupName());

        if (groupProfile.getFrndpic() != null) {

            Bitmap pic = BitmapFactory.decodeByteArray(groupProfile.getFrndpic(), 0, groupProfile.getFrndpic().length);
            mProfileImage.setImageBitmap(pic);
        }

        mSaveBtn.setOnClickListener(this);
        mEdtiImageTextBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (mSaveBtn == v) {

            profileEdit();
//			Utils.hideKeyBoardMethod(getActivity(), mSaveBtn);
            Utils.hideKeyBoard(homeActivity);

        }

        if (mEdtiImageTextBtn == v) {
            select_Image();
//			Utils.hideKeyBoardMethod(getActivity(), mSaveBtn);
            Utils.hideKeyBoard(homeActivity);
        }

    }


    public void profileEdit() {

        if (Utils.isDeviceOnline(getActivity())) {
            if (GlobalData.connection != null && GlobalData.connection.isConnected()) {


                if (!groupProfile.getGroupChat().isJoined()) {
                    MultiUserChat groupChat = new MultiUserChat(GlobalData.connection, groupProfile.getRemote_jid());
                    try {


                        DiscussionHistory history = new DiscussionHistory();
                        history.setMaxStanzas(0);

                        groupChat.join(InAppMessageActivity.myModel.getRemote_jid(), /*Apiurls.ADMIN_PASSWORD*/null,
                                history, SmackConfiguration
                                        .getPacketReplyTimeout());

                        groupProfile.setGroupChat(groupChat);
                    } catch (XMPPException e) {
                        Utils.printLog("Failed join group");
                        e.printStackTrace();
                    }
                }

                Message message = groupProfile.getGroupChat().createMessage();
                message.setSubject(GlobalData.subject_gourp_name_changed);
                message.addBody("custom_subject", GlobalData.subject_gourp_name_changed);
                message.setBody(mGroupNameEdt.getText().toString());

                try {
                    groupProfile.getGroupChat().sendMessage(message);
                    Toast.makeText(getActivity(), "Group name " + mGroupNameEdt.getText().toString() + " has been successfully changed",
                            Toast.LENGTH_SHORT).show();
                    homeActivity.getmUserNameTitle().setText(mGroupNameEdt.getText().toString());
                    getActivity().onBackPressed();

                } catch (XMPPException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    Toast.makeText(getActivity(), "Failed group name changed",
                            Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(getActivity(), "Not connected to server", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), "Check your network connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void sendMessageOnIconChange(String message) throws XMPPException {

        Message messag = groupProfile.getGroupChat().createMessage();
        messag.setSubject(GlobalData.subject_gourp_icon_changed);
        messag.addBody("custom_subject", GlobalData.subject_gourp_icon_changed);

        try {
			/*Bitmap bit = Utils.decodeFile(message,
					GlobalData.fileorignalWidth,
					GlobalData.fileorignalWidth);*/

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(message, options);
            message = Utils.convertTobase64string(bitmap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            messag.setBody(message);
            byte[] arrPic = Utils.decodeToImage(message);
            groupProfile.getGroupChat().sendMessage(messag);
            GlobalData.dbHelper.updateGroupIconInDB(groupProfile.getGroupChat().getRoom(), arrPic);


            Bitmap pic = null;
            if (arrPic != null) {
                pic = BitmapFactory.decodeByteArray(arrPic, 0, arrPic.length);

            }
            if (pic != null) {
                homeActivity.getmActionBarImage().setImageBitmap(pic);
                GroupProfile groupProfile = GroupProfile.newInstance();
                if (groupProfile != null) {
                    groupProfile.getImageView().setImageBitmap(pic);
                }
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public void select_Image() {

        final CharSequence[] items1 = {"Use Camera", "Choose Existing"};

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle("Choose Option :");
        builder1.setItems(items1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if (items1[item].equals("Choose Existing")) {

                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else if (items1[item].equals("Use Camera")) {

                    Boolean isSDPresent = android.os.Environment
                            .getExternalStorageState().equals(
                                    android.os.Environment.MEDIA_MOUNTED);
                    if (isSDPresent) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE,
                                "Umbochatgrouppic");

                        mCapturedImageURI = getActivity()
                                .getContentResolver()
                                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);

                        Intent cameraIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                mCapturedImageURI);

                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No SD card sorry!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE
                && resultCode == getActivity().RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            grouppicPath = cursor.getString(columnIndex);
            cursor.close();
			
			/*try {
			startCropImage(new File(grouppicPath));
			} catch (Exception e) {
				e.printStackTrace();
			}*/

            String imagePath = "";

            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(grouppicPath, bitmapOptions);
                imagePath = saveFileInProfileFolder(bitmap, homeActivity);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            if (imagePath != null && imagePath.length() > 0) {
                startCropImage(new File(imagePath));
            } else {
                startCropImage(new File(grouppicPath));
            }


        } else if (requestCode == CAMERA_REQUEST
                && resultCode == getActivity().RESULT_OK) {

            try {

                //startCropImage();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (REQUEST_CODE_CROP_IMAGE == requestCode) {
            String path = "";
            if (data != null) {
                path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }
            } else {
                return;
            }

            try {
                if (groupProfile.getGroupChat() != null && groupProfile.getGroupChat().isJoined()) {

                    //	sendMessageOnIconChange(grouppicPath);
                    sendMessageOnIconChange(path);


                    if (grouppicPath.trim().length() != 0) {
                        if (finalBitmap != null) {
                            finalBitmap.recycle();
                        }
					
					
					/*finalBitmap = Utils.decodeFile(path,
							GlobalData.profilepicthmb, GlobalData.profilepicthmb);*/

					/*finalBitmap = Utils.decodeFile(path,
							GlobalData.profilepicthmb, GlobalData.profilepicthmb);
					
					if (finalBitmap != null) {
						mProfileImage.setImageBitmap(finalBitmap);
					}*/

                    }


                    if (grouppicPath != null && grouppicPath.length() > 0) {// upload image path
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                        finalBitmap = bitmap;

                        if (finalBitmap != null) {
                            mProfileImage.setImageBitmap(finalBitmap);
                        }

                        saveFileInProfileFolderWithGroupName(bitmap, homeActivity, groupProfile.getGroupName().trim());
                    }


                } else {
                    try {

                        DiscussionHistory history = new DiscussionHistory();
                        history.setMaxStanzas(0);


                        groupProfile.getGroupChat().join(InAppMessageActivity.myModel.getRemote_jid(), null,
                                history, SmackConfiguration
                                        .getPacketReplyTimeout());


                    } catch (XMPPException e) {
                        Utils.printLog("Failed join group");
                        e.printStackTrace();
                    }
                }
            } catch (XMPPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }


    }


    private void startCropImage(File file) {

        Intent intent = new Intent(getActivity(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        //homeActivity.onBothTabPageControlIsGone();
        Utils.hideKeyBoardMethod(getActivity(), mSaveBtn);
        homeActivity.getActionbar_title().setText(groupProfile.getGroupName());
        //homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);

        //homeActivity.getLayout_name_status().setVisibility(View.GONE);

        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
        homeActivity.invalidateOptionMenuItem();

        addName_1 = null;


    }

    private void setOnLineOfflineHandler() {
        onLineOfflineHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {

                super.handleMessage(msg);
                if (getActivity() != null) {

                    try {
                        sms19.inapp.msg.model.Contactmodel contactmodel = null;
                        try {
                            contactmodel = GlobalData.dbHelper
                                    .getCustomStatus(groupProfile.getRemote_jid());
                        } catch (Exception e) {
                            contactmodel = null;
                            e.printStackTrace();
                        }
                        if (contactmodel != null) {
                            if (contactmodel.getStatus() != null) {

                            }
                            //String	frndname=contactmodel.getName();
                            mGroupNameEdt.setText(groupProfile.getGroupName());
                            //	homeActivity.getmUserNameTitle().setText(frndname);

                            byte[] frndpic = contactmodel.getAvatar();
                            if (frndpic != null) {

                                Bitmap pic2 = BitmapFactory.decodeByteArray(frndpic, 0, frndpic.length);
                                //pic2 = Utils.getResizedBitmap(pic2,GlobalData.fileorignalWidth,GlobalData.fileorignalheight);
                                mProfileImage.setImageBitmap(pic2);
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        };
    }

    public static String saveFileInProfileFolder(Bitmap bitmap, Activity homeActivity) {
        OutputStream output;

        String path = Utils.getProfilePath();
        File file = new File(path);

        try {
            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();

            Utils.saveImageInGallery(homeActivity, path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return path;
    }

    public String saveFileInProfileFolderWithGroupName(Bitmap bitmap, Activity homeActivity, String filename) {
        OutputStream output;

        String path = Utils.getProfilePathForGroupUpload(filename);
        File file = new File(path);

        try {
            output = new FileOutputStream(file);
            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();

            //Utils.saveImageInGallery(homeActivity, path );
            clientConnection(path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return path;
    }


    public void clientConnection(String path) {
        if (!(GlobalData.FTP_URL.equalsIgnoreCase("")
                || GlobalData.FTP_USER.equalsIgnoreCase("")
                || GlobalData.FTP_PASS.equalsIgnoreCase("") || GlobalData.FTP_HOST
                .equalsIgnoreCase(""))) {

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                try {
                    if (GlobalData.clientFtp != null) {
                        GlobalData.clientFtp.disconnect(true);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    GlobalData.clientFtp = null;
                }


                GlobalData.clientFtp = null;
                GlobalData.clientFtp = new FTPClient();
                GlobalData.clientFtp.connect(GlobalData.FTP_HOST.trim(), 21);
                GlobalData.clientFtp.login(GlobalData.FTP_USER.trim(), GlobalData.FTP_PASS);


                try {
                    ///String		userId = Utils.getUserId(getActivity());
                    //String test=GlobalData.clientFtp.currentDirectory();
                    //	GlobalData.clientFtp.changeDirectory("/"+userId);


                    ExecutorService executor = Executors.newFixedThreadPool(6);
                    Object[] iObj = new Object[7];
                    final File f = new File(path);


                    iObj[0] = f;


                    Runnable worker = new sms19.inapp.msg.asynctask.UploadThread(iObj);
                    executor.execute(worker);

                } catch (IllegalStateException e3) {
                    // TODO Auto-generated catch block
                    e3.printStackTrace();
                }

            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }

            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }
            } catch (FTPIllegalReplyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }
            } catch (FTPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
