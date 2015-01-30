package com.comp490.studybuddy.calendar;

import java.util.UUID;

import com.comp490.studybuddy.models.CalendarEventModel;
import com.comp490.studybuddy.todolist.Task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CalDBAdapter {
	public static final String TAG = "calEvents";
	   private CalDBHelper dbHelper;
	   private SQLiteDatabase sqlDatabase;
	   private final Context context;
	   
		protected static final String DATABASE_NAME = "calEvents.db";
		protected static final int DATABASE_VERSION = 1;
		
		protected static final String EVENT_TABLE = "_calEvents";
		private static final String EVENT_ID = "_eventId";
		private static final String EVENT_NAME = "_eventName";
		private static final String EVENT_START_DATE = "_startDate";
		private static final String EVENT_END_DATE = "_endDate";

		private static final String CREATE_EVENT_TABLE = 
				"create table " + EVENT_TABLE + " ( " +
				EVENT_ID + " text primary key, " +
				EVENT_NAME + " text not null, " + 
				EVENT_START_DATE + " text not null, " +
				EVENT_END_DATE + " text " + " ); ";
		
		private static final String EVENT_TABLE_UPGRADE =
		         "Drop table if exists " + CalDBAdapter.EVENT_TABLE;
		
		private static class CalDBHelper extends SQLiteOpenHelper {

			public CalDBHelper(Context context, String name, 
					CursorFactory factory, int version) {
				super(context, name, factory, version);
				Log.d(TAG, "Created:" +DATABASE_NAME);
			}  
			
			@Override
			public void onCreate(SQLiteDatabase db) {  
		         db.execSQL(CalDBAdapter.CREATE_EVENT_TABLE);
		         Log.d(TAG, CREATE_EVENT_TABLE);
		      }

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
				db.execSQL(CalDBAdapter.EVENT_TABLE_UPGRADE);
				Log.d(TAG, EVENT_TABLE_UPGRADE);
			}
		} //End CalDBHelper
		
		public CalDBAdapter(Context context) {
			this.context = context;;
		}
		
		public CalDBAdapter open() {
			dbHelper = new CalDBHelper(context, this.DATABASE_NAME, null, this.DATABASE_VERSION);
		    sqlDatabase = dbHelper.getWritableDatabase();
		    return this;
		}
		
		public boolean close() {
			try {
				dbHelper.close();
				return true;
			}
			catch(Exception e) {
				Log.d(TAG, "Error closing" + DATABASE_NAME + e);
				return false;
			}
		}
		
		public Cursor getAllEvents() {
			return sqlDatabase.query(EVENT_TABLE, 
					new String[] {EVENT_ID, EVENT_NAME, EVENT_START_DATE, EVENT_END_DATE}, 
					null, null, null, null, null);
		}
		
		public void insertEvent(CalendarEventModel event) {
			ContentValues values = new ContentValues();
			values.put(EVENT_ID, event.getId());
			values.put(EVENT_NAME, event.getName());
			values.put(EVENT_START_DATE, event.getStart());
			values.put(EVENT_END_DATE, event.getEnd());
		    sqlDatabase.insert(EVENT_TABLE, null, values); 
		}
		
		public Cursor getEventById(String eventId) {
		   Log.d(TAG, "getTaskById " + eventId);
		   return sqlDatabase.query(EVENT_TABLE,
		         new String[] {EVENT_ID, EVENT_NAME, EVENT_START_DATE, EVENT_END_DATE},
		         EVENT_ID + " = '" + eventId + "'", null, null, null, null);
		}
		
		//TODO
		//public Cursor getEventByYear(String yearMonth) {
			//Log.d(TAG, "getTaskById" +yearMonth);
			//return sqlDatabase.query();
        //}
		
		public void editExistingTask(CalendarEventModel event) {
			ContentValues values = new ContentValues();
			values.put(EVENT_ID, event.getId());
			values.put(EVENT_NAME, event.getName());
			values.put(EVENT_START_DATE, event.getStart());
			values.put(EVENT_END_DATE, event.getEnd());
			sqlDatabase.update(EVENT_TABLE, values, EVENT_ID + " = '" + event.getId() + "'", null);
			Log.d(TAG, "editExistingTask " + event.getName());
		}
		
		//delete Task
		public void deleteEvent(CalendarEventModel event) {
			String eventId = event.getId();
			sqlDatabase.delete(EVENT_TABLE, EVENT_ID + " = '" + eventId + "'", null); 
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
