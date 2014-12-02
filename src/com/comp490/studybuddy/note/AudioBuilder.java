package com.comp490.studybuddy.note;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;

public class AudioBuilder {
	 //essentially another context of Note activity, but required for getting views
	// temp fix for now
	private NoteActivity noteActivity;
	private AudioBuilder audioBuilder = this;
	private NoteEntryModel entry;
	private MediaRecorder recorder = null;
	private Status status = Status.PAUSED;
	private static final String LOG_TAG = "Sound Record";
	private String tempStorage = "/Temp/Notes/Audio/";  //move to Notes/Audio after save
	private String soundFilePath;
	private int viewID;
	
	// Views 
	private LinearLayout soundButtonAndTitle;
	ImageButton soundButton;
	TextView soundTitle;
	
	public AudioBuilder(NoteEntryModel entry, NoteActivity noteContext) {
		this.noteActivity = noteContext;
		this.entry = entry;
	}
	
	public boolean startRecording() { // on actionView REC button press
		if (recorder == null) {
			recorder = new MediaRecorder();
			noteActivity.getRecorders().add(recorder);
		}
		
		if(status.equals(Status.RECORDING)) {
			//exit here because we're already doing it.
			return false;
		}

		try {
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			//soundFilePath = context.getExternalFilesDir(null) + tempStorage +
			//		new SimpleDateFormat("MM-dd-yyyy_KK-mm-ss-a", Locale.getDefault()).format(new Date()) + ".3gp";
			soundFilePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/SB_Audio_" + new SimpleDateFormat("MM-dd-yyyy_KK-mm-ss-a", Locale.getDefault()).format(new Date())
					+ ".3gp";
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(soundFilePath);
			recorder.prepare();
			recorder.start();
			status = Status.RECORDING;
			entry.setFilePath(soundFilePath);
			return true;
		} catch (IllegalStateException e) {
			Log.e(LOG_TAG, "startRecording() broke illegalstate");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(LOG_TAG, "startRecording() broke ioexception");
			e.printStackTrace();
		}
		return false; //we must have had an exception.
	}

	public boolean stopRecording() {
		if (status.equals(Status.RECORDING)) {
			Toast.makeText(noteActivity, "Stopped Recording", Toast.LENGTH_SHORT).show();
			if (recorder != null) {
				try {
					recorder.stop();
				} catch (IllegalStateException e) {
					Log.e(LOG_TAG, "Failed to stop recorder");
					e.printStackTrace();
				}
				noteActivity.getRecorders().remove(recorder);
				recorder.release();
			}
			recorder = null;
			status = Status.PAUSED;
			this.entry.addFile(this.soundFilePath); //Add the file to the entry so it can be saved later.
			
			createSoundButton();
			
			return true;
		}
		return false;
	}
	
	protected void createSoundButton(){
		// Creating dynamic container (linearlayout) to hold imagebutton and title
		soundButtonAndTitle = new LinearLayout(noteActivity);
		LayoutParams llParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		soundButtonAndTitle.setLayoutParams(llParams);		
		soundButton = new ImageButton(noteActivity);
		soundButton.setImageResource(R.drawable.ic_action_volume_on);
		
		//TextView displays name of sound
		soundTitle = new TextView(noteActivity);
		soundTitle.setText("Sound");
		
		//Generate IDs, one for deletion and the other for renaming
		viewID = noteActivity.generateViewID();
		soundButtonAndTitle.setId(viewID);
		entry.setViewID(viewID);	
		
		//We can use this to rename the title later
		int secondViewID = noteActivity.generateViewID();
		soundTitle.setId(secondViewID);
		entry.setSecondaryViewID(secondViewID);
		
		//set the audionote title and add it to the note
		//this.setName(soundTitle.getText().toString());
		//we should store the actual noteentry somewhere
		//this.note.add(audio);
		//store the audionote for destroy		
		
		soundButtonAndTitle.addView(soundButton);
		soundButtonAndTitle.addView(soundTitle);
		LinearLayout layout = (LinearLayout)noteActivity.findViewById(R.id.note_inner_layout);
		layout.addView(soundButtonAndTitle);
		
		soundButton.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback soundMenu = new SoundPlayMenu(noteActivity, audioBuilder, entry);
				noteActivity.startActionMode(soundMenu);
				return true;
			}
		});
	}
	
	public Status getStatus() {
		return status;
	}
	
	public enum Status {
		RECORDING, PLAYING, PAUSED;
	}

	public void onDestroy() {
		try {
			if (recorder != null){
				recorder.release();
				recorder = null;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "onDestroy of audiobuilder failed");
		}
	}
	
	protected int getID(){
		return viewID;
	}
}
