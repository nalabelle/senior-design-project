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
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;
import com.comp490.studybuddy.models.NoteEntryModel.NoteType;
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

	// Photo and Video related variables
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;	
	private VideoView vid = null;
	private Uri fileUri;
	private static File mediaFile;
	static String path;
	
	
	private static final String LOG_TAG = "Notes";
	
	//let's make us one Note for now, can add more later!
	private NoteModel note = new NoteModel();
	
	
	// **********************not sure if needed ******************/
	//Storage so we can kill all recorders and players.
	protected ArrayList<MediaPlayer> players = new ArrayList<MediaPlayer>();
	protected ArrayList<MediaRecorder> recorders = new ArrayList<MediaRecorder>();
		
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
			TextBuilder text = new TextBuilder(this, noteEntry);
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

		View v = (View) menu.findItem(R.id.action_record_sound).getActionView();
		
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
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, MEDIA_TYPE_VIDEO);
	}
	
	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, MEDIA_TYPE_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode != Activity.RESULT_OK){
			Log.e(LOG_TAG, "onActivityResult failed or was cancelled (back press)");
		}
		
		if (requestCode == MEDIA_TYPE_VIDEO && resultCode == Activity.RESULT_OK) {	
			NoteEntryModel noteEntry = this.note.add(NoteEntryModel.NoteType.VIDEO);
			noteEntry.setFilePath(mediaFile.toString()); //not sure about this
			VideoBuilder videoBuilder = new VideoBuilder(noteActivity, data.getData(), noteEntry);
			
			
			/*
			vid = new VideoView(this);
			vid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

			vid.setVideoURI(data.getData());
			android.widget.FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
					300, 300); //Need to fix this to appropriate dimensions based on device
			vid.setLayoutParams(fl);

			MediaController media_Controller = new MediaController(this);
			media_Controller.setAnchorView(vid);
			vid.setMediaController(media_Controller);
			LinearLayout layout = (LinearLayout) findViewById(R.id.note_inner_layout);
			layout.addView(vid);
			
			*/
			// DisplayMetrics dm = new DisplayMetrics();
			// this.getWindowManager().getDefaultDisplay().getMetrics(dm);
			// int height = dm.heightPixels;
			// int width = dm.widthPixels;
			// vid.setMinimumWidth(width);
			// vid.setMinimumHeight(height);
			// vid.setMediaController(media_Controller);	        
	    }
		
		else if ( requestCode == MEDIA_TYPE_IMAGE && resultCode == Activity.RESULT_OK) {

			Bundle extras = data.getExtras();
			Bitmap btm = (Bitmap) extras.get("data");
			FileOutputStream fs;
			
			/* *********************************************
			* BUG!!! MEDIAFILE not working all the time
			***********************************************/
			try {
				clickie(mediaFile.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
				Log.e(LOG_TAG, "Picture mediaFile not working");
			}

			try {
				fs = new FileOutputStream(mediaFile); // Throws b/c mediafile 
				btm.compress(Bitmap.CompressFormat.JPEG, 100, fs);
			} catch (Exception e) {
				Log.e(LOG_TAG, "Picture FileOutStream or compress exception");
				e.printStackTrace();
			}
			// End of bug!

			// Creates picture object and view
			NoteEntryModel noteEntry = this.note.add(NoteEntryModel.NoteType.PICTURE);
			noteEntry.setFilePath(mediaFile.toString());
			PictureBuilder picObject = new PictureBuilder(noteActivity, btm, noteEntry);

		}	
		
		else
			;//error
	}
	
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
	}
	
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("TextNote", "failed to create directory");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} 
		else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
			path = mediaFile.toString();
		} 
		else {
			return null;
		}
		return mediaFile;
	}
	
	
// ************ Audio Related *********************************
	protected boolean hasMic(){ // verify device can record sound 
		PackageManager pm = this.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
	}

/* Basically following what Nik was doing. In the event this activity is 
 * destroyed we need to release all players/recorders. Since they aren't 
 * accessable within this activity,	we need a place to store them.
 */

	@Override
	protected void onDestroy() {

		if (audio != null){
			audio.stopRecording(); 
		} 
		for(MediaPlayer p : players) {
			if (p != null){
				try {
					p.stop(); //might be already be stopped
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				try {
					p.release();
				} catch (Exception e) {
					Log.e(LOG_TAG, "MediaPlayer array list did not release");
				}
				p = null;
			}
		}
		
		for(MediaRecorder r : recorders) {
			if (r != null){
				try {
					r.stop(); //might be already be stopped
				} catch (IllegalStateException e) {
					Log.e(LOG_TAG, "MediaRecorder array list did not stop");
				}
				try {
					r.release();
				} catch (Exception e) {
					Log.e(LOG_TAG, "MediaRecorder array list did not release");
				}
				r = null;
			}
		}	
		super.onDestroy();
	}
	
	public ArrayList<MediaPlayer> getPlayers(){
		return players; //so we can add and remove players
	}
	
	public ArrayList<MediaRecorder> getRecorders(){
		return recorders; //so we can add and remove recorders
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

/* Issues as of 12/1/2014 (Anthony)
 * 
 * 1. Video is not responding to onclick listener, therefore cannot be 
 * manipulated. Probably because its a widget and not a view.
 * 
 * 2. ArrayLists of recorders and players aren't working. Which are used to 
 * make sure all of them are released in the event of NoteActivity closure
 * 
 * 3. However, if we want to use the recorder or player within StudyBuddy,
 * 	we need to use a service or something otherwise SB crashes
 * 
 * 4. Currently cannot stop a playback after exiting SoundMenu 
 * 
 */