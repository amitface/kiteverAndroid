package sms19.listview.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.EmailEditor;
import sms19.listview.newproject.SettingHeaderActivity;
import sms19.listview.newproject.model.FtpDataListModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.fragment.POSSettingScreenFragment;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static sms19.inapp.msg.constant.Apiurls.KIT19_BASE_URL;

public class StorageAdapter extends BaseAdapter implements NetworkManager {

    private ArrayList<FtpDataListModel> arrayList;
    private ArrayList<Integer> checkedpositions;
    boolean[] checkBoxState;
    Activity activity;
    private int KEY_DELETE_STORAGE_FILE = 1;
    private int lastPosition;

    public StorageAdapter(ArrayList<FtpDataListModel> arrayList, Activity activity) {
        // TODO Auto-generated constructor stub
        this.arrayList = arrayList;
        checkedpositions = new ArrayList<>();
        checkBoxState = new boolean[arrayList.size()];
        this.activity = activity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.storage_adapter, parent,
                    false);
            // holder.layout=(RelativeLayout)convertView.findViewById(R.id.list_layout);
            holder.image = (ImageView) convertView.findViewById(R.id.ftp_image);
            holder.dataName = (TextView) convertView
                    .findViewById(R.id.image_name);
            holder.image_size = (TextView) convertView.findViewById(R.id.image_size);
            holder.delete = (ImageView) convertView
                    .findViewById(R.id.deleteStorageFile);

            holder.dataName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            holder.image_size.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

            setRobotoThinFont(holder.dataName,activity);
            setRobotoThinFont(holder.image_size,activity);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // holder.checkBox.setTag(position);
        FtpDataListModel dataListModel = arrayList.get(position);
        if (dataListModel.getDataType().equalsIgnoreCase("A")) {
            holder.image.setImageResource(R.drawable.audio_icon);
        } else if (dataListModel.getDataType().equalsIgnoreCase("V")) {
            holder.image.setImageResource(R.drawable.video_icon);
        } else if (dataListModel.getDataType().equalsIgnoreCase("I")) {
            holder.image.setImageResource(R.drawable.image_icon);
        } else if (dataListModel.getDataType().equalsIgnoreCase("F")) {
            holder.image.setImageResource(R.drawable.file_icon);
        }
        // holder.image.setImageBitmap(null);
        holder.dataName.setText(dataListModel.getDataName());
        holder.image_size.setText(dataListModel.getSize() + "KB");

        final String url = dataListModel.getMessageBody();

        holder.dataName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url.endsWith(".mp3")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "audio/*");
                    activity.startActivity(intent);
                } else if (url.endsWith(".mp4") || url.endsWith(".3gp")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "video/*");
                    activity.startActivity(intent);
                } else {
                PopupWebView(url);
                }

            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                deleteStorage(position);
            }
        });


        return convertView;
    }

    private void PopupWebView(String url) {

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Storage Media");
        WebView wv = new WebViewHelper(activity);
        wv.clearHistory();
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadUrl(url);
        WebClientClass webViewClient = new WebClientClass();
        wv.setWebViewClient(webViewClient);
        // wv.setWebViewClient(new WebViewClient());
        alert.setView(wv);
        alert.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    public ArrayList<Integer> getcheckeditemcount() {
        return this.checkedpositions;
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {
                if(requestId ==KEY_DELETE_STORAGE_FILE)
                {
                    DeleteStorageFile deleteStorageFile = new Gson().fromJson(response,DeleteStorageFile.class);
                    if(deleteStorageFile.getStatus().equalsIgnoreCase("true"))
                    {
                        Toast.makeText(activity,deleteStorageFile.getMessage(),Toast.LENGTH_SHORT).show();
                        arrayList.remove(lastPosition);
                        notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(activity,deleteStorageFile.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(activity, activity.getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
    }

    private void deleteFromStorage(int position) {
        lastPosition = position;
        if (Utils.isDeviceOnline(activity)) {

            Map map = new HashMap<>();
            map.put("Page", "StorageDeleteAPI");
            map.put("UserID", Utils.getUserId(activity));
            map.put("FileType", arrayList.get(position).getDataType());
            map.put("MessageBody", arrayList.get(position).getMessageBody());
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_DELETE_STORAGE_FILE, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast.makeText(activity, activity.getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity, "Internet connection not found", Toast.LENGTH_SHORT).show();
        }

    }

    private static class ViewHolder {
        ImageView image;
        TextView dataName, image_size;
        ImageView delete;
        // CheckBox checkBox;
    }

    private void deleteStorage(final int position) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage("Are you sure you want to delete " + arrayList.get(position).getDataName() + " ?")
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                deleteFromStorage(position);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        }).show();
    }

    private static class WebViewHelper extends WebView {
        public WebViewHelper(Context context) {
            super(context);
        }

        // Note this!
        @Override
        public boolean onCheckIsTextEditor() {
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (!hasFocus())
                        requestFocus();
                    break;
            }
            return super.onTouchEvent(ev);
        }
    }


    private class WebClientClass extends WebViewClient {
        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (pd == null) {
                pd = new ProgressDialog(activity);
            }
            /*pd.setTitle("Please wait");
            pd.setMessage("Page is loading..");
			pd.show();*/

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (pd != null)
                	pd.dismiss();
               /* if (url.endsWith(".mp4") || url.endsWith(".3gp")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "video*//*");
                    view.getContext().startActivity(intent);
                }*/

                super.onPageFinished(view, url);
        }


    }

    public class DeleteStorageFile
    {
        private String Status;

        private String Message;

        public String getStatus ()
        {
            return Status;
        }

        public void setStatus (String Status)
        {
            this.Status = Status;
        }

        public String getMessage ()
        {
            return Message;
        }

        public void setMessage (String Message)
        {
            this.Message = Message;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Status = "+Status+", Message = "+Message+"]";
        }
    }

}
