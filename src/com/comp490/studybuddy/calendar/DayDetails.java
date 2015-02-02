package com.comp490.studybuddy.calendar;

import com.comp490.studybuddy.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	    //Create a string to search for events on this day
	    String[] date = day_mon_yr.split("-");
	    String thisDay = "" + date[2] + "-" + monthStringToInt(date[0]) + "-" + date[1];
	    Log.d("TAG", thisDay);
	    //Get Events For the Day
	    
	    //Display the events
	}
	
	public int monthStringToInt(String month) {
		int monthInt = -1;
		switch(month) {
			case "January": monthInt = 1; break;
			case "February": monthInt = 2; break;
			case "March": monthInt = 3; break;
			case "April": monthInt = 4; break;
			case "May": monthInt = 5; break;
			case "June": monthInt = 6;  break;
			case "July": monthInt = 7; break;
			case "August": monthInt = 8; break;
			case "September": monthInt = 9; break;
			case "October": monthInt = 10; break;
			case "November": monthInt = 11; break;
			case "December": monthInt = 12; break;
		}
		return monthInt;
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
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			    return true;
			default:
				return super.onOptionsItemSelected(item);
		}		
	}

}
