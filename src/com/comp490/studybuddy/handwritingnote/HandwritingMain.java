//Author: Tom, Xintong
package com.comp490.studybuddy.handwritingnote;

import android.os.Bundle;
import android.app.Activity;

public class HandwritingMain extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HandwritingNote view=new HandwritingNote(this);
		setContentView(view);
		addContentView(view.btnEraseAll, view.params);
	}
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}