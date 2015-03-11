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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.database.DBHelper;
import com.comp490.studybuddy.models.CalendarEvent;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class CalenActivity extends OrmLiteBaseActivity<DBHelper> {
	
	private DateTime displayedDateTime;
	private DateTime todayDateTime;
    private TextView currentMonth;
    
    //TODO Header for Days MON --> SUN
    private TextView calHeader;
    
    //Calendar skeleton and adapter
    private GridView calendarGrid;
    private GridCellAdapter adapter;
    
    private final DateTimeFormatter dateFormatter = 
    		DateTimeFormat.forPattern("MMMM yyyy");

    private int width;
    private static int height;
    
    private ActionBar actionBar;
    
    private GestureDetectorCompat swipeDetector;
    private static ArrayList<CalendarEvent> eventList;
    
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
        
        //Initail drawing of the calendar, set to the current month
        changeCalendarDisplay(); 
	} //End on Create
	
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
        adapter = new GridCellAdapter(getApplicationContext(), R.id.grid_day);
        currentMonth.setText(displayedDateTime.toString(dateFormatter));
        adapter.notifyDataSetChanged();
        calendarGrid.setAdapter(adapter);
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
			    startActivity(intent);
			    return true;
			default:
				return super.onOptionsItemSelected(item);
		}		
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
	
	//Grid Adapter
	public class GridCellAdapter extends BaseAdapter implements OnClickListener
	{
        private final Context context;
        private final List<String> list;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private List<CalendarEvent> eventList;

        public GridCellAdapter(Context context, int cellID)
        {
                super();
                this.context = context;
                this.list = new ArrayList<String>();
                getEvents();

                // Print Month
                printMonth();

                // Find Number of Events
                eventsPerMonthMap = findEventsMonth(displayedDateTime.getYear(), displayedDateTime.getMonthOfYear());
        }
        
        public void getEvents() {
        	//calendar is the current view of the calendar as a dateTime
        	//Get the previous month and next month
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
		public String getItem(int position)
        {
            return list.get(position);
        }
        
        //Show number of events per days as red text in upper left
        // Buggy Shows red one on Jan 30 and Dec 30...
		private HashMap<String, Integer> findEventsMonth(int year, int month)
		{
		    HashMap<String, Integer> map = new HashMap<String, Integer>();
		    for(int i = 0; i < eventList.size(); i++) {
		    	CalendarEvent event = eventList.get(i);
		    	String startDate = event.getStart();
		    	DateTime date = DateTime.parse(startDate);
		    	int y = date.getYear();
		    	int m = date.getMonthOfYear();
		    	int d = date.getDayOfMonth();
		    	String dayAndMonth = "" + d + date.monthOfYear().getAsText();
		    	
		    	if (y == year && m == month) {
		    		if (map.containsKey(dayAndMonth)) {
			    	    map.put(dayAndMonth, map.get(dayAndMonth)+1);
			    	} else { 
			    	    map.put(dayAndMonth,1);
			    	}
		    	}
		    	
		    	
		    }
		    return map;
		}
        
        //print to cells: Monday to Sunday
        private void printMonth()
        {
        	//Set up dates with padding.
        	DateTime startDate = displayedDateTime.withDayOfMonth(displayedDateTime.dayOfMonth().getMinimumValue()).withDayOfWeek(displayedDateTime.dayOfWeek().getMinimumValue());
        	DateTime endDate = displayedDateTime.withDayOfMonth(displayedDateTime.dayOfMonth().getMaximumValue()).withDayOfWeek(displayedDateTime.dayOfWeek().getMaximumValue());
        	
        	//Iterate over every date in the period (startDate --> endDate).
        	for (DateTime date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
        		//Check if we're in the right month.
        		if(!displayedDateTime.monthOfYear().equals(date.monthOfYear())) {
        			if(date.isBefore(displayedDateTime.withDayOfMonth(displayedDateTime.dayOfMonth().getMinimumValue())))  //Previous Month
        				list.add(date.getDayOfMonth() + "-GREY" + "-" + date.monthOfYear().getAsText() + "-" + date.year().getAsText());
        			else if(date.isAfter(displayedDateTime.withDayOfMonth(displayedDateTime.dayOfMonth().getMaximumValue()))) //Future Month
        				list.add(date.getDayOfMonth() + "-GREY" + "-" + date.monthOfYear().getAsText() + "-" + date.year().getAsText());
        		} else { //we're in this month.
        			if(todayDateTime.withTimeAtStartOfDay().isEqual(date.withTimeAtStartOfDay())) //today
	        			list.add(date.getDayOfMonth() + "-WHITE" + "-" + date.monthOfYear().getAsText() + "-" + date.year().getAsText());
        			else
        				list.add(date.getDayOfMonth() + "-BLUE" + "-" + date.monthOfYear().getAsText() + "-" + date.year().getAsText());
        		}
        	}
        } //End print Month
        
        
        @Override
        public int getCount()
        {
            return list.size();
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
            {
                View row = convertView;
                if (row == null)
                {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.day_button, parent, false);
                    //Workingish. Should be based on number of rows...
                    row.setLayoutParams(new GridView.LayoutParams(width/8, height/9));
                }

                // day cells, button action
                gridcell = (Button) row.findViewById(R.id.grid_day);
                gridcell.setOnClickListener(this);
                
                
                // Parse print month information
                String[] day_color = list.get(position).split("-");
                String theday = day_color[0];
                String themonth = day_color[2];
                String theyear = day_color[3];
                
                String dayAndMonth = theday + themonth;
                
                num_events_per_day = (TextView) row.findViewById(R.id.num_events);
                //Add number of events per day from HashMap.
                if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
                    {
                        if (eventsPerMonthMap.containsKey(dayAndMonth))
                            {
                                Integer numEvents = eventsPerMonthMap.get(dayAndMonth);
                                num_events_per_day.setText(numEvents.toString());
                                if (numEvents == 0) {
                                	num_events_per_day.setVisibility(View.GONE);
                                }
                             }
                        else {
                        	num_events_per_day.setVisibility(View.GONE);
                        }
                                
                    }
                else {
                	num_events_per_day.setVisibility(View.GONE);
                }

                // Set the Day in each GridCell, date as tag
                gridcell.setText(theday);
                gridcell.setTag(themonth + "-" + theday + "-" + theyear);

                //Colorize days on calendar
                //Sandwiching months days
                if (day_color[1].equals("GREY"))
                {
                    gridcell.setTextColor(Color.LTGRAY);
                }
                //Current Month
                if (day_color[1].equals("WHITE"))
                {
                    gridcell.setTextColor(Color.WHITE);
                }
                //Day
                if (day_color[1].equals("BLUE"))
                {
                    gridcell.setTextColor(Color.BLUE);
                }
                return row;
            }
        
        //TODO setpopup day detail
        @Override
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
        
	} //End GridCellAdapter

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
	            //Log causing Null pointers
	        	// Log.d(DEBUG_TAG, "onFling: " + event1.toString() + "\n Event2:"
	            		//+event2.toString());
	            //Threshold
	            //if(velocityX < 100)
	            	//return false;
	            //findViewById(R.layout.activity_customcal_phone)
	            if (velocityX < 0)
	            	moveFuture(null);
	            else if (velocityX > 0)
	            	movePast(null);
	            return true;
	        }
	}
}// End CalenActivity
