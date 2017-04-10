package csms19.inapp.msg.customgallery;

import com.kitever.android.R;
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

public class Activity_homecustomgallery extends FragmentActivity implements
		ImageFragment.OnImageSelectedListener,
		VideoFragment.OnVideoSelectedListener {

	private FragmentTabHost mTabHost;
	private TextView chooserheadertext;

	private RelativeLayout chooserhbackbtnlay;

	private static Uri fileUri;

	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_customgallery);
//		String name = getIntent().getStringExtra("username");

		chooserheadertext = (TextView) findViewById(R.id.chooserheadertext);
		chooserhbackbtnlay = (RelativeLayout) findViewById(R.id.chooserhbackbtnlay);

//		chooserheadertext.setText(name);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

		mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabcontent);
		chooserhbackbtnlay.setOnClickListener(clickListener);

		if (getIntent() != null
				&& (getIntent().getBooleanExtra("isFromBucket", false))) {

			if (getIntent().getBooleanExtra("image", false)) {
				// headerBarTitle
				// .setText(getResources().getString(R.string.image));

				Bundle bundle = new Bundle();
				chooserheadertext.setText(getIntent().getStringExtra("name"));
				bundle.putString("name", getIntent().getStringExtra("name"));
				mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(
						"Images" + "     "), ImageFragment.class, bundle);

			} else {
				// headerBarTitle
				// .setText(getResources().getString(R.string.video));

				Bundle bundle = new Bundle();
				chooserheadertext.setText(getIntent().getStringExtra("name"));
				bundle.putString("name", getIntent().getStringExtra("name"));
				mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(
						"Videos" + "      "), VideoFragment.class, bundle);
			}
		}
		// else {
		//
		// if (MediaChooserConstants.showVideo) {
		// mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(
		// "Videos" + "      "), VideoFragment.class, null);
		// }
		//
		// if (MediaChooserConstants.showImage) {
		//
		// mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(
		// "Images" + "      "), ImageFragment.class, null);
		// }
		//
		// // }
		//
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

			TextView textView = (TextView) mTabHost.getTabWidget()
					.getChildAt(i).findViewById(android.R.id.title);
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

		if ((mTabHost.getTabWidget().getChildAt(0) != null)) {
			((TextView) (mTabHost.getTabWidget().getChildAt(0)
					.findViewById(android.R.id.title)))
					.setTextColor(Color.BLACK);
		}

		if ((mTabHost.getTabWidget().getChildAt(1) != null)) {
			((TextView) (mTabHost.getTabWidget().getChildAt(1)
					.findViewById(android.R.id.title)))
					.setTextColor(Color.BLACK);
		}

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				ImageFragment imageFragment = (ImageFragment) fragmentManager
						.findFragmentByTag("tab1");
				VideoFragment videoFragment = (VideoFragment) fragmentManager
						.findFragmentByTag("tab2");
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				if (tabId.equalsIgnoreCase("tab1")) {

					if (imageFragment != null) {

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

					if (videoFragment != null) {

						if (imageFragment != null) {
							fragmentTransaction.hide(imageFragment);
						}

						fragmentTransaction.show(videoFragment);
						if (videoFragment.getAdapter() != null) {
							videoFragment.getAdapter().notifyDataSetChanged();
						}
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

	@Override
	public void onImageSelected(int count) {
		if (mTabHost.getTabWidget().getChildAt(1) != null) {
			if (count != 0) {
				// ((TextView) mTabHost.getTabWidget().getChildAt(1)
				// .findViewById(android.R.id.title)).setText("Images "
				// + count);
				ImageFragment imageFragment = (ImageFragment) getSupportFragmentManager()
						.findFragmentByTag("tab1");
				if (imageFragment != null) {
					finish();
					if (BucketHomeFragmentActivity.mediasellistnr != null) {
						BucketHomeFragmentActivity.mediasellistnr
								.imageSelected(imageFragment
										.getSelectedImageList().get(0));
					}

					// Intent imageIntent = new Intent();
					// // imageIntent
					// //
					// .setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
					// // imageIntent.putStringArrayListExtra("list",
					// // imageFragment.getSelectedImageList());
					// imageIntent.putExtra("data", imageFragment
					// .getSelectedImageList().get(0));
					// imageIntent.putExtra("type", "IMG");
					// setResult(Activity.RESULT_OK, imageIntent);

				}

			} else {
				((TextView) mTabHost.getTabWidget().getChildAt(1)
						.findViewById(android.R.id.title)).setText("Images");
			}
		} else {
			if (count != 0) {
				// ((TextView) mTabHost.getTabWidget().getChildAt(1)
				// .findViewById(android.R.id.title)).setText("Images "
				// + count);

				ImageFragment imageFragment = (ImageFragment) getSupportFragmentManager()
						.findFragmentByTag("tab1");
				if (imageFragment != null) {
					// Intent imageIntent = new Intent();
					// // imageIntent
					// //
					// .setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
					// // imageIntent.putStringArrayListExtra("list",
					// // imageFragment.getSelectedImageList());
					// imageIntent.putExtra("data", imageFragment
					// .getSelectedImageList().get(0));
					// imageIntent.putExtra("type", "IMG");
					// setResult(Activity.RESULT_OK, imageIntent);
					finish();
					if (BucketHomeFragmentActivity.mediasellistnr != null) {
						BucketHomeFragmentActivity.mediasellistnr
								.imageSelected(imageFragment
										.getSelectedImageList().get(0));
					}

				}

			} else {
				((TextView) mTabHost.getTabWidget().getChildAt(1)
						.findViewById(android.R.id.title)).setText("Images");
			}
		}
	}

	@Override
	public void onVideoSelected(int count) {
		if (count != 0) {
			// ((TextView) mTabHost.getTabWidget().getChildAt(1)
			// .findViewById(android.R.id.title)).setText("Videos "
			// + count);

			VideoFragment videoFragment = (VideoFragment) getSupportFragmentManager()
					.findFragmentByTag("tab2");

			// Intent videoIntent = new Intent();
			// // videoIntent
			// //
			// .setAction(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
			// // videoIntent.putStringArrayListExtra("list",
			// // videoFragment.getSelectedVideoList());
			//
			// videoIntent.putExtra("data", videoFragment.getSelectedVideoList()
			// .get(0));
			// videoIntent.putExtra("type", "VID");
			//
			// setResult(Activity.RESULT_OK, videoIntent);

			finish();
			if (BucketHomeFragmentActivity.mediasellistnr != null) {
				BucketHomeFragmentActivity.mediasellistnr
						.vidoeSelected(videoFragment.getSelectedVideoList()
								.get(0));
			}

		} else {
			((TextView) mTabHost.getTabWidget().getChildAt(1)
					.findViewById(android.R.id.title)).setText("Videos");
		}
	}

	public int convertDipToPixels(float dips) {
		return (int) (dips
				* Activity_homecustomgallery.this.getResources()
						.getDisplayMetrics().density + 0.5f);
	}

}
