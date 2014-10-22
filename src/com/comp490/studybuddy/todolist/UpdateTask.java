package com.comp490.studybuddy.todolist;

import com.comp490.studybuddy.R;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import java.util.HashMap; 

public class UpdateTask extends Activity{
   public static final String TASK_ID = "taskId";
   public static final String TASK_NAME = "taskName";
   public static final String TASK_DATE = "taskDate";
	EditText taskName;
	DBHelper db = new DBHelper(this);
	
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_update_task);
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
	 
	public void updateTask(View view) {
		HashMap<String, String> queryValues =  new  HashMap<String, String>();
		taskName = (EditText) findViewById(R.id.taskName);
		Intent intent = getIntent();
		String task_tag = intent.getStringExtra(TASK_ID);
		queryValues.put(TASK_ID, task_tag);
		queryValues.put(TASK_NAME, taskName.getText().toString());
		db.updateTask(queryValues);
		this.displayTasks(view);
	}
	
	public void removeTask(View view) {
		Intent intent = getIntent();
		String task_tag = intent.getStringExtra(TASK_ID);
		db.deleteTask(task_tag);
		this.displayTasks(view);
		
	}
	
	public void displayTasks(View view) {
		Intent intent = new Intent(getApplicationContext(), ToDoList.class);
		startActivity(intent);
	}
	
}
