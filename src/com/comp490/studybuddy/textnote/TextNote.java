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
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.comp490.studybuddy.R;

public class TextNote extends Activity {
	
	// Sound related variables
	boolean micExists, recording, playing, paused = false;
	boolean aRecordingExists = false; 
	ActionBar actionBar;
	final Context context = this;
	int count = 1; 
	MediaRecorder recorder = null;
	ActionMode mActionMode, oActionMode;
	MediaPlayer player = null;
	int soundPlayBackPosition; 
	int currentResourceID = -1;
	int currentSoundTitleID = -1;
	private static final String LOG_TAG = "Sound Record";
	String soundFilePath;


	// Photo and Video related variables
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;	
	private ImageView pic = null;
	private VideoView vid = null;
	private Uri fileUri;
	private static File mediaFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_note);
	}

/*  General note about sound interface. The recording interface is controlled
	 by an actionView. That is when the mic button is clicked, the actionView 
	 is created to display record options. The playback interface is created 
	 using a contextual action mode, which is also a menu, but a different 
	 implementation.	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false); //hide actionbar title
		actionBar.setDisplayShowHomeEnabled(false); //hide actionbar icon
		getMenuInflater().inflate(R.menu.text_note, menu);

		View v = (View) menu.findItem(R.id.action_record_sound).getActionView();
		
		//listeners for Record actionView menu and appropriate response		
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
			v.findViewById(R.id.ibActionSoundStop).setOnClickListener(new View.OnClickListener(){
				public void onClick(View v1) {
					if (recording){
						rec.setTextColor(Color.WHITE);
						stopRecording();
					}
				}
			});
			v.findViewById(R.id.ibActionSoundSave).setOnClickListener(new View.OnClickListener(){
				public void onClick(View v1) {
					if (aRecordingExists){
						createSoundButton();
					}
					rec.setTextColor(Color.WHITE);
					// back out of menu
					dispatchKeyEvent(new KeyEvent (KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
					dispatchKeyEvent(new KeyEvent (KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
				}
			});	
		}	
		return true;
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
		case R.id.action_take_video: {
			//onClick of video icon
			takeVideo();
			return true;
		}
		case R.id.action_create_text:{
			//onClick of keyboard icon
			createEditText();
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
	
	// Contextual action mode for sound playback (when a sound icon is touched
	// a menu is loaded to allow controls for the sound)
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.note_play_sound, menu);
			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			// menu selection responses:
			case R.id.menuSoundPlay: {
				playSound();
				return true;
			}
			case R.id.menuSoundPause: {
				pauseSound();
				return true;
			}
			case R.id.menuSoundStop: {
				stopPlayback();
				return true;
			}
			case R.id.menuUnlockView: {
				clickie("TODO: be able to move view");
				item.setIcon(R.drawable.ic_action_secure);
				return true;
			}			
			case R.id.menuSoundDelete: {
				stopPlayback();
				
				// Popup Confirmation dialogbox for deletion
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Delete Sound?");
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try { //clicked yes
									View viewToDelete = findViewById(currentResourceID);
									((LinearLayout) viewToDelete.getParent())
											.removeView(viewToDelete);
								} catch (Exception e1) {
									Log.e(LOG_TAG, "Delete of soundbutton failed");
								}
								try {
									File file = new File(soundFilePath);
									boolean deleted = file.delete();
									if (deleted) {
										Toast.makeText(getBaseContext(),
												"File Deleted: " + soundFilePath,
												Toast.LENGTH_LONG).show();
									}
								} catch (Exception e) {
									Log.e(LOG_TAG, "Delete of sound file failed");
								}
								mode.finish(); // close the contextual menu
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
				return true;
			}			
			case R.id.menuSoundRename: {
				// Pop up dialog to rename sound file
				final EditText editName = new EditText(context);
				final TextView soundTitle;
				soundTitle = (TextView) findViewById(currentSoundTitleID);	
				try {
					editName.setText(((TextView) soundTitle).getText());
					editName.selectAll();
				} catch (Exception e) {
					Log.e(LOG_TAG, "Sound title textview failed to open");
					e.printStackTrace();
				}				
				new AlertDialog.Builder(context)
					.setTitle("Rename Sound Title")
					.setView(editName)
					.setPositiveButton("Rename", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichButton){
								try {
									soundTitle.setText(editName.getText().toString());
								} catch (Exception e) {
									Log.e(LOG_TAG, "Sound title textview failed to rename");
									e.printStackTrace();
								}
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichButton){}
					})
					.show();
				return true;
			}
			default:
				return false;
			}
		}
	    // Called when the user exits the action mode (check mark)
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	   	 if (playing || paused){
	   		 //stopPlayback(); if commented allows other activities while playing
	   	 }
	        mActionMode = null;
	    }
	};	
	
	// Contextual action mode options for Views other than sound.
	// Pictures are only removed from the note, not from device.
	private ActionMode.Callback optionsActionModeCallback = new ActionMode.Callback() {
		
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.note_edittext_pics, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; 
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			// menu selection responses:
			case R.id.menuDeleteView: {				
				// Pop up Confirmation dialog box for deletion
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Delete Item?");
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try { //clicked yes
									View viewToDelete = findViewById(currentResourceID);
									((LinearLayout) viewToDelete.getParent())
											.removeView(viewToDelete);
								} catch (Exception e1) {
									Log.e(LOG_TAG, "Delete of view failed");
								}
								mode.finish(); // close the contextual menu
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
				return true;
			}
			case R.id.menuUnlockView: {
				clickie("TODO: be able to move view");
				item.setIcon(R.drawable.ic_action_secure);
				return true;
			}			
			default:
				return false;
			}
		}
		
	    // Called when the user exits the action mode (check mark)
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	   	 oActionMode = null;
	    }
	};		
	
	private void createEditText(){		
		final EditText textBox = new EditText(getBaseContext());
		textBox.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		textBox.setMaxLines(10);
		textBox.setHint("Enter note here");
		textBox.requestFocus();
		textBox.setId(generateViewID());
		textBox.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		LinearLayout layout = (LinearLayout)findViewById(R.id.note_inner_layout);
		layout.addView(textBox);
		textBox.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v1) {
				if (oActionMode != null) {
				} else { // Start the CAB using the ActionMode.Callback defined above
					currentResourceID = textBox.getId();
					oActionMode = startActionMode(optionsActionModeCallback);
					v1.setSelected(true);
				}
			}
		});
	}	
	
	protected void createSoundButton(){
		// Creating dynamic container (linearlayout) to hold imagebutton and title
		stopRecording();
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
		
		soundButtonAndTitle.addView(soundButton);
		soundButtonAndTitle.addView(soundTitle);
		LinearLayout layout = (LinearLayout)findViewById(R.id.note_inner_layout);
		layout.addView(soundButtonAndTitle);
		soundTitle.setContentDescription(soundFilePath);
		soundButtonAndTitle.setId(generateViewID());
		
		soundButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v2) {
				if (mActionMode != null) {
				} else { // Start the CAB using the ActionMode.Callback defined
							// above
					soundFilePath = (String) soundTitle.getContentDescription(); // update to current button file path
					currentResourceID = soundButtonAndTitle.getId();
					currentSoundTitleID = soundTitle.getId();
					mActionMode = startActionMode(mActionModeCallback);
					v2.setSelected(true);
				}
			}
		});
		aRecordingExists = false;
	}
	
	// generate a random ID for a view that isn't being used
	private int generateViewID(){
		int result;
		Random rand = new Random();
		do { //must create unused ID to be able to refer to sound title textview
			result = rand.nextInt(10000 + count);		
		} while (findViewById(result) != null);
		return result;
	}
	
	public void clickie(String message){ //for testing
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
		
		
		if (requestCode == MEDIA_TYPE_VIDEO && resultCode == Activity.RESULT_OK) {
			vid = new VideoView(getBaseContext());
			vid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			LinearLayout layout = (LinearLayout)findViewById(R.id.note_inner_layout);
	       
            layout.addView(vid);
	        
	        
	        
	    }
		
		else if ( requestCode == MEDIA_TYPE_IMAGE && resultCode == Activity.RESULT_OK) {
			pic = new ImageView(getBaseContext());
			pic.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			LinearLayout layout = (LinearLayout)findViewById(R.id.note_inner_layout);
			pic.setId(generateViewID());
			layout.addView(pic);
			
			// For deletion and other options
			pic.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v1) {
					if (oActionMode != null) {
					} else { 
						currentResourceID = pic.getId();
						oActionMode = startActionMode(optionsActionModeCallback);
						v1.setSelected(true);
					}
				}
			});
			
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
			if (recorder == null){
				recorder = new MediaRecorder();
			}
			
			try {
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				soundFilePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/SB_Audio_" + new SimpleDateFormat("MM-dd-yyyy_KK-mm-ss-a", Locale.getDefault()).format(new Date())
						+ ".3gp";
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				recorder.setOutputFile(soundFilePath);
				recorder.prepare();
				recorder.start();
				recording = true;
				aRecordingExists = true;
			} catch (IllegalStateException e) {
				Log.e(LOG_TAG, "startRecording() broke illegalstate");
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(LOG_TAG, "startRecording() broke ioexception");
				e.printStackTrace();
			}
	}
	
	private void stopRecording(){
		if (recording){
			Toast.makeText(this, "Stopped Recording", Toast.LENGTH_SHORT).show();
			recorder.stop();
			recording = false;
		}
	}
	
	private void playSound(){ 
		try {
			if (paused){
				Toast.makeText(this, "Playing Sound", Toast.LENGTH_SHORT).show();
				player.start();
				playing = true;
				paused = false;
			}
			else if (!playing){
				player = new MediaPlayer();
				player.setDataSource(soundFilePath);
				player.prepare();
				player.start();
				player.setOnCompletionListener(new OnCompletionListener() {					
					@Override
					public void onCompletion(MediaPlayer mp) {
						stopPlayback();						
					}
				});
				playing = true;
			} //else was already playing, do nothing			
		} catch (Exception e) {
			Log.e(LOG_TAG, "playBack() broke");
		}
	}
	
	private void pauseSound(){
		if (playing){
			player.pause();
			paused = true;
			playing = false;
		}
	}
	
	private void stopPlayback(){
		if (playing || paused){
			Toast.makeText(this, "Stopped Playback", Toast.LENGTH_SHORT).show();
			playing = false;
			paused = false;
			player.stop();
			player.release();
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

	
