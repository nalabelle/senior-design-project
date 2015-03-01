package com.comp490.studybuddy.database;

import com.comp490.studybuddy.models.CalendarEventModel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * For use with the ContentProvider
 * ContentProvider not working
 * Useless Class for now
 *
 */
public class buddyDBOpenHelper extends SQLiteOpenHelper{
	
	protected static final String DATABASE_NAME = "buddyBase.db";
	protected static final int DATABASE_VERSION = 1;
	private static final String TAG = "buddyDB";
	
	protected static final String EVENT_TABLE = "calEvents";
	private static final String EVENT_ID = "eventId";
	private static final String EVENT_NAME = "eventName";
	private static final String EVENT_START_DATE = "startDate";
	private static final String EVENT_END_DATE = "endDate";
	
	public static final String TASK_TABLE = "tasks";
	public static final String TASK_ID = "taskId";
	public static final String TASK_NAME = "taskName";
	public static final String TASK_DATE = "taskDate";
	
	private ContentResolver mContentResolver;

	private static final String CREATE_EVENT_TABLE = "create table " +
			EVENT_TABLE + " (" + EVENT_ID + " integer primary key autoincrement, " +
			EVENT_NAME + " TEXT " + EVENT_START_DATE + " INTEGER " +
			EVENT_END_DATE + " INTEGER );";
	
	private static final String CREATE_TODO_TABLE = "CREATE TABLE " +
			TASK_TABLE + " ( "+ TASK_ID + " INTEGER PRIMARY KEY, "
            + TASK_NAME + " TEXT " + TASK_DATE + " INTEGER );";
			
		
	public buddyDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContentResolver = context.getContentResolver();
	}
	
	//Create the Database
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TODO_TABLE);
		db.execSQL(CREATE_EVENT_TABLE);
	}
	
	//Called when version mismatch, db on disk old
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade from" +oldVersion+ " to " +newVersion);
		db.execSQL("DROP TABLE IF EXISTS " +EVENT_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " +TASK_TABLE);
		onCreate(db);
		
	}
	
	/* -------------------- CALENDAR EVENT METHODS --------------------------*/
	public boolean addEvent(CalendarEventModel event) {
		try {
			ContentValues values = new ContentValues();
			values.put(EVENT_NAME, event.getName());
			values.put(EVENT_START_DATE, event.getStart());
			values.put(EVENT_END_DATE, event.getEnd());
			mContentResolver.insert(CalContentProvider.CAL_URI, values);
			Log.d(TAG, "Added Event: " +values.toString());
			return true;
		} catch (Exception e) {
			Log.d(TAG, "Failed to add event: " +e);
			return false;
		}		
	}
	
	public boolean deleteEvent(String eventName) {
		String selection = "EVENT_NAME = \"" +eventName +"\"";
		int rowsDeleted;
		try {
			//Takes the uri to a provider, a selection, 
			//and a String[] of additional selection Arguments
			rowsDeleted = mContentResolver.delete(CalContentProvider.CAL_URI, 
					selection, null);
			Log.d(TAG, "Deleted Event(s) #:"+rowsDeleted);
			return true;
		} catch (Exception e) {
			Log.d(TAG, "Failed to delete event: " +e);
			return false;
		}		
	}
	
	/*
	//TODO
	public CalendarEventModel findEvent(String eventName) {
		String[] projection = {EVENT_ID, EVENT_NAME, EVENT_START_DATE,
				EVENT_END_DATE};
		    	
		    	String selection = "EVENT_NAME = \"" + eventName + "\"";
		    	
		    	Cursor cursor = mContentResolver.query(CalContentProvider.CAL_URI, 
		              projection, selection, null, null);
		    	
		    	CalendarEventModel event = new CalendarEventModel();
				
			if (cursor.moveToFirst()) {
				cursor.moveToFirst();
				event.setName(cursor.getString(1));
				event.setStart(Integer.parseInt(cursor.getString(2)));
					event.setEnd(Integer.parseInt(cursor.getString(3)));
				cursor.close();
			} else {
				event = null;
			}
			return event;
	}
	*/
}
//butt
//-gladreal