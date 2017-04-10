package com.kitever.app.context;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class WebClientClass extends WebViewClient {
    ProgressDialog pd = null;
    boolean timeout;
    Context context;
    ProgressBar pbar;

    public WebClientClass(Context context, ProgressBar pbar)
    {
       // timeout = true;
        this.context=context;
        this.pbar=pbar;

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        pbar.setVisibility(View.VISIBLE);
       /* Handler myHandler = new Handler();
        Runnable run = new Runnable() {
            public void run() {
                if(timeout) {
                    pbar.setVisibility(View.GONE);
                    alertMessage();
                }
            }
        };
        myHandler.postDelayed(run, 10000);  *///10sec timeout for loading webview
       super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
       // timeout = false;
        pbar.setVisibility(View.GONE);
        super.onPageFinished(view, url);
    }


}