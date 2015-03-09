
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.database;

import java.util.UUID;
import com.comp490.studybuddy.models.CalendarEventModel;
import com.comp490.studybuddy.todolist.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//controls db
public class oldDBAdapter {
	   


   //insert new task into Task table
   public void insertTask(Task task) {
      ContentValues initialValues = new ContentValues();
      initialValues.put(TASK_COLUMN_NAME, task.getName());
      initialValues.put(TASK_COLUMN_DATE, task.getDate().getTimeInMillis());
      initialValues.put(TASK_COLUMN_TIME, task.getTime().getTimeInMillis());
      initialValues.put(TASK_COLUMN_PRIORITY, task.getPriority());
      initialValues.put(TASK_COLUMN_NOTIFICATION, task.getNotification());
      sqlDatabase.insert(TASK_TABLE_NAME, null, initialValues);       
      Log.d(TAG, "insertTask " + task.getName());
   }
   
   //get all tasks in db
   public Cursor getaAllTasks() {
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
   public void deleteTask(int taskId) {
      sqlDatabase.delete(TASK_TABLE_NAME, TASK_COLUMN_ID + " = '" + taskId + "'", null);        
      Log.d(TAG, "deleteTask " + taskId);
   }
   

	public Cursor getAllNotes() {
		Log.d(TAG, "getAllNotes");
		return sqlDatabase.query(NOTE_TABLE_NAME, new String[] {
				NOTE_COLUMN_ID, NOTE_COLUMN_NAME, NOTE_COLUMN_TYPE,
				NOTE_COLUMN_PATH, NOTE_COLUMN_VIEWID,
				NOTE_COLUMN_SECONDARY_VIEWID },
				null, null, null, null, null, null);
	}
	
	public boolean insertNote(int id, String name, String type, String path, int vId, int vId2) {
		ContentValues initval = new ContentValues();
		initval.put(NOTE_COLUMN_ID, id);
		initval.put(NOTE_COLUMN_NAME, name);
		initval.put(NOTE_COLUMN_TYPE, type);
		initval.put(NOTE_COLUMN_PATH, path);
		initval.put(NOTE_COLUMN_VIEWID, vId);
		initval.put(NOTE_COLUMN_SECONDARY_VIEWID, vId2);
		Log.d(TAG, String.format("insertNote %s", name));
		return (sqlDatabase.insert(NOTE_TABLE_NAME, null, initval) > -1);

	}

}
