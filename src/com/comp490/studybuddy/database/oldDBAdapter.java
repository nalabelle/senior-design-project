
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.database;

import java.util.UUID;
import com.comp490.studybuddy.models.CalendarEventModel;
import com.comp490.studybuddy.todolist.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//controls db
public class oldDBAdapter {
	   


   //insert new task into Task table
   public void insertTask(Task task) {
      ContentValues initialValues = new ContentValues();
      initialValues.put(TASK_COLUMN_NAME, task.getName());
      initialValues.put(TASK_COLUMN_DATE, task.getDate().getTimeInMillis());
      initialValues.put(TASK_COLUMN_TIME, task.getTime().getTimeInMillis());
      initialValues.put(TASK_COLUMN_PRIORITY, task.getPriority());
      initialValues.put(TASK_COLUMN_NOTIFICATION, task.getNotification());
      sqlDatabase.insert(TASK_TABLE_NAME, null, initialValues);       
      Log.d(TAG, "insertTask " + task.getName());
   }
   
   //get all tasks in db
   public Cursor getAllTasks() {
      Log.d(TAG, "getAllTasks");
      return sqlDatabase.query(TASK_TABLE_NAME,
            new String[] {TASK_COLUMN_ID, TASK_COLUMN_NAME, 
            TASK_COLUMN_DATE, TASK_COLUMN_TIME, 
            TASK_COLUMN_PRIORITY, TASK_COLUMN_NOTIFICATION},
            null, null, null, null, null);
   }
   
   //get task by id
   public Cursor getTaskById(String taskId) {
      Log.d(TAG, "getTaskById " + taskId);
      return sqlDatabase.query(TASK_TABLE_NAME,
            new String[] {TASK_COLUMN_ID, TASK_COLUMN_NAME, 
            TASK_COLUMN_DATE, TASK_COLUMN_TIME, 
            TASK_COLUMN_PRIORITY, TASK_COLUMN_NOTIFICATION},
            TASK_COLUMN_ID + " = '" + taskId + "'", null, null, null, null);
   }
   
   //edit existing task
   public void editExistingTask(Task task) {
      ContentValues updateValues;
      // Update Task table first
      updateValues = new ContentValues();
      updateValues.put(TASK_COLUMN_NAME, task.getName());
      updateValues.put(TASK_COLUMN_DATE, task.getDate().getTimeInMillis());      
      updateValues.put(TASK_COLUMN_TIME, task.getTime().getTimeInMillis());
      updateValues.put(TASK_COLUMN_PRIORITY, task.getPriority());
      updateValues.put(TASK_COLUMN_NOTIFICATION, task.getNotification());
      sqlDatabase.update(TASK_TABLE_NAME, updateValues, TASK_COLUMN_ID + " = '" + task.getId() + "'", null);
      Log.d(TAG, "editExistingTask " + task.getName());
   }
   
   //delete Task
   public void deleteTask(Task task) {
      deleteTask(task.getId());
        Log.d(TAG, "deleteTask " + task.getName());
   }
   
   //delete Task
   public void deleteTask(int taskId) {
      sqlDatabase.delete(TASK_TABLE_NAME, TASK_COLUMN_ID + " = '" + taskId + "'", null);        
      Log.d(TAG, "deleteTask " + taskId);
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

	public Cursor getAllNotes() {
		Log.d(TAG, "getAllNotes");
		return sqlDatabase.query(NOTE_TABLE_NAME, new String[] {
				NOTE_COLUMN_ID, NOTE_COLUMN_NAME, NOTE_COLUMN_TYPE,
				NOTE_COLUMN_PATH, NOTE_COLUMN_VIEWID,
				NOTE_COLUMN_SECONDARY_VIEWID },
				null, null, null, null, null, null);
	}
	
	public boolean insertNote(int id, String name, String type, String path, int vId, int vId2) {
		ContentValues initval = new ContentValues();
		initval.put(NOTE_COLUMN_ID, id);
		initval.put(NOTE_COLUMN_NAME, name);
		initval.put(NOTE_COLUMN_TYPE, type);
		initval.put(NOTE_COLUMN_PATH, path);
		initval.put(NOTE_COLUMN_VIEWID, vId);
		initval.put(NOTE_COLUMN_SECONDARY_VIEWID, vId2);
		Log.d(TAG, String.format("insertNote %s", name));
		return (sqlDatabase.insert(NOTE_TABLE_NAME, null, initval) > -1);

	}

}
