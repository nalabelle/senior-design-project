/**
 * Author: Uyen Nguyen
 * Date started: 10/6/2014
 * Date Completed: IP
 * Peer Review: IP 
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "todolist.db";
   private static final int DATABASE_VERSION = 1;
   public static final String TABLE_NAME = "tasks";
   public static final String TASK_ID = "taskId";
   public static final String TASK_NAME = "taskName";
   public static final String TASK_DATE = "taskDate";
   private static final String LOGCAT = null;

   public DBHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
      Log.d(LOGCAT,"Created " + TABLE_NAME);
    }
   
   @Override
   public void onCreate(SQLiteDatabase db) {
      String query = "CREATE TABLE tasks ( taskId INTEGER PRIMARY KEY, taskName TEXT)";
      db.execSQL(query);
      Log.d(LOGCAT, query);
   }
   
   @Override
   public void onUpgrade(SQLiteDatabase db, int old_version, int current_version) {
      String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
      db.execSQL(query);
      onCreate(db);
   }
   
   public void insertTask(HashMap<String, String> queryValues) {
      Log.d(LOGCAT,"Insert");
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(TASK_NAME, queryValues.get(TASK_NAME));
      db.insert(TABLE_NAME, null, values);
      db.close();
   }
   
   public int updateTask(HashMap<String, String> queryValues) {
      Log.d(LOGCAT,"Update");
      SQLiteDatabase db = this.getWritableDatabase();  
      ContentValues values = new ContentValues();
      values.put(TASK_NAME, queryValues.get(TASK_NAME));
      return db.update("tasks", values, "taskId" + " = ?", new String[] { queryValues.get("taskId") });
   }
   
   public void deleteTask(String id) {        
      Log.d(LOGCAT,"Delete");
      SQLiteDatabase db = this.getWritableDatabase();  
      String deleteQuery = "DELETE FROM  tasks where taskId='"+ id +"'";
      Log.d("query", deleteQuery);      
      db.execSQL(deleteQuery);
   }
   
   public ArrayList<HashMap<String, String>> getAllTasks() {
      ArrayList<HashMap<String, String>> taskList = new ArrayList<HashMap<String, String>>();
      String selectQuery = "SELECT * FROM " + TABLE_NAME;
      SQLiteDatabase db = this.getWritableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);
      if (cursor.moveToFirst()) {
         do {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TASK_ID, cursor.getString(0));
            map.put(TASK_NAME, cursor.getString(1));
            taskList.add(map);
         } while(cursor.moveToNext());
       }
       return taskList;
   }
   
   public HashMap<String, String> getTaskInfo(String id) {
      HashMap<String, String> taskList = new HashMap<String, String>();
      SQLiteDatabase db = this.getReadableDatabase();
      String selectQuery = "SELECT * FROM tasks where taskId='"+ id +"'";
      Cursor cursor = db.rawQuery(selectQuery, null);
      if (cursor.moveToFirst()) {
         do {
            taskList.put(TASK_NAME, cursor.getString(1));
         } while (cursor.moveToNext());
      }              
      return taskList;
   }  
   
}
