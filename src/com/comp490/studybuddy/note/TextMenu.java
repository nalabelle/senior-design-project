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

//Text Contextual Action Mode
public class TextMenu implements ActionMode.Callback {
	private NoteActivity noteActivity;
	private TextBuilder textBuilder;
	private static final String LOG_TAG = "Text Action Menu Callback";
	
	public TextMenu(NoteActivity noteActivity, TextBuilder textBuilder) {
		this.noteActivity = noteActivity;
		this.textBuilder = textBuilder;
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
						@Override
						public void onClick(DialogInterface dialog, int id) {
							try { // clicked yes
								// delete view, the entry and all corresponding objects/references
								View viewToDelete = noteActivity.findViewById(textBuilder
										.getID());
								((LinearLayout) viewToDelete.getParent())
										.removeView(viewToDelete);
								textBuilder.deleteObject();
								textBuilder = null;
							} catch (Exception e1) {
								Log.e(LOG_TAG, "Delete of EditText failed");
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
			mode.finish(); // may need to move this inside dialog?
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
