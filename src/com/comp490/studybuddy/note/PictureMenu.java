package com.comp490.studybuddy.note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.comp490.studybuddy.R;

/* Submenu created onLongClick of a photothumbnail. As of now, options include
 * deleting the thumbnail or inspecting the photo with PhotoViewerActivity.  
 */

public class PictureMenu implements ActionMode.Callback {

	// Picture Contextual Action Mode
	private NoteActivity noteActivity;
	private PictureBuilder picObject;
	private static final String LOG_TAG = "Picture Action Menu Callback";
	private int xDelta;
	private int yDelta;
	ImageView pic;
	OnTouchListener listen = null;

	public PictureMenu(NoteActivity noteActivity, PictureBuilder picObject) {
		this.noteActivity = noteActivity;
		this.picObject = picObject;
		pic = (ImageView) noteActivity.findViewById(picObject.getID());
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.note_picture, menu);
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
		
		case R.id.menuInspectPhoto: {
			Intent intent = new Intent(noteActivity.getApplicationContext(), PhotoViewerActivity.class);
			intent.putExtra("path", picObject.getFilePath());
			noteActivity.startActivity(intent);
			return true;
		}
		case R.id.menuUnlockView: {
			allowViewMovement();
			noteActivity.clickie("Picture position unLocked.");
			return true;
		}
		case R.id.menuDeleteView: {
			AlertDialog.Builder builder = new AlertDialog.Builder(noteActivity);
         builder.setIcon(android.R.drawable.ic_dialog_alert);
         builder.setTitle("Delete Item?");
		 builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							try { // clicked yes
								((ViewGroup) pic.getParent()).removeView(pic);
								picObject.deleteObject();
								picObject = null;
							} catch (Exception e1) {
								Log.e(LOG_TAG, "Delete of pic failed");
							}
						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
			mode.finish();
			return true;
		}
		default:
			return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		if (listen != null){
			pic.setOnTouchListener(null);
			noteActivity.clickie("Picture position locked.");
			picObject.setXY();
		}
	}
	
	public void allowViewMovement(){
		
		final ViewGroup parent = (ViewGroup) noteActivity.findViewById(R.id.note_layout);

		listen = new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent event) {
			    final int X = (int) event.getRawX();
			    final int Y = (int) event.getRawY();

			    switch (event.getAction() & MotionEvent.ACTION_MASK) {
			        case MotionEvent.ACTION_DOWN:
			            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			            xDelta = X - lParams.leftMargin;
			            yDelta = Y - lParams.topMargin;
			            break;
			        case MotionEvent.ACTION_UP:
			            break;
			        case MotionEvent.ACTION_POINTER_DOWN:
			            break;
			        case MotionEvent.ACTION_POINTER_UP:
			            break;
			        case MotionEvent.ACTION_MOVE:
			            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			            
			            if (X - xDelta < 0)
			            	layoutParams.leftMargin = 0;
			            else 
			            	layoutParams.leftMargin = X - xDelta;
			            if (Y - yDelta < 0)
			            	layoutParams.topMargin = 0;
			            else
			            	layoutParams.topMargin = Y - yDelta;
			            
			            layoutParams.bottomMargin = -250;
			            layoutParams.rightMargin = -250; 

			            view.setLayoutParams(layoutParams);
			            break;
			    }
			    parent.invalidate();
			    return true;
		}};
		
		pic.setOnTouchListener(listen);
	}
}
