package com.comp490.studybuddy.database;

import com.comp490.studybuddy.models.CalendarEvent;
import com.comp490.studybuddy.models.NoteEntry;
import com.comp490.studybuddy.models.Task;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import java.sql.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "studybuddy.db";
	private static final int DATABASE_VERSION = 9;
	
	private Dao<Task, Integer> taskDao = null;
	private Dao<CalendarEvent, Integer> calDao = null;
	private Dao<NoteEntry, Integer> noteDao = null;
	
	private String[] classes = {
			"com.comp490.studybuddy.models.Task",
			"com.comp490.studybuddy.models.CalendarEvent",
			"com.comp490.studybuddy.models.NoteEntry"
			};

	public DBHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);//, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			Log.i(DBHelper.class.getName(), "onCreate");
			for(String className : classes) {
				Class<?> obj = Class.forName(className);
				TableUtils.createTable(connectionSource, obj);
			}
		} catch (SQLException | ClassNotFoundException e) {
			Log.e(DBHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DBHelper.class.getName(), "onUpgrade");
			for(String className : classes) {
				Class<?> obj = Class.forName(className);
				TableUtils.dropTable(connectionSource, obj, true);
			}
			onCreate(database, connectionSource);
		} catch (SQLException | ClassNotFoundException e) {
			Log.e(DBHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Task, Integer> getTaskDao() throws SQLException {
		if (taskDao == null) {
			taskDao = getDao(Task.class);
		}
		return taskDao;
	}
	
	public Dao<CalendarEvent, Integer> getCalendarEventDao() throws SQLException {
		if (calDao == null) {
			calDao = getDao(CalendarEvent.class);
		}
		return calDao;
	}
	
	public Dao<NoteEntry, Integer> getNoteEntryDao() throws SQLException {
		if (noteDao == null) {
			noteDao = getDao(NoteEntry.class);
		}
		return noteDao;
	}
	
	@Override
	public void close() {
		super.close();
		taskDao = null;
		noteDao = null;
		calDao = null;
	}

}
