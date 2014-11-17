package com.comp490.studybuddy.models;

import android.util.Log;

public class CalendarEventModel {
	private long eventStart;
	private long eventEnd;
	private String eventName;
	private final String TAG = "EventModel";
	
	//require both a name and a start time, others are optional.
	public CalendarEventModel(String name, long start) {
		this.eventStart = start;
		this.eventName = name;
	}
	
	public String getName() {
		return eventName;
	}
	
	public boolean setName(String name) {
		try {
			eventName = name;
			return true;
		}
		catch(Exception e) {
			Log.d(TAG, "Setting name failed\n"+e);
			return false;
		}
	}
	
	public long getStart() {
		return eventStart;
	}
	
	public boolean setStart(long start) {
		try {
			eventStart = start;
			return true;
		}
		catch(Exception e) {
			Log.d(TAG, "Setting start time failed\n"+e);
			return false;
		}
	}
	
	public long getEnd() {
		return eventEnd;
	}
	
	public boolean setEnd(long end) {
		try {
			eventEnd = end;
			return true;
		}
		catch(Exception e) {
			Log.d(TAG, "Setting end time failed\n"+e);
			return false;
		}
	}
	
}
