package com.comp490.studybuddy.todolist;

import com.comp490.studybuddy.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.HashMap;


public class AddTask extends Activity{
   public static final String TASK_NAME = "taskName";
   public static final String TASK_DATE = "taskDate";
	EditText task;
	DBHelper db = new DBHelper(this);
	
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_add_task);
      task = (EditText) findViewById(R.id.taskName);
   }
   
	public void addNewTask(View view) {
		HashMap<String, String> queryValues =  new  HashMap<String, String>();
		queryValues.put(TASK_NAME, task.getText().toString());
		db.insertTask(queryValues);
		this.displayTasks(view);
	}
	
	public void displayTasks(View view) {
		Intent intent = new Intent(getApplicationContext(), ToDoList.class);
		startActivity(intent);
	}	
	
}
