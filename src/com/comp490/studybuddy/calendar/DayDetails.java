package com.comp490.studybuddy.calendar;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.CalendarEventModel;

public class DayDetails extends Activity {
	
	private TextView dayDetailText;
	private Intent prevIntent;
	private String dateTimeToday;
	private CalendarAdapter db;
    private ListView listView;
	private ActionBar actionBar;
	private ArrayList<CalendarEventModel> eventList;
	private final DateTimeFormatter dateFormatter = 
    		DateTimeFormat.forPattern("MMMM d, yyyy");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_day_detail);
	    //Grab the list view, create an Array to hold events
	    listView = (ListView) findViewById(R.id.listView1);
	    eventList = new ArrayList<CalendarEventModel>();
	    
	    prevIntent = getIntent();
	    dateTimeToday = prevIntent.getStringExtra("DT");
	    DateTime today = DateTime.parse(dateTimeToday);
	    dayDetailText = (TextView) this.findViewById(R.id.textView1);
	    dayDetailText.setText(today.toString(dateFormatter));
	    //Time DateTime String to relevant info for Query
	    dateTimeToday = dateTimeToday.substring(0,10);
	    Log.d("DayDetail", "Searching for: " +dateTimeToday);
	    //Get Events For the Day
	    this.db = new CalendarAdapter();
	    eventList = db.getEventByDay(dateTimeToday);
	    if (eventList.isEmpty()) {Log.d("DAYDETAIL", "NULL");}
	    //Create an adapter to transfer the the events to the list of textViews
	    CustomListAdapter adapter = new CustomListAdapter(this, 
	    		R.layout.day_detail_list_item, eventList);
	    listView.setAdapter(adapter);
	}
	
	private class CustomListAdapter extends ArrayAdapter<CalendarEventModel> {
		private Context mContext;
		private ArrayList<CalendarEventModel> events;
		
		public CustomListAdapter(Context context, int textViewId, 
				ArrayList<CalendarEventModel> eventList) {
			super(context, textViewId, eventList);
			mContext = context;
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
			//This logic is run many times ~3x  Why?
			if(events.get(position) != null) {
				text.setTextColor(Color.WHITE);
				text.setText(events.get(position).getName());
				Log.d("DAYDETAIL", ""+events.get(position).getName());
				text.setBackgroundColor(Color.BLUE);
			}
			return mView;
		}
		
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
