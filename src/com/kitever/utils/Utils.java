package com.kitever.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.kitever.android.R;
import com.kitever.app.context.BaseApplicationContext;
import com.kitever.app.context.CustomStyle;

import java.io.IOException;

import sms19.inapp.msg.CircularImageView;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class Utils {

	public static String CONTACTS_UPDATE_ACTION = "com.kitever.update_contacts";
	public static String CONTACTS_MESSAGE = "contact_message";
	public static String CONTACTS_MESSAGE_TYPE = "contact_message_type";
	public static final int CONTACTS_SYNC_UPDATE = 10;
	public static final int CONTACTS_ADD_UPDATE = 20;

	public static final int CONTACTS_EDIT_UPDATE = 30;
	public static final int CONTACTS_DELETE_UPDATE = 40;

	public static final int FLOATING_BUTTON_MARGIN = 65;

	public static ColorDrawable setActionBarBackground() {
		return new ColorDrawable(Color.parseColor("#006966"));
	}

	public static Spanned setActionBarTextAndColor(final String str) {
		return Html.fromHtml("<font color='#ffffff'>" + str + "</font>");
	}

	/**
	 * Get ISO 3166-1 alpha-2 country code for this device (or null if not
	 * available)
	 * 
	 * @param context
	 *            Context reference to get the TelephonyManager instance from
	 * @return country code or null
	 */
	public static String getIsoCode(Context context) {
		try {
			final TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			final String simCountry = tm.getSimCountryIso();
			if (simCountry != null && simCountry.length() == 2) { // SIM country
																	// code is
																	// available
				return simCountry.toUpperCase();
			} else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device
																				// is
																				// not
																				// 3G
																				// (would
																				// be
																				// unreliable)
				String networkCountry = tm.getNetworkCountryIso();
				if (networkCountry != null && networkCountry.length() == 2) { // network
																				// country
																				// code
																				// is
																				// available
					return networkCountry.toUpperCase();
				}
			}
		} catch (Exception e) {

		}
		return null;
	}

	public static boolean isValidString(String str) {
		str = str.trim();
		return str != null && !str.isEmpty();

	}
	public static void actionBarSetting(Activity context, ActionBar actionbar, String str) {

		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(false);
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(CustomStyle.HEADER_BACKGROUND)));
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);

		View view = context.getLayoutInflater().inflate(R.layout.custon_actionbar_view,
				null, false);
		// mBackButton = (LinearLayout)
		// view.findViewById(R.id.action_bat_back_btn);
		CircularImageView mActionBarImage = (sms19.inapp.msg.CircularImageView) view
				.findViewById(R.id.profile_image);
		mActionBarImage.setVisibility(View.GONE);
		TextView sorting_title = (TextView) view.findViewById(R.id.sorting_title);
		TextView mUserNameTitle = (TextView) view.findViewById(R.id.name);

		TextView mUserStatusTitle = (TextView) view.findViewById(R.id.status);
		mUserStatusTitle.setVisibility(View.GONE);
		TextView actionbar_title = (TextView) view.findViewById(R.id.actionbar_title);

		actionbar_title.setVisibility(View.VISIBLE);
		// mBackButton.setOnClickListener(this);
		LinearLayout layout_name_status = (LinearLayout) view
				.findViewById(R.id.layout_name_status);
		layout_name_status.setVisibility(View.GONE);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.RIGHT);
		params.setMargins(0, 0, 0, 0);

		actionbar.setCustomView(view, params);
		setRobotoThinFont(actionbar_title,context);
		Toolbar parent = (Toolbar) view.getParent();
		parent.setContentInsetsAbsolute(0, 0);
		actionbar_title.setText("  "+str);
		actionbar_title.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		//actionbar_title.setText(com.kitever.utils.Utils.setActionBarTextAndColor(str));

	}

	public static void actionBarSettingWithBack(Activity context, ActionBar actionbar, String str) {

		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(CustomStyle.HEADER_BACKGROUND)));
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);

		View view = context.getLayoutInflater().inflate(R.layout.custon_actionbar_view,
				null, false);
		// mBackButton = (LinearLayout)
		// view.findViewById(R.id.action_bat_back_btn);
		CircularImageView mActionBarImage = (sms19.inapp.msg.CircularImageView) view
				.findViewById(R.id.profile_image);
		mActionBarImage.setVisibility(View.GONE);
		TextView sorting_title = (TextView) view.findViewById(R.id.sorting_title);
		TextView mUserNameTitle = (TextView) view.findViewById(R.id.name);

		TextView mUserStatusTitle = (TextView) view.findViewById(R.id.status);
		mUserStatusTitle.setVisibility(View.GONE);
		TextView actionbar_title = (TextView) view.findViewById(R.id.actionbar_title);

		actionbar_title.setVisibility(View.VISIBLE);
		// mBackButton.setOnClickListener(this);
		LinearLayout layout_name_status = (LinearLayout) view
				.findViewById(R.id.layout_name_status);
		layout_name_status.setVisibility(View.GONE);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.RIGHT);
		params.setMargins(0, 0, 0, 0);

		actionbar.setCustomView(view, params);

		Toolbar parent = (Toolbar) view.getParent();
		parent.setContentInsetsAbsolute(0, 0);
		actionbar_title.setText(str);
		setRobotoThinFont(actionbar_title,context);
		actionbar_title.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

		BaseApplicationContext application = (BaseApplicationContext) context.getApplication();
		Tracker mTracker = application.getDefaultTracker();
		mTracker.setScreenName(context.getString(R.string.app_name)+" - "+ str+ " Page");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

			Class<T> rawType = (Class<T>) type.getRawType();
			if (rawType != String.class) {
				return null;
			}
			return (TypeAdapter<T>) new StringAdapter();
		}
	}

	public static class StringAdapter extends TypeAdapter<String> {
		public String read(JsonReader reader) throws IOException {
			if (reader.peek() == JsonToken.NULL) {
				reader.nextNull();
				return "";
			}
			return reader.nextString();
		}
		public void write(JsonWriter writer, String value) throws IOException {
			if (value == null) {
				writer.nullValue();
				return;
			}
			writer.value(value);
		}
	}

	
}
