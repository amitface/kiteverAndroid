package sms19.listview.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;

import org.apache.http.util.EncodingUtils;

import java.util.List;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.model.DeliveryReport;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static sms19.inapp.msg.constant.Apiurls.KIT19_BASE_URL;

public class ReportMailAdapter extends ArrayAdapter<DeliveryReport> {
	private List<DeliveryReport> details;
	private int resource;
	private Context context;
	private MoonIcon moonIcon;
	String email = "";
	String message = "";
	String Status = "";
	String Date = "";
	String DateT = "";
	String DateO = "";
	String DateTime = "";
	String tempId="";

	public ReportMailAdapter(Context context, int resource,
			List<DeliveryReport> objects) {
		super(context, resource, objects);
		this.details = objects;
		this.context = context;
		this.resource = resource;
		moonIcon = new MoonIcon(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		try {
			return details.size();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public DeliveryReport getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(resource, null);

			viewHolder.mail = (TextView) convertView.findViewById(R.id.textMObile);
			viewHolder.msg = (TextView) convertView.findViewById(R.id.textMessage);
			viewHolder.status = (TextView) convertView.findViewById(R.id.textStaus);
			viewHolder.date = (TextView) convertView.findViewById(R.id.textDate2);
			viewHolder.statisimage = (TextView) convertView.findViewById(R.id.imagestatus);


			setRobotoThinFont(viewHolder.mail,context);
			setRobotoThinFont(viewHolder.msg,context);
			setRobotoThinFont(viewHolder.date,context);
			setRobotoThinFont(viewHolder.status,context);



			viewHolder.mail.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			viewHolder.msg.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			viewHolder.status.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			viewHolder.date.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}


		try {
			email = details.get(position).getEmailID().trim();
			tempId = details.get(position).getTemplateMailId().trim();
			if(tempId.length()==0) message="Customize";
			else message = details.get(position).getMessage().trim();
			Status = details.get(position).getStatus().trim();
			Date = details.get(position).getDateTime().trim();
		} catch (Exception e2) {
			e2.printStackTrace();
		}


		try {

			int pod = Date.indexOf("T");
			DateT = Date.substring((pod + 1), (pod + 9));
			DateO = Date.substring(0, Date.indexOf("T"));

			String dataWithGoodFormat[] = DateO.split("-");
			String dataGood = dataWithGoodFormat[2] + "-"
					+ dataWithGoodFormat[1] + "-" + dataWithGoodFormat[0];
			DateTime = DateT;

		} catch (Exception e1) {

			e1.printStackTrace();
		}

		try {
			viewHolder.date.setText(DateTime);
			viewHolder.mail.setText(email);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			if (message.length() > 20) {
				String mn = message.substring(0, 20);
				String htmlstr="<u>"+mn+ "<b>...see more<b> </u>";
				viewHolder.msg.setText(Html.fromHtml(htmlstr));
			} else {
				String htmlstr="<u>"+message+ " </u>";
				viewHolder.msg.setText(Html.fromHtml(htmlstr));
				//msg.setText(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		if (Status.equalsIgnoreCase("0")) {
			viewHolder.statisimage.setText(context.getString(R.string.double_tick));
			viewHolder.statisimage.setTextColor(Color.parseColor("#00E676"));
			moonIcon.setTextfont(viewHolder.statisimage);
		} else if (Status.equalsIgnoreCase("2")) {
			viewHolder.statisimage.setText(context.getString(R.string.single_tick));
			viewHolder.statisimage.setTextColor(Color.parseColor("#FFEA00"));
			moonIcon.setTextfont(viewHolder.statisimage);
		} else {
			viewHolder.statisimage.setText(context.getString(R.string.cross_tick));
			viewHolder.statisimage.setTextColor(Color.parseColor("#C62828"));
			moonIcon.setTextfont(viewHolder.statisimage);
		}

		// *****************************************SET DATA

		viewHolder.msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(tempId!=null && tempId.length()>0) {
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setTitle(details.get(position).getMessage());
					WebView wv = new WebViewHelper(context);
					wv.clearHistory();
					wv.getSettings().setJavaScriptEnabled(true);
					wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
					String Pass = Utils.getPassword(context);
					String userId = Utils.getUserId(context);
					System.out.println("temp Id" + tempId);
					String postData = "password=" + Pass.trim();// +"&userid"+userId.trim()+"&templateid"+tempId;
					String base_url=KIT19_BASE_URL.replace("NewService.aspx?Page=","");
					wv.postUrl(base_url + "ViewMailTemplate.aspx?userid="
									+ userId + "&templateid=" + tempId,
							EncodingUtils.getBytes(postData, "BASE64"));
					Log.i("Templateid","id-"+tempId);

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
			}
		});

		return convertView;
	}


	private static class ViewHolder {
		TextView mail, msg, status, date, statisimage;
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