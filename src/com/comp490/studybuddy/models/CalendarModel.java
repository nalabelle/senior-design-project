package com.comp490.studybuddy.models;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormatter;

public class CalendarModel {
	private DateTime calendar;
	
	public CalendarModel() {
		this.calendar = new DateTime();
	}

	public String toString(DateTimeFormatter dateFormatter) {
		return this.calendar.toString(dateFormatter);
	}

	public int getMonth() {
		return this.calendar.getMonthOfYear();
	}

	public int getYear() {
		return this.calendar.getYear();
	}

	public int get(DateTimeFieldType dayOfMonth) {
		return calendar.get(dayOfMonth);
	}
}
