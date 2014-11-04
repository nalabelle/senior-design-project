/**
 * Author: Uyen Nguyen
 * Date started: 10/6/2014
 * Date Completed: IP
 * Peer Review: Anthony Sager 10/21/2014 
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

/**Database helper*/
public class DBHelper extends SQLiteOpenHelper {
   static final String TAG = "TaskData";
   private static final String DATABASE_NAME = "todolist.db";
   private static final int DATABASE_VERSION = 1;
   public static final String TABLE_NAME = "tasks";
   public static final String TASK_ID = "taskId";
   public static final String TASK_NAME = "taskName";
   public static final String TASK_DATE = "taskDate";

   public DBHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
      Log.d(TAG,"Created " + TABLE_NAME);
    }
   
   @Override
   /**Only called once when the database is accessed for the first time 
    * Contains all the table create statements */
   public void onCreate(SQLiteDatabase db) {
      String query = "CREATE TABLE " + TABLE_NAME + " ( "
            + TASK_ID + " INTEGER PRIMARY KEY, "
            + TASK_NAME + " TEXT) ";
      db.execSQL(query);
      Log.d(TAG, "onCreate " + query);
   }
   
   @Override
   /**Called every time database is modified 
    * Drops existing table and creates table again */
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
      db.execSQL(query);
      onCreate(db);
      Log.d(TAG, "onUpgrade " + query);
   }
   
   /**Inserts task*/
   public void addTask(HashMap<String, String> queryValues) {
      Log.d(TAG,"addTask ");
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(TASK_NAME, queryValues.get(TASK_NAME));
      db.insert(TABLE_NAME, null, values);
      db.close();
   }
   
   /**Updates row*/
   public int updateTask(HashMap<String, String> queryValues) {
      Log.d(TAG,"updateTask ");
      SQLiteDatabase db = this.getWritableDatabase();  
      ContentValues values = new ContentValues();
      values.put(TASK_NAME, queryValues.get(TASK_NAME));
      return db.update(TABLE_NAME, values, TASK_ID + " = ?", new String[]{queryValues.get(TASK_ID)});
   }
   
   /**Removes row*/
   public void deleteTask(String id) {        
      SQLiteDatabase db = this.getWritableDatabase();  
      String deleteQuery = "DELETE FROM " + TABLE_NAME 
            + " where " + TASK_ID 
            + "='" + id + "'";
      Log.d("query", deleteQuery);      
      db.execSQL(deleteQuery);
   }
   
   /**Read from database and loops through rows to store all values in an ArrayList*/
   public ArrayList<HashMap<String, String>> getAllTasks() {
      ArrayList<HashMap<String, String>> taskList = new ArrayList<HashMap<String, String>>();
      String selectQuery = "SELECT * FROM " + TABLE_NAME;
      Log.d(TAG,"getAllTasks " + selectQuery);
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
   
   /**Read from database and selects row by id */
   public HashMap<String, String> getTaskInfo(String id) {
      HashMap<String, String> taskList = new HashMap<String, String>();
      SQLiteDatabase db = this.getReadableDatabase();
      String selectQuery = "SELECT * FROM " + TABLE_NAME 
            + " where " + TASK_ID 
            + "='" + id + "'";
      Log.d(TAG,"getTaskInfo: "+ selectQuery);
      Cursor cursor = db.rawQuery(selectQuery, null);
      if (cursor.moveToFirst()) {
         do {
            taskList.put(TASK_NAME, cursor.getString(1));
         } while (cursor.moveToNext());
      }              
      return taskList;
   }  
   
}

