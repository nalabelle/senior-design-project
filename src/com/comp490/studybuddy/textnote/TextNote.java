/*
Author Name and CSUN ID: Anthony Summer Nik
Date started: 9/26/2014
Date Completed:
Peer review: 
Date:
Team members:
Contributing team members: Anthony Summer Nik
 */

package com.comp490.studybuddy.textnote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.comp490.studybuddy.R;

public class TextNote extends Activity {
	
	boolean micExists, recording, playing = false;
	MediaRecorder recorder = null;
	MediaPlayer player = null;
	private static final String LOG_TAG = "Sound Record";
	String filename;

	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	private ImageView a = null;
	@SuppressWarnings("unused")
	private Uri fileUri;
	private static File mediaFile;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_note);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_note, menu);
		View v = (View) menu.findItem(R.id.action_record_sound).getActionView();
		
		//listeners for Record action view menu		
		if (hasMic()){
			final Button b = (Button) v.findViewById(R.id.bActionSoundRecord);
			b.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v1) {
					if (!recording){
						b.setTextColor(Color.RED);					
						startRecording();
					}
				}
			});
			v.findViewById(R.id.ibActionSoundPlay).setOnClickListener(new View.OnClickListener(){
				public void onClick(View v1) {
					if (!playing || !recording){
						playSound();
					}
				}
			});
			v.findViewById(R.id.ibActionSoundStop).setOnClickListener(new View.OnClickListener(){
				public void onClick(View v1) {
					if (playing || recording){
						b.setTextColor(Color.WHITE);
						stopRecordOrPlay();
					}
				}
			});
			v.findViewById(R.id.ibActionSoundPause).setOnClickListener(new View.OnClickListener(){
				public void onClick(View v1) {
					clickie();
				}
			});
			v.findViewById(R.id.ibActionSoundSave).setOnClickListener(new View.OnClickListener(){
				public void onClick(View v1) {
					clickie();
				}
			});	
		}
	
		return true;
	}
	
	public void clickie(){ //for testing
		Toast.makeText(this, "Listener working", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
		case R.id.action_record_sound: {
			// loads sub menu via xml
			return true;
		}
		//insert other action menu options
		default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void takePhoto(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		a = (ImageView) findViewById(R.id.imageView1);
		if (resultCode == Activity.RESULT_OK) {
				Bundle extras = data.getExtras();
				Bitmap btm = (Bitmap) extras.get("data");
				FileOutputStream b;
				try {
					b = new FileOutputStream(mediaFile);
					btm.compress(Bitmap.CompressFormat.JPEG, 100, b);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				a.setImageBitmap(btm);
		}
	}
	
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} 
		else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} 
		else {
			return null;
		}
		return mediaFile;
	}
	
	protected boolean hasMic(){ // verify device can record sound 
		PackageManager pm = this.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
	}
	
	private void startRecording() { //start actionView button press

			if (playing && player != null){
				stopPlayback();
			}
			
			//TO DO: option: record from end of previous recording
			Toast.makeText(this, "Recording", Toast.LENGTH_SHORT).show();
			recorder = new MediaRecorder();
			try {
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				filename = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/SB_Audio_" + new SimpleDateFormat("MM-dd-yyyy_KK-mm-ss-a", Locale.getDefault()).format(new Date())
						+ ".3gp";
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				recorder.setOutputFile(filename);

				recorder.prepare();
				recorder.start();
				recording = true;
			} catch (IllegalStateException e) {
				Log.e(LOG_TAG, "startRecording() broke illegalstate");
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(LOG_TAG, "startRecording() broke ioexception");
				e.printStackTrace();
			}
	}
	
	private void stopRecordOrPlay(){ //stop actionView button press
		if (playing){
			stopPlayback();
		}
		if (recording){
			stopRecording();
		}
	}
	
	private void stopPlayback(){
		Toast.makeText(this, "Stopped Playback", Toast.LENGTH_SHORT).show();
		player.stop();
		player.release();
		player = null;
	}
	
	private void stopRecording(){
		Toast.makeText(this, "Stopped Recording", Toast.LENGTH_SHORT).show();
		recorder.stop();
		recorder.reset();
		recorder.release();
		recorder = null;
	}
	
	private void playSound(){
		try {
			player = new MediaPlayer();
			player.setDataSource(filename);
			player.prepare();
			player.start();
			playing = true;
		} catch (Exception e) {
			Log.e(LOG_TAG, "playBack() broke");
		}
	}
}

/* SOUND: TO DO 10/4/2014 
 * Allow play/record to continue while activity paused/stopped
 * Remove errors (they have no effect)
 * Sound file selection
 * Create buttons representing saved sound files, allow them to display actionView controls. 
 * Save the sound files and buttons for later use
 * Allow the user to rename the sound files
 * Put sound files in a directory
 * */

	
