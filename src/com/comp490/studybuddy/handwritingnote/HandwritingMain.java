

//This is for summer's account testing
package com.comp490.studybuddy.handwritingnote;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.comp490.studybuddy.R;

public class HandwritingMain extends Activity {

	private HandwritingNote hNote = null;
	FrameLayout flNote;
	ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_handwriting_main);

		flNote = (FrameLayout) findViewById(R.id.flHandwriting);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.handwriting_main, menu);
	    actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false); //hide action bar title
		actionBar.setDisplayShowHomeEnabled(false); //hide action bar icon
	    return super.onCreateOptionsMenu(menu);
	 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_startDraw:
	            startDraw();
	            return true;
	        case R.id.action_undo:
	            undo();
	            return true;
	        case R.id.action_redo:
	            redo();
	            return true;
	        case R.id.action_clearAll:
	            clearAll();
	            return true;
	        case R.id.action_save:
	            save();
	            return true;
	        case R.id.action_penWidthThick:
	        	penWidth(10);
	        	return true;
	        case R.id.action_penWidthThin:
	        	penWidth(5);
	        	return true;
	        case R.id.action_penColorBlack:
	        	penColor(1);
	        	return true;
	        case R.id.action_penColorBlue:
	        	penColor(2);
	        	return true;
	        case R.id.action_penColorCyan:
	        	penColor(3);
	        	return true;
	        case R.id.action_penColorGreen:
	        	penColor(4);
	        	return true;
	        case R.id.action_penColorRed:
	        	penColor(5);
	        	return true;
	        case R.id.action_penColorYellow:
	        	penColor(6);
	        	return true;
	        case R.id.action_penColorWhite:
	        	penColor(7);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void startDraw() {
		hNote = new HandwritingNote(HandwritingMain.this, flNote.getWidth(), flNote.getHeight());
		flNote.removeAllViews();
		flNote.addView(hNote);
	}
	
	public void undo() {
		if (hNote == null)
			return;

		hNote.undo();
	}
	
	public void redo() {
	if (hNote == null)
		return;

		hNote.redo();
	}

	public void clearAll() {
		hNote = new HandwritingNote(HandwritingMain.this, flNote.getWidth(), flNote.getHeight());
		flNote.removeAllViews();
		flNote.addView(hNote);
	}

	public void save() {
		if (hNote == null)
			return;

		hNote.saveFile();
	}
	
	public void penWidth(int w){
		hNote.setPenWidth(w);
	}
	
	public void penColor(int c){
		hNote.setPenColor(c);
	}
}