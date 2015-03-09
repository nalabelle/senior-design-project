package com.comp490.studybuddy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "studdybuddy.db";
	private static final int DATABASE_VERSION = 6;
	
	private String[] classes = {
			"com.comp490.studybuddy.models.TasksModel",
			"com.comp490.studybuddy.models.CalendarEventModel",
			"com.comp490.studybuddy.models.NoteEntryModel"
			};

	public DBAdapter(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		DBReflector dbr = new DBReflector(db);
		for(String className : classes) {
			dbr.createTableFromClass(className);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.onCreate(db);
	}
	
	public void onOpen() {
		DBReflector dbr = new DBReflector(db);
		for(String className : classes) {
			dbr.createClassFromTable(className);
		}
	}

}
