
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import java.util.Calendar;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.todolist.CancelHandler;
import com.comp490.studybuddy.todolist.DbAdapter;
import com.comp490.studybuddy.todolist.Task;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

//Activity edits  task if Task object in the bundle exists, otherwise creates a new task
public class ModifyTask extends DefaultActivity {

   private Task task = null;
   //the job of activity edit or add 
   private int taskJob;
   private final int EDIT_TASK = 1;
   private final int ADD_TASK = 2;

   private DbAdapter dbAdapter;

   @Override
   //save, cancel or back menu options
   public boolean onOptionsItemSelected(MenuItem item) {
      Intent intent;
      Bundle bundle;
      switch (item.getItemId()) {
      //cancel or back is selected
      case R.id.modify_actionbar_cancel:
      case android.R.id.home:
         // set the result for the previous activity
         intent = new Intent();
         bundle = new Bundle();
         bundle.putSerializable(Task.TASK_BUNDLE, this.task);
         intent.putExtras(bundle);
         setResult(ViewTask.EDIT_TASK, intent);
         //show confirmation dialog
         CancelHandler.cancelDialog(this);
         return true;
         
      //save button is selected
      case R.id.modify_actionbar_save:
         //check if job is to add or edit
         if(this.taskJob == ADD_TASK) {
            //add new task to db
            addNewTask();
         } 
         else {
            //edit current task
            editExistingTask();
            //set result for previous activity
            intent = new Intent();
            bundle = new Bundle();
            bundle.putSerializable(Task.TASK_BUNDLE, this.task);
            intent.putExtras(bundle);
            setResult(ViewTask.EDIT_TASK, intent);
         }
         //close activity
         finish();
         return true;
         
      //default case returns base class fn
      default:
         return super.onOptionsItemSelected(item);
      }
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_modify_task);
      dbAdapter = new DbAdapter(this);
      dbAdapter.open();
      //check if job is to add or edit and retrieve Task object from bundle
      Bundle modifyTaskBundle = this.getIntent().getExtras();
      try {
         this.task = (Task) modifyTaskBundle.getSerializable(Task.TASK_BUNDLE);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      //if task is not null, change the taskJob to edit task
      if (task != null) {
         this.taskJob = this.EDIT_TASK;
         //load data from existing Task object and put to form
         viewTaskForm();
      } 
      else {
         //init new Task object
         this.task = new Task();
         //change taskJob to add new task
         this.taskJob = this.ADD_TASK;
      }
   }
  
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.modify_task, menu);
      return true;
   }

   //edit current task
   private void editExistingTask() {
      //load data from form to this.task object
      updateTask();
      //call dbAdapter to update task
      dbAdapter.editExistingTask(this.task);
      Toast.makeText(getBaseContext(), "Task updated: " + this.task.getName(), Toast.LENGTH_LONG).show();
   }

   //retrieve data from form and update this.task object 
   private void updateTask() {
      //set task name
      String taskName = ((EditText)findViewById(R.id.modify_edittext_task_name)).getText().toString();
      this.task.setName(taskName);
      
      //set due date
      DatePicker datePicker = (DatePicker) findViewById(R.id.modify_datepicker_date);
      this.task.getDate().set(Calendar.DATE, datePicker.getDayOfMonth());
      this.task.getDate().set(Calendar.MONTH, datePicker.getMonth());
      this.task.getDate().set(Calendar.YEAR, datePicker.getYear());
      
      //set due time
      TimePicker timePicker = (TimePicker) findViewById(R.id.modify_timepicker_time);
      this.task.getTime().set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
      this.task.getTime().set(Calendar.MINUTE, timePicker.getCurrentMinute());
      
      //set priority 
      int priority = ((Spinner)findViewById(R.id.modify_spinner_priority)).getSelectedItemPosition();
      this.task.setPriority(priority);
      
      //set notification 
      CompoundButton notificationSwitch = (Switch)findViewById(R.id.modify_switch_notification);
      if(notificationSwitch.isChecked()){this.task.setNotification(1);}
   }
   

   //add new task to db
   private void addNewTask() {
      //load data from form to this.task object
      updateTask();
      //get a new task id and set it
      String taskId = dbAdapter.getNewTaskId();
      this.task.setId(taskId);
      //call the dbAdapter to add new task
      this.dbAdapter.insertTask(this.task);
      Toast.makeText(getBaseContext(), "Task added: " + this.task.getName(), Toast.LENGTH_LONG).show();
   }

   //edit task load data from this.task object and put to form 
   private void viewTaskForm() {
      //if job is edit, retrieve data from this Task object and put it into form components
      if (this.taskJob == this.EDIT_TASK) {
         //set task name
         EditText taskNameEditText = (EditText) findViewById(R.id.modify_edittext_task_name);
         taskNameEditText.setText(this.task.getName());
        
         //set  date
         DatePicker taskDatePicker = (DatePicker) findViewById(R.id.modify_datepicker_date);
         taskDatePicker.updateDate(this.task.getDate().get(Calendar.YEAR),
               this.task.getDate().get(Calendar.MONTH),
               this.task.getDate().get(Calendar.DATE));
        
         //set time
         TimePicker taskTimePicker = (TimePicker)findViewById(R.id.modify_timepicker_time);
         taskTimePicker.setCurrentHour(this.task.getTime().get(Calendar.HOUR_OF_DAY));
         taskTimePicker.setCurrentMinute(this.task.getTime().get(Calendar.MINUTE));
       
         //set priority 
         Spinner taskPrioritySpinner = (Spinner) findViewById(R.id.modify_spinner_priority);
         taskPrioritySpinner.setSelection(this.task.getPriority());
        
         //set notification 
         CompoundButton taskNotificationSwitch = (Switch)findViewById(R.id.modify_switch_notification);
         if (this.task.getNotification() == 0) {
            taskNotificationSwitch.setChecked(false);
         } else {
            taskNotificationSwitch.setChecked(true);
         }  
      }
   }


}

/*
use for alarms?

notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
   {
      if(isChecked) {
         //do stuff when Switch is ON
         } 
      else {
         //do stuff when Switch if OFF
         }    
   }
});
*/
