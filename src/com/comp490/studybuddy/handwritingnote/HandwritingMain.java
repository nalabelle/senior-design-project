//Author: Tom, Xintong
package com.comp490.studybuddy.handwritingnote;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.comp490.studybuddy.R;

public class HandwritingMain extends Activity {

	private HandwritingNote hNote = null;

	FrameLayout flNote;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_handwriting_main);

		flNote = (FrameLayout) findViewById(R.id.flHandwriting);
	}

	public void clear(View v) {
		hNote = new HandwritingNote(HandwritingMain.this, flNote.getWidth(), flNote.getHeight());
		flNote.removeAllViews();
		flNote.addView(hNote);
	}

	public void save(View v) {
		if (hNote == null)
			return;

		hNote.saveFile();
	}

	public void undo(View v) {
		if (hNote == null)
			return;

		hNote.undo();
	}

	public void testButton(View v) {
		hNote = new HandwritingNote(HandwritingMain.this, flNote.getWidth(), flNote.getHeight());
		flNote.removeAllViews();
		flNote.addView(hNote);
	}
}