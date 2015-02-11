/**
 * Author: Wesley Liang
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen && Wesley Liang
 */

/*
 * Code is largely adapted from the google developer training tutorial found at:
 * http://developer.android.com/training/animation/index.html
 * This project is open source, developed in adherence to the guideline found here:
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 */

package com.comp490.studybuddy.flashcards;

import com.comp490.studybuddy.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FlashMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flash_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
