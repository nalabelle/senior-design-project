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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp490.studybuddy.MainActivity;
import com.comp490.studybuddy.R;
import com.comp490.studybuddy.database.DBHelper;
import com.comp490.studybuddy.models.NoteEntry;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

/* General Information: 
 * 
 * NoteModel = data structure that holds everything created using the note (entries)
 * NoteModelEntry = entries contain information about text, photos, videos
 * "<insert media type>Builder" = creates the Views, listeners when an entry is created 
 * "<insert media type>Menu" = loads a submenu of options to use on entries (delete, rename,etc) 
 */

public class NoteActivity extends OrmLiteBaseActivity<DBHelper> {	
	
	// Sound related variables
	final Context context = this;
	NoteActivity noteActivity = this;
	SoundBuilder audio = null; 
	
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
		
	private NoteEntry drawEntry = null;
	public boolean showPopUp = true;
	
	private SparseArray<NoteEntry> noteList = new SparseArray<NoteEntry>();
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//load previous stuff.
		this.initializeViews();
		createPopup();
		//createPopup();
	}
	
	private void initializeViews() {
		try {
			List<NoteEntry> list = getHelper().getNoteEntryDao().queryForAll();
			for (NoteEntry entry : list) {
				switch(entry.getType()) {
				case AUDIO:
					new SoundBuilder(entry, this);
					break;
				case DRAW:

					drawEntry = entry; // can't access views from oncreate?
					ViewGroup layout;
					TextView blank = new TextView(this);
					
					try {
						layout = (ViewGroup) this
								.findViewById(R.id.note_layout);
						layout.addView(blank);
					} catch (Exception e) {
						clickie("layout failed on start");
						e.printStackTrace();
						break;
					}

					try {
						blank.post(new Runnable() {
							@Override
							public void run() {
								new DrawMenu(noteActivity, drawEntry);
							}
						});
					} catch (Exception e) {
						clickie("startup post or runnable failed");
						e.printStackTrace();
					}
					
					break;
				case PICTURE:
					new PictureBuilder(this, entry);
					break;
				case TEXT:
					new TextBuilder(this, entry);
					break;
				case VIDEO:
					new VideoBuilder(this, entry);
					break;
				default:
					break;
				
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//This clears the focus out of edit text entries if they're not clicked and hides the keyboard.
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
	}
	

	
	// ********** MENU INTERFACES *********************
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Action bar clicks (black bar on top)
		switch(item.getItemId()){
		case android.R.id.home:
			startActivity(new Intent(this, MainActivity.class));
			return true;
		case R.id.action_draw:{
			// onClick of Pen icon
			if (drawEntry == null){
				drawEntry = new NoteEntry(NoteEntry.NoteType.DRAW);
			}
			this.startActionMode(new DrawMenu(this, drawEntry));
			return true;
		}
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
			new TextBuilder(this, new NoteEntry(NoteEntry.NoteType.TEXT));
			return true;
		}
		case R.id.action_save_note: {
			this.saveNotes();
			return true;
		}
		case R.id.action_screen_orientation:{
			this.changeOrientation();
	        return true;
		}
		/*
		//insert other action menu options here
		case R.id.action_launch_flashcards: {
			startActivity(new Intent(this, FlashMain.class));
			saveNotes();
			this.finish();
			return true;
		}
		case R.id.action_launch_calendar: {
			startActivity(new Intent(this, CalenActivity.class));
			saveNotes();
			this.finish();
			return true;
		}
		case R.id.action_launch_todo: {
			startActivity(new Intent(this, ToDoMain.class));
			saveNotes();
			this.finish();
			return true;
		}	*/   
		default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/*  General note about sound interface. The recording interface is controlled
	 by an actionView. That is when the mic button is clicked, the actionView 
	 is created to display record options. The playback interface is created 
	 using a contextual action mode, which is also a menu, but a different 
	 implementation.	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_main_menu, menu);				
		View soundActionView = menu.findItem(R.id.action_record_sound).getActionView();
		
		if(hasMic()) {
			
		//listeners for Record actionView menu and appropriate response
		final Button rec = (Button) soundActionView.findViewById(R.id.bActionSoundRecord);
		soundActionView.findViewById(R.id.bActionSoundRecord).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//add the audionote entry. Not created until record is clicked once
				if (audio == null){
					NoteEntry noteEntry = new NoteEntry(NoteEntry.NoteType.AUDIO);
					audio = new SoundBuilder(noteEntry, noteActivity);					
				} 
				if (!audio.getStatus().equals(SoundBuilder.Status.RECORDING)){ // already recording?
					rec.setTextColor(Color.RED);
					audio.startRecording();
				}
			}
		});
		soundActionView.findViewById(R.id.ibActionSoundStop).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v1) {
				if (audio != null){
					if (audio.getStatus().equals(SoundBuilder.Status.RECORDING)){
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
		}else{
			clickie("No microphone detected");
		}
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
			NoteEntry noteEntry = new NoteEntry(NoteEntry.NoteType.VIDEO);
			noteEntry.setFilePath(data.getData().toString());
			new VideoBuilder(noteActivity, noteEntry);	//VideoBuilder videoBuilder = 		        
	    }
		
		else if ( requestCode == MEDIA_TYPE_IMAGE && resultCode == Activity.RESULT_OK) {
		   // ********** creates PICTURE **************************
			NoteEntry noteEntry = new NoteEntry(NoteEntry.NoteType.PICTURE);
			noteEntry.setFilePath(path); // photofilepath
			new PictureBuilder(noteActivity, noteEntry); //PictureBuilder picObject = 
		}	
		
		else //error
			Log.e(LOG_TAG, "onActivityResult failed to create media");
	}
	
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

	public void addNote(NoteEntry entry) {
		noteList.put(entry.getID(), entry);
	}
	
	public void deleteNote(NoteEntry entry) {
		noteList.delete(entry.getID());
	}
	
	public void saveNotes() {
		try {
			int key = 0;
			for(int i = 0; i < noteList.size(); i++) {
			   key = noteList.keyAt(i);
			   NoteEntry entry = noteList.get(key);
			   getHelper().getNoteEntryDao().update(entry);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void changeOrientation(){
		int orientation = getResources().getConfiguration().orientation;
		switch(orientation) {
        case Configuration.ORIENTATION_PORTRAIT:
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            clickie("Switched to Landscape");
            break;
        case Configuration.ORIENTATION_LANDSCAPE:
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            clickie("Switched to Portrait");
            break;
		}
	}
	
	
	public void createPopup(){
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(noteActivity);
		alertDialogBuilder.setMessage("Welcome to the Note!")
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {


				try {
					new DrawMenu(noteActivity, drawEntry);
				} catch (Exception e) {
					clickie("Fail");
					e.printStackTrace();
				}
				dialog.cancel();
				
			}
		});
		
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();

	}
}
