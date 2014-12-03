package com.comp490.studybuddy.note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.comp490.studybuddy.R;

public class VideoMenu implements ActionMode.Callback {
	private NoteActivity noteActivity;
	private VideoBuilder videoBuilder;
	private static final String LOG_TAG = "Video Menu Callback";

	// Video Contextual Action Mode

	public VideoMenu(NoteActivity noteActivity, VideoBuilder videoBuilder) {
		this.noteActivity = noteActivity;
		this.videoBuilder = videoBuilder;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.note_edittext_pics, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.menuUnlockView: {
			return true;
		}
		case R.id.menuDeleteView: {
			AlertDialog.Builder builder = new AlertDialog.Builder(noteActivity);
			builder.setMessage("Delete Item?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							try { // clicked yes
								View viewToDelete = noteActivity
										.findViewById(videoBuilder.getID());
								((LinearLayout) viewToDelete.getParent())
										.removeView(viewToDelete);
								videoBuilder.deleteObject();
								videoBuilder = null;
							} catch (Exception e1) {
								Log.e(LOG_TAG, "Delete of VideoView failed");
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
			mode.finish();
			return true;
		}
		default:
			return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// TODO Auto-generated method stub
	}
}
