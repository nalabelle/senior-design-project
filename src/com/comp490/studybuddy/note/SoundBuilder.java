package com.comp490.studybuddy.note;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;

public class SoundBuilder {

	private NoteActivity noteActivity;
	private NoteEntry entry;
	private Status status = Status.PAUSED;
	private static final String LOG_TAG = "Sound Record";
	//private String tempStorage = "/Temp/Notes/Audio/";  //move to Notes/Audio after save
	private String soundFilePath;
	
	// Views 
	private LinearLayout soundButtonAndTitle;
	private ImageButton soundButton;
	private TextView soundTitle;
	
	public SoundBuilder(NoteEntry entry, NoteActivity noteContext) {
		this.noteActivity = noteContext;
		this.entry = entry;
		if(this.entry.getFilePath() != null)
			this.createSoundButton();
		else
			this.entry.setType(NoteEntry.NoteType.AUDIO);
	}
	
	public boolean startRecording() { // on actionView REC button press
		if (noteActivity.recorder == null) {
			noteActivity.recorder = new MediaRecorder();
			noteActivity.recorder.setMaxDuration(15000000); //~ 4hrs
		}
		
		if(status.equals(Status.RECORDING)) {
			//exit here because we're already doing it.
			return false;
		}

		try {
			noteActivity.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			noteActivity.recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			//soundFilePath = context.getExternalFilesDir(null) + tempStorage +
			//		new SimpleDateFormat("MM-dd-yyyy_KK-mm-ss-a", Locale.getDefault()).format(new Date()) + ".3gp";
			soundFilePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/SB_Audio_" + new SimpleDateFormat("MM-dd-yyyy_KK-mm-ss-a", Locale.getDefault()).format(new Date())
					+ ".3gp";
			noteActivity.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			noteActivity.recorder.setOutputFile(soundFilePath);
			noteActivity.recorder.prepare();
			noteActivity.recorder.start();
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
			if (noteActivity.recorder != null) {
				try {
					noteActivity.recorder.stop();
				} catch (IllegalStateException e) {
					Log.e(LOG_TAG, "Failed to stop recorder");
					e.printStackTrace();
				}
				noteActivity.recorder.release();
			}
			noteActivity.recorder = null;
			status = Status.PAUSED;
			this.entry.setFilePath(this.soundFilePath); //Add the file to the entry so it can be saved later.
			
			createSoundButton();
			
			return true;
		}
		return false;
	}
	
	protected void createSoundButton(){
		// Creating dynamic container (linearlayout) to hold imagebutton and title
		soundButtonAndTitle = new LinearLayout(noteActivity);
		LayoutParams llParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		soundButtonAndTitle.setLayoutParams(llParams);		
		soundButton = new ImageButton(noteActivity);
		soundButton.setImageResource(R.drawable.ic_action_volume_on);
		soundButton.setClickable(false);
		
		//TextView displays name of sound
		soundTitle = new TextView(noteActivity);
		soundTitle.setText("Sound");
		soundTitle.setClickable(false);
		
		//Generate IDs, one for deletion and the other for renaming
		int viewID = noteActivity.generateViewID();
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
		
		if(entry.getX() != 0) {
			soundButtonAndTitle.setX(entry.getX());
			soundButtonAndTitle.setY(entry.getY());
		}
		
		ViewGroup layout = (ViewGroup)noteActivity.findViewById(R.id.note_layout);
		layout.addView(soundButtonAndTitle);
		
		soundButtonAndTitle.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback soundMenu = new SoundPlayMenu(noteActivity, entry);
				noteActivity.startActionMode(soundMenu);
				return true;
			}
		});
		
		try {
			this.noteActivity.getHelper().getNoteEntryDao().createOrUpdate(entry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Status getStatus() {
		return status;
	}
	
	public enum Status {
		RECORDING, PLAYING, PAUSED;
	}
}
