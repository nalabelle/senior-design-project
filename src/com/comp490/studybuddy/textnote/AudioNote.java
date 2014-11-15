package com.comp490.studybuddy.textnote;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.comp490.studybuddy.models.NoteEntryModel;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class AudioNote {
	private Context context;
	private NoteEntryModel entry;
	private MediaRecorder recorder = null;
	private MediaPlayer player = null;
	private Status status = Status.PAUSED;
	private static final String LOG_TAG = "Sound Record";
	private String tempStorage = "/Temp/Notes/Audio/";  //move to Notes/Audio after save
	private String soundFilePath;
	
	
	public AudioNote(Context context, NoteEntryModel note) {
		this.context = context;
		//create a new audio type entry.
		this.entry = note;
	}
	
	public boolean startRecording() { // on actionView REC button press
		if (recorder == null) {
			recorder = new MediaRecorder();
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
			Toast.makeText(this.context, "Stopped Recording", Toast.LENGTH_SHORT).show();
			recorder.stop();
			status = Status.PAUSED;
			this.entry.addFile(this.soundFilePath); //Add the file to the entry so it can be saved later.
			return true;
		}
		return false;
	}

	public void playSound() {
		try {
			if (status.equals(Status.PAUSED)) {
				Toast.makeText(this.context, "Playing Sound", Toast.LENGTH_SHORT).show();
				player.start();
				status = Status.PLAYING;
			} else if (!status.equals(Status.PLAYING)) {
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
				status = Status.PLAYING;
			} // else was already playing, do nothing
		} catch (Exception e) {
			Log.e(LOG_TAG, "playBack() broke");
		}
	}

	public void pauseSound() {
		if (status.equals(Status.PLAYING)) {
			player.pause();
			status = Status.PAUSED;
		}
	}

	private void stopPlayback() {
		if (status.equals(Status.PLAYING) || status.equals(Status.PAUSED)) {
			Toast.makeText(this.context, "Stopped Playback", Toast.LENGTH_SHORT).show();
			status = Status.PAUSED; //do we really need a full stop?
			player.stop();
			player.release();
		}
	}
	
	public Status getStatus() {
		return status;
	}
	
	public enum Status {
		RECORDING, PLAYING, PAUSED;
	}

	public void setName(String text) {
		this.entry.setName(text);
	}
	
	public String getName() {
		return this.entry.getName();
	}
	
	public String getFilePath() {
		return this.soundFilePath;
	}

	public void onDestroy() {
		if (recorder != null){
			recorder.release();
		}
		if (player != null){
			player.release();
		}
	}
}
