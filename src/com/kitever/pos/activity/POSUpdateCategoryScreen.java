package com.kitever.pos.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.kitever.android.R;

public class POSUpdateCategoryScreen extends BasePosActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Update Category");
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
