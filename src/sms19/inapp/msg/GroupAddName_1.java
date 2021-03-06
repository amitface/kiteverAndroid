package sms19.inapp.msg;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kitever.android.R;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.Home;

public class GroupAddName_1 extends Fragment implements OnClickListener {

    private View v;
    private ImageView grouppic;
    public static EditText groupname_et;
    public static int RESULT_LOAD_IMAGE = 1;
    public static int CAMERA_REQUEST = 1888;
    public static Uri mCapturedImageURI = null;
    static Bitmap finalBitmap = null;
    public static String grouppicPath = "";
    private static GroupAddName_1 addName_1;
    private Home homeActivity;
    private String mLastTitle = "";


    public static GroupAddName_1 getInstance() {

        return addName_1;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (Home) getActivity();

//		homeActivity.groupActionBarControlIsVisual();
//		homeActivity.getActionbar_title().setVisibility(View.VISIBLE);
//		mLastTitle=homeActivity.getActionbar_title().getText().toString();
//		
//		homeActivity.getActionbar_title().setText("Add Group");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addName_1 = this;
        v = inflater.inflate(R.layout.fragment_groupchat, container, false);

        grouppicPath = "";
        finalBitmap = null;
        Init();
        return v;
    }

    private void Init() {
        grouppic = (ImageView) v.findViewById(R.id.grouppic);
        groupname_et = (EditText) v.findViewById(R.id.groupname_et);
        grouppic.setOnClickListener(this);
//		homeActivity.getCamera_btn().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.grouppic:
                Utils.hideKeyBoardMethod(getActivity(), grouppic);
                select_Image();
                break;

            case R.id.chat_addfilebuttonlay:
                //homeActivity.addGroupmember_frag();
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

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            grouppicPath = cursor.getString(columnIndex);
            cursor.close();
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
        }
        if (grouppicPath.trim().length() != 0) {
            if (finalBitmap != null) {
                finalBitmap.recycle();
            }
            finalBitmap = Utils.decodeFile(grouppicPath,
                    GlobalData.profilepicthmb, GlobalData.profilepicthmb);
            if (finalBitmap != null) {
                finalBitmap = Utils.getCircularBitmapWithBorder(finalBitmap, 1,
                        0x009d0000);
                grouppic.setImageBitmap(finalBitmap);
            }

        }
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

		/*homeActivity.onBothTabPageControlIsGone();
		homeActivity.getActionbar_title().setText(mLastTitle);
		homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);*/

    }

}
