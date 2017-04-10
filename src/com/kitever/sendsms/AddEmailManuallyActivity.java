package com.kitever.sendsms;

import java.util.ArrayList;

import sms19.inapp.msg.model.Countrymodel;
import sms19.listview.adapter.AddContactManuallyAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.models.ContactInfo;
import com.kitever.sendsms.fragments.AddContactManuallyInterface;

import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class AddEmailManuallyActivity extends ActionBarActivity implements
		AddContactManuallyInterface {

	// private final String TAG = "AddContactManuallyActivity";

	private ListView mListView;

	private EditText mEditText;
	private TextView countryNameTextView;
	private Spinner countryCodeSpinner;
	private ArrayList<Countrymodel> countryArrayList = null;
	private String countryCodeString = "";

	private ArrayList<String> manualArrayList = new ArrayList<>();

	private ArrayList<ContactInfo> contactInfoArrayList = new ArrayList<>();

	// private ArrayList<String> withCommaList = new ArrayList<>();
	// private ArrayAdapter<String> itemsAdapter;
	private AddContactManuallyAdapter itemsAdapter;
	private Button addButton, doneButton;

	private boolean isWrongInput;

	private int defaultIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_email_manually);

		actionBarSettingWithBack(this,getSupportActionBar(),"Add recipients manually");

		mListView = (ListView) findViewById(R.id.mail_listView);

		countryCodeSpinner = (Spinner) findViewById(R.id.mail_countryCodeSpinner);
		countryNameTextView = (TextView) findViewById(R.id.mail_countryNameTextView);
		mEditText = (EditText) findViewById(R.id.mail_editText1);

		final String iso_code = com.kitever.utils.Utils
				.getIsoCode(AddEmailManuallyActivity.this);

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// countryArrayList=Utils.getCountryList(AddEmailManuallyActivity.this);
		//
		// if(countryArrayList!=null){
		// if(iso_code!=null){
		// if(!iso_code.equals("")){
		// final int size=countryArrayList.size();
		// for(int i=0;i<size;i++){
		// if(countryArrayList.get(i).getCountryISOCode().equals(iso_code)){
		// defaultIndex=i;
		// break;
		// }
		// }
		// }
		// }
		// }
		// runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// CountryListAdapter adapter =new
		// sms19.inapp.msg.adapter.CountryListAdapter(AddEmailManuallyActivity.this,countryArrayList);
		// countryCodeSpinner.setAdapter(adapter);
		// countryCodeSpinner.setSelection(defaultIndex);
		// }
		// });
		//
		// }
		// }).start();

		addButton = (Button) findViewById(R.id.mail_addBtn);
		doneButton = (Button) findViewById(R.id.mail_doneBtn);

		setRobotoThinFontButton(addButton,this);
		setRobotoThinFontButton(doneButton,this);

		addButton.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		doneButton.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		addButton.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
		doneButton.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mEditText.getText().toString().trim().equals("")) {
					isWrongInput = true;
				}

				if (isWrongInput) {
					Toast.makeText(getApplicationContext(),
							"Please input a valid number", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				final String str = countryNameTextView.getText().toString()
						.trim()
						+ mEditText.getText().toString().trim();
				// final String
				// strComma=countryNameTextView.getText().toString().trim()+","+mEditText.getText().toString().trim();

				if (manualArrayList.contains(str)) {
					mEditText.setText("");
					Toast.makeText(getApplicationContext(),
							"Mobile Number already inserted to LIST",
							Toast.LENGTH_SHORT).show();
				} else {

					ContactInfo contactInfo = new ContactInfo();

					contactInfo.contact_email = mEditText.getText().toString()
							.trim();
					// contactInfo.countryCode=countryNameTextView.getText().toString().trim();
					if (isValidEmail(contactInfo.contact_email)) {
						contactInfo.isSelected = true;

						contactInfoArrayList.add(contactInfo);

						manualArrayList.add(str);

						mEditText.setText("");
						itemsAdapter.addList(manualArrayList);
						// itemsAdapter.notifyDataSetChanged();
					} else
						Toast.makeText(AddEmailManuallyActivity.this,
								"Email Id not valid", Toast.LENGTH_SHORT)
								.show();
				}
			}
		});

		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				/*
				 * Intent intent = new Intent();
				 * intent.putStringArrayListExtra("MANUAL_NUM_LIST",
				 * withCommaList); setResult(RESULT_OK, intent);
				 */

				Intent intent = new Intent();
				intent.putExtra("MANUAL_NUM_LIST", contactInfoArrayList);
				setResult(RESULT_OK, intent);

				finish();
			}
		});

		// final ArrayList<String> contactInfos=
		// getIntent().getStringArrayListExtra("LIST");

		@SuppressWarnings("unchecked")
		final ArrayList<ContactInfo> contactInfos = (ArrayList<ContactInfo>) getIntent()
				.getSerializableExtra("LIST");

		if (contactInfos != null) {
			// withCommaList.addAll(contactInfos);
			/*
			 * for (String string : withCommaList) {
			 * manualArrayList.add(string.replaceAll(",","")); }
			 */
			contactInfoArrayList.addAll(contactInfos);

			for (ContactInfo contactInfo : contactInfoArrayList) {
				manualArrayList.add(contactInfo.getContact_email());
			}

		}

		itemsAdapter = new AddContactManuallyAdapter(
				AddEmailManuallyActivity.this,
				R.layout.list_view_add_contact_manually, manualArrayList);

		mListView.setAdapter(itemsAdapter);

		// mListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// // withCommaList.remove(position);
		// contactInfoArrayList.remove(position);
		// manualArrayList.remove(position);
		// itemsAdapter.notifyDataSetChanged();
		// }
		// });

		mEditText.addTextChangedListener(new TextWatcher() {
			String phoneNumber;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				phoneNumber = mEditText.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCodeString.length() > 0) {
					if (isValidEmail(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber,
								countryCodeString);

						if (!status) {
							isWrongInput = true;
							mEditText.setError("Invalid phone");
						} else {
							isWrongInput = false;
							mEditText.setError(null);
						}
					}
				}
			}
		});
	}

	private boolean isValidPhoneNumber(CharSequence phoneNumber) {

		if (!TextUtils.isEmpty(phoneNumber)) {
			return Patterns.PHONE.matcher(phoneNumber).matches();
		}

		return false;
	}

	private boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	private boolean parseContact(String contact, String countrycode) {
		if (countrycode != null && countrycode.length() > 0) {
			try {
				countrycode = countrycode.substring(1, countrycode.length());
			} catch (Exception e) {
				// TODO: handle exception
				countrycode = countrycode
						.substring(1, countrycode.length() - 1);
			}

		}

		PhoneNumber phoneNumber = null;
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer
				.parseInt(countrycode));
		boolean isValid = false;
		PhoneNumberType isMobile = null;
		try {
			phoneNumber = phoneNumberUtil.parse(contact, isoCode);
			isValid = phoneNumberUtil.isValidNumber(phoneNumber);
			isMobile = phoneNumberUtil.getNumberType(phoneNumber);

		} catch (NumberParseException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return isValid
				&& (PhoneNumberType.MOBILE == isMobile || PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

	@Override
	public void changelist(int position) {
		// TODO Auto-generated method stub
		manualArrayList.remove(position);
		contactInfoArrayList.remove(position);
	}

}
