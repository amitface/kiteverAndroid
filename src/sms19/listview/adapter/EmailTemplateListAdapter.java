package sms19.listview.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitever.android.R;

import org.apache.http.util.EncodingUtils;

import java.util.ArrayList;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.model.MailTemplateListDetailModel;

import static sms19.inapp.msg.constant.Apiurls.KIT19_BASE_URL;


public class EmailTemplateListAdapter extends BaseAdapter {

	ArrayList<MailTemplateListDetailModel> templateList;
	Context context;

	public EmailTemplateListAdapter(Context context,
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.email_template_item,
					parent, false);
			holder.templateTitle = (TextView) convertView
					.findViewById(R.id.template_title);
			holder.TemplateDetails = (TextView) convertView
					.findViewById(R.id.template_details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MailTemplateListDetailModel model = templateList.get(position);
		holder.templateTitle.setText(model.getTemplateTitle());
		// holder.TemplateDetails.setText(model.getTemplate());
		holder.TemplateDetails.setText("Click to view");
		holder.TemplateDetails.setPaintFlags(holder.TemplateDetails
				.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		holder.TemplateDetails.setOnClickListener(new OnClickListener() {

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
				String base_url=KIT19_BASE_URL.replace("NewService.aspx?Page=","");
				wv.postUrl(base_url+"/ViewMailTemplate.aspx?userid="
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

//		holder.templateTitle.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, EmailEditor.class);
//				intent.putExtra("choice","Edit");
//				intent.putExtra("templateId",model.getTemplateId().trim());
//				context.startActivity(intent);
//			}
//		});
		return convertView;
	}

	private static class ViewHolder {
		private TextView templateTitle, TemplateDetails;
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
