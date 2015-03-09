package com.comp490.studybuddy.models;

import android.util.Log;

public class CalendarEventModel {
	@Attributes(primaryKey = true, notNull = false)
	private String id;
	@Attributes(notNull = true, primaryKey = false)
	private String eventName;
	@Attributes(notNull = true, primaryKey = false)
	private String startDate;
	private String endDate;
	private String description;
	private String color;
	private final String TAG = "EventModel";
	
	public CalendarEventModel() {
		id = null;
		eventName = null;
		startDate = null;
		endDate = null;
		description = null;
		color = null;
		
	}
	
	//require both a name and a start time, others are optional.
	public CalendarEventModel(String id, String name, String start) {
		this.id = id;
		this.startDate = start;
		this.eventName = name;
	}
	
	public CalendarEventModel(String id, String name, String start, String end) {
		this.id = id;
		this.startDate = start;
		this.eventName = name;
		this.endDate = end;
	}
	
	public CalendarEventModel(String id, String name, String start, String end, 
			String desc, String color) {
		this.id = id;
		this.startDate = start;
		this.eventName = name;
		this.endDate = end;
		this.description = desc;
		this.color = color;
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
		return startDate;
	}
	
	public boolean setStart(String start) {
		try {
			startDate = start;
			return true;
		}
		catch(Exception e) {
			Log.d(TAG, "Setting start time failed\n"+e);
			return false;
		}
	}
	
	public String getEnd() {
		return endDate;
	}
	
	public boolean setEnd(String end) {
		try {
			endDate = end;
			return true;
		}
		catch(Exception e) {
			Log.d(TAG, "Setting end time failed\n"+e);
			return false;
		}
	}
	
	public String getDesc() {
		return description;
	}
	
	public boolean setDesc(String desc) {
		try {
			description = desc;
			return true;
		}
		catch(Exception e) {
			Log.d(TAG, "Setting description failed\n"+e);
			return false;
		}
	}
	
	public String getColor() {
		return color;
	}
	
	public boolean setColor(String color) {
		try {
			this.color = color;
			return true;
		}
		catch(Exception e) {
			Log.d(TAG, "Setting color failed\n"+e);
			return false;
		}
	}
}
