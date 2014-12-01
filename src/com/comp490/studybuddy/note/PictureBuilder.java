package com.comp490.studybuddy.note;

import android.graphics.Bitmap;
import android.view.ActionMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;
import com.comp490.studybuddy.models.NoteModel;

public class PictureBuilder {
	protected ImageView pic;
	private NoteActivity noteActivity; //essentially context of Note activity
	private PictureBuilder picObject = this;
	private Bitmap bitmap;
	private NoteModel note;
	private NoteEntryModel noteEntry;
	private int viewID;
	
	public PictureBuilder(NoteActivity noteContext, Bitmap bitmap, NoteEntryModel noteEntry){
		this.noteEntry = noteEntry;
		this.noteActivity = noteContext;
		this.bitmap = bitmap;
		createPicView();
		
	}	
	
	private void createPicView(){

		pic = new ImageView(noteActivity);
		pic.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		LinearLayout layout = (LinearLayout) noteActivity.findViewById(R.id.note_inner_layout);
		viewID = noteActivity.generateViewID();
		pic.setId(viewID); //required for deletion
		noteEntry.setViewID(viewID);
		layout.addView(pic);

		pic.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback picMenu = new PictureMenu(noteActivity, picObject);
				noteActivity.startActionMode(picMenu);
				return true;
			}
		});
		
		pic.setImageBitmap(bitmap);
		//noteEntry.setFilePath(pic.)
	}	
	
	public int getID(){
		return viewID;
	}
	
	// might be unnecessary
	protected void deleteObject(){
		pic = null;
		picObject = null;
	}

}
