package com.comp490.studybuddy.note;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.RelativeLayout;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;

public class DrawingLoader {
	private NoteActivity noteActivity;
	private NoteEntry noteEntry;
	private Drawing drawing;
	private RelativeLayout noteLayout;
	
	public DrawingLoader(NoteActivity noteActivity, NoteEntry noteEntry) {
		this.noteActivity = noteActivity;
		this.noteEntry = noteEntry;
		createDrawView();
	}
	
	private void createDrawView(){
		try {
			noteActivity.getHelper().getNoteEntryDao().createOrUpdate(noteEntry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create a temporary transparent view to cover everything else
		noteLayout = (RelativeLayout) noteActivity.findViewById(R.id.note_layout);
		drawing = new Drawing(noteActivity, noteLayout.getWidth(), noteLayout.getHeight(), this.noteEntry.getDrawPath());
		noteLayout.addView(drawing);
		noteActivity.clickie("Drawing Loaded");
		//convert drawing to bitmap and set as background
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
	
	// Setting background as bitmap requires different methods depending on API
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