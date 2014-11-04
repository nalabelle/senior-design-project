/**
 * Author: Uyen Nguyen
 * Date started: 10/6/2014
 * Date Completed: IP
 * Peer Review: Anthony Sager 10/21/2014 
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import com.comp490.studybuddy.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap; 

public class UpdateTask extends Activity{
   public static final String TASK_ID = "taskId";
   public static final String TASK_NAME = "taskName";
   public static final String TASK_DATE = "taskDate";
   EditText taskName;
   DBHelper db = new DBHelper(this);
   
   @Override
   /**Initialize activity*/
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_update_task);
      ActionBar actionBar = getActionBar();
      actionBar.hide();
      taskName = (EditText) findViewById(R.id.taskName);
      Intent intent = getIntent();
      String task_tag = intent.getStringExtra(TASK_ID);
      Log.d("Reading: ", "Reading all tasks..");
      HashMap<String, String> taskList = db.getTaskInfo(task_tag);
      Log.d(TASK_NAME,taskList.get(TASK_NAME));
      if(taskList.size()!=0) {
         taskName.setText(taskList.get(TASK_NAME));
      }
   }
   
   /**Updates task in db*/ 
   public void updateTask(View view) {
      HashMap<String, String> queryValues =  new  HashMap<String, String>();
      taskName = (EditText) findViewById(R.id.taskName);
      Intent intent = getIntent();
      String task_tag = intent.getStringExtra(TASK_ID);
      queryValues.put(TASK_ID, task_tag);
      queryValues.put(TASK_NAME, taskName.getText().toString());
      db.updateTask(queryValues);
      Toast.makeText(getBaseContext(), "Task updated", 
            Toast.LENGTH_LONG).show();
      this.displayTasks(view);
   }
   
   /**Deletes task in db after alert dialog confirmation*/ 
   public void removeTask(final View view) {
      new AlertDialog.Builder(this)
      .setTitle("Delete task")
      .setMessage("Are you sure you want to delete this task?")
      .setPositiveButton("No",new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
            }
         })
      .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog,int id) {
            Intent intent = getIntent();
            String task_tag = intent.getStringExtra(TASK_ID);
            db.deleteTask(task_tag);
            Toast.makeText(getBaseContext(), "Task deleted", Toast.LENGTH_LONG).show();
            UpdateTask.this.displayTasks(view);
         }
      })
      .show();
   }
   
   /**Returns to list view*/
   public void displayTasks(View view) {
      Intent intent = new Intent(getApplicationContext(), ToDoList.class);
      startActivity(intent);
   }
   
}