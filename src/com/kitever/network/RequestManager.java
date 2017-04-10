package com.kitever.network;

import android.graphics.Bitmap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kitever.app.context.BaseApplicationContext;

import java.util.Map;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;



public class RequestManager {

	private RequestQueue requestQueue;
	private StringRequest stringPostRequest;

	private String getPostBaseUrl() {
		String stringUrl = "";
		//stringUrl = "http://test.kitever.com/NewService.aspx";
		stringUrl =Apiurls.KIT19_BASE_URL.replace("?Page=", "");
		if (stringUrl.equalsIgnoreCase("")) {
			stringUrl = Utils
					.getBaseUrlValue(BaseApplicationContext.baseContext);
			if (stringUrl.equalsIgnoreCase("")) {
				stringUrl = "http://kitever.com/NewService.aspx";
			}
		}		
		stringUrl = stringUrl.replace(" ", "");
		return stringUrl;
	}

	private String getBaseUrl() {
		String stringUrl = "";
		stringUrl = Apiurls.KIT19_BASE_URL;
		if (stringUrl.equalsIgnoreCase("")) {
			stringUrl = Utils
					.getBaseUrlValue(BaseApplicationContext.baseContext);
			if (stringUrl.equalsIgnoreCase("")) {
				stringUrl = "http://kitever.com/NewService.aspx?Page=";
			}
		}		
		stringUrl = stringUrl.replace(" ", "");
		return stringUrl;
	}

	public void sendPostRequest(final NetworkManager obj, final int requestId,
			final Map<String, String> params) throws Exception{

		 stringPostRequest = new StringRequest(
				Request.Method.POST, getPostBaseUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {						
						obj.onReceiveResponse(requestId, response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						obj.onErrorResponse(error);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// Map<String, String> params = new HashMap<String, String>();
				// params.put("Page", "FetchFTPDataList");
				// params.put("userid", "121");
				return params;
			}
		};

		stringPostRequest.setTag(obj);
		requestQueue = Volley
				.newRequestQueue(BaseApplicationContext.baseContext);
		stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		requestQueue.add(stringPostRequest);
	}

	public void sendGetRequest(final NetworkManager obj, final int requestId,
			String url) {

		// prepare the Request
		StringRequest stringGetRequest = new StringRequest(Request.Method.GET,
				getBaseUrl() + url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						obj.onReceiveResponse(requestId, response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						obj.onErrorResponse(error);
					}
				}) {
		};
		RequestQueue requestQueue = Volley
				.newRequestQueue(BaseApplicationContext.baseContext);
		requestQueue.add(stringGetRequest);

	}

	public Bitmap sendImageDownloadRequest(final NetworkManager obj,
			final int requestId, final String url) {
		final Bitmap bitMapResponse = null;
		ImageRequest downloadImage = new ImageRequest(url,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {

					}
				}, 0, 0, null, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
					}
				});
		RequestQueue requestQueue = Volley
				.newRequestQueue(BaseApplicationContext.baseContext);
		requestQueue.add(downloadImage);
		return bitMapResponse;
	}

	public RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public StringRequest getStringPostRequest() {
		return stringPostRequest;
	}
}
