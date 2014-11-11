package com.comp490.studybuddy.calendar;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.comp490.studybuddy.R;

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
import android.widget.TimePicker;


public class AddEvent extends Activity {
	
	private ActionBar actionBar;
	private static boolean butt = false;
	
	
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
				//Todo Finish Save event
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}		
	}
	
	public static class TimePickerFragment extends DialogFragment
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

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
		}
	}
	
	public static class DatePickerFragment extends DialogFragment
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

		public void onDateSet(DatePicker view, int year, int month, int day) {
			final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMMM dd, yyyy");
			
			//How to figure out what button was pressed?
			if (butt) {
				Button fromDateButt = (Button) getActivity().findViewById(R.id.fromButtDate);
				DateTime from = new DateTime(year, ++month, day, 0, 0);
				fromDateButt.setText(from.toString(dateFormatter));
			}
			else {
				Button toDateButt = (Button) getActivity().findViewById(R.id.toButtDate);
				DateTime to = new DateTime(year, ++month, day, 0, 0);
				toDateButt.setText(to.toString(dateFormatter));
			}
			
			 /*
			switch (getActivity().getView().getId()) {
				case R.id.fromButtDate:
					fromDateButt.setText();
				case R.id.toButtDate:
					toDateButt.setText(monthName+" "+day+" "+year);
			}
			*/
				
		}
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	    switch (v.getId()) {
	    	case R.id.fromButtDate:
	    		butt = true;
	    	case R.id.toButtDate:
	    		butt = false;
	    }
	}
	
	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	}

}
