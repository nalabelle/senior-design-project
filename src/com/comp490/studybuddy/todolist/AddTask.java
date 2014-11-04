/**
 * Author: Uyen Nguyen
 * Date started: 9/26/2014
 * Date Completed: IP
 * Peer Review: Anthony Sager 10/21/2014 
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import com.comp490.studybuddy.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;

public class AddTask extends Activity{
   public static final String TASK_NAME = "taskName";
   public static final String TASK_DATE = "taskDate";
   EditText task;
   DBHelper db = new DBHelper(this);
   
   @Override
   /**Initialize activity*/
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_add_task);
      task = (EditText) findViewById(R.id.taskName);
      ActionBar actionBar = getActionBar();
      actionBar.hide();
   }
   
   /**Inserts new task into db */
   public void addNewTask(View view) {
      HashMap<String, String> queryValues =  new HashMap<String, String>();
      queryValues.put(TASK_NAME, task.getText().toString());
      db.addTask(queryValues);
      Toast.makeText(getBaseContext(), "Task added", 
            Toast.LENGTH_LONG).show();
      this.displayTasks(view);
   }
   
   /**Returns to list view*/
   public void displayTasks(View view) {
      Intent intent = new Intent(getApplicationContext(), ToDoList.class);
      startActivity(intent);
   }  
   
}