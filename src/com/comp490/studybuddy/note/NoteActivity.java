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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;
import com.comp490.studybuddy.models.NoteEntryModel.NoteType;
import com.comp490.studybuddy.models.NoteModel;
import com.comp490.studybuddy.note.AudioObject.Status;

public class NoteActivity extends Activity {
	
	// Sound related variables
	ActionBar actionBar;
	final Context context = this;
	NoteActivity noteActivity = this;

	int soundPlayBackPosition; 
	int currentResourceID = -1;
	int currentSoundTitleID = -1;


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
	
	//We need to stuff the audio notes somewhere temporarily so we can kill all recorders and players.
	protected ArrayList<AudioObject> audioNotes = new ArrayList<AudioObject>();
	
	// Contextual action mode for sound playback (when a sound icon is touched
	// a menu is loaded to allow controls for the sound)
	private ActionMode.Callback mActionModeCallback = new SoundPlayMenu(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_note);
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
			TextObject text = new TextObject(this, noteEntry);
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
		getMenuInflater().inflate(R.menu.text_note, menu);

		View v = (View) menu.findItem(R.id.action_record_sound).getActionView();
		
		//add the audionote entry.
		final AudioObject audio = new AudioObject(this, new NoteEntryModel(NoteType.AUDIO), noteActivity);
		
		//listeners for Record actionView menu and appropriate response
		final Button rec = (Button) v.findViewById(R.id.bActionSoundRecord);
		v.findViewById(R.id.bActionSoundRecord).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!audio.getStatus().equals(AudioObject.Status.RECORDING)){
					rec.setTextColor(Color.RED);
				}
				audio.startRecording();
			}
		});
		v.findViewById(R.id.ibActionSoundStop).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v1) {
				if (!audio.getStatus().equals(AudioObject.Status.RECORDING)){
					rec.setTextColor(Color.WHITE);
				}
				audio.stopRecording();
				audioNotes.add(audio);
				//createSoundButton(audio);
				dispatchKeyEvent(new KeyEvent (KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
				dispatchKeyEvent(new KeyEvent (KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));	
			}
		});
		//the save button should be hidden until there's a recording, or not here. I'm going with not here for now.
		//We have git history if you want to see what it was.

		return true;
	}
/*
	protected void createSoundButton(AudioNote audio){
		// Creating dynamic container (linearlayout) to hold imagebutton and title
		final LinearLayout soundButtonAndTitle = new LinearLayout(getBaseContext());
		LayoutParams llParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		soundButtonAndTitle.setLayoutParams(llParams);		
		ImageButton soundButton = new ImageButton(getBaseContext());
		soundButton.setImageResource(R.drawable.ic_action_volume_on);
		
		//TextView displays name of sound
		final TextView soundTitle = new TextView(getBaseContext());
		soundTitle.setText("Sound " + count);
		soundTitle.setId(generateViewID());
		count++;
		
		//set the audionote title and add it to the note
		audio.setName(soundTitle.getText().toString());
		//we should store the actual noteentry somewhere
		//this.note.add(audio);
		//store the audionote for destroy
		this.audioNotes.add(audio);
		
		
		soundButtonAndTitle.addView(soundButton);
		soundButtonAndTitle.addView(soundTitle);
		LinearLayout layout = (LinearLayout)findViewById(R.id.note_inner_layout);
		layout.addView(soundButtonAndTitle);
		soundTitle.setContentDescription(audio.getFilePath());
		soundButtonAndTitle.setId(generateViewID());
		
		soundButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v2) {
				if (mActionMode != null) {
				} else { // Start the CAB using the ActionMode.Callback defined above
					currentResourceID = soundButtonAndTitle.getId();
					currentSoundTitleID = soundTitle.getId();
					mActionMode = startActionMode(mActionModeCallback);
					v2.setSelected(true);
				}
			}
		});
	}
	*/
	
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
			Log.e(LOG_TAG, "fail");
		}
		
		if (requestCode == MEDIA_TYPE_VIDEO && resultCode == Activity.RESULT_OK) {	
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
				// TODO Auto-generated catch block
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
			PictureObject picObject = new PictureObject(noteActivity, btm, noteEntry);

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
	
	protected boolean hasMic(){ // verify device can record sound 
		PackageManager pm = this.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
	}

	@Override
	protected void onDestroy() {
		for(AudioObject audio : this.audioNotes) {
			audio.onDestroy();
		}
		super.onDestroy();
	}

	public void audioState(int i, Status change) {
		String audioName = ((TextView) findViewById(currentSoundTitleID)).getText().toString();
		AudioObject audioNote = null;
		for(AudioObject note : this.audioNotes) {
			if(note.getName().equals(audioName))
				audioNote = note;
		}
		if(audioNote == null)
			return;
		switch(change) {
		case PLAYING:
			audioNote.playSound();
			break;
		case PAUSED:
			audioNote.pauseSound();
			break;
		default:
			break;
		}
		
	}

	public boolean deleteAudio() {
		String audioName = ((TextView) findViewById(currentSoundTitleID)).getText().toString();
		AudioObject audioNote = null;
		for(AudioObject note : this.audioNotes) {
			if(note.getName().equals(audioName))
				audioNote = note;
		}
		if(audioNote == null)
			return false;
		final String path = audioNote.getFilePath();
		
		// Popup Confirmation dialogbox for deletion
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Delete Sound?");
		builder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try { // clicked yes
							View viewToDelete = findViewById(currentResourceID);
							((LinearLayout) viewToDelete.getParent()).removeView(viewToDelete);
						} catch (Exception e1) {
							Log.e(LOG_TAG, "Delete of soundbutton failed");
						}
						try {
							File file = new File(path);
							boolean deleted = file.delete();
							if (deleted) {
								Toast.makeText(getBaseContext(),
										"File Deleted: " + path,
										Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							Log.e(LOG_TAG, "Delete of sound file failed");
						}
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
		return false;
	}	
}

/* SOUND: TO DO 10/4/2014 
 * Allow play/record to continue while activity paused/stopped - DONE
 * Remove errors (they have no effect) - IGNORING
 * Sound file selection - DONE
 * Create buttons representing saved sound files, allow them to display actionView controls.  - DONE
 * Save the sound files and buttons for later use 
 * Allow the user to rename the sound files - DONE
 * Put sound files in a directory
 * */

/* SOUND: TO DO 10/18/2014
 * figure out how to get correct file path to play multiple files - DONE
 * delete sound icons and files - DONE
 * rename buttons - DONE
 * 
 * */

/* SOUND & OTHER THINGS: TO DO 11/2/2014
 * discovered bug when switching between files - DONE
 * fix edittext constant focus
*/

	
