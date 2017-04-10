/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package csms19.inapp.msg.customgallery;

import sms19.inapp.msg.constant.Mediaselected;
import com.kitever.android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class BucketHomeFragmentActivity extends FragmentActivity implements
		Mediaselected {

	private FragmentTabHost mTabHost;
	private TextView chooserheadertext;

	private RelativeLayout chooserhbackbtnlay;

	private static Uri fileUri;

	private final Handler handler = new Handler();

	public static Mediaselected mediasellistnr;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_customgallery);
		String name = getIntent().getStringExtra("username");
		mediasellistnr = this;
		chooserheadertext = (TextView) findViewById(R.id.chooserheadertext);
		chooserhbackbtnlay = (RelativeLayout) findViewById(R.id.chooserhbackbtnlay);
		if (name != null)
			chooserheadertext.setText(name);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

		chooserhbackbtnlay.setOnClickListener(clickListener);

		mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabcontent);

		if (MediaChooserConstants.showVideo) {
			mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(
					"Videos" + "      "), BucketVideoFragment.class, null);
		}
		// ///////Images set right tab so write below
		if (MediaChooserConstants.showImage) {
			mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(
					"Images" + "      "), BucketImageFragment.class, null);
		}

		// mTabHost.getTabWidget().setBackgroundColor(
		// getResources().getColor(R.color.tabs_color));

		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

			View childView = mTabHost.getTabWidget().getChildAt(i);
			TextView textView = (TextView) childView
					.findViewById(android.R.id.title);

			if (textView.getLayoutParams() instanceof RelativeLayout.LayoutParams) {

				RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) textView
						.getLayoutParams();
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
				params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
				textView.setLayoutParams(params);

			} else if (textView.getLayoutParams() instanceof LinearLayout.LayoutParams) {
				LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) textView
						.getLayoutParams();
				params.gravity = Gravity.CENTER;
				textView.setLayoutParams(params);
			}
			textView.setTextColor(getResources().getColor(R.color.black));
			textView.setTextSize(convertDipToPixels(10));
		}

		((TextView) (mTabHost.getTabWidget().getChildAt(1)
				.findViewById(android.R.id.title))).setTextColor(Color.BLACK);
		((TextView) (mTabHost.getTabWidget().getChildAt(0)
				.findViewById(android.R.id.title))).setTextColor(Color.BLACK);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				BucketImageFragment imageFragment = (BucketImageFragment) fragmentManager
						.findFragmentByTag("tab1");
				BucketVideoFragment videoFragment = (BucketVideoFragment) fragmentManager
						.findFragmentByTag("tab2");
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				if (tabId.equalsIgnoreCase("tab1")) {

					if (imageFragment == null) {
						BucketImageFragment newImageFragment = new BucketImageFragment();
						fragmentTransaction.add(R.id.realTabcontent,
								newImageFragment, "tab1");

					} else {

						if (videoFragment != null) {
							fragmentTransaction.hide(videoFragment);
						}

						fragmentTransaction.show(imageFragment);

					}
					((TextView) (mTabHost.getTabWidget().getChildAt(0)
							.findViewById(android.R.id.title)))
							.setTextColor(Color.BLACK);
					((TextView) (mTabHost.getTabWidget().getChildAt(1)
							.findViewById(android.R.id.title)))
							.setTextColor(Color.BLACK);

				} else {

					if (videoFragment == null) {

						final BucketVideoFragment newVideoFragment = new BucketVideoFragment();
						fragmentTransaction.add(R.id.realTabcontent,
								newVideoFragment, "tab2");

					} else {

						if (imageFragment != null) {
							fragmentTransaction.hide(imageFragment);
						}

						fragmentTransaction.show(videoFragment);
					}

					((TextView) (mTabHost.getTabWidget().getChildAt(0)
							.findViewById(android.R.id.title)))
							.setTextColor(Color.BLACK);
					((TextView) (mTabHost.getTabWidget().getChildAt(1)
							.findViewById(android.R.id.title)))
							.setTextColor(Color.BLACK);

				}

				fragmentTransaction.commit();
			}
		});

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (view == chooserhbackbtnlay) {
				finish();
			}
		}
	};

	public int convertDipToPixels(float dips) {
		return (int) (dips
				* BucketHomeFragmentActivity.this.getResources()
						.getDisplayMetrics().density + 0.5f);
	}

	@Override
	public void imageSelected(String path) {
		// TODO Auto-generated method stub
		Intent imageIntent = new Intent();

		imageIntent.putExtra("data", path);
		imageIntent.putExtra("type", "IMG");
		setResult(Activity.RESULT_OK, imageIntent);
		finish();
//		finishActivity(100);
	}

	@Override
	public void vidoeSelected(String path) {
		// TODO Auto-generated method stub
		Intent videoIntent = new Intent();
		// videoIntent
		//
		// .setAction(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
		// videoIntent.putStringArrayListExtra("list",
		// videoFragment.getSelectedVideoList());

		videoIntent.putExtra("data", path);
		videoIntent.putExtra("type", "VID");

		setResult(Activity.RESULT_OK, videoIntent);
		finish();
	}
}
