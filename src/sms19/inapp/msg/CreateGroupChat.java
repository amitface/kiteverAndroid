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
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kitever.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import eu.janmuller.android.simplecropimage.CropImage;
import sms19.inapp.msg.adapter.BroadCastFirstAdapter;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;

public class CreateGroupChat extends Fragment implements OnClickListener {

    private View v;
    private ImageView grouppic;

    public static int RESULT_LOAD_IMAGE = 1;
    public static int CAMERA_REQUEST = 1888;
    public static Uri mCapturedImageURI = null;
    static Bitmap finalBitmap = null;
    public String grouppicPath = "";
    private static CreateGroupChat addName_1;
    private InAppMessageActivity homeActivity;
    private String mLastTitle = "";

    private EditText mGroupNameEdt;
    private Button mCreateGroup;
    private Button mCancel;
    private Button mBrowser;
    private int LAST_HIDE_MENU = 0;

    private BroadCastFirstAdapter chatAdapter = null;
    sms19.inapp.msg.CircularImageView circularImageView;

    private final String TAG = "MainActivity";
    public static final String TEMP_PHOTO_FILE_NAME = "SMS19.jpg";

    private final int REQUEST_CODE_GALLERY = 0x1;
    private final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    private final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private File mFileTemp;


    public static CreateGroupChat getInstance() {
        return addName_1;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (InAppMessageActivity) getActivity();

        homeActivity.groupActionBarControlIsVisual();

        homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
        mLastTitle = homeActivity.getActionbar_title().getText().toString();


        homeActivity.getLayout_name_status().setVisibility(View.GONE);
        homeActivity.getmActionBarImage().setVisibility(View.GONE);
        homeActivity.getActionbar_title().setVisibility(View.VISIBLE);

        mLastTitle = homeActivity.getActionbar_title().getText().toString();

        homeActivity.getActionbar_title().setText("Create Group Chat");

        LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
        ConstantFields.HIDE_MENU = 3;
        homeActivity.invalidateOptionMenuItem();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addName_1 = this;
        v = inflater.inflate(R.layout.create_group_chat, container, false);
        GlobalData.CREATE_GROUP_FRAGMENT = true;
        grouppicPath = "";
        finalBitmap = null;
        Init(v);

        String state = GlobalData.profilepicPath;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(GlobalData.profilepicPath, TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(GlobalData.profilepicPath, TEMP_PHOTO_FILE_NAME);
        }
        return v;
    }

    private void Init(View view) {
        mCreateGroup = (Button) view.findViewById(R.id.create);
        mCancel = (Button) view.findViewById(R.id.cancel);
        mBrowser = (Button) view.findViewById(R.id.browser);
        mGroupNameEdt = (EditText) view.findViewById(R.id.edt_groupname);

        circularImageView = (CircularImageView) view.findViewById(R.id.imageview);

        mCreateGroup.setOnClickListener(this);
        mBrowser.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browser:
                Utils.hideKeyBoardMethod(getActivity(), mBrowser);
                select_Image();
                break;

            case R.id.create:
                Utils.hideKeyBoardMethod(getActivity(), mCreateGroup);
                homeActivity.addGroupmember_frag();
                break;

            case R.id.cancel:
                Utils.hideKeyBoardMethod(getActivity(), mCreateGroup);
                homeActivity.backPress();
                break;

            default:
                break;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE
                && resultCode == getActivity().RESULT_OK && null != data) {

            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                grouppicPath = cursor.getString(columnIndex);
                cursor.close();
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


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST
                && resultCode == getActivity().RESULT_OK) {

            try {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(
                        mCapturedImageURI, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndex(filePathColumn[0]);

                grouppicPath = cursor.getString(column_index);
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            startCropImage(new File(grouppicPath));

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

            if (grouppicPath.trim().length() != 0) {
                if (finalBitmap != null) {
                    finalBitmap.recycle();
                }
                /*finalBitmap = Utils.decodeFile(grouppicPath,
                        GlobalData.fileorignalWidth, GlobalData.fileorignalheight);*/
                grouppicPath = path;
                //	finalBitmap = Utils.decodeFile(path,GlobalData.fileorignalWidth, GlobalData.fileorignalheight);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                finalBitmap = BitmapFactory.decodeFile(path, options);

                if (finalBitmap != null) {
                    circularImageView.setImageBitmap(finalBitmap);
                }
            }
        }
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


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        GlobalData.CREATE_GROUP_FRAGMENT = false;
        homeActivity.onBothTabPageControlIsGone();
        homeActivity.getActionbar_title().setText(mLastTitle);
        homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);

        homeActivity.getLayout_name_status().setVisibility(View.GONE);

        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
        homeActivity.invalidateOptionMenuItem();
        Utils.hideKeyBoard(getActivity());
        Utils.saveSelectedItem(getActivity(), new HashMap<String, Contactmodel>());

    }


    public EditText getGroupname_et() {
        return mGroupNameEdt;
    }


    private void startCropImage(File file) {

        Intent intent = new Intent(getActivity(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


}
