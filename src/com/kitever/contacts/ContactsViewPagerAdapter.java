package com.kitever.contacts;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ContactsViewPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] { "Contacts", "Groups" };
	Context context;

	public ContactsViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

			// Open FragmentTab1.java
		case 0:
			ContactsFragment fragmenttab1 = new ContactsFragment();
			return fragmenttab1;

			// Open FragmentTab2.java
		case 1:
			GroupsFragment fragmenttab2 = new GroupsFragment();
			return fragmenttab2;

	/*		// Open FragmentTab3.java
		case 2:
			FragmentTab3 fragmenttab3 = new FragmentTab3();
			return fragmenttab3;*/
		}
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}
