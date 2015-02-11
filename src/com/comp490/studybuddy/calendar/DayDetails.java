package com.comp490.studybuddy.calendar;

import java.util.ArrayList;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.CalendarEventModel;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DayDetails extends Activity {
	
	private TextView dayDetailText;
	private Intent prevIntent;
	private String day_mon_yr;
	private CalDBAdapter db;
    private Cursor cursor;
    private ListView listView;
	private ActionBar actionBar;
	private ArrayList<CalendarEventModel> eventList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_day_detail);
	    //Grab the list view, create an Array to hold events
	    listView = (ListView) findViewById(R.id.listView1);
	    eventList = new ArrayList<CalendarEventModel>();
	    
	    prevIntent = getIntent();
	    day_mon_yr = prevIntent.getStringExtra("Day");
	    dayDetailText = (TextView) this.findViewById(R.id.textView1);
	    dayDetailText.setText(day_mon_yr);
	    //Create a string to search for events on this day
	    String[] date = day_mon_yr.split("-");
	    //PROBLEM HERE padding single digit dates, doesnt work for double digit
	    String thisDay = "" + date[2] + "-0" + monthStringToInt(date[0]) + "-0" + date[1];
	    Log.d("TAG", thisDay);
	    //Get Events For the Day
	    db = new CalDBAdapter(this);
	    db.open();
	    cursor = db.getEventByDay(thisDay);
	    //Get events from cursors, add to arrayList  
	    if (cursor.moveToFirst()){
			   while(!cursor.isAfterLast()){
				  String id = cursor.getString(cursor.getColumnIndex("_eventId"));
				  String name = cursor.getString(cursor.getColumnIndex("_eventName"));
			      String startDate = cursor.getString(cursor.getColumnIndex("_startDate"));
			      String endDate = cursor.getString(cursor.getColumnIndex("_endDate"));
			      CalendarEventModel event = new CalendarEventModel(id, name, startDate, endDate);
			      eventList.add(event);
			      cursor.moveToNext();
			   }
			}
	    db.close();
	    if (eventList.isEmpty()) {Log.d("DAYDETAIL", "NULL");}
	    //Create an adapter to transfer the the events to the list of textViews
	    CustomListAdapter adapter = new CustomListAdapter(this, 
	    		R.layout.day_detail_list_item, eventList);
	    listView.setAdapter(adapter);
	}
	
	private class CustomListAdapter extends ArrayAdapter<CalendarEventModel> {
		private Context mContext;
		private int id;
		private ArrayList<CalendarEventModel> events;
		
		public CustomListAdapter(Context context, int textViewId, 
				ArrayList<CalendarEventModel> eventList) {
			super(context, textViewId, eventList);
			mContext = context;
			id = textViewId;
			events = eventList;
			
		}
		
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View mView = v;
			if(mView == null) {
				LayoutInflater vi = (LayoutInflater)mContext.getSystemService
						(Context.LAYOUT_INFLATER_SERVICE);
				mView = vi.inflate(R.layout.day_detail_list_item, null);
				
			}
			TextView text = (TextView) mView.findViewById(R.id.textViewItem);
			if(events.get(position) != null) {
				text.setTextColor(Color.WHITE);
				text.setText(events.get(position).getName());
				Log.d("DAYDETAIL", "events.get(position).getName()");
				text.setBackgroundColor(Color.BLUE);
			}
			return mView;
		}
		
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
