package com.comp490.studybuddy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service{

	private static final String TAG = "MyService";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
	} //can start new thread here for background processes

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}
}