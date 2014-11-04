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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.textnote.TextNote;

public class CalenActivity extends Activity {
	
	private Calendar calendar;
    private int month, year;
    private TextView currentMonth;
    
    //Days of week, add events. TODO
    private TextView calHeader;
    private ImageButton prevButton;
    private ImageButton nextButton;
    
    //Calendar skeleton and adapter
    private GridView calendarGrid;
    private GridCellAdapter adapter;
    
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    private int width;
    private static int height;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customcal_phone);
		ActionBar actionBar = getActionBar();
	    actionBar.hide();
		
		//Make it fit, get width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
		
		calendar = Calendar.getInstance(Locale.getDefault());
		month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        currentMonth = (TextView) this.findViewById(R.id.curr_month);
        currentMonth.setText(dateFormatter.format(dateTemplate, calendar.getTime()));
        
        prevButton = (ImageButton) this.findViewById(R.id.prevMonth);
        nextButton = (ImageButton) this.findViewById(R.id.nextMonth);
        
        //How to align to columns? Option for Su->Sa || M -> Su TODO
        calHeader = (TextView) this.findViewById(R.id.cal_header);
       // calHeader.setText("New Event"); //Esthetic bug fix for now

        calendarGrid = (GridView) this.findViewById(R.id.gridView1);
        
        // Grid --> Calendar
        adapter = new GridCellAdapter(getApplicationContext(), R.id.grid_day, month, year);
        adapter.notifyDataSetChanged();
        calendarGrid.setAdapter(adapter);
	}
	
    private void changeCalendarDisplay(int month, int year)
    {
        adapter = new GridCellAdapter(getApplicationContext(), R.id.grid_day, month, year);
        calendar.set(year, month - 1, calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(dateFormatter.format(dateTemplate, calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarGrid.setAdapter(adapter);
    }
	
	public void movePast(View v)
	{
		if (month <= 1)
        {
            month = 12;
            year--;
        }
		else
        {
            month--;
        }
    
		changeCalendarDisplay(month, year);
	}
	
	public void moveFuture(View v)
	{
		if (month > 11)
		{
			month = 1;
            year++;
        }
        else
        {
        	month++;
        }
		
		changeCalendarDisplay(month, year);
    }
	
	//Grid --> Calendar
	public class GridCellAdapter extends BaseAdapter implements OnClickListener
	{
        private final Context context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        private final int month, year;
        private int daysInMonth;
        private int currDayOfMonth;
        private int currWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        //better way? Locale based
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

        public GridCellAdapter(Context context, int cellID, int month, int year)
        {
                super();
                this.context = context;
                this.list = new ArrayList<String>();
                this.month = month;
                this.year = year;
                
                Calendar calendar = Calendar.getInstance();
                setCurrDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
                setCurrWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

                // Print Month
                printMonth(month, year);

                // Find Number of Events
                eventsPerMonthMap = findEventsMonth(year, month);
        }
        
        //print to cells
        private void printMonth(int m, int y)
        {
            int prevMonthPadding = 0;
            int daysInPrevMonth = 0;
            
            int prevMonth = 0;
            int prevYear = 0;
            
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = m - 1;
            daysInMonth = getDaysinMonth(currentMonth);

            GregorianCalendar cal = new GregorianCalendar(y, currentMonth, 1);

            if (currentMonth == 11) //Dec
            {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getDaysinMonth(prevMonth);
                nextMonth = 0;
                prevYear = y;
                nextYear = y + 1;
            }
            else if (currentMonth == 0) // Jan
            {
                prevMonth = 11;
                prevYear = y - 1;
                nextYear = y;
                daysInPrevMonth = getDaysinMonth(prevMonth);
                nextMonth = 1;
                 
            }
            else //Not dec && Not jan
            {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = y;
                prevYear = y;
                daysInPrevMonth = getDaysinMonth(prevMonth); 
            }

           //Pad front of month
            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            prevMonthPadding = currentWeekDay;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && m == 1)
            {
                ++daysInMonth;
            }

            // Previous Month days, fill in beginning of curr month if necessary
            for (int i = 0; i < prevMonthPadding; i++)
            {
                list.add(String.valueOf((daysInPrevMonth - prevMonthPadding + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthString(prevMonth) + "-" + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++)
            {
                if (i == getCurrDayOfMonth())
                {
                    list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthString(currentMonth) + "-" + y);
                }
                else
                {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthString(currentMonth) + "-" + y);
                }
            }

            // Fill in end of curr months, if necessary
            for (int i = 0; i < list.size() % 7; i++)
            {
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthString(nextMonth) + "-" + nextYear);
            }
        } //End print Month
        
        public String getMonthString(int i)
        {
        	return months[i];
        }

        public int getDaysinMonth(int i)
        {
        	return daysOfMonth[i];
        }

        public String getItem(int position)
        {
            return list.get(position);
        }
        
        public int getCurrDayOfMonth()
        {
            return currDayOfMonth;
        }
        
        public void setCurrDayOfMonth(int currDayOfMonth)
        {
            this.currDayOfMonth = currDayOfMonth;
        }
        
        public int getCurrWeekDay()
        {
            return currWeekDay;
        }
        
        public void setCurrWeekDay(int currWeekDay)
        {
            this.currWeekDay = currWeekDay;
        }
        
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
        
        //Where to store/ retrieve events?
        //String == event desc.,  Integer is the day
        private HashMap<String, Integer> findEventsMonth(int year, int month)
        {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
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
