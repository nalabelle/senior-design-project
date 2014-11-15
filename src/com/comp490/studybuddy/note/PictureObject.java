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

public class PictureObject {
	protected ImageView pic;
	private NoteActivity noteContext; //essentially context of Note activity
	private PictureObject picObject = this;
	private Bitmap bitmap;
	private NoteEntryModel entry;
	
	public PictureObject(NoteActivity noteContext, Bitmap bitmap, NoteEntryModel note){
		this.entry = note;
		this.noteContext = noteContext;
		this.bitmap = bitmap;
		createPicView();
	}	
	
	private void createPicView(){

		Toast.makeText(noteContext, "HELP", Toast.LENGTH_LONG).show();

		pic = new ImageView(noteContext);
		pic.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		LinearLayout layout = (LinearLayout) noteContext.findViewById(R.id.note_inner_layout);
		pic.setId(noteContext.generateViewID()); //required for deletion
		layout.addView(pic);

		pic.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback picMenu = new PictureMenu(noteContext, picObject);
				noteContext.startActionMode(picMenu);
				return true;
			}
		});
		
		pic.setImageBitmap(bitmap);
	}	
	
	public int getID(){
		return pic.getId();
	}
	
	// might be unnecessary
	protected void deleteObject(){
		pic = null;
		picObject = null;
	}

}
