package com.comp490.studybuddy.note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.Log;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comp490.studybuddy.R;

//Text Contextual Action Mode
public class TextMenu implements ActionMode.Callback {
	private NoteActivity noteActivity;
	private TextBuilder textBuilder;
	private static final String LOG_TAG = "Text Action Menu Callback";
	private int xDelta;
	private int yDelta;
	TextView text;
	OnTouchListener listen = null;
	
	public TextMenu(NoteActivity noteActivity, TextBuilder textBuilder) {
		this.noteActivity = noteActivity;
		this.textBuilder = textBuilder;
		text = (TextView) noteActivity.findViewById(textBuilder.getID());
	}
	
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.note_text, menu);
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
			allowViewMovement();
			noteActivity.clickie("Text unlocked and moveable.");
			return true;
		}
		case R.id.menuDeleteView: {
			AlertDialog.Builder builder = new AlertDialog.Builder(noteActivity);
			builder.setMessage("Delete Item?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							try { // clicked yes
								// delete view, the entry and all corresponding objects/references
								((ViewGroup) text.getParent()).removeView(text);
								textBuilder.deleteObject();
								textBuilder = null;
							} catch (Exception e1) {
								Log.e(LOG_TAG, "Delete of EditText failed");
							}							
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
			mode.finish(); // may need to move this inside dialog?
			return true;
		}
		default:
			return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		if (listen != null){
			text.setOnTouchListener(null);
			noteActivity.clickie("Text Position Locked.");
			//TO DO: need to update location X, Y in entry
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

			          //required to prevent squishing of view in right and bottom
			            layoutParams.bottomMargin = -250;
			            layoutParams.rightMargin = -250; 
			            
			            view.setLayoutParams(layoutParams);
			            break;
			    }
			    parent.invalidate();
			    return true;
		}};
		
		text.setOnTouchListener(listen);
	}
}
