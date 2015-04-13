package com.comp490.studybuddy.note;

import java.sql.SQLException;

import android.text.InputType;
import android.view.ActionMode;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;

public class TextBuilder{
	protected EditText textBox;
	private NoteActivity noteActivity;
	private TextBuilder textBuilder = this;
	private NoteEntry entry;
	
	public TextBuilder(NoteActivity noteActivity, NoteEntry entry){
		this.noteActivity = noteActivity;
		this.entry = entry;
		createTextView();
	}
	
	private void createTextView(){
		textBox = new EditText(noteActivity);
		textBox.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		if(entry.getText() == null) {
			textBox.setHint("Enter Text");
		} else {
			textBox.setText(entry.getText());
		}
		if(entry.getX() != 0) {
			textBox.setX(entry.getX());
			textBox.setY(entry.getY());
		}
		textBox.requestFocus();
		textBox.setClickable(true);
		int viewID = (noteActivity.generateViewID());
		textBox.setId(viewID); //required for deletion
		entry.setViewID(viewID);
		textBox.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		ViewGroup layout = (ViewGroup) noteActivity.findViewById(R.id.note_layout);
		layout.addView(textBox);

		textBox.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback textMenu = new TextMenu(noteActivity, textBuilder);
				noteActivity.startActionMode(textMenu);
				return true;
			}
		});
		textBox.setOnFocusChangeListener(new OnFocusChangeListener() {

			//This saves the note when you move focus away from it.
			//TO DO: not saving if you press back to close the softkeyboard and back to exit
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				//I think this cast should be safe.
				EditText e = (EditText) v;
				if(!hasFocus) {
					entry.setText(e.getText().toString());
					try {
						noteActivity.getHelper().getNoteEntryDao().update(entry);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		});
				
		try {
			this.noteActivity.getHelper().getNoteEntryDao().createOrUpdate(entry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.noteActivity.addNote(entry);
	}
	
	protected NoteEntry getEntry(){
		return entry;
	}
}
