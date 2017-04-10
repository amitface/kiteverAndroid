package com.kitever.app.context;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.kitever.android.R;

import sms19.listview.newproject.Home;


public class AlertDialogManager {
    AlertDialog levelDialog = null;


    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     *                - pass null if you don't want icon
     */
    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.successalert : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showAlertPopup(final Context context, String title, String message,
                               Boolean status, final Class<?> c) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.successalert : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(context, c);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }

        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void webviewPoup(final Context context, String title, String url, byte[] data) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.webviewpopup);
        dialog.setTitle(title);
        final WebView wv = (WebView) dialog.findViewById(R.id.popupwebview);
        ProgressBar pbar = (ProgressBar) dialog.findViewById(R.id.popuploader);
        Button close = (Button) dialog.findViewById(R.id.close);

        if (Build.VERSION.SDK_INT >= 19) {
            wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // deleteCookie(context,wv);
        wv.clearHistory();
        wv.clearCache(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setCacheMode(wv.getSettings().LOAD_NO_CACHE);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.postUrl(url, data);
        WebClientClass webViewClient = new WebClientClass(context, pbar);
        wv.setWebViewClient(webViewClient);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCookie(context, wv);
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void deleteCookie(Context context, WebView wv) {
        CookieSyncManager.createInstance(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("cookies", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.d("cookie", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
        wv.clearCache(true);
        wv.destroy();
    }

    public String getCookie(String siteName, String CookieName) {
        String CookieValue = null;
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        String[] temp = cookies.split(";");
        for (String ar1 : temp) {
            if (ar1.contains(CookieName)) {
                String[] temp1 = ar1.split("=");
                CookieValue = temp1[1];
            }
        }
        return CookieValue;
    }

}