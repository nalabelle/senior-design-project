/**
 * Author: Alex Swanson 103706731?
 * Date started: Early Sept
 * Date Completed: IP
 * Peer Review: Anthony Sager 
 * Date: 11/12/2014 (Reviewed)
 * Team members: Buddy Corp
 * Contribution: IP
 * 
 * 
 */

package com.comp490.studybuddy.calendar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.database.DBHelper;
import com.comp490.studybuddy.models.CalendarEvent;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * 
 * 
 * XMLS: 
 * activity_customcal_phone: 2 text views month name, header. Grid View
 * day_button
 */
public class CalenActivity extends OrmLiteBaseActivity<DBHelper> {
	
	private DateTime displayedDateTime;
	private DateTime todayDateTime;
    private TextView currentMonth;
    //TODO Header for Days MON --> SUN
    private TextView calHeader;
    //Calendar skeleton and adapter
    private GridView calendarGrid;
    //private GridCellAdapter adapter;
    private GridCellAdapterTest adapter;
    private final DateTimeFormatter dateFormatter = 
    		DateTimeFormat.forPattern("MMMM yyyy");
    private int width;
    private static int height;
    private ActionBar actionBar;
    private GestureDetectorCompat swipeDetector;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customcal_phone);
        
		this.todayDateTime = new DateTime();
		// Check if returning from different Cal activity goto that Date
		Intent intent = getIntent();
		String dateTimeString = intent.getStringExtra("date");
		if (dateTimeString != null)
			this.displayedDateTime = DateTime.parse(dateTimeString);
		else
			this.displayedDateTime = new DateTime();
        
		//Set the Name of the Month (Month Year)
        currentMonth = (TextView) this.findViewById(R.id.curr_month);
        currentMonth.setText(displayedDateTime.toString(dateFormatter));
        
        //TODO Unused Ideally want MON --> SUN as headers to columns
        calHeader = (TextView) this.findViewById(R.id.cal_header);

        //Apply swipe detection to calendar
        swipeDetector = new GestureDetectorCompat(this, new SwipeListener());
        calendarGrid = (GridView) this.findViewById(R.id.gridView1);
        
        calendarGrid.setOnTouchListener(new View.OnTouchListener() {  
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                          swipeDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        changeCalendarDisplay();  //Initial drawing of the calendar, set to the current month
	} //End onCreate
	
	//ReDraw Calendar when orientation is changed.
	@Override
	public void onConfigurationChanged(Configuration newConfig) {       
	    super.onConfigurationChanged(newConfig);
	    changeCalendarDisplay();
	}
	
	//Logic of how to reDraw Calendar
    private void changeCalendarDisplay()
    {
    	DisplayMetrics metrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        //adapter = new GridCellAdapter(getApplicationContext(), R.id.grid_day);
        adapter = new GridCellAdapterTest(getApplicationContext(), R.id.cellList);
        currentMonth.setText(displayedDateTime.toString(dateFormatter));
        adapter.notifyDataSetChanged();
        calendarGrid.setAdapter(adapter);
    }
	
	//Swipe Right, move the calendar back one month
	public void movePast(View v)
	{
		this.displayedDateTime = this.displayedDateTime.minusMonths(1);
		changeCalendarDisplay();
	}
	
	//Swipe Left, move the calendar forward one month
	public void moveFuture(View v)
	{
		this.displayedDateTime = this.displayedDateTime.plusMonths(1);
		changeCalendarDisplay();
    }
	
	//Swipe Logic to move calendar back and forward
	private class SwipeListener extends GestureDetector.SimpleOnGestureListener {
		private static final String DEBUG_TAG = "Gestures"; 
		        
		@Override
		public boolean onDown(MotionEvent event) { 
			Log.d(DEBUG_TAG,"onDown: " + event.toString()); 
		    return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, 
				float velocityX, float velocityY) {
		        	if (velocityX < 0)
		            	moveFuture(null);
		            else if (velocityX > 0)
		            	movePast(null);
		            return true;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		actionBar = getActionBar();
	    actionBar.show();
		getMenuInflater().inflate(R.menu.calen, menu);
		menu.findItem(R.id.createEvent).setVisible(false);
		menu.findItem(R.id.editEvent).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	//User hits "+" to create a new event
	//Pass the DateTime object for the currently viewed month
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
			case R.id.action_settings:
				return true;
			case R.id.addEvent:
				Intent intent = new Intent(getApplicationContext(), AddEvent.class);
				intent.putExtra("DT", displayedDateTime.toString());
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    startActivity(intent);
			    return true;
			default:
				return super.onOptionsItemSelected(item);
		}		
	}

	//Begin Adapter Land, First GridCells --> Text(day) and List(events)
	public class GridCellAdapterTest extends BaseAdapter
	{
		ListView gridcell;
		TextView date;
        private final Context context;
        private final List<String> list;
        private List<CalendarEvent> eventList;

        public GridCellAdapterTest(Context context, int cellID)
        {
                super();
                this.context = context;
                this.list = new ArrayList<String>();
                DateTime startDate = displayedDateTime.withDayOfMonth(displayedDateTime.dayOfMonth().getMinimumValue()).withDayOfWeek(displayedDateTime.dayOfWeek().getMinimumValue());
            	DateTime endDate = displayedDateTime.withDayOfMonth(displayedDateTime.dayOfMonth().getMaximumValue()).withDayOfWeek(displayedDateTime.dayOfWeek().getMaximumValue());
            	
            	//Iterate over every date in the period (startDate --> endDate).
            	for (DateTime date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            		list.add(date.toString());
            	}
            	getEvents();
        }
        
        public void getEvents() {
        	Log.d("TAG", "Called GetEVENTS");
        	//Get the previous month and next month from the displayed month
        	DateTime prevDT = new DateTime(displayedDateTime);
        	prevDT = prevDT.minusMonths(1);
        	DateTime nextDT = new DateTime(displayedDateTime);
        	nextDT = nextDT.plusMonths(1);
        	//Turn to Strings
        	String prev = prevDT.toString();
        	String curr = displayedDateTime.toString();
        	String next = nextDT.toString();
        	//Get relevant part Year and Month
        	prev = prev.substring(0,7)+"%";
        	curr = curr.substring(0,7)+"%";
        	next = next.substring(0,7)+"%";
        	
    	    //Get Events For the Day
    		try {
    			QueryBuilder<CalendarEvent, ?> queryBuilder = getHelper().getDao(
    					CalendarEvent.class).queryBuilder();
    			queryBuilder.where()
    				.like(CalendarEvent.CAL_EVENT_START_DATE, prev).or()
    				.like(CalendarEvent.CAL_EVENT_START_DATE, curr).or()
    				.like(CalendarEvent.CAL_EVENT_START_DATE, next);
    			PreparedQuery<CalendarEvent> preparedQuery = queryBuilder.prepare();
    			eventList = getHelper().getDao(CalendarEvent.class).query(preparedQuery);
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
            {
        		List<CalendarEvent> dayEvents = new ArrayList<CalendarEvent>();
                View row = convertView;
                if (row == null)
                {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.cal_cell_list, parent, false);
                    //Workingish. Should be based on number of rows...
                    row.setLayoutParams(new GridView.LayoutParams(width/8, height/9));    
                }
                
                gridcell = (ListView) row.findViewById(R.id.cellList);
                date = (TextView) row.findViewById(R.id.dayNumber);
                DateTime temp = DateTime.parse(list.get(position));
                date.setText(""+temp.getDayOfMonth());
                String thisYearMonDay = temp.toString().substring(0,10);
                //only call gird adapter if necessary
                if(eventList.isEmpty() == false) {
                	for(Iterator<CalendarEvent> i = eventList.iterator(); i.hasNext();) {
                		CalendarEvent tempEvent = i.next();
                		String dateTimeString = tempEvent.getStart().toString().substring(0,10);
                		if (dateTimeString.equals(thisYearMonDay)){
                			dayEvents.add(tempEvent);
                		}
                		if (dayEvents.isEmpty() == false) {
                			eventList.remove(dayEvents);
                			CustomListAdapter adapter = new CustomListAdapter(getBaseContext(), 
                	    		R.layout.cal_day_listitem, dayEvents);
                			gridcell.setAdapter(adapter);
                		}
                		
                	}
                }
                
                row.setOnClickListener(new View.OnClickListener() {
    				@Override
    				public void onClick(View view) {
    					Log.v("TAG", "ROW PRESSED");
    				}
    		    });
    			
                //gridcell.setOnClickListener(this);
                
                //If it has events
                
                
                
                //set a tag on the view
                String tag = temp.toString().substring(0,10);
                gridcell.setTag(tag);
                Log.d("Gird", "RowAdapted");
                
                return row;
            }
        
       
        
        @Override
        public int getCount()
        {
            return list.size();
        }
        
        @Override
        public String getItem(int position)
        {
            return list.get(position);
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }

	} //End GridAdapter
	
	//List Adapter to turn grid cells(days) into a List of events
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
				mView = vi.inflate(R.layout.cal_day_listitem, null);	
			}
			
			ListView list = (ListView) parent;
			Log.d("TEST", (String) list.getTag());
			TextView text = (TextView) mView.findViewById(R.id.eventItem);
			
			if(events.get(position) != null) {
					text.setTextColor(Color.WHITE);
					text.setText(events.get(position).getName());
					Log.d("DAYDETAIL", ""+events.get(position).getName());
					int color = colorFinder(events.get(position).getColor());
					text.setBackgroundColor(getResources().getColor(color));
			}
			mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.v("TAG", "List PRESSED");
				}
		    });
			
			return mView;
		}
		
		private int colorFinder(String color)
		{
			int androidColor = R.color.blue;
			if (color == null) { return androidColor;}
			switch(color) {
				case "Blue":
					break;
				case "Purple":
					androidColor = R.color.purple;
					break;
				case "Green":
					androidColor = R.color.green;
					break;
				case "Orange":
					androidColor = R.color.orange;
					break;
				case "Red":
					androidColor = R.color.red;
					break;
				default:
					break;
			}
			return androidColor;
			
		}
		
		    /*
	        public void onClick(View view)
	        {
	        	//Get the tag of the day touched. Eg. February-2-2015 
	        	String date_month_year = (String) view.getTag();
	        	//Turn into Date Time object
	        	DateTimeFormatter format = DateTimeFormat.forPattern("MMMM-dd-yyyy");
	        	DateTime monthDayYear = format.parseDateTime(date_month_year);
	        	//Pack information into event
	        	Intent dayLaunch = new Intent(CalenActivity.this, DayDetails.class);
	        	dayLaunch.putExtra("Day", date_month_year);    
	        	dayLaunch.putExtra("DT", monthDayYear.toString());
	    		startActivity(dayLaunch);
	        }
	        */
	}// End of ListAdapter
	

	
}// End CalenActivity
