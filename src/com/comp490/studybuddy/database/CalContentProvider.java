package com.comp490.studybuddy.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class CalContentProvider extends ContentProvider {
	
	public static final Uri CAL_URI = 
			Uri.parse("content://com.comp490.studybuddy.calprovider/calevents");
	
	public static final String AUTHORITY = "com.comp490.studybuddy.calprovider";
	public static final String BASE_PATH = "calevents";
	
	//Column names
	public static final String EVENT_TABLE = "CalEvents";
	public static final String EVENT_ID = "eventId";
	public static final String EVENT_NAME = "eventName";
	public static final String EVENT_START_DATE = "startDate";
	public static final String EVENT_END_DATE = "endDate";
	
	private buddyDBOpenHelper db;
	
	// Differentiates queries 
	// if the request is for the whole table (ALL_ROWS)
	// or if the request is for a particular row (SINGLE_ROW)
	// Used in getType(Uri uri)
	private static final int ALL_ROWS = 10;
	private static final int SINGLE_ROW = 20;
	private static final UriMatcher uriMatcher;
	static {
		   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		   uriMatcher.addURI(AUTHORITY, BASE_PATH, ALL_ROWS);
		   uriMatcher.addURI(AUTHORITY, BASE_PATH +"/#", SINGLE_ROW);
		  }

	@Override
	public boolean onCreate() {
		db = new buddyDBOpenHelper(getContext(), buddyDBOpenHelper.DATABASE_NAME,
				null, buddyDBOpenHelper.DATABASE_VERSION );
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		switch(uriMatcher.match(uri)) {
			case ALL_ROWS:
				return "vnd.android.cursor.dir/events";
			case SINGLE_ROW:
				return "vnd.android.cursor.item/events";
			default:
				throw new IllegalArgumentException("Unsupported URI: " +uri);
		}
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
