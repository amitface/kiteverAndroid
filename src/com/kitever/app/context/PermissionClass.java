package com.kitever.app.context;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class PermissionClass extends AppCompatActivity {
    AlertDialogManager alert = new AlertDialogManager();
    Activity Act;
    Activity activity;
    boolean b = false;
    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 5;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static final int CONTACTS_PERMISSION_REQUEST_CODE = 4;

    public PermissionClass(Activity Act) {
        this.Act = Act;
        this.activity = Act;
    }

    public boolean checkPermission(@NonNull String permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(Act, permissions) == PackageManager.PERMISSION_GRANTED) {
                b = true;
            } else {
                b = false;
                // ActivityCompat.requestPermissions(Act, new String[]{Manifest.permission.READ_CONTACTS}, permissionType);
            }
        } else {
            b = true;
        }
        return b;
    }

    private void goToSettings() {
        if (Act == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + Act.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        Act.startActivity(i);
    }

    public void showSettingsAlert(String pstring) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Act);

            // Setting Dialog Title
            alertDialog.setTitle("You Don't have Permission");

            // Setting Dialog Message
            alertDialog
                    .setMessage(pstring + " Permission Not Enabled. Please allow in App Settings for additional functionality");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            goToSettings();
                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
        }
    }


    public boolean checkPermissionForRecord() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForReadExternalStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForContacts() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForRecord() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            // Toast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            showSettingsAlert("Microphone Read");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            showSettingsAlert("Storage Read");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForReadExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            showSettingsAlert("Storage Read");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            //Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            showSettingsAlert("Camera Read");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }

    }

    public void requestPermissionForContacts() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
            // Toast.makeText(activity, "Contacts Read permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            showSettingsAlert("Contact Read");

        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_REQUEST_CODE);

        }

    }

}
