package com.comp490.studybuddy.calendar;

import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.ActionBar;
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
import com.comp490.studybuddy.database.DBHelper;
import com.comp490.studybuddy.models.CalendarEvent;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class DayDetails extends OrmLiteBaseActivity<DBHelper> {
	
	private TextView dayDetailText;
	private Intent prevIntent;
	private String dateTimeToday;
    private ListView listView;
	private ActionBar actionBar;
	private List<CalendarEvent> eventList;
	private final DateTimeFormatter dateFormatter = 
    		DateTimeFormat.forPattern("MMMM d, yyyy");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_day_detail);
	    //Grab the list view, create an Array to hold events
	    listView = (ListView) findViewById(R.id.listView1);
	    
	    prevIntent = getIntent();
	    dateTimeToday = prevIntent.getStringExtra("DT");
	    DateTime today = DateTime.parse(dateTimeToday);
	    dayDetailText = (TextView) this.findViewById(R.id.textView1);
	    dayDetailText.setText(today.toString(dateFormatter));
	    //Time DateTime String to relevant info for Query
	    dateTimeToday = dateTimeToday.substring(0,10);
	    Log.d("DayDetail", "Searching for: " +dateTimeToday);

	    //Get Events For the Day
		QueryBuilder<CalendarEvent, ?> queryBuilder;
		try {
			queryBuilder = getHelper().getDao(
					CalendarEvent.class).queryBuilder();
			queryBuilder.where().like(CalendarEvent.CAL_EVENT_START_DATE,
					dateTimeToday);
			PreparedQuery<CalendarEvent> preparedQuery = queryBuilder.prepare();
			eventList = getHelper().getDao(CalendarEvent.class).query(preparedQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    if (eventList == null || eventList.isEmpty()) {Log.d("DAYDETAIL", "NULL");}
	    //Create an adapter to transfer the the events to the list of textViews
	    CustomListAdapter adapter = new CustomListAdapter(this, 
	    		R.layout.day_detail_list_item, eventList);
	    listView.setAdapter(adapter);
	}
	
	private class CustomListAdapter extends ArrayAdapter<CalendarEvent> {
		private Context mContext;
		private int id;
		private List<CalendarEvent> events;
		
		public CustomListAdapter(Context context, int textViewId, 
				List<CalendarEvent> eventList) {
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
