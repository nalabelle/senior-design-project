package com.comp490.studybuddy.textnote;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.textnote.AudioNote.Status;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


/*
 * This is called when you click on a sound button!
 */
public class SoundActionModeCallback implements ActionMode.Callback {
	private TextNote textNote;
	private static final String LOG_TAG = "Sound Action Menu Callback";
	
	public SoundActionModeCallback(TextNote textNote) {
		this.textNote = textNote;
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
			this.textNote.audioState(item.getItemId(), AudioNote.Status.PLAYING);
			return true;
		}
		case R.id.menuSoundPause: {
			this.textNote.audioState(item.getItemId(), Status.PAUSED);
			return true;
		}
		case R.id.menuSoundStop: {
			this.textNote.audioState(item.getItemId(), Status.PAUSED); //again, stop necessary?
			return true;
		}
		case R.id.menuUnlockView: {
			this.textNote.clickie("TODO: be able to move view");
			item.setIcon(R.drawable.ic_action_secure);
			return true;
		}
		case R.id.menuSoundDelete: {
			this.textNote.audioState(item.getItemId(), Status.PAUSED);
			if(this.textNote.deleteAudio())
				mode.finish(); // close the contextual menu
			return true;
		}
		/* this case should just rename the NoteEntry name and not the actual file.
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
					.setPositiveButton("Rename",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									try {
										soundTitle.setText(editName.getText()
												.toString());
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
		}*/
		default:
			return false;
		}
	}

	// Called when the user exits the action mode (check mark)
	@Override
	public void onDestroyActionMode(ActionMode mode) {
		//this.actionMode = null;
	}

}
