package com.comp490.studybuddy.note;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.comp490.studybuddy.R;

/* This plays our videos in fullscreen if play button is selected from VideoMenu 
 * */
/* TO DO: 
 * 	Exception handling in case file Uri is no longer valid
 *    onPause()/ onStart() resource management
 * */
public class VideoViewerActivity extends Activity {
	private Uri videoUri;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.gc(); //try run garbage collector before playback
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		videoUri = Uri.parse(extras.getString("videoUri"));
      VideoView vid = new VideoView(getApplicationContext());
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(
          WindowManager.LayoutParams.FLAG_FULLSCREEN,  
           WindowManager.LayoutParams.FLAG_FULLSCREEN);
      setContentView(vid);
      vid.setVideoURI(videoUri);
      vid.setMediaController(new MediaController(this));
      vid.requestFocus();
      vid.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
