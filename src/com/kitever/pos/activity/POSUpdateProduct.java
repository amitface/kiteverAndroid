package com.kitever.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.kitever.android.R;
import com.kitever.customviews.FloatingActionButton;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSProductAdapter;

public class POSUpdateProduct extends BasePosActivity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Update Product");
		setBottomAction(false, false, false, false, false);
		setLayoutContentView(R.layout.pos_updatecategory_layout);		
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
