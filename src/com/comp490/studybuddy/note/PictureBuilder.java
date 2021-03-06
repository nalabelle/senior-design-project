package com.comp490.studybuddy.note;

import java.io.File;
import java.sql.SQLException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.view.ActionMode;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;

/* Generates the View to house our newly created photos. Only a thumbnail
 * is displayed to avoid memory issues. OnClick spawns a menu for other
 * options including fullscreen display, and deletion (PictureMenu.java). * 
 */

public class PictureBuilder {
	protected ImageView pic;
	private NoteActivity noteActivity; //essentially context of Note activity
	private PictureBuilder pictureBuilder = this;
	private NoteEntry entry;
	private Bitmap bitmap;
	
	public PictureBuilder(NoteActivity noteActivity, NoteEntry entry){
		this.entry = entry;
		this.noteActivity = noteActivity;
		createPicView();
	}	
	
	private void createPicView(){

		//Create the view, ID, size, etc
		pic = new ImageView(noteActivity);
		pic.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		ViewGroup layout = (ViewGroup) noteActivity.findViewById(R.id.note_layout);
		int viewID = noteActivity.generateViewID();
		pic.setId(viewID); //required for deletion
		entry.setViewID(viewID);
		if(entry.getX() != 0) {
			pic.setX(entry.getX());
			pic.setY(entry.getY());
		}
		layout.addView(pic);
		
		//Find dimensions of display to determine size of thumbnail
		Display display = noteActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		//int height = size.y;
		
		int THUMBSIZE = width / 4; //change # to alter size of thumbnail (scales, X & Y)
		bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(entry.getFilePath()), THUMBSIZE, THUMBSIZE);
		
		//Fix b/c some devices rotate images when saving
		rotateBitmap();
		
		pic.setOnLongClickListener(new View.OnLongClickListener() {			
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback picMenu = new PictureMenu(noteActivity, pictureBuilder);
				noteActivity.startActionMode(picMenu);
				return true;
			}
		});
		
		pic.setImageBitmap(bitmap);
		
		
		try {
			this.noteActivity.getHelper().getNoteEntryDao().createOrUpdate(entry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public NoteEntry getEntry(){
		return entry;
	}
	
	public String getFilePath(){
		return entry.getFilePath();
	}
	
	//Correct thumbnail rotation due to some devices default rotation of image
	public void rotateBitmap() {
		int rotate = 0;
		try {
			File imageFile = new File(entry.getFilePath());
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Matrix matrix = new Matrix();
		matrix.postRotate(rotate);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}
}
