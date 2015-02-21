/*
Author Name and CSUN ID: Anthony Summer Nik
Date started: 9/26/2014
Date Completed:
Peer review: 
Date:
Team members:
Contributing team members: Anthony Summer Nik
 */

package com.comp490.studybuddy.note;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;
import com.comp490.studybuddy.models.NoteModel;

/* General Information: 
 * 
 * NoteModel = data structure that holds everything created using the note (entries)
 * NoteModelEntry = entries contain information about text, photos, videos
 * "<insert media type>Builder" = creates the Views, listeners when an entry is created 
 * "<insert media type>Menu" = loads a submenu of options to use on entries (delete, rename,etc) 
 */

public class NoteActivity extends Activity {
	
	// Sound related variables
	ActionBar actionBar;
	final Context context = this;
	NoteActivity noteActivity = this;
	AudioBuilder audio = null; 
	
	//Players
	protected MediaPlayer player = null;
	protected MediaRecorder recorder = null;

	// Photo and Video related variables
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;	
	private Uri fileUri;
	private static File mediaFile;
	static String path;	
	
	private static final String LOG_TAG = "Notes";
	
	//let's make us one Note for now, can add more later!
	private NoteModel note = new NoteModel();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
	}
	
	// ********** MENU INTERFACES *********************
	
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
			if (hasCamera()) //block if camera not present
				takePhoto();
			else
				Log.w(LOG_TAG, "No camera on device");
			return true;
		}
		case R.id.action_take_video: {
			//onClick of video icon
			takeVideo();
			return true;
		}
		case R.id.action_create_text:{
			//onClick of keyboard icon

			NoteEntryModel noteEntry = this.note.add(NoteEntryModel.NoteType.TEXT);
			new TextBuilder(this, noteEntry); //TextBuilder text = 
			return true;
		}
		case R.id.action_launch_handwritting:{
			// launcher for handwritting
			clickie("TODO: Link to handwritting implementation");
		}
		// TO DO: SAVE NOTE AND NEW NOTE
		
		//insert other action menu options here
		default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/*  General note about sound interface. The recording interface is controlled
	 by an actionView. That is when the mic button is clicked, the actionView 
	 is created to display record options. The playback interface is created 
	 using a contextual action mode, which is also a menu, but a different 
	 implementation.	 */
	
	//This is for the sound stuff.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(!hasMic()) {
			return false;
		}

		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false); //hide actionbar title
		actionBar.setDisplayShowHomeEnabled(false); //hide actionbar icon
		getMenuInflater().inflate(R.menu.note_main_menu, menu);

		View v = menu.findItem(R.id.action_record_sound).getActionView();
		
		//listeners for Record actionView menu and appropriate response
		final Button rec = (Button) v.findViewById(R.id.bActionSoundRecord);
		v.findViewById(R.id.bActionSoundRecord).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//add the audionote entry. Not created until record is clicked once
				if (audio == null){
					NoteEntryModel noteEntry = note.add(NoteEntryModel.NoteType.AUDIO);
					audio = new AudioBuilder(noteEntry, noteActivity);					
				} 
				if (!audio.getStatus().equals(AudioBuilder.Status.RECORDING)){ // already recording?
					rec.setTextColor(Color.RED);
					audio.startRecording();
				}
			}
		});
		v.findViewById(R.id.ibActionSoundStop).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v1) {
				if (audio != null){
					if (audio.getStatus().equals(AudioBuilder.Status.RECORDING)){
						rec.setTextColor(Color.WHITE);
					}
					audio.stopRecording();
					//audioNotes.add(audio);
					//createSoundButton(audio);
					
					//exit menu
					dispatchKeyEvent(new KeyEvent (KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
					dispatchKeyEvent(new KeyEvent (KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
					audio = null;  
				}
			}
		});

		return true;
	}
	
// ********** PHOTO / VIDEO related **************************
	
	// verify device has a camera
	private boolean hasCamera(){
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}
	
	public void takeVideo() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		fileUri = getUri(MEDIA_TYPE_VIDEO);
		//Do not remove below comment, NEEDS INVESTIGATING -> i.e. determine where video file is stored -Ant
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 
		startActivityForResult(intent, MEDIA_TYPE_VIDEO);
	}
	
	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getUri(MEDIA_TYPE_IMAGE);
		if (fileUri != null){
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
      intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, 0);
			startActivityForResult(intent, MEDIA_TYPE_IMAGE);
		}
		else {
			clickie("Photo File failed to create");
		}
	}
	
	@Override //Runs after device takes pic or video
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode != Activity.RESULT_OK){
			Log.e(LOG_TAG, "onActivityResult failed or was cancelled (back press)");
		}
		
		if (requestCode == MEDIA_TYPE_VIDEO && resultCode == Activity.RESULT_OK) {	
		   // ********** creates VIDEO **************************
			NoteEntryModel noteEntry = this.note.add(NoteEntryModel.NoteType.VIDEO);
			noteEntry.setFilePath(mediaFile.toString()); //not sure about this
			new VideoBuilder(noteActivity, data.getData(), noteEntry);	//VideoBuilder videoBuilder = 		        
	    }
		
		else if ( requestCode == MEDIA_TYPE_IMAGE && resultCode == Activity.RESULT_OK) {
		   // ********** creates PICTURE **************************
			NoteEntryModel noteEntry = this.note.add(NoteEntryModel.NoteType.PICTURE);
			noteEntry.setFilePath(path); // photofilepath
			new PictureBuilder(noteActivity, noteEntry); //PictureBuilder picObject = 
		}	
		
		else //error
			Log.e(LOG_TAG, "onActivityResult failed to create media");
	}
	
	/*
	private static Uri getUri(int type) {
		
		File temp = null;
		try {
			temp = getMediaFile(type);
		} catch (Exception e) {
			Log.d("Note", "failed to create mediafile");
			e.printStackTrace();
			return null;
		}
		return Uri.fromFile(temp);
	}*/

	//Generate file and to store Photo or Video 
	private static Uri getUri(int type) {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"StudyBuddy_Files");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("Note", "failed to create directory");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
			path = mediaFile.getAbsolutePath();
		} 
		else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
			path = mediaFile.toString();
		} 
		else {
			return null;
		}
		return Uri.fromFile(mediaFile);
	}
	
	
