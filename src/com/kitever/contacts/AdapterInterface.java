package com.kitever.contacts;

public interface AdapterInterface {

	void onItemClicked(int position, int forWhat);
	
	void onItemsEmpty();
	
	void onItemsAvailable();
	
}
