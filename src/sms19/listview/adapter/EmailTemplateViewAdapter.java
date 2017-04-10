package sms19.listview.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import org.apache.http.util.EncodingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.EmailEditor;
import sms19.listview.newproject.model.MailTemplateListDetailModel;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static sms19.inapp.msg.constant.Apiurls.KIT19_BASE_URL;

/**
 * Created by android on 2/3/17.
 */

public class EmailTemplateViewAdapter extends BaseAdapter implements NetworkManager {

    private final int KEY_DELETE_TEMPLATE_EMAIL = 1;
    ArrayList<MailTemplateListDetailModel> templateList;
    private int lastClicked;
    Context context;

    public EmailTemplateViewAdapter(Context context,
                                    ArrayList<MailTemplateListDetailModel> templateList) {
        this.templateList = templateList;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return templateList.size();
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.email_list_template_item,
                    parent, false);
            holder.templateTitle = (TextView) convertView
                    .findViewById(R.id.template_title);
            holder.TemplateDetails = (FrameLayout) convertView
                    .findViewById(R.id.template_details);
            holder.template_delete_email = (FrameLayout) convertView
                    .findViewById(R.id.template_delete_email);
            holder.template_edit = (FrameLayout) convertView.findViewById(R.id.template_edit);
            holder.tvTemplateSubject = (TextView) convertView.findViewById(R.id.tvTemplateSubject);

            holder.templateTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            setRobotoThinFont(holder.templateTitle, context);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MailTemplateListDetailModel model = templateList.get(position);
        holder.templateTitle.setText(model.getTemplateTitle());
        // holder.TemplateDetails.setText(model.getTemplate());
        String subject="";
        if (model.getmTemplateSubject().toString().length() > 15)
            subject = model.getmTemplateSubject().toString().substring(0, 15);
        else
            subject = model.getmTemplateSubject().toString();

        holder.tvTemplateSubject.setText(subject);
        holder.TemplateDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(model.getTemplateTitle());
                WebView wv = new WebViewHelper(context);
                wv.clearHistory();
                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                String Pass = Utils.getPassword(context);
                String userId = Utils.getUserId(context);
                String tempId = model.getTemplateId().trim();
                System.out.println("temp Id" + tempId);
                String postData = "password=" + Pass.trim();// +"&userid"+userId.trim()+"&templateid"+tempId;
                String base_url = KIT19_BASE_URL.replace("NewService.aspx?Page=", "");
                wv.postUrl(base_url + "/ViewMailTemplate.aspx?userid="
                                + userId + "&templateid=" + tempId,
                        EncodingUtils.getBytes(postData, "BASE64"));

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
        });

        holder.template_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EmailEditor.class);
                intent.putExtra("choice", "Edit");
                intent.putExtra("templateId", model.getTemplateId().trim());
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });

        holder.template_delete_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Utils.isDeviceOnline(context)) {
                        Map map = new HashMap<>();
                        map.put("Page", "DeleteMailTemplate");
                        map.put("UserID", Utils.getUserId(context));
                        map.put("TemplateMailID", model.getTemplateId().trim());
                        lastClicked = position;
                        new RequestManager().sendPostRequest(EmailTemplateViewAdapter.this, KEY_DELETE_TEMPLATE_EMAIL, map);
                    }
                } catch (Exception e) {
                    Toast.makeText(context, context.getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {
            if (requestId == KEY_DELETE_TEMPLATE_EMAIL) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                templateList.remove(lastClicked);
                notifyDataSetChanged();
            } else
                Toast.makeText(context, context.getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();

        } else
            Toast.makeText(context, context.getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    private static class ViewHolder {
        private TextView templateTitle, tvTemplateSubject;
        private FrameLayout TemplateDetails, template_delete_email, template_edit;
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
                pd = new ProgressDialog(context);
            }
            pd.setTitle("Please wait");
            pd.setMessage("Page is loading..");
            pd.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (pd != null)
                pd.dismiss();
            super.onPageFinished(view, url);
        }
    }
}