// ************ Audio Related *********************************
	protected boolean hasMic(){ // verify device can record sound 
		PackageManager pm = this.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
	}

/* Releasing recorder/player on exit of Note. Perhaps future updates
 * could allow the recorder/player to spawn a service and allow
 * playback/recording while using other parts of the app. 
 */

	@Override
	protected void onDestroy() {

		if (audio != null) {
			audio.stopRecording();
		}

		//try catch blocks may be unnecessary, being safe for now
		if (player != null) { 
			try {
				player.stop(); // might be already be stopped
			} catch (IllegalStateException e) {
				Log.e(LOG_TAG, "Player did not stop on destroy");
			}
			try {
				player.release();
			} catch (Exception e) {
				Log.e(LOG_TAG, "Player did not release on destroy");
			}
			player = null;
		}

		if (recorder != null) {
			try {
				recorder.stop(); // might be already be stopped
			} catch (IllegalStateException e) {
				Log.e(LOG_TAG, "Recorder did not stop on destroy");
			}
			try {
				recorder.release();
			} catch (Exception e) {
				Log.e(LOG_TAG, "Recorder did not release on destroy");
			}
			recorder = null;
		}
		super.onDestroy();
	}
	
	protected NoteModel getNoteModel(){ //used for NoteModel entry deletions
		return note;
	}	
	
	// generate a random ID for a view that isn't being used
	protected int generateViewID(){
		int result;
		Random rand = new Random();
		do { //must create unused ID to be able to refer to sound title textview
			result = rand.nextInt(10000);		
		} while (findViewById(result) != null);
		return result;
	}
	
	public void clickie(String message){ //for testing
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
