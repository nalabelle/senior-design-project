package com.comp490.studybuddy.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class CalContentProvider extends ContentProvider {
	
	public static final Uri CONTENT_URI = 
			Uri.parse("content://com.comp490.studybuddy.calprovider/calevents");
	
	private buddyDBOpenHelper db;

	@Override
	public boolean onCreate() {
		db = new buddyDBOpenHelper(getContext(), buddyDBOpenHelper.DATABASE_NAME,
				null, buddyDBOpenHelper.DATABASE_VERSION );
		return false;
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
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
