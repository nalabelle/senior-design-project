package com.comp490.studybuddy.database;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import java.sql.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "sb.db";
	private static final int DATABASE_VERSION = 6;
	
	// the DAO object we use to access the SimpleData table
	//private Dao<SimpleData, Integer> simpleDao = null;
	//private RuntimeExceptionDao<SimpleData, Integer> simpleRuntimeDao = null;
	
	private String[] classes = {
			"com.comp490.studybuddy.models.TasksModel",
			"com.comp490.studybuddy.models.CalendarEvent",
			"com.comp490.studybuddy.models.NoteEntryModel"
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
		this.onCreate(database);
	}
	
	@Override
	public void close() {
		super.close();
		//simpleDao = null;
		//simpleRuntimeDao = null;
	}

}
