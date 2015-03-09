package com.comp490.studybuddy.models;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="Calendar")
public class CalendarEvent {
	//static are for the DB Table queries, if you change these, things break.
	public static final String CAL_EVENT_START_DATE = "startDate";
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String eventName;
	@DatabaseField(columnName = CAL_EVENT_START_DATE)
	private String startDate;
	@DatabaseField
	private String endDate;
	@DatabaseField
	private String description;
	@DatabaseField
	private String color;
	private final String TAG = "EventModel";
	
	CalendarEvent() {
		
	}
	
	//require both a name and a start time, others are optional.
	public CalendarEvent(String name, String start) {
		this.startDate = start;
		this.eventName = name;
	}
	
	public CalendarEvent(int id, String name, String start, String end) {
		this.id = id;
		this.startDate = start;
		this.eventName = name;
		this.endDate = end;
	}
	
	public CalendarEvent(int id, String name, String start, String end, 
			String desc, String color) {
		this.id = id;
		this.startDate = start;
		this.eventName = name;
		this.endDate = end;
		this.description = desc;
		this.color = color;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean setId(int id) {
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
