package com.comp490.studybuddy.models;

import org.joda.time.DateTime;

public class CalendarEventModel {
	private DateTime 	eventStart;
	private int 		eventDuration; //minutes!
	private String 		where;
	private String 		eventName;
	private String 		eventDescription;
	
	//require both a name and a start time, others are optional.
	public CalendarEventModel(String name, DateTime start) {
		this.eventStart = start;
		this.eventName = name;
	}
}
