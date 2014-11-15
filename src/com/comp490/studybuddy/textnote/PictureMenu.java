package com.comp490.studybuddy.textnote;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.comp490.studybuddy.R;

public class PictureMenu implements ActionMode.Callback {
	

	//Picture Contextual Action Mode
		private TextNote textNote;
		private PictureObject picObject;
		private static final String LOG_TAG = "Picture Action Menu Callback";
		
		public PictureMenu(TextNote textNote, PictureObject picObject) {
			this.textNote = textNote;
			this.picObject = picObject;
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
						View viewToDelete = textNote.findViewById(picObject.getID());
						((LinearLayout) viewToDelete.getParent()).removeView(viewToDelete);
						picObject.deleteObject();
						picObject = null;
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

