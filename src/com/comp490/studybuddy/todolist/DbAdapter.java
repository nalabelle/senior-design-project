
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import java.util.UUID;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//controls db
public class DbAdapter {

   public static final String TAG = "todolist";
   
   private DbHelper dbHelper;
   private SQLiteDatabase sqlDatabase;
   
   //current activity
   private final Context context;
   
   public static final String DATABASE_NAME = "to_do_list.db";
   
   public static final int DATABASE_VERSION = 1;
   
   public static final String TASK_TABLE_NAME = "_task";
   public static final String TASK_COLUMN_ID = "_id";
   public static final String TASK_COLUMN_NAME = "_title";
   public static final String TASK_COLUMN_DATE = "_date";
   public static final String TASK_COLUMN_TIME = "_time";
   public static final String TASK_COLUMN_PRIORITY = "_priority";
   public static final String TASK_COLUMN_NOTIFICATION = "_notification";


   //db helper class
   private static class DbHelper extends SQLiteOpenHelper {
      
      public DbHelper(Context context, String name, CursorFactory factory, int version) {
         super(context, name, factory, version);         
         Log.d(TAG,"Created " + DATABASE_NAME);
      }

      //create Task table
      @Override         
      public void onCreate(SQLiteDatabase db) {  
         String query 
         = "Create table " + TASK_TABLE_NAME
         + " ( "
         + TASK_COLUMN_ID + " text primary key, "
         + TASK_COLUMN_NAME + " text not null, "
         + TASK_COLUMN_DATE + " integer not null, "
         + TASK_COLUMN_TIME + " integer not null, "
         + TASK_COLUMN_PRIORITY + " integer not null, "
         + TASK_COLUMN_NOTIFICATION + " integer not null)";
         db.execSQL(query);         
         Log.d(TAG, query);
      }
      
      //drop Task table if exists
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {         
         String query = "Drop table if exists " + DbAdapter.TASK_TABLE_NAME;
         db.execSQL(query);        
         Log.d(TAG, query);
      }
   }
   
   //constructor, pass the current activity to the context
   public DbAdapter(Context context) {
      this.context = context;
   }
   
   //open db connection
   public DbAdapter open() {
      dbHelper = new DbHelper(context, this.DATABASE_NAME, null, this.DATABASE_VERSION);
      sqlDatabase = dbHelper.getWritableDatabase();
      return this;
   }
   
   //close db connection
   public void close() {
      dbHelper.close();
   }

   //insert new task into Task table
   public void insertTask(Task task) {
      ContentValues initialValues = new ContentValues();
      initialValues.put(TASK_COLUMN_ID, task.getId());
      initialValues.put(TASK_COLUMN_NAME, task.getName());
      initialValues.put(TASK_COLUMN_DATE, task.getDate().getTimeInMillis());
      initialValues.put(TASK_COLUMN_TIME, task.getTime().getTimeInMillis());
      initialValues.put(TASK_COLUMN_PRIORITY, task.getPriority());
      initialValues.put(TASK_COLUMN_NOTIFICATION, task.getNotification());
      sqlDatabase.insert(TASK_TABLE_NAME, null, initialValues);       
      Log.d(TAG, "insertTask " + task.getName());
   }
   
   //get all tasks in db
   public Cursor getAllTasks() {
      Log.d(TAG, "getAllTasks");
      return sqlDatabase.query(TASK_TABLE_NAME,
            new String[] {TASK_COLUMN_ID, TASK_COLUMN_NAME, 
            TASK_COLUMN_DATE, TASK_COLUMN_TIME, 
            TASK_COLUMN_PRIORITY, TASK_COLUMN_NOTIFICATION},
            null, null, null, null, null);
   }
   
   //get task by id
   public Cursor getTaskById(String taskId) {
      Log.d(TAG, "getTaskById " + taskId);
      return sqlDatabase.query(TASK_TABLE_NAME,
            new String[] {TASK_COLUMN_ID, TASK_COLUMN_NAME, 
            TASK_COLUMN_DATE, TASK_COLUMN_TIME, 
            TASK_COLUMN_PRIORITY, TASK_COLUMN_NOTIFICATION},
            TASK_COLUMN_ID + " = '" + taskId + "'", null, null, null, null);
   }
   
   //edit existing task
   public void editExistingTask(Task task) {
      ContentValues updateValues;
      // Update Task table first
      updateValues = new ContentValues();
      updateValues.put(TASK_COLUMN_NAME, task.getName());
      updateValues.put(TASK_COLUMN_DATE, task.getDate().getTimeInMillis());      
      updateValues.put(TASK_COLUMN_TIME, task.getTime().getTimeInMillis());
      updateValues.put(TASK_COLUMN_PRIORITY, task.getPriority());
      updateValues.put(TASK_COLUMN_NOTIFICATION, task.getNotification());
      sqlDatabase.update(TASK_TABLE_NAME, updateValues, TASK_COLUMN_ID + " = '" + task.getId() + "'", null);
      Log.d(TAG, "editExistingTask " + task.getName());
   }
   
   //delete Task
   public void deleteTask(Task task) {
      deleteTask(task.getId());
        Log.d(TAG, "deleteTask " + task.getName());
   }
   
   //delete Task
   public void deleteTask(String taskId) {
      sqlDatabase.delete(TASK_TABLE_NAME, TASK_COLUMN_ID + " = '" + taskId + "'", null);        
      Log.d(TAG, "deleteTask " + taskId);
   }

   //get new randomly generated task id for when adding a new task
   public String getNewTaskId() {
      Log.d(TAG, "getNewTaskId");
      String uuid = null;
      Cursor cursor = null;
      do {
         uuid = UUID.randomUUID().toString();
         cursor = getTaskById(uuid);
      } while (cursor.getCount() > 0);
      return uuid;
   }

}
