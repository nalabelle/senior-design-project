package com.comp490.studybuddy.note;

import java.sql.SQLException;

import android.text.InputType;
import android.view.ActionMode;
import android.view.View;
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
	private int viewID;
	
	public TextBuilder(NoteActivity noteActivity, NoteEntry entry){
		this.noteActivity = noteActivity;
		this.entry = entry;
		createTextView();
		this.entry.setType(NoteEntry.NoteType.TEXT);
		try {
			this.noteActivity.getHelper().getNoteEntryDao().createOrUpdate(entry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createTextView(){
		textBox = new EditText(noteActivity);
		textBox.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		textBox.setHint("Enter Text");
		textBox.requestFocus();
		textBox.setClickable(true);
		viewID = (noteActivity.generateViewID());
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
	}
	
	public int getID(){
		return viewID;
	}
	
	// might be unnecessary, but probably beneficial for garbage collection
	protected void deleteObject(){
		try {
			noteActivity.getHelper().getNoteEntryDao().delete(entry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textBox = null;
		textBuilder = null;
	}
}
