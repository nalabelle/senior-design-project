/**
 * Author: Alex Swanson 103706731?
 * Date started: Early Sept
 * Date Completed: IP
 * Peer Review: IP
 * Date: You mean today?
 * Team members: Buddy Corp
 * Contribution: IP
 * 
 * Working Calendar... no functionality
 * TODO
 * Fix broken button/header text issues
 * Move New Event button out of header section.
 * Find a way to stretch calendar on large screens... new day_button xml? relative layout fill?
 * Sprint 2: add event tracking, add events,
 *  scrolling (back/forward in time), day detail pop up
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
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comp490.studybuddy.R;

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
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customcal_phone);
		
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
        
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        //better way? Locale based
        //private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

        public GridCellAdapter(Context context, int cellID)
        {
                super();
                this.context = context;
                this.list = new ArrayList<String>();
                
                // Print Month
                printMonth();

                // Find Number of Events
                eventsPerMonthMap = findEventsMonth(calendar.getYear(), calendar.getMonthOfYear());
        }
        
        public String getItem(int position)
        {
            return list.get(position);
        }
        
		//Where to store/ retrieve events?
		//String == event desc.,  Integer is the day
		private HashMap<String, Integer> findEventsMonth(int year, int month)
		{
		    HashMap<String, Integer> map = new HashMap<String, Integer>();
		    return map;
		}
        
        //print to cells
        private void printMonth()
        {
        	//Set up dates with padding.
        	DateTime startDate = calendar.withDayOfMonth(calendar.dayOfMonth().getMinimumValue()).withDayOfWeek(calendar.dayOfWeek().getMinimumValue());
        	DateTime endDate = calendar.withDayOfMonth(calendar.dayOfMonth().getMaximumValue()).withDayOfWeek(calendar.dayOfWeek().getMaximumValue());
        	
        	//Iterate over every date in the period.
        	for (DateTime date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
        		//Check if we're in the right month.
        		if(!calendar.monthOfYear().equals(date.monthOfYear())) {
        			if(date.isBefore(calendar.withDayOfMonth(calendar.dayOfMonth().getMinimumValue())))
        				list.add(date.getDayOfMonth() + "-GREY" + "-" + date.monthOfYear().getAsText() + "-" + date.year().getAsText());
        			else if(date.isAfter(calendar.withDayOfMonth(calendar.dayOfMonth().getMaximumValue())))
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
                
                //Add number of events per day from HashMap.
                if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
                    {
                        if (eventsPerMonthMap.containsKey(theday))
                            {
                                num_events_per_day = (TextView) row.findViewById(R.id.num_events);
                                Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                                num_events_per_day.setText(numEvents.toString());
                            }
                    }

                // Set the Day in each GridCell, date as tag
                gridcell.setText(theday);
                gridcell.setTag(theday + "-" + themonth + "-" + theyear);

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
        	//Get info for day poked
        	String date_month_year = (String) view.getTag();
        	Intent dayLaunch = new Intent(CalenActivity.this, DayDetails.class);
        	dayLaunch.putExtra("Day", date_month_year);        	
    		startActivity(dayLaunch);
        }
        
	} //End GridCellAdapter
}// End CalenActivity
