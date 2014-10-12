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

import android.app.ActionBar;
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
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	
	private ImageView pic = null;
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
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false); //hide actionbar title
		actionBar.setDisplayShowHomeEnabled(false); //hide actionbar icon
		getMenuInflater().inflate(R.menu.text_note, menu);

		View v = (View) menu.findItem(R.id.action_record_sound).getActionView();
		
		//listeners for Record actionView menu		
		if (hasMic()){
			final Button rec = (Button) v.findViewById(R.id.bActionSoundRecord);
			rec.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v1) {
					if (!recording){
						rec.setTextColor(Color.RED);					
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
						rec.setTextColor(Color.WHITE);
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
					createSoundButton();
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
		// Action bar clicks (black bar on top)
		switch(item.getItemId()){
		case R.id.action_record_sound: {
			// onClick of sound button loads actionView via xml
			return true;
		}
		case R.id.action_take_photo: {
			//onClick of photo icon
			takePhoto();
			return true;
		}
		case R.id.action_create_text:{
			//onClick of keyboard icon
			createEditText();
			return true;
		}
		//insert other action menu options here
		default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void createEditText(){
		EditText textBox = new EditText(getBaseContext());
		textBox.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		textBox.setMaxLines(10);
		textBox.setHint("Enter note here");
		textBox.requestFocus();
		textBox.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		LinearLayout layout = (LinearLayout)findViewById(R.id.note_inner_layout);
		layout.addView(textBox);
	}
	
	protected void createSoundButton(){
		//TO DO: to wire button to actionView buttons
		ImageButton soundButton = new ImageButton(getBaseContext());
		soundButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		soundButton.setImageResource(R.drawable.ic_action_volume_on);
		LinearLayout layout = (LinearLayout)findViewById(R.id.note_inner_layout);
		layout.addView(soundButton);
	}

	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		pic = new ImageView(getBaseContext());
		pic.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		LinearLayout layout = (LinearLayout)findViewById(R.id.note_inner_layout);
		layout.addView(pic);

		if (resultCode == Activity.RESULT_OK) {
				Bundle extras = data.getExtras();
				Bitmap btm = (Bitmap) extras.get("data");
				FileOutputStream fs;
				try {
					fs = new FileOutputStream(mediaFile);
					btm.compress(Bitmap.CompressFormat.JPEG, 100, fs);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				pic.setImageBitmap(btm);
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
	
	private void startRecording() { //on actionView REC button press
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
	
	private void stopRecordOrPlay(){ //on actionView STOP button press
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
	
	private void playSound(){ // on actionView PLAY button press
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

	@Override
	protected void onDestroy() {
		if (recorder != null){
			recorder.release();
		}
		if (player != null){
			player.release();
		}
		super.onDestroy();
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

	
