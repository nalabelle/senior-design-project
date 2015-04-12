package com.comp490.studybuddy.note;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;

public class DrawMenu implements ActionMode.Callback {
	private NoteActivity noteActivity;
	private NoteEntry noteEntry;
	private Drawing drawing;
	private RelativeLayout noteLayout;
	//private static final String LOG_TAG = "DrawMenu Action Menu Callback";
	
	public DrawMenu(NoteActivity noteActivity, NoteEntry noteEntry) {
		this.noteActivity = noteActivity;
		this.noteEntry = noteEntry;
		
		try {
			noteActivity.getHelper().getNoteEntryDao().createOrUpdate(noteEntry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create a temporary transparent view to cover everything else
		noteLayout = (RelativeLayout) noteActivity.findViewById(R.id.note_layout);
		if (noteEntry.getDrawPath() != null){
			setBitmap(true);
		}
		drawing = new Drawing(noteActivity, noteLayout.getWidth(), noteLayout.getHeight(), noteEntry.getDrawPath());
		noteLayout.addView(drawing);	
	}
	
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.note_draw, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_penWidth: {
			drawing.setPenWidth(noteActivity);
			return true;
		}
		case R.id.action_penColor: {
			drawing.setPenColor(noteActivity);			
			return true;
		}
		case R.id.action_eraser: {
			drawing.eraser();			
			return true;
		}
		case R.id.action_undo: {
			drawing.undo();
			return true;
		}
		case R.id.action_redo: {
			drawing.redo();
			return true;
		}
		case R.id.action_clearAll: {			
			AlertDialog.Builder builder = new AlertDialog.Builder(noteActivity);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Clear drawing?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							noteLayout.removeView(drawing);
							noteEntry.setDrawPath(null);
							drawing = new Drawing(noteActivity, noteLayout.getWidth(), noteLayout.getHeight(), null);
							noteLayout.addView(drawing);
							setBitmap(true); //true = clear background
							noteActivity.clickie("Drawing Removed");
							dialog.dismiss();
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
			return true;
		}
		case R.id.action_save: {
			drawing.saveFile(); //not sure if still working
			return true;
		}
		default:
			return false;
		}
	}

	// On closure of menu
	@Override
	public void onDestroyActionMode(ActionMode mode) {
		noteActivity.clickie("Drawing Closed");
		setBitmap(false); 
		noteLayout.removeView(drawing);
		noteEntry.setDrawPath(drawing.getSavePath());
		try {
			noteActivity.getHelper().getNoteEntryDao().update(noteEntry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void setBitmap(boolean clear) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			// for API < 16
			if (clear) {
				noteLayout.setBackgroundDrawable(null); //clear screen
			} else {
				noteLayout.setBackgroundDrawable(new BitmapDrawable(noteActivity
						.getResources(), drawing.bitmap));
			}
		} else {// for the rest
			if (clear) {
				noteLayout.setBackground(null); //clear screen
			} else {
				noteLayout.setBackground(new BitmapDrawable(noteActivity
						.getResources(), drawing.bitmap));
			}
		}
	}
}