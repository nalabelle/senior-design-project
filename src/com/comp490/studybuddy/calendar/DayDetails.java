package com.comp490.studybuddy.calendar;

import com.comp490.studybuddy.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DayDetails extends Activity {
	
	private TextView dayDetailText;
	private Intent prevIntent;
	private String day_mon_yr;
	
	private ActionBar actionBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_day_detail);
	    
	    prevIntent = getIntent();
	    day_mon_yr = prevIntent.getStringExtra("Day");
	    
	    dayDetailText = (TextView) this.findViewById(R.id.textView1);
	    
	    dayDetailText.setText(day_mon_yr);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		actionBar = getActionBar();
	    actionBar.show();
		getMenuInflater().inflate(R.menu.calen, menu);
		menu.findItem(R.id.createEvent).setVisible(false);
		menu.findItem(R.id.editEvent).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id) {
			case R.id.action_settings:
				return true;
			case R.id.addEvent:
				Intent intent = new Intent(getApplicationContext(), AddEvent.class);
			    startActivity(intent);
			    return true;
			default:
				return super.onOptionsItemSelected(item);
		}		
	}

}
