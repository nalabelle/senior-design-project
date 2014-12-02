package com.comp490.studybuddy.note;

import android.text.InputType;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;

public class TextBuilder{
	protected EditText textBox;
	private NoteActivity noteActivity;
	private TextBuilder textBuilder = this;
	private NoteEntryModel entry;
	private int viewID;
	
	public TextBuilder(NoteActivity noteActivity, NoteEntryModel entry){
		this.noteActivity = noteActivity;
		this.entry = entry;
		createTextView();
	}
	
	private void createTextView(){
		textBox = new EditText(noteActivity);
		textBox.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		textBox.setHint("Enter Text");
		textBox.requestFocus();
		viewID = (noteActivity.generateViewID());
		textBox.setId(viewID); //required for deletion
		entry.setViewID(viewID);
		textBox.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		LinearLayout layout = (LinearLayout) noteActivity.findViewById(R.id.note_inner_layout);
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
		noteActivity.getNoteModel().remove(entry);
		textBox = null;
		textBuilder = null;
	}
}
