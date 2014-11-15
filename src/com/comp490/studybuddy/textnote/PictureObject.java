package com.comp490.studybuddy.textnote;

import android.graphics.Bitmap;
import android.view.ActionMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.comp490.studybuddy.R;

public class PictureObject {
	protected ImageView pic;
	private TextNote textNote;
	private PictureObject picObject = this;
	private Bitmap bitmap;
	
	public PictureObject(TextNote textNote, Bitmap bitmap){
		this.textNote = textNote;
		this.bitmap = bitmap;
		createPicView();
	}	
	
	private void createPicView(){

		Toast.makeText(textNote, "HELP", Toast.LENGTH_LONG).show();

		pic = new ImageView(textNote);
		pic.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		LinearLayout layout = (LinearLayout) textNote.findViewById(R.id.note_inner_layout);
		pic.setId(textNote.generateViewID()); //required for deletion
		layout.addView(pic);

		pic.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback picMenu = new PictureMenu(textNote, picObject);
				textNote.startActionMode(picMenu);
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
