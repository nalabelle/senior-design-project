package com.comp490.studybuddy.note;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.ActionMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;

public class PictureBuilder {
	protected ImageView pic;
	private NoteActivity noteActivity; //essentially context of Note activity
	private PictureBuilder pictureBuilder = this;
	private NoteEntryModel entry;
	private int viewID;
	
	public PictureBuilder(NoteActivity noteActivity, NoteEntryModel entry){
		this.entry = entry;
		this.noteActivity = noteActivity;
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
		
		int THUMBSIZE = 75; 
		Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(entry.getFilePath()), THUMBSIZE, THUMBSIZE);

		pic.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback picMenu = new PictureMenu(noteActivity, pictureBuilder);
				noteActivity.startActionMode(picMenu);
				return true;
			}
		});
		
		pic.setImageBitmap(bitmap);
		//setPic();
		//noteEntry.setFilePath(pic.)
	}	
	
	public int getID(){
		return viewID;
	}
	
	public String getFilePath(){
		return entry.getFilePath();
	}
	
	// might be unnecessary
	protected void deleteObject(){
		noteActivity.getNoteModel().remove(entry);
		pic = null;
		pictureBuilder = null;
	}
	
	/*
	private void setPic() {
	    // Get the dimensions of the View
	    int targetW = pic.getWidth();
	    int targetH = pic.getHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(entry.getFilePath(), bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(entry.getFilePath(), bmOptions);
	    pic.setImageBitmap(bitmap);
	} */

}
