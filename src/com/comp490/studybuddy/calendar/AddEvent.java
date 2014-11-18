package com.comp490.studybuddy.calendar;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.database.buddyDBOpenHelper;
import com.comp490.studybuddy.models.CalendarEventModel;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


public class AddEvent extends Activity {
	
	private ActionBar actionBar;
	private int dateButt = -1;
	private int timeButt = -1;
	private CalendarEventModel event;
	private DateTime startDateTime;
	private DateTime finishDateTime;
	private int startYear;
	private int startMonth;
	private int startDay;
	private int startHour;
	private int startMin;
	private buddyDBOpenHelper db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_task_calendar);
		
		//currentDateTime = new DateTime();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		actionBar = getActionBar();
	    actionBar.show();
		getMenuInflater().inflate(R.menu.calen, menu);
		menu.findItem(R.id.addEvent).setVisible(false);
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
			case R.id.createEvent:
				try {
					startDateTime = new DateTime(startYear, startMonth, startDay, 
							startHour, startMin);
					long startTime = startDateTime.getMillis();
					EditText eventText = (EditText) findViewById(R.id.eventName);
					String eventName = eventText.getText().toString(); 
					event = new CalendarEventModel(eventName, startTime);
					// Save Event
					db = new buddyDBOpenHelper(this);
					db.addEvent(event);
					//Return to Calendar
					Intent back2Cal = new Intent(this, CalenActivity.class);       	
		    		startActivity(back2Cal);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), 
							"Uhoh, check your fields\n"+e, 30).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}		
	}
	
	public class TimePickerFragment extends DialogFragment
    	implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final DateTime currentDateTime = new DateTime();
			int hour = currentDateTime.getHourOfDay();
			int minute = currentDateTime.getMinuteOfHour();

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		// Do something with user selected time
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("h:mm a");
			DateTime time = new DateTime(1, 1, 1, hourOfDay, minute);
					
			if (timeButt == 1) {
				startHour = hourOfDay;
				startMin = minute;
				Button fromTimeButt = (Button) getActivity().findViewById(R.id.fromButtTime);
				fromTimeButt.setText(time.toString(dateFormatter));
			}
			else if (timeButt == 0) {
				Button toTimeButt = (Button) getActivity().findViewById(R.id.toButtTime);
				toTimeButt.setText(time.toString(dateFormatter));
			}
		}
	}
	
	public class DatePickerFragment extends DialogFragment
    	implements DatePickerDialog.OnDateSetListener {
		
		final DateTime currentDateTime = new DateTime();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			
			int year = currentDateTime.getYear();
			int month = currentDateTime.getMonthOfYear();
			int day = currentDateTime.getDayOfMonth();

			//Joda's month indexing is +1 compared to android.
			month--;
			// Create a new instance of DatePickerDialog
			DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), this, year, month, day);
			//Get the CalendarView for the DatePicker, Hide the week numbers
			CalendarView cv = dpdialog.getDatePicker().getCalendarView();
			cv.setShowWeekNumber(false);
			return dpdialog;
					
		}

		// Do something with user selected date
		public void onDateSet(DatePicker view, int year, int month, int day) {
			final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMMM dd, yyyy");
			
			//How to figure out what button was pressed?
			if (dateButt == 1) {
				startYear = year;
				startMonth = month+1;
				startDay = day;
				Button fromDateButt = (Button) getActivity().findViewById(R.id.fromButtDate);
				DateTime from = new DateTime(year, ++month, day, 0, 0);
				fromDateButt.setText(from.toString(dateFormatter));
			}
			else if (dateButt == 0){
				Button toDateButt = (Button) getActivity().findViewById(R.id.toButtDate);
				DateTime to = new DateTime(year, ++month, day, 0, 0);
				toDateButt.setText(to.toString(dateFormatter));
			}		
		}
	}
	
	public void showDatePickerDialog(View v) {
		 switch (v.getId()) {
	    	case R.id.fromButtDate:
	    		dateButt = 1;
	    		break;
	    	case R.id.toButtDate:
	    		dateButt = 0;
	    		break;
	    }
		
		DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void showTimePickerDialog(View v) {
		switch (v.getId()) {
    	case R.id.fromButtTime:
    		timeButt = 1;
    		break;
    	case R.id.toButtTime:
    		timeButt = 0;
    		break;
    }
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	}

}
