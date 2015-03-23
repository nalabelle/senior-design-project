package com.comp490.studybuddy.calendar;

import java.sql.SQLException;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.database.DBHelper;
import com.comp490.studybuddy.models.CalendarEvent;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;


public class AddEvent extends OrmLiteBaseActivity<DBHelper> {
	
	private ActionBar actionBar;
	private int dateButt = -1;
	private int timeButt = -1;
	private CalendarEvent event;
	private DateTime startDateTime;
	private DateTime finishDateTime;
	private String color;
	private int startYear;
	private int startMonth;
	private int startDay;
	private int startHour;
	private int startMin;
	private ArrayList<String> colorImgName;
	
	String[] colorName = {"Blue", "Purple", "Green", "Orange", "Red"};
	Integer[] imgId = {R.drawable.blue, R.drawable.purple, R.drawable.green, R.drawable.orange, R.drawable.red};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_task_calendar);
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
		    		
					event = new CalendarEvent( 
							eventName, startDateTime.toString(), "", "", color);

		    		try {
		    			getHelper().getDao(CalendarEvent.class).create(event);
		    		} catch (SQLException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
					
					//Return to Calendar
					Intent back2Cal = new Intent(this, CalenActivity.class);
					back2Cal.putExtra("date", startDateTime.toString());
					back2Cal.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    		startActivity(back2Cal);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), 
							"Uhoh, check your fields\n"+e, Toast.LENGTH_LONG).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}		
	}
	
	//TimePicker Buttons
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
	} //End Time Picker
	
	//DatePicker Buttons
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
	
	//Date Picker Fragment
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
	} //End DatePicker
	
	public void showColorPickerDialog(View v) {
		DialogFragment newFragment = new ListViewFragment();
	    newFragment.show(getFragmentManager(), "colorPicker");
	}
	
	//ListFragment
	public class ListViewFragment extends DialogFragment {
	
		public ListViewFragment create() {
			ListViewFragment frag = new ListViewFragment();
			return frag;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			getDialog().setTitle("Set Color");
	        ListView v = new ListView(getActivity());
	        CustomListAdapter adapter = new CustomListAdapter(this.getActivity().getBaseContext(), colorName, imgId);
	        v.setAdapter(adapter); 

	        //Handle on Clicks
	        v.setClickable(true);
	        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	        	//Handle onTouch
	            @Override
	            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	            	//Add Color to Event
	            	color = (String) arg1.getTag();
	                Button colorButt = 
							(Button) getActivity().findViewById(R.id.colorButt);
	                colorButt.setText((String) arg1.getTag());
	                getDialog().dismiss();
	            }
	        });
	        
	        return v;
	    }
		
		
	}
	
	//List Adapter for Color picker
	private class CustomListAdapter extends ArrayAdapter<String> {
		private Context mContext;
		private final String[] colorName;
		private final Integer[] imgId;
		
		public CustomListAdapter(Context context, String[] colorName,
				Integer[] imgId) {
			super(context, R.layout.color_list, colorName);
			mContext = context;
			this.colorName = colorName;
			this.imgId = imgId;
			
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
			img.setImageResource(imgId[position]);
			text.setText(colorName[position]);
			
			mView.setTag(colorName[position]);
			
			return mView;
		}
		
	}
	

}
