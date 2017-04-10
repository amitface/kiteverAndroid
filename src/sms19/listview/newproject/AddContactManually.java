package sms19.listview.newproject;

import java.util.ArrayList;

import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Countrymodel;
import sms19.listview.database.DataBaseDetails;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.kitever.android.R;

public class AddContactManually extends ActionBarActivity {
	EditText num1, num2, num3, num4, num5, num6, num7, num8, num9, num10;
	Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6,
			spinner7, spinner8, spinner9, spinner10;
	DataBaseDetails db = new DataBaseDetails(this);
	Button add;
	String Mobile = "", UserId = "", Password = "";
	private ArrayList<Countrymodel> countrymodels = null;
	private String countryCode1 = "", countryCode2 = "", countryCode3 = "",
			countryCode4 = "", countryCode5 = "", countryCode6 = "",
			countryCode7 = "", countryCode8 = "", countryCode9 = "",
			countryCode10 = "";
	private TextView name_country1, name_country2, name_country3,
			name_country4, name_country5, name_country6, name_country7,
			name_country8, name_country9, name_country10;

	// protected means we can access this in class,package and subclass.. not
	// access in world(whole code)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// super keyword is used for call parent constructor.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addrecipientmanually);

		num1 = (EditText) findViewById(R.id.editText1);
		num2 = (EditText) findViewById(R.id.editText2);
		num3 = (EditText) findViewById(R.id.editText3);
		num4 = (EditText) findViewById(R.id.editText4);
		num5 = (EditText) findViewById(R.id.editText5);
		num6 = (EditText) findViewById(R.id.editText6);
		num7 = (EditText) findViewById(R.id.editText7);
		num8 = (EditText) findViewById(R.id.editText8);
		num9 = (EditText) findViewById(R.id.editText9);
		num10 = (EditText) findViewById(R.id.editText10);

		name_country1 = (TextView) findViewById(R.id.name_country1);
		name_country2 = (TextView) findViewById(R.id.name_country2);
		name_country3 = (TextView) findViewById(R.id.name_country3);
		name_country4 = (TextView) findViewById(R.id.name_country4);
		name_country5 = (TextView) findViewById(R.id.name_country5);
		name_country6 = (TextView) findViewById(R.id.name_country6);
		name_country7 = (TextView) findViewById(R.id.name_country7);
		name_country8 = (TextView) findViewById(R.id.name_country8);
		name_country9 = (TextView) findViewById(R.id.name_country9);
		name_country10 = (TextView) findViewById(R.id.name_country10);

		spinner1 = (Spinner) findViewById(R.id.country_code1);
		spinner2 = (Spinner) findViewById(R.id.country_code2);
		spinner3 = (Spinner) findViewById(R.id.country_code3);
		spinner4 = (Spinner) findViewById(R.id.country_code4);
		spinner5 = (Spinner) findViewById(R.id.country_code5);
		spinner6 = (Spinner) findViewById(R.id.country_code6);
		spinner7 = (Spinner) findViewById(R.id.country_code7);
		spinner8 = (Spinner) findViewById(R.id.country_code8);
		spinner9 = (Spinner) findViewById(R.id.country_code9);
		spinner10 = (Spinner) findViewById(R.id.country_code10);
		add = (Button) findViewById(R.id.done);

		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
		bar.setTitle(Html
				.fromHtml("<font color='#ffffff'>Add Recipient</font>"));
		// bar.setHomeAsUpIndicator(R.drawable.arrow_new);
		countrymodels = new ArrayList<Countrymodel>();

