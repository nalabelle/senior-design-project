package com.comp490.studybuddy.note;

import android.content.Intent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;

/* Submenu created onLongClick of a photothumbnail. As of now, options include
 * deleting the thumbnail or inspecting the photo with PhotoViewerActivity.  
 */

public class PictureMenu implements ActionMode.Callback {

	// Picture Contextual Action Mode
	private NoteActivity noteActivity;
	private PictureBuilder picObject;
	private NoteEntry entry;
	//private static final String LOG_TAG = "Picture Action Menu Callback";
	ImageView pic;

	public PictureMenu(NoteActivity noteActivity, PictureBuilder picBuilder) {
		this.noteActivity = noteActivity;
		this.picObject = picBuilder;
		entry = picBuilder.getEntry();
		pic = (ImageView) noteActivity.findViewById(entry.getViewID());
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.note_picture, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.menuInspectPhoto: {
			Intent intent = new Intent(noteActivity.getApplicationContext(), PhotoViewerActivity.class);
			intent.putExtra("path", picObject.getFilePath());
			noteActivity.startActivity(intent);
			return true;
		}
		case R.id.menuUnlockView: {
			UpdateView.move(pic, noteActivity);
			noteActivity.clickie("Picture position unLocked.");
			return true;
		}
		case R.id.menuDeleteView: {
			UpdateView.deleteView(entry, noteActivity, pic);
			mode.finish();
			picObject = null;
			return true;
		}
		default:
			return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		if (picObject != null){
			pic.setOnTouchListener(null);
			UpdateView.setXY(entry, noteActivity, pic);
		}
	}
}
