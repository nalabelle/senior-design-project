package com.comp490.studybuddy.note;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.comp490.studybuddy.R;

//Text Contextual Action Mode
public class TextMenu implements ActionMode.Callback {
	private NoteActivity noteActivity;
	private TextBuilder textBuilder;
	private TextView text;

	public TextMenu(NoteActivity noteActivity, TextBuilder textBuilder) {
		this.noteActivity = noteActivity;
		this.textBuilder = textBuilder;
		text = (TextView) noteActivity.findViewById(textBuilder.entry.getViewID());
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.note_text, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menuUnlockView: {
			EditView.move(text, noteActivity); //creates temp onTouch listener
			noteActivity.clickie("Text unlocked and moveable.");
			return true;
		}
		case R.id.menuDeleteView: {
			EditView.deleteView(textBuilder.entry, noteActivity, text);
			textBuilder = null;
			mode.finish(); 
			return true;
		}
		default:
			return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		if (textBuilder != null){
			text.setOnTouchListener(null);
			noteActivity.clickie("Text Position Locked.");
			EditView.setXY(textBuilder.entry, noteActivity, text);
			//textBuilder.setXY();
		}
	}
}
