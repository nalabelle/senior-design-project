package com.comp490.studybuddy.textnote;

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
	private TextNote textNote;
	private TextObject textObject;
	private static final String LOG_TAG = "Text Action Menu Callback";
	
	public TextMenu(TextNote textNote, TextObject textObject) {
		this.textNote = textNote;
		this.textObject = textObject;
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
				try {
					View viewToDelete = textNote.findViewById(textObject.getID());
					((LinearLayout) viewToDelete.getParent()).removeView(viewToDelete);
					textObject.deleteObject();
					textObject = null;
				} catch (Exception e) {
					Log.e(LOG_TAG, "Delete of view failed");
					e.printStackTrace();
				}			
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
