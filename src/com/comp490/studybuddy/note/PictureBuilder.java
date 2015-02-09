package com.comp490.studybuddy.note;

import android.graphics.Bitmap;
import android.view.ActionMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;

public class PictureBuilder {
	protected ImageView pic;
	private NoteActivity noteActivity; //essentially context of Note activity
	private PictureBuilder pictureBuilder = this;
	private Bitmap bitmap;
	private NoteEntryModel entry;
	private int viewID;
	
	public PictureBuilder(NoteActivity noteActivity, Bitmap bitmap, NoteEntryModel entry){
		this.entry = entry;
		this.noteActivity = noteActivity;
		this.bitmap = bitmap;
		this.entry.setType(NoteEntryModel.NoteType.PICTURE);
		createPicView();		
	}	
	
	private void createPicView(){

		pic = new ImageView(noteActivity);
		pic.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		LinearLayout layout = (LinearLayout) noteActivity.findViewById(R.id.note_inner_layout);
		viewID = noteActivity.generateViewID();
		pic.setId(viewID); //required for deletion
		entry.setViewID(viewID);
		layout.addView(pic);

		pic.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback picMenu = new PictureMenu(noteActivity, pictureBuilder);
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
		noteActivity.getNoteModel().remove(entry);
		pic = null;
		pictureBuilder = null;
	}

}
