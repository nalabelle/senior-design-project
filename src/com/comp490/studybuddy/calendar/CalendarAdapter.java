package com.comp490.studybuddy.calendar;

import java.util.ArrayList;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.util.SparseArray;

import com.comp490.studybuddy.database.DBAdapter;
import com.comp490.studybuddy.models.CalendarEventModel;

public class CalendarAdapter {
	SparseArray<CalendarEventModel> list = new SparseArray<CalendarEventModel>();

	public void insertEvent(CalendarEventModel event) {
		if(event.getId() == null) {
			int i = 0;
			while(i > list.size()) {
				if(list.get(i) != null) {
					i++;
				} else {
					event.setId(i + "");
					break;
				}
			}
		}
		list.put(Integer.parseInt(event.getId()), event);
	}

	public ArrayList<CalendarEventModel> getEventByDay(String dateTimeToday) {
		ArrayList<CalendarEventModel> events = new ArrayList<CalendarEventModel>();
		for(int i = 0, nsize = list.size(); i < nsize; i++) {
		    CalendarEventModel event = list.valueAt(i);
		    if(event.getStart().equalsIgnoreCase(dateTimeToday))
		    	events.add(event);
		}
		return events;
	}
	
	public void load(Context ctx) {
		DBAdapter db = new DBAdapter(ctx);
		for(db.)
		
	}

	public ArrayList<CalendarEventModel> getAllEvents() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Cursor getAllEvents() {
		return sqlDatabase.query(CAL_EVENT_TABLE, 
				new String[] {CAL_EVENT_ID, CAL_EVENT_NAME, CAL_EVENT_START_DATE, 
					CAL_EVENT_END_DATE, CAL_EVENT_DESCRIPTION, CAL_EVENT_COLOR}, 
				null, null, null, null, null);
	}
	
	public void insertEvent(CalendarEventModel event) {
		ContentValues values = new ContentValues();
		values.put(CAL_EVENT_ID, event.getId());
		values.put(CAL_EVENT_NAME, event.getName());
		values.put(CAL_EVENT_START_DATE, event.getStart());
		values.put(CAL_EVENT_END_DATE, event.getEnd());
		values.put(CAL_EVENT_DESCRIPTION, event.getDesc());
		values.put(CAL_EVENT_COLOR, event.getColor());
	    sqlDatabase.insert(CAL_EVENT_TABLE, null, values); 
	}
	
	public Cursor getEventById(String eventId) {
	   Log.d(TAG, "getTaskById " + eventId);
	   return sqlDatabase.query(CAL_EVENT_TABLE,
	         new String[] {CAL_EVENT_ID, CAL_EVENT_NAME, CAL_EVENT_START_DATE, 
			   CAL_EVENT_END_DATE, CAL_EVENT_DESCRIPTION, CAL_EVENT_COLOR},
	         CAL_EVENT_ID + " = '" + eventId + "'", null, null, null, null);
	}
	
	// For Day Details grabs events for a current day 
	public Cursor getEventByDay(String curr) {
		Log.d(TAG, "getEventsByDay: " +curr);
		return sqlDatabase.query(CAL_EVENT_TABLE,
    		new String[] {CAL_EVENT_ID, CAL_EVENT_NAME, CAL_EVENT_START_DATE, 
				CAL_EVENT_END_DATE, CAL_EVENT_DESCRIPTION, CAL_EVENT_COLOR},
	   		CAL_EVENT_START_DATE + " LIKE " + "'" + curr + "%'",
	   		null, null, null, null);
	}
	
	// For the main Calendar View grabs events for the current month
	// As well as the previous month and next month
	public Cursor getEventByYear(String prevMonth, String curr, String nextMonth) {
		Log.d(TAG, "getEventsByTime:" +prevMonth+ " " +curr + " " +nextMonth);
		return sqlDatabase.query(CAL_EVENT_TABLE,
    		new String[] {CAL_EVENT_ID, CAL_EVENT_NAME, CAL_EVENT_START_DATE, 
				CAL_EVENT_END_DATE, CAL_EVENT_DESCRIPTION, CAL_EVENT_COLOR},
    		CAL_EVENT_START_DATE + " LIKE " + "'" + prevMonth + "%'" + " OR " +
	   		CAL_EVENT_START_DATE + " LIKE " + "'" + curr + "%'" + " OR " +
	   		CAL_EVENT_START_DATE + " LIKE " + "'" + nextMonth + "%'", null, null, null, null);
	}
	
	public void editExistingTask(CalendarEventModel event) {
		ContentValues values = new ContentValues();
		values.put(CAL_EVENT_ID, event.getId());
		values.put(CAL_EVENT_NAME, event.getName());
		values.put(CAL_EVENT_START_DATE, event.getStart());
		values.put(CAL_EVENT_END_DATE, event.getEnd());
		values.put(CAL_EVENT_DESCRIPTION, event.getDesc());
		values.put(CAL_EVENT_COLOR, event.getColor());
		sqlDatabase.update(CAL_EVENT_TABLE, values, CAL_EVENT_ID + " = '" + 
				event.getId() + "'", null);
		Log.d(TAG, "editExistingTask " + event.getName());
	}
	
	//delete Task
	public void deleteEvent(CalendarEventModel event) {
		String eventId = event.getId();
		sqlDatabase.delete(CAL_EVENT_TABLE, CAL_EVENT_ID + " = '" + eventId + "'", null); 
		Log.d(TAG, "deleteEvent " + event.getName());
	}
	
	public String getNewEventId() {
	   Log.d(TAG, "getEventById ");
	   String eventId = null;
	   Cursor cursor = null;
	   do {
	      eventId = UUID.randomUUID().toString();
	      cursor = getEventById(eventId);
	   } while (cursor.getCount() > 0);
	   return eventId;
	}


}
