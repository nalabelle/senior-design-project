package com.comp490.studybuddy.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
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
	
	private buddyDBOpenHelper myOpenHelper;
	
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
		myOpenHelper = new buddyDBOpenHelper(getContext(), buddyDBOpenHelper.DATABASE_NAME,
				null, buddyDBOpenHelper.DATABASE_VERSION );
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		switch(uriMatcher.match(uri)) {
			case ALL_ROWS:
				return "vnd.android.cursor.dir/calEvents";
			case SINGLE_ROW:
				return "vnd.android.cursor.item/calEvents";
			default:
				throw new IllegalArgumentException("Unsupported URI: " +uri);
		}
	}
	/**
	 * Creates and returns a cursor based o the query
	 * 
	 * @param Uri the uri to query
	 * @param projection the columns to return in the cursor
	 * @param selection WHERE clause, controls which rows are selected.
	 * 				If null selects all rows.
	 * @param selectionArgs additional selection criteria
	 * @param sortOrder the sorting order for selected rows.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	    queryBuilder.setTables(buddyDBOpenHelper.EVENT_TABLE);
		
		SQLiteDatabase db = myOpenHelper.getReadableDatabase();
		
		switch (uriMatcher.match(uri)) {
	      case SINGLE_ROW : 
	        String rowID = uri.getPathSegments().get(1);
	        queryBuilder.appendWhere(EVENT_ID + "=" + rowID);
	      default: break;
	    }
		
		String groupBy = null;
		String having = null;
		
		Cursor cursor = queryBuilder.query(db, projection, selection,
		        selectionArgs, groupBy, having, sortOrder);

		return cursor;
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
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
