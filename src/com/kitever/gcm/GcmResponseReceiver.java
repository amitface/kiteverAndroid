package com.kitever.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmResponseReceiver extends WakefulBroadcastReceiver {
//
//	@Override
//	protected String getGCMIntentServiceClassName(Context context) {
//
//		return "com.kitever.gcm.GcmCustomService";
//	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ComponentName comp = new ComponentName(context.getPackageName(),
				GcmCustomService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
	}

}

    