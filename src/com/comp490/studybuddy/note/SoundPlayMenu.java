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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;


/*
 * This is called when you long click on a sound button!
 */
public class SoundPlayMenu implements ActionMode.Callback {
	private NoteActivity noteActivity;
	MediaPlayer player = null;
	NoteEntryModel entry;
	private Status status = Status.PAUSED;
	private static final String LOG_TAG = "Sound Action Menu Callback";
	final String path;
	
	public SoundPlayMenu(NoteActivity noteActivity, NoteEntryModel entry) {
		this.noteActivity = noteActivity;
		this.entry = entry;
		path = entry.getFilePath();
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
			//this.audioState(Status.PLAYING);
			playSound();
			return true;
		}
		case R.id.menuSoundPause: {
			pauseSound();
			//this.audioState(Status.PAUSED);
			return true;
		}
		case R.id.menuSoundStop: {
			stopPlayback();
			//this.audioState(Status.PAUSED); //again, stop necessary?
			return true;
		}
		case R.id.menuUnlockView: {
			this.noteActivity.clickie("TODO: be able to move view");
			item.setIcon(R.drawable.ic_action_secure);
			return true;
		}
		case R.id.menuSoundDelete: {
			//this.audioState(Status.PAUSED);
			if (player != null){
				stopPlayback(); //if ran to completion, would have already stopped
			}
			if (deleteAudio()){
				noteActivity.getNoteModel().remove(entry); //remove entry from arraylist				
			}
			else {
				Log.e(LOG_TAG, "Delete of soundbutton failed");
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
				editName.setText(((TextView) soundTitle).getText());
				editName.selectAll();
			} catch (Exception e) {
				Log.e(LOG_TAG, "Sound title textview failed to open");
				e.printStackTrace();
			}
			new AlertDialog.Builder(noteActivity)
					.setTitle("Rename Sound Title")
					.setView(editName)
					.setPositiveButton("Rename",
							new DialogInterface.OnClickListener() {
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

	public boolean deleteAudio() {
//		String audioName = ((TextView) findViewById(currentSoundTitleID)).getText().toString();
//		AudioNote audioNote = null;
//		for(AudioNote note : this.audioNotes) {
//			if(note.getName().equals(audioName))
//				audioNote = note;
//		}
//		if(audioNote == null)
//			return false;

		
		// Popup Confirmation dialogbox for deletion
		
		AlertDialog.Builder builder = new AlertDialog.Builder(noteActivity);
		builder.setMessage("Delete Sound?");
		builder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try { // clicked yes
							View viewToDelete = noteActivity.findViewById(entry.getViewID());
							((LinearLayout) viewToDelete.getParent()).removeView(viewToDelete);
						} catch (Exception e1) {
							Log.e(LOG_TAG, "Delete of soundbutton failed");
						}
						try {
							File file = new File(path);
							boolean deleted = file.delete();
							if (deleted) {
								Toast.makeText(noteActivity,
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
	
	
	public void playSound() {
		try {
			if (player != null && status.equals(Status.PAUSED)) {
				Toast.makeText(noteActivity, "Playing Sound", Toast.LENGTH_SHORT).show();
				player.start();
				status = Status.PLAYING;
			} else if (!status.equals(Status.PLAYING)) {
				player = new MediaPlayer();
				player.setDataSource(path);
				noteActivity.getPlayers().add(player);
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
			Toast.makeText(noteActivity, "Stopped Playback", Toast.LENGTH_SHORT).show();
			status = Status.PAUSED; //do we really need a full stop?
			noteActivity.getPlayers().remove(player);
			player.stop();
			player.release();
			player = null;
		}
	}
	
//	public void audioState(Status change) {
////		String audioName = ((TextView) findViewById(currentSoundTitleID)).getText().toString();
////		AudioNote audioNote = null;
////		for(AudioNote note : this.audioNotes) {
////			if(note.getName().equals(audioName))
////				audioNote = note;
////		}
////		if(audioNote == null)
////			return;
//		switch(change) {
//		case PLAYING:
//			//audioNote.playSound();
//			playSound();
//			break;
//		case PAUSED:
//			//audioNote.pauseSound();
//			pauseSound();
//			break;
//		default:
//			break;
//		}	
//	}
//	
	public Status getStatus() {
		return status;
	}
	
	public enum Status {
		RECORDING, PLAYING, PAUSED;
	}

	// Called when the user exits the action mode (check mark)
	@Override
	public void onDestroyActionMode(ActionMode mode) {}

}
