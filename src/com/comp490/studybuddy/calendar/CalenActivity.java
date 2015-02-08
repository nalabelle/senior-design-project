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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.CalendarEventModel;

public class CalenActivity extends Activity {
	
	private DateTime calendar;
	private DateTime originalDate;
    private TextView currentMonth;
    
    //Days of week, add events. TODO
    private TextView calHeader;
    private ImageButton prevButton;
    private ImageButton nextButton;
    
    //Calendar skeleton and adapter
    private GridView calendarGrid;
    private GridCellAdapter adapter;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMMM yyyy");

    private int width;
    private static int height;
    
    private ActionBar actionBar;
    
    private GestureDetectorCompat swipeDetector;
    private static ArrayList<CalendarEventModel> eventList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customcal_phone);
		
		swipeDetector = new GestureDetectorCompat(this, new SwipeListener());
		
		//Make it fit, get width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        
        this.calendar = new DateTime();
        this.originalDate = new DateTime();
        
        currentMonth = (TextView) this.findViewById(R.id.curr_month);
        currentMonth.setText(calendar.toString(dateFormatter));
        
        prevButton = (ImageButton) this.findViewById(R.id.prevMonth);
        nextButton = (ImageButton) this.findViewById(R.id.nextMonth);
        
        //How to align to columns? Option for Su->Sa || M -> Su TODO
        calHeader = (TextView) this.findViewById(R.id.cal_header);
        // calHeader.setText("New Event"); //Esthetic bug fix for now

        calendarGrid = (GridView) this.findViewById(R.id.gridView1);
        
        // Grid --> Calendar
        adapter = new GridCellAdapter(getApplicationContext(), R.id.grid_day);
        adapter.notifyDataSetChanged();
        calendarGrid.setAdapter(adapter); 
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {

        swipeDetector.onTouchEvent(event);  
        Log.d("TAG", "Touch");
        return super.onTouchEvent(event);
    }
	
    private void changeCalendarDisplay()
    {
        adapter = new GridCellAdapter(getApplicationContext(), R.id.grid_day);
        currentMonth.setText(calendar.toString(dateFormatter));
        adapter.notifyDataSetChanged();
        calendarGrid.setAdapter(adapter);
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
	
	public void movePast(View v)
	{
		this.calendar = this.calendar.minusMonths(1);
		changeCalendarDisplay();
	}
	
	public void moveFuture(View v)
	{
		this.calendar = this.calendar.plusMonths(1);
		changeCalendarDisplay();
    }
	
	//Grid --> Calendar
	public class GridCellAdapter extends BaseAdapter implements OnClickListener
	{
        private final Context context;

        private final List<String> list;
        private CalDBAdapter db;
        private Cursor cursor;
        
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<Integer, Integer> eventsPerMonthMap;
        private ArrayList<CalendarEventModel> eventList;

        public GridCellAdapter(Context context, int cellID)
        {
                super();
                this.context = context;
                this.list = new ArrayList<String>();
                db = new CalDBAdapter(context);
                db.open();
                eventList = new ArrayList<CalendarEventModel>();
                getEvents();
                
                // Print Month
                printMonth();

                // Find Number of Events
                eventsPerMonthMap = findEventsMonth(calendar.getYear(), calendar.getMonthOfYear());
        }
        
        public void getEvents() {
        	//calendar is the current view of the calendar as a dateTime
        	//Get the previous month and next month
        	DateTime prevDT = new DateTime(calendar);
        	prevDT = prevDT.minusMonths(1);
        	DateTime nextDT = new DateTime(calendar);
        	nextDT = nextDT.plusMonths(1);
        	//Turn to Strings
        	String prev = prevDT.toString();
        	String curr = calendar.toString();
        	String next = nextDT.toString();
        	//Get relevant part Year and Month
        	prev = prev.substring(0,7);
        	curr = curr.substring(0,7);
        	next = next.substring(0,7);
        	
        	cursor = db.getEventByYear(prev, curr, next);
    		//cursor = db.getAllEvents();
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
    	}
        
        public String getItem(int position)
        {
            return list.get(position);
        }
        
        //Show number of events per days as red text in upper left
        // Buggy Shows red one on Jan 30 and Dec 30...
		private HashMap<Integer, Integer> findEventsMonth(int year, int month)
		{
		    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		    for(int i = 0; i < eventList.size(); i++) {
		    	CalendarEventModel event = eventList.get(i);
		    	String startDate = event.getStart();
		    	DateTime date = DateTime.parse(startDate);
		    	int y = date.getYear();
		    	int m = date.getMonthOfYear();
		    	int d = date.getDayOfMonth();
		    	
		    	if (y == year && m == month) {
		    		if (map.containsKey(d)) {
			    	    map.put(d, map.get(d)+1);
			    	} else { 
			    	    map.put(d,1);
			    	}
		    	}
		    	
		    	
		    }
		    return map;
		}
        
        //print to cells: Monday to Sunday
        private void printMonth()
        {
        	//Set up dates with padding.
        	DateTime startDate = calendar.withDayOfMonth(calendar.dayOfMonth().getMinimumValue()).withDayOfWeek(calendar.dayOfWeek().getMinimumValue());
        	DateTime endDate = calendar.withDayOfMonth(calendar.dayOfMonth().getMaximumValue()).withDayOfWeek(calendar.dayOfWeek().getMaximumValue());
        	
        	//Iterate over every date in the period (startDate --> endDate).
        	for (DateTime date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
        		//Check if we're in the right month.
        		if(!calendar.monthOfYear().equals(date.monthOfYear())) {
        			if(date.isBefore(calendar.withDayOfMonth(calendar.dayOfMonth().getMinimumValue())))  //Previous Month
        				list.add(date.getDayOfMonth() + "-GREY" + "-" + date.monthOfYear().getAsText() + "-" + date.year().getAsText());
        			else if(date.isAfter(calendar.withDayOfMonth(calendar.dayOfMonth().getMaximumValue()))) //Future Month
        				list.add(date.getDayOfMonth() + "-GREY" + "-" + date.monthOfYear().getAsText() + "-" + date.year().getAsText());
        		} else { //we're in this month.
        			if(originalDate.withTimeAtStartOfDay().isEqual(date.withTimeAtStartOfDay())) //today
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
                
                int day = Integer.parseInt(theday);
                
                num_events_per_day = (TextView) row.findViewById(R.id.num_events);
                //Add number of events per day from HashMap.
                if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
                    {
                        if (eventsPerMonthMap.containsKey(day))
                            {
                                Integer numEvents = (Integer) eventsPerMonthMap.get(day);
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
        	//Pack information into event
        	Intent dayLaunch = new Intent(CalenActivity.this, DayDetails.class);
        	dayLaunch.putExtra("Day", date_month_year);    
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
	            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + "\n Event2:"
	            		+event2.toString());
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
