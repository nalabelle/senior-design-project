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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;


/*
 * This is called when you long click on a sound button!
 */
public class SoundPlayMenu implements ActionMode.Callback {
	private NoteActivity noteActivity;
	NoteEntry entry;
	private Status status = Status.PAUSED;
	private static final String LOG_TAG = "Sound Action Menu Callback";
	final String path;
	private int xDelta;
	private int yDelta;
	LinearLayout soundView;
	OnTouchListener listen = null;
	
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
			allowViewMovement();
			noteActivity.clickie("Sound Icon moveable.");
			//item.setIcon(R.drawable.ic_action_secure);
			return true;
		}
		case R.id.menuSoundDelete: {
			//this.audioState(Status.PAUSED);
			if (noteActivity.player != null){
				stopPlayback(); //if ran to completion, would have already stopped
			}
			if (!deleteAudio()){
				Log.e(LOG_TAG, "....Delete of sound button failed");
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
					.setTitle("Rename Sound Title")
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

	public boolean deleteAudio() {

		// Popup Confirmation dialogbox for deletion
		
		AlertDialog.Builder builder = new AlertDialog.Builder(noteActivity);
		builder.setMessage("Delete Sound?");
		builder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						try { // clicked yes
							((ViewGroup) soundView.getParent()).removeView(soundView);
							noteActivity.getHelper().getNoteEntryDao().delete(entry);
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
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
		return true;
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
		if (listen != null){
			soundView.setOnTouchListener(null);
			noteActivity.clickie("Sound Icon position locked.");
			//TO DO: need to update location X, Y in entry
		}
	}
	
	public void allowViewMovement(){
		
		final ViewGroup parent = (ViewGroup) noteActivity.findViewById(R.id.note_layout);

		listen = new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent event) {
			    final int X = (int) event.getRawX();
			    final int Y = (int) event.getRawY();

			    switch (event.getAction() & MotionEvent.ACTION_MASK) {
			        case MotionEvent.ACTION_DOWN:
			            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			            xDelta = X - lParams.leftMargin;
			            yDelta = Y - lParams.topMargin;
			            break;
			        case MotionEvent.ACTION_UP:
			            break;
			        case MotionEvent.ACTION_POINTER_DOWN:
			            break;
			        case MotionEvent.ACTION_POINTER_UP:
			            break;
			        case MotionEvent.ACTION_MOVE:
			            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			            
			            if (X - xDelta < 0)
			            	layoutParams.leftMargin = 0;
			            else 
			            	layoutParams.leftMargin = X - xDelta;
			            if (Y - yDelta < 0)
			            	layoutParams.topMargin = 0;
			            else
			            	layoutParams.topMargin = Y - yDelta;
			            
			            layoutParams.bottomMargin = -250;
			            layoutParams.rightMargin = -250; 

			            view.setLayoutParams(layoutParams);
			            break;
			    }
			    parent.invalidate();
			    return true;
		}};
		
		soundView.setOnTouchListener(listen);
	}
}
