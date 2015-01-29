package com.comp490.studybuddy.models;

import android.util.Log;

public class CalendarEventModel {
	private String id;
	private String eventStart;
	private String eventEnd;
	private String eventName;
	private final String TAG = "EventModel";
	
	public CalendarEventModel() {
		id = null;
		eventName = null;
		eventStart = null;
		eventEnd = null;
	}
	
	//require both a name and a start time, others are optional.
	public CalendarEventModel(String id, String name, String start) {
		this.id = id;
		this.eventStart = start;
		this.eventName = name;
	}
	
	public CalendarEventModel(String id, String name, String start, String end) {
		this.id = id;
		this.eventStart = start;
		this.eventName = name;
		this.eventEnd = end;
	}
	
	public String getId() {
		return id;
	}
	
	public boolean setId(String id) {
		try {
			this.id = id;
			return true;
		}
		catch(Exception e) {
			Log.d(TAG, "Setting id failed\n"+e);
			return false;
		}
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
	
	public String getStart() {
		return eventStart;
	}
	
	public boolean setStart(String start) {
		try {
			eventStart = start;
			return true;
		}
		catch(Exception e) {
			Log.d(TAG, "Setting start time failed\n"+e);
			return false;
		}
	}
	
	public String getEnd() {
		return eventEnd;
	}
	
	public boolean setEnd(String end) {
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
