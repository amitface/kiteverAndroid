package sms19.listview.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.WebClientClass;
import com.kitever.pos.activity.POSBrandScreen;
import com.kitever.pos.activity.PosAddUpdateBrand;
import com.kitever.pos.adapter.POSBrandAdapter;
import com.kitever.pos.model.data.BrandModelData;
import com.kitever.utils.TotalRows;

import org.apache.http.util.EncodingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.Home;
import sms19.listview.newproject.MicroSite;
import sms19.listview.newproject.model.MicrositeDetails;

/**
 * Created by android on 16/2/17.
 */

public class MicroSiteAdapter extends BaseAdapter {


    private ArrayList<MicrositeDetails> filteredArrayList = null;
    private ArrayList<MicrositeDetails> modelList;

    private Context context;


    public MicroSiteAdapter(FragmentActivity activity, ArrayList<MicrositeDetails> micrositeDetails) {
        context = activity;
        modelList = micrositeDetails;
        filteredArrayList = micrositeDetails;
    }

    @Override
    public int getCount() {        // TODO Auto-generated method stub

        return filteredArrayList.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.micro_site_layout,
                    parent, false);
            viewHolder.leads = (TextView) convertView
                    .findViewById(R.id.micro_site_leads);
//			viewHolder.editBrand = (ImageView) convertView
//					.findViewById(R.id.edit_img);
//			viewHolder.deleteBrand = (ImageView) convertView
//					.findViewById(R.id.delete_img);
            viewHolder.visits = (TextView) convertView.findViewById(R.id.micro_site_visits);
            viewHolder.page = (TextView) convertView.findViewById(R.id.micro_site_page);
            viewHolder.medium = (TextView) convertView.findViewById(R.id.micro_site_medium);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (filteredArrayList.get(position).getPtype() != null)
            viewHolder.medium.setText(filteredArrayList.get(position).getPtype());
        if (filteredArrayList.get(position).getShortUrl() != null)
            viewHolder.page.setText(filteredArrayList.get(position).getShortUrl());
        if (filteredArrayList.get(position).getSiteVisits() != null)
            viewHolder.visits.setText(filteredArrayList.get(position).getSiteVisits());
        if (filteredArrayList.get(position).getSiteLeads() != null)
            viewHolder.leads.setText(filteredArrayList.get(position).getSiteLeads());
        viewHolder.page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMircoSitePopup(filteredArrayList.get(position).getShortUrl());
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView medium, page, visits, leads;

    }

    private void showMircoSitePopup(String link) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("MicroSite");
        WebView wv = new WebViewHelper(context);
        if (Build.VERSION.SDK_INT >= 19) {
            wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ProgressBar progressBar = new ProgressBar(context);
        wv.clearHistory();
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadUrl(link);

        // wv.loadUrl("http://kitever.com/BuyCredit.aspx?tab=plans&userid="
        // + UserId);
        WebClientClass webViewClient = new WebClientClass(context,progressBar);
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
}

