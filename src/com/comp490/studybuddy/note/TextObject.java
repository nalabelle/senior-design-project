package com.comp490.studybuddy.note;

import android.text.InputType;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;

public class TextObject{
	protected EditText textBox;
	private NoteActivity textNote;
	private TextObject textObject = this;
	private NoteEntryModel entry;	
	
	
	public TextObject(NoteActivity textNote, NoteEntryModel note){
		this.textNote = textNote;
		this.entry = note;
		createTextView();
	}
	
	private void createTextView(){
		textBox = new EditText(textNote);
		textBox.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		textBox.setHint("Enter Text");
		textBox.requestFocus();
		textBox.setId(textNote.generateViewID()); //required for deletion
		textBox.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		LinearLayout layout = (LinearLayout) textNote.findViewById(R.id.note_inner_layout);
		layout.addView(textBox);

		textBox.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback textMenu = new TextMenu(textNote, textObject);
				textNote.startActionMode(textMenu);
				return true;
			}
		});
	}
	
	public int getID(){
		return textBox.getId();
	}
	
	// might be unnecessary
	protected void deleteObject(){
		textBox = null;
		textObject = null;
	}
}
