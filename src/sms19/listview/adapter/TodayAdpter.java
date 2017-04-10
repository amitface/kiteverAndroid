package sms19.listview.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;

import java.util.List;

import sms19.listview.newproject.model.SendSmsReport.ReportSms;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class TodayAdpter extends ArrayAdapter<ReportSms> {
	private List<ReportSms> details;
	private int resource;
	private Context context;
	private MoonIcon moonIcon;
	String mobile = "";
	String message = "";
	String Status = "";
	String Date = "";
	String DateT = "";
	String DateO = "";
	String DateTime = "";

	public TodayAdpter(Context context, int resource, List<ReportSms> objects) {

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
	public ReportSms getItem(int position) {
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

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resource, null);
		}

		TextView mob, msg, status, date, statisimage;

		mob = (TextView) convertView.findViewById(R.id.textMObile);
		msg = (TextView) convertView.findViewById(R.id.textMessage);
		status = (TextView) convertView.findViewById(R.id.textStaus);
		date = (TextView) convertView.findViewById(R.id.textDate2);
		statisimage = (TextView) convertView.findViewById(R.id.imagestatus);


		setRobotoThinFont(mob,context);
		setRobotoThinFont(msg,context);
		setRobotoThinFont(date,context);
		setRobotoThinFont(status,context);



		mob.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		msg.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		status.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		date.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));



		try {
			mobile = details.get(position).getMobile().trim();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		try {
			message = details.get(position).getMessage().trim();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		try {
			Status = details.get(position).getMessageStatus().trim();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		try {
			Date = details.get(position).getMessageDeliveredDate().trim();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		// *****************************************DATE
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

		// *****************************************DATE

		// *****************************************SET DATA
		try {
			date.setText(DateTime);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			mob.setText(mobile);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			if (message.length() > 20) {
				String mn = message.substring(0, 20);
				String htmlstr=" <u><b>...see more<b> </u>";
				msg.setText(mn+Html.fromHtml(htmlstr));
			} else {
				msg.setText(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Status.equalsIgnoreCase("0")) {
			statisimage.setText(context.getString(R.string.double_tick));
			statisimage.setTextColor(Color.parseColor("#00695C"));
			moonIcon.setTextfont(statisimage);
		} else if (Status.equalsIgnoreCase("2")) {
			statisimage.setText(context.getString(R.string.single_tick));
			statisimage.setTextColor(Color.parseColor("#FDD835"));
			moonIcon.setTextfont(statisimage);
		} else {
			statisimage.setText(context.getString(R.string.cross_tick));
			statisimage.setTextColor(Color.parseColor("#C62828"));
			moonIcon.setTextfont(statisimage);
		}

		// *****************************************SET DATA

		msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context)

						.setMessage(details.get(position).getMessage().trim())
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								})

						.setIcon(android.R.drawable.ic_dialog_alert).show();
			}
		});

		return convertView;
	}

}
