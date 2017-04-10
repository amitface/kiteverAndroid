package com.kitever.network;

import com.android.volley.VolleyError;

public interface NetworkManager {

	void onReceiveResponse(int requestId, String response);

	void onErrorResponse(VolleyError error);
}
