package sms19.listview.newproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.ArrayList;
import java.util.Iterator;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.adapter.EmailTemplateListAdapter;
import sms19.listview.newproject.model.MailTemplateListDetailModel;
import sms19.listview.newproject.model.MailTemplateListModel;
import sms19.listview.newproject.model.UserMailTemplateListDetail;

public class EmailTemplateActivity extends ActionBarActivity implements
		NetworkManager {
	private int TEMPLATE_REQ_ID = 100;
	ListView templateListView;
	TextView notAvailableTxt;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_template_list);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
		bar.setTitle(Html.fromHtml("<font color='#ffffff'>Email Template </font>"));
		bar.setDisplayHomeAsUpEnabled(true);
		progressBar = (ProgressBar) findViewById(R.id.progressBar_id);
		if (Utils.isDeviceOnline(this)) {
			callTemplateWebservice();
		} else {
			try {
				SharedPreferences preferences = getSharedPreferences(
						"EmailTemplatePref", Context.MODE_PRIVATE);
				if (preferences != null) {
					String response = preferences.getString(
							"EmailTemplateData", null);
					onReceiveResponse(TEMPLATE_REQ_ID, response);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		templateListView = (ListView) findViewById(R.id.email_template_listview);
		notAvailableTxt = (TextView) findViewById(R.id.nothing_txt);
	}

	private void callTemplateWebservice() {
		String userID = Utils.getUserId(EmailTemplateActivity.this);
		new RequestManager().sendGetRequest(EmailTemplateActivity.this,
				TEMPLATE_REQ_ID, "GetUserMailTemplateListDetail&UserId="
						+ userID);
	}

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		if (progressBar != null && progressBar.isShown()) {
			progressBar.setVisibility(View.GONE);
		}
		if (TEMPLATE_REQ_ID == requestId && response != null) {

			final ArrayList<MailTemplateListDetailModel> dataList = new ArrayList<MailTemplateListDetailModel>();
			try {
				Gson gson = new Gson();
				MailTemplateListModel mailTemplateListModel = gson.fromJson(response, MailTemplateListModel.class);


				ArrayList<UserMailTemplateListDetail> userMailTemplateListDetails = (ArrayList<UserMailTemplateListDetail>) mailTemplateListModel.getUserMailTemplateListDetail();
				Iterator<UserMailTemplateListDetail> iterator = userMailTemplateListDetails.iterator();
				while (iterator.hasNext()) {
					UserMailTemplateListDetail temp = iterator.next();
					dataList.add(new MailTemplateListDetailModel(
							String.valueOf(temp.getTemplateId()), String.valueOf(temp.getMId()),
							temp.getTemplateTitle(), temp.getTemplate(),
							String.valueOf(temp.getUserId()), temp.getTemplateSubject()));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (dataList != null && dataList.size() > 0) {
				notAvailableTxt.setVisibility(View.GONE);
				templateListView.setVisibility(View.VISIBLE);
				EmailTemplateListAdapter adapter = new EmailTemplateListAdapter(EmailTemplateActivity.this,
						dataList);
				templateListView.setAdapter(adapter);
				templateListView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								intent.putExtra("TemplateTitle",
										dataList.get(position)
												.getTemplateTitle());
								intent.putExtra("TemplateId",
										dataList.get(position).getTemplateId());
								setResult(Activity.RESULT_OK, intent);
								finish();
							}
						});
				SharedPreferences preferences = getSharedPreferences(
						"EmailTemplatePref", Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("EmailTemplateData", response);
				editor.commit();
			} else {
				notAvailableTxt.setVisibility(View.VISIBLE);
				templateListView.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		if (progressBar != null && progressBar.isShown()) {
			progressBar.setVisibility(View.GONE);
		}

		Toast.makeText(EmailTemplateActivity.this, "Please try again later",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
