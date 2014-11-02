//Author: Xintong Shi (Summer)
/*package com.comp490.studybuddy.handwritingnote;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.comp490.studybuddy.R;

public class HandwritingMain extends Activity {

	//List Handwriting Notes
	private List <HandwritingNote> hwnotes;
	//Get GridView of Handwriting Notes
	private AdaptNotes adaptnotes;
	//Maximum number of handwriting notes can be created
	private static final int MAX_hwnotes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_handwriting_main);
		
		hwnotes = new ArrayList<HandwritingNote>();
		adaptnotes = new AdaptNotes(getApplicationContext(), R.layout.hwnote_item, R.id.filename, hwnotes);
		
		final GridView noteView = (GrideView) findViewById(R.id.hwnoteView);
		
		registerForContextView(hwnotesView);
		hwnotesView.setAdapter(AdaptNotes);
		hwnotesView.setOnItemClickerListener(
				new OnItemClickListener(){
					@Override
					public void onItemClick(
							final 
					
					
					
				}
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.handwriting_main, menu);
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
*/