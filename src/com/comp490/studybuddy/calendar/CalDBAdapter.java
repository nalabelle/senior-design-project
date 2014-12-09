package com.comp490.studybuddy.calendar;

import com.comp490.studybuddy.models.CalendarEventModel;

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
				"create table " + EVENT_TABLE + " ( " 
				+ EVENT_ID + " integer primary key autoincrement, " +
				EVENT_NAME + " text not null " + 
				EVENT_START_DATE + " integer " +
				EVENT_END_DATE + " integer " + " ); ";
		
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
					new String[] {EVENT_ID, EVENT_NAME}, 
					null, null, null, null, null);
		}
		
		/*
		public long insertEvent(CalendarEventModel event) {
			ContentValues values = new ContentValues();
			//values.put(EVENT_ID, null);
		}
		*/
		
		
}
