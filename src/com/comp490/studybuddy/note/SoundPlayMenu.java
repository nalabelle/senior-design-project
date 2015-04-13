package com.comp490.studybuddy.note;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;


/*
 * This is called when you long click on a sound button!
 */
public class SoundPlayMenu implements ActionMode.Callback {
	private NoteActivity noteActivity;
	private NoteEntry entry;
	private Status status = Status.PAUSED;
	private static final String LOG_TAG = "Sound Action Menu Callback";
	final String path;
	LinearLayout soundView;
	
	public SoundPlayMenu(NoteActivity noteActivity, NoteEntry entry) {
		this.noteActivity = noteActivity;
		this.entry = entry;		
		path = entry.getFilePath();
		soundView = (LinearLayout) noteActivity.findViewById(entry.getViewID());
	}

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
			UpdateView.move(soundView, noteActivity);
			noteActivity.clickie("Sound Icon moveable.");
			return true;
		}
		case R.id.menuSoundDelete: {
			if (noteActivity.player != null){
				stopPlayback(); //if ran to completion, would have already stopped
			}
			
			UpdateView.deleteView(entry, noteActivity, soundView);
			entry = null;
			
			try { //attempt to delete file on device
				File file = new File(path);
				file.delete();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mode.finish(); // close the contextual menu
			return true;
		}
	 	case R.id.menuSoundRename: {
			// Pop up dialog to rename sound file
			final EditText editName = new EditText(noteActivity);
			final TextView soundTitle;
			soundTitle = (TextView) noteActivity.findViewById(entry.getSecondaryViewID());
			try {
				editName.setText(soundTitle.getText());
				editName.selectAll();
			} catch (Exception e) {
				Log.e(LOG_TAG, "Sound title textview failed to open");
				e.printStackTrace();
			}
			new AlertDialog.Builder(noteActivity)
					.setTitle("Rename sound title")
					.setIcon(android.R.drawable.ic_menu_help)
					.setView(editName)
					.setPositiveButton("Rename",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									try {
										String newName = editName.getText().toString();
										soundTitle.setText(newName);
										entry.setName(newName);
									} catch (Exception e) {
										Log.e(LOG_TAG, "Sound title textview failed to rename");
										e.printStackTrace();
									}
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).show();
			return true;
		}
		default:
			return false;
		}
	}
	
	public void playSound() {
		try {			
			if (noteActivity.player != null && status.equals(Status.PAUSED) && noteActivity.player.isPlaying()){
				// special case of loading a soundplaymenu (clicking on a sound 
				// icon), while playing something already. So we kill the player,
				// and start fresh.
				stopPlayback();
			}
			
			if (noteActivity.player != null && status.equals(Status.PAUSED)) { //resumes from pause
				Toast.makeText(noteActivity, "Playing Sound", Toast.LENGTH_SHORT).show();
				noteActivity.player.start();
				status = Status.PLAYING;				
			} else if (!status.equals(Status.PLAYING)) { 
				noteActivity.player = new MediaPlayer();
				noteActivity.player.setDataSource(path);
				noteActivity.player.prepare();
				noteActivity.player.start();
				noteActivity.player.setOnCompletionListener(new OnCompletionListener() {
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
			noteActivity.player.pause();
			status = Status.PAUSED;
		}
	}

	private void stopPlayback() {
		if (noteActivity.player != null && (status.equals(Status.PLAYING) || status.equals(Status.PAUSED))) {
			Toast.makeText(noteActivity, "Stopped Playback", Toast.LENGTH_SHORT).show();
			status = Status.PAUSED; //do we really need a full stop?
			noteActivity.player.stop();
			noteActivity.player.release();
			noteActivity.player = null;
		}
	}
	
	public Status getStatus() {
		return status;
	}
	
	public enum Status {
		RECORDING, PLAYING, PAUSED;
	}

	// Called when the user exits the action mode (check mark)
	@Override
	public void onDestroyActionMode(ActionMode mode) {
	// Note: Intentionally not stopping playback (you can listen to a recording and do other SB stuff)
		if (entry != null){
			soundView.setOnTouchListener(null);
			UpdateView.setXY(entry, noteActivity, soundView);
		}
	}
}