		countrymodels = Utils.getCountryList(AddContactManually.this);
		if (countrymodels != null) {
			int pos = 0;
			for (int i = 0; i < countrymodels.size(); i++) {
				if (countrymodels.get(i).getCountry_name().toLowerCase()
						.contains("india")) {
					pos = i;
				}
			}

			sms19.inapp.msg.adapter.CountryListAdapter adapter1 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner1.setAdapter(adapter1);
			spinner1.setSelection(pos);
			spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					countryCode1 = countrymodels.get(arg2).getCountrycode()
							.trim();
					if (!countryCode1.contains("+")) {
						countryCode1 = "+" + countryCode1;

						// Toast.makeText(getApplicationContext(), countryCode,
						// Toast.LENGTH_SHORT).show();
					}
					name_country1.setText(countryCode1);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			sms19.inapp.msg.adapter.CountryListAdapter adapter2 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner2.setAdapter(adapter2);
			spinner2.setSelection(pos);
			spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					countryCode2 = countrymodels.get(arg2).getCountrycode()
							.trim();
					if (!countryCode2.contains("+")) {
						countryCode2 = "+" + countryCode2;

						// Toast.makeText(getApplicationContext(), countryCode,
						// Toast.LENGTH_SHORT).show();
					}
					name_country2.setText(countryCode2);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			sms19.inapp.msg.adapter.CountryListAdapter adapter3 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner3.setAdapter(adapter3);
			spinner3.setSelection(pos);
			spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					countryCode3 = countrymodels.get(arg2).getCountrycode()
							.trim();
					if (!countryCode3.contains("+")) {
						countryCode3 = "+" + countryCode3;

						// Toast.makeText(getApplicationContext(), countryCode,
						// Toast.LENGTH_SHORT).show();
					}
					name_country3.setText(countryCode3);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			sms19.inapp.msg.adapter.CountryListAdapter adapter4 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner4.setAdapter(adapter4);
			spinner4.setSelection(pos);
			spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					countryCode4 = countrymodels.get(arg2).getCountrycode()
							.trim();
					if (!countryCode4.contains("+")) {
						countryCode4 = "+" + countryCode4;

						// Toast.makeText(getApplicationContext(), countryCode,
						// Toast.LENGTH_SHORT).show();
					}
					name_country4.setText(countryCode4);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			sms19.inapp.msg.adapter.CountryListAdapter adapter5 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner5.setAdapter(adapter5);
			spinner5.setSelection(pos);
			spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					countryCode5 = countrymodels.get(arg2).getCountrycode()
							.trim();
					if (!countryCode5.contains("+")) {
						countryCode5 = "+" + countryCode5;

						// Toast.makeText(getApplicationContext(), countryCode,
						// Toast.LENGTH_SHORT).show();
					}
					name_country5.setText(countryCode5);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			sms19.inapp.msg.adapter.CountryListAdapter adapter6 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner6.setAdapter(adapter6);
			spinner6.setSelection(pos);
			spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					countryCode6 = countrymodels.get(arg2).getCountrycode()
							.trim();
					if (!countryCode6.contains("+")) {
						countryCode6 = "+" + countryCode6;

						// Toast.makeText(getApplicationContext(), countryCode,
						// Toast.LENGTH_SHORT).show();
					}
					name_country6.setText(countryCode6);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			sms19.inapp.msg.adapter.CountryListAdapter adapter7 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner7.setAdapter(adapter7);
			spinner7.setSelection(pos);
			spinner7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					countryCode7 = countrymodels.get(arg2).getCountrycode()
							.trim();
					if (!countryCode7.contains("+")) {
						countryCode7 = "+" + countryCode7;

						// Toast.makeText(getApplicationContext(), countryCode,
						// Toast.LENGTH_SHORT).show();
					}
					name_country7.setText(countryCode7);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			sms19.inapp.msg.adapter.CountryListAdapter adapter8 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner8.setAdapter(adapter8);
			spinner8.setSelection(pos);
			spinner8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					countryCode8 = countrymodels.get(arg2).getCountrycode()
							.trim();
					if (!countryCode8.contains("+")) {
						countryCode8 = "+" + countryCode8;

						// Toast.makeText(getApplicationContext(), countryCode,
						// Toast.LENGTH_SHORT).show();
					}
					name_country8.setText(countryCode8);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			sms19.inapp.msg.adapter.CountryListAdapter adapter9 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner9.setAdapter(adapter9);
			spinner9.setSelection(pos);
			spinner9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					countryCode9 = countrymodels.get(arg2).getCountrycode()
							.trim();
					if (!countryCode9.contains("+")) {
						countryCode9 = "+" + countryCode9;

						// Toast.makeText(getApplicationContext(), countryCode,
						// Toast.LENGTH_SHORT).show();
					}
					name_country9.setText(countryCode9);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			sms19.inapp.msg.adapter.CountryListAdapter adapter10 = new sms19.inapp.msg.adapter.CountryListAdapter(
					AddContactManually.this, countrymodels);
			spinner10.setAdapter(adapter10);
			spinner10.setSelection(pos);
			spinner10
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							countryCode10 = countrymodels.get(arg2)
									.getCountrycode().trim();
							if (!countryCode10.contains("+")) {
								countryCode10 = "+" + countryCode10;

								// Toast.makeText(getApplicationContext(),
								// countryCode, Toast.LENGTH_SHORT).show();
							}
							name_country10.setText(countryCode10);

						}

						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
		}
		num1.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num1.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode1.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber, countryCode1);

						if (!status) {
							num1.setError("Invalid phone");
						} else {
							num1.setError(null);
						}

					}

				}

			}
		});
		num2.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num2.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode2.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber, countryCode2);

						if (!status) {
							num2.setError("Invalid phone");
						} else {
							num2.setError(null);
						}

					}

				}

			}
		});
		num3.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num3.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode3.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber, countryCode3);

						if (!status) {
							num3.setError("Invalid phone");
						} else {
							num3.setError(null);
						}

					}

				}

			}
		});
		num4.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num4.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode4.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber, countryCode4);

						if (!status) {
							num4.setError("Invalid phone");
						} else {
							num4.setError(null);
						}

					}

				}

			}
		});
		num5.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num5.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode6.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber, countryCode6);

						if (!status) {
							num5.setError("Invalid phone");
						} else {
							num5.setError(null);
						}

					}

				}

			}
		});
		num6.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num6.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode6.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber, countryCode6);

						if (!status) {
							num6.setError("Invalid phone");
						} else {
							num6.setError(null);
						}

					}

				}

			}
		});
		num7.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num7.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode7.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber, countryCode7);

						if (!status) {
							num7.setError("Invalid phone");
						} else {
							num7.setError(null);
						}

					}

				}

			}
		});
		num8.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num8.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode8.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber, countryCode8);

						if (!status) {
							num8.setError("Invalid phone");
						} else {
							num8.setError(null);
						}

					}

				}

			}
		});
		num9.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num9.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode9.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber, countryCode9);

						if (!status) {
							num9.setError("Invalid phone");
						} else {
							num9.setError(null);
						}

					}

				}

			}
		});
		num10.addTextChangedListener(new TextWatcher() {
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
				phoneNumber = num10.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCode10.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber,
								countryCode10);

						if (!status) {
							num10.setError("Invalid phone");
						} else {
							num10.setError(null);
						}

					}

				}

			}
		});
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> numCodelist = new ArrayList<String>();
				boolean flag = false;
				String e1 = num1.getText().toString();
				String e2 = num2.getText().toString();
				String e3 = num3.getText().toString();
				String e4 = num4.getText().toString();
				String e5 = num5.getText().toString();
				String e6 = num6.getText().toString();
				String e7 = num7.getText().toString();
				String e8 = num8.getText().toString();
				String e9 = num9.getText().toString();
				String e10 = num10.getText().toString();

				if (e1 != null && e1.length() > 0) {

					flag = true;
					numCodelist.add(countryCode1 + "," + e1);
				}

				if (e2 != null && e2.length() > 0) {
					flag = true;
					numCodelist.add(countryCode2 + "," + e2);
				}

				if (e3 != null && e3.length() > 0) {
					flag = true;
					numCodelist.add(countryCode3 + "," + e3);
				}

				if (e4 != null && e4.length() > 0) {
					flag = true;
					numCodelist.add(countryCode4 + "," + e4);
				}

				if (e5 != null && e5.length() > 0) {
					flag = true;
					numCodelist.add(countryCode5 + "," + e5);
				}

				if (e6 != null && e6.length() > 0) {
					flag = true;
					numCodelist.add(countryCode6 + "," + e6);
				}

				if (e7 != null && e7.length() > 0) {
					flag = true;
					numCodelist.add(countryCode7 + "," + e7);
				}

				if (e8 != null && e8.length() > 0) {
					// Toast.makeText(getApplicationContext(),
					// "Please enter ten digit number" + "," + e8,
					// Toast.LENGTH_SHORT).show();
					flag = true;
					numCodelist.add(countryCode8 + "," + e8);
				}

				if (e9 != null && e9.length() > 0) {
					flag = true;
					numCodelist.add(countryCode9 + "," + e9);
				}

				if (e10 != null && e10.length() > 0) {
					flag = true;
					numCodelist.add(countryCode10 + "," + e10);
				}

				// else {
				// String num[] = { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 };
				// fetchMobileandUserId();
				//
				// db.Open();
				//
				// for (int i = 0; i < num.length; i++) {
				// Log.w("all all ", "numbers" + num[i] + ":::i=" + i);
				// String a = num[i];
				// Log.w("BB BB", "numbers" + num[i] + ":::i=" + i);
				// if (a.length() == 10 && a.length() > 0) {
				// db.addReceipent(UserId, a, "");
				// Log.w("Num NUM:",
				// "::::::::::::::::::::::::::::::::numbers"
				// + a + ":::i=" + i);
				// } else {
				//
				// }
				//
				// }
				// db.close();
				/*
				 * try { SendSmsScreen.sendSmsFlag.finish(); } catch (Exception
				 * e) { e.printStackTrace(); }
				 */
				if (flag) {
				
					Intent intent = new Intent();
					intent.putStringArrayListExtra("CodeWithNumber",numCodelist);
					setResult(RESULT_OK, intent);
					
					finish();
				} else {
					new AlertDialog.Builder(AddContactManually.this)
							.setCancelable(false)
							.setMessage("Please enter number to add recipient")
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();

										}
									}).show();
				}

				// }

			}
		});

	}

	private boolean isValidPhoneNumber(CharSequence phoneNumber) {

		if (!TextUtils.isEmpty(phoneNumber)) {
			return Patterns.PHONE.matcher(phoneNumber).matches();
		}

		return false;
	}

	private boolean parseContact(String contact, String countrycode) {
		if (countrycode != null && countrycode.length() > 0) {
		try {
			countrycode = countrycode.substring(1, countrycode.length());
		} catch (Exception e) {
			// TODO: handle exception
			countrycode = countrycode.substring(1, countrycode.length()-1);
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

		return isValid && (PhoneNumberType.MOBILE == isMobile ||
				PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		return false;
	}

	public void fetchMobileandUserId() {
		db.Open();

		Cursor c;

		c = db.getLoginDetails();
		int count = c.getCount();

		if (count >= 1) {
			while (c.moveToNext()) {

				Mobile = c.getString(1);
				UserId = c.getString(3);
				Password = c.getString(5);

			}
		}
		db.close();
	}

}
