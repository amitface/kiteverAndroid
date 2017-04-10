package com.kitever.pos.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

public class PosModeOfPaymentDetailScreen extends Dialog implements
		android.view.View.OnClickListener {

	private EditText chequeNo, bankNameOrRefNum, remarkOrDes;
	private TextView date;
	private String modeType, chequeNoVal, dateVal, remarkOrDesVal,
			bankNameOrRefNumVal;
	private Context context;
	private FrameLayout inputLayout, calendarLayout;
	private CalendarView calenderView;
	String ref_number="";

	public PosModeOfPaymentDetailScreen(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.payment_detail_dialog_layout);
		setScreen();
	}

	private void setScreen() {
		
		chequeNo = (EditText) findViewById(R.id.cheque_no_edit);
		bankNameOrRefNum = (EditText) findViewById(R.id.bank_name_edit);
		remarkOrDes = (EditText) findViewById(R.id.remark_edit);
		inputLayout = (FrameLayout) findViewById(R.id.input_layout);
		calendarLayout = (FrameLayout) findViewById(R.id.calendar_layout);
		date = (TextView) findViewById(R.id.date_id);
		calenderView = (CalendarView) findViewById(R.id.calendarView);
		calenderView
				.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
					@Override
					public void onSelectedDayChange(CalendarView calendarView,
							int i, int i1, int i2) {
//						date.setText(i2 + " / " + i1 + " / " + i);
						date.setText((i1+1) + "/" + i2 + "/" + i);
						// Toast.makeText(
						// context,
						// "Selected Date:\n" + "Day = " + i2 + "\n"
						// + "Month = " + i1 + "\n" + "Year = "
						// + i, Toast.LENGTH_LONG).show();
						inputLayout.setVisibility(View.VISIBLE);
						calendarLayout.setVisibility(View.GONE);
					}

				});
		if (modeType != null
				&& (modeType.equalsIgnoreCase("RTGS") || (modeType
						.equalsIgnoreCase("Credit Card")))) {
			chequeNo.setVisibility(View.GONE);
			bankNameOrRefNum.setHint("Ref Number");
			remarkOrDes.setHint("Description");
			ref_number="Referenece Number";

		} else {
			chequeNo.setVisibility(View.VISIBLE);
			bankNameOrRefNum.setHint("Bank name");
			remarkOrDes.setHint("Remark");
			ref_number="Bank Name";
		}
		Button doneBtn = (Button) findViewById(R.id.done_btn);
		Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
		doneBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		date.setOnClickListener(this);
	}

	public void setPaymentType(String modeType) {
		this.modeType = modeType;
	}

	public String getChequeNo() {
		return chequeNoVal;
	}

	public String getDate() {
		return dateVal;
	}

	public String getBankNameOrRefNum() {
		return bankNameOrRefNumVal;
	}

	public String getRemarkOrDes() {
		return remarkOrDesVal;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.done_btn:
			chequeNoVal = chequeNo.getText().toString().trim();
			dateVal = date.getText().toString().trim();
			bankNameOrRefNumVal = bankNameOrRefNum.getText().toString().trim();
			remarkOrDesVal = remarkOrDes.getText().toString().trim();
			
			if(modeType.equalsIgnoreCase("Cheque"))
			{
				if (chequeNoVal == null || chequeNoVal.equalsIgnoreCase("")) {
					Toast.makeText(getContext(), "Please enter cheque number",
							Toast.LENGTH_LONG).show();
				}
			}
			
			if (dateVal == null || dateVal.equalsIgnoreCase("")) {
				Toast.makeText(getContext(), "Please enter date",
						Toast.LENGTH_LONG).show();
			} else if (bankNameOrRefNumVal == null
					|| bankNameOrRefNumVal.equalsIgnoreCase("")) {
				Toast.makeText(getContext(), ref_number,
						Toast.LENGTH_LONG).show();
			} else {
				dismiss();
			}

			break;
		case R.id.cancel_btn:
			dismiss();
			break;
		case R.id.date_id:
			inputLayout.setVisibility(View.GONE);
			calendarLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.calendarView:

			break;
		default:
			break;
		}
	}

}
