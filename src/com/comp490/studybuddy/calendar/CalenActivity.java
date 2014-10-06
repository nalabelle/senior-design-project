/**
 * Author: Alex Swanson 103706731?
 * Date started: Early Sept
 * Date Completed: IP
 * Peer Review: IP
 * Date: You mean today?
 * Team members: Buddy Corp
 * Contribution: IP
 */

package com.comp490.studybuddy.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.comp490.studybuddy.R;

public class CalenActivity extends Activity {
	
	private Calendar calendar;
    private int month, year;
    private TextView currentMonth;
    private TextView calHeader;
    private Button newEvent;
    
    private GridView calendarGrid;
    private GridCellAdapter adapter;
    
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customcal);
		
		calendar = Calendar.getInstance(Locale.getDefault());
		month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        currentMonth = (TextView) this.findViewById(R.id.curr_month);
        currentMonth.setText(dateFormatter.format(dateTemplate, calendar.getTime()));
        
        currentMonth = (TextView) this.findViewById(R.id.curr_month);
        currentMonth = (TextView) this.findViewById(R.id.curr_month);

        calendarGrid = (GridView) this.findViewById(R.id.gridView1);

        // Initialized
        adapter = new GridCellAdapter(getApplicationContext(), R.id.grid_day, month, year);
        adapter.notifyDataSetChanged();
        calendarGrid.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class GridCellAdapter extends BaseAdapter implements OnClickListener
	{
        private final Context context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private final int month, year;
        private int daysInMonth, prevMonthDays;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

        public GridCellAdapter(Context context, int textViewResourceId, int month, int year)
        {
                super();
                this.context = context;
                this.list = new ArrayList<String>();
                this.month = month;
                this.year = year;
                
                Calendar calendar = Calendar.getInstance();
                setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
                setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

                // Print Month
                printMonth(month, year);

                // Find Number of Events
                eventsPerMonthMap = findEventsMonth(year, month);
        }
        
        private String getMonthString(int i)
        {
        	return months[i];
        }

        private String getWeekDayString(int i)
        {
            return weekdays[i];
        }

        private int getDaysinMonth(int i)
            {
            return daysOfMonth[i];
            }

        public String getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public int getCount()
        {
            return list.size();
        }
              
        private void printMonth(int mm, int yy)
        {
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            daysInMonth = getDaysinMonth(currentMonth);

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

            if (currentMonth == 11)
            {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getDaysinMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            }
            else if (currentMonth == 0)
            {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getDaysinMonth(prevMonth);
                nextMonth = 1;
                 
            }
            else
            {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getDaysinMonth(prevMonth); 
            }

           //Pad front of month
            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
            {
                ++daysInMonth;
            }

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++)
            {
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthString(prevMonth) + "-" + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++)
            {
                if (i == getCurrentDayOfMonth())
                {
                    list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthString(currentMonth) + "-" + yy);
                }
                else
                {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthString(currentMonth) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++)
            {
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthString(nextMonth) + "-" + nextYear);
            }
        }
        
        private HashMap<String, Integer> findEventsMonth(int year, int month)
        {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
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
                }

                // Get a reference to the Day gridcell
                gridcell = (Button) row.findViewById(R.id.grid_day);
                gridcell.setOnClickListener(this);

                // ACCOUNT FOR SPACING
                String[] day_color = list.get(position).split("-");
                String theday = day_color[0];
                String themonth = day_color[2];
                String theyear = day_color[3];
                if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
                    {
                        if (eventsPerMonthMap.containsKey(theday))
                            {
                                //num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                                Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                                num_events_per_day.setText(numEvents.toString());
                            }
                    }

                // Set the Day GridCell
                gridcell.setText(theday);
                gridcell.setTag(theday + "-" + themonth + "-" + theyear);

                if (day_color[1].equals("GREY"))
                {
                    gridcell.setTextColor(Color.LTGRAY);
                }
                if (day_color[1].equals("WHITE"))
                {
                    gridcell.setTextColor(Color.WHITE);
                }
                if (day_color[1].equals("BLUE"))
                {
                    gridcell.setTextColor(Color.BLUE);
                }
                return row;
            }
        
        
        @Override
        public void onClick(View view)
        {
        	//Get info for day poked
        	String date_month_year = (String) view.getTag();

            try
            {
            	Date parsedDate = dateFormatter.parse(date_month_year);
            }
           catch (ParseException e)
           {
        	   e.printStackTrace();
           }
        }
        
        
        public int getCurrentDayOfMonth()
        {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth)
        {
            this.currentDayOfMonth = currentDayOfMonth;
        }
        public void setCurrentWeekDay(int currentWeekDay)
        {
            this.currentWeekDay = currentWeekDay;
        }
        public int getCurrentWeekDay()
        {
            return currentWeekDay;
        }
	}
}
