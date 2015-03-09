package com.comp490.studybuddy.calendar;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.database.DBAdapter;
import com.comp490.studybuddy.models.CalendarEventModel;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
	private DBAdapter db;
	private ArrayList<String> colorImgName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_task_calendar);
		colorImgName = new ArrayList<String>();
		
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
					EditText eventText = (EditText) findViewById(R.id.eventName);
					String eventName = eventText.getText().toString(); 
					db = new DBAdapter(this);
					db.open();
					event = new CalendarEventModel( 
							eventName, startDateTime.toString());
					// Save Event
					db.insertEvent(event);
					//Return to Calendar
					Intent back2Cal = new Intent(this, CalenActivity.class);
					back2Cal.putExtra("date", startDateTime.toString());
					back2Cal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | 
							Intent.FLAG_ACTIVITY_NEW_TASK);
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
	
	//Time Picker Fragment
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
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			final DateTimeFormatter dateFormatter = 
					DateTimeFormat.forPattern("h:mm a");
			DateTime time = new DateTime(1, 1, 1, hourOfDay, minute);
					
			if (timeButt == 1) {
				startHour = hourOfDay;
				startMin = minute;
				Button fromTimeButt = 
						(Button) getActivity().findViewById(R.id.fromButtTime);
				fromTimeButt.setText(time.toString(dateFormatter));
			}
			else if (timeButt == 0) {
				Button toTimeButt = 
						(Button) getActivity().findViewById(R.id.toButtTime);
				toTimeButt.setText(time.toString(dateFormatter));
			}
		}
	}
	
	//DatePickerFragment
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
			DatePickerDialog dpdialog = 
					new DatePickerDialog(getActivity(), this, year, month, day);
			//Get the CalendarView for the DatePicker, Hide the week numbers
			CalendarView cv = dpdialog.getDatePicker().getCalendarView();
			cv.setShowWeekNumber(false);
			return dpdialog;
					
		}

		// Do something with user selected date
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			final DateTimeFormatter dateFormatter =
					DateTimeFormat.forPattern("MMMM dd, yyyy");
			
			//How to figure out what button was pressed?
			if (dateButt == 1) {
				startYear = year;
				startMonth = month+1;
				startDay = day;
				Button fromDateButt = 
						(Button) getActivity().findViewById(R.id.fromButtDate);
				DateTime from = new DateTime(year, ++month, day, 0, 0);
				fromDateButt.setText(from.toString(dateFormatter));
			}
			else if (dateButt == 0){
				Button toDateButt = 
						(Button) getActivity().findViewById(R.id.toButtDate);
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
	
	public void showColorPickerDialog(View v) {
		DialogFragment newFragment = new ListViewFragment();
	    newFragment.show(getFragmentManager(), "colorPicker");
	}
	
	//ListFragment
	public class ListViewFragment extends DialogFragment {
	
		//TODO
		CustomListAdapter adapter;
		
		
	}
	
	private class CustomListAdapter extends ArrayAdapter<String> {
		private Context mContext;
		private int id;
		private ArrayList<String> list;
		
		public CustomListAdapter(Context context, int textViewId, 
				ArrayList colorImgName) {
			super(context, textViewId, colorImgName);
			mContext = context;
			id = textViewId;
			list = colorImgName;
			
		}
		
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View mView = v;
			if(mView == null) {
				LayoutInflater vi = (LayoutInflater)mContext.getSystemService
						(Context.LAYOUT_INFLATER_SERVICE);
				mView = vi.inflate(R.layout.color_list, null);
				
			}
			//Apply image
			ImageView img = (ImageView) mView.findViewById(R.id.img);
			TextView text = (TextView) mView.findViewById(R.id.txt);
			if(list.get(position) != null) {
				String[] temp = list.get(position).split("-");
				int circle = Integer.parseInt(temp[0]);
				String colorName = temp[2];
				img.setImageResource(circle);
				text.setTextColor(Color.WHITE);
				text.setText(colorName);
			}
			return mView;
		}
		
	}
	

}
