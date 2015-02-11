
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

//import com.comp490.studybuddy.todolist.NotificationReceiver;
import com.comp490.studybuddy.R;
import com.comp490.studybuddy.todolist.CancelHandler;
import com.comp490.studybuddy.todolist.DbAdapter;
import com.comp490.studybuddy.todolist.Task;
import java.util.Calendar;
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

//Activity edits task if Task object in the bundle exists, else creates a new task
public class ModifyTask extends DefaultActivity {

   //private NotificationReceiver notificationReceiver;
   private Calendar cal;
   private Task task = null;
   //the job of activity edit or add 
   private int taskJob;
   private final int EDIT_TASK = 1;
   private final int ADD_TASK = 2;
   private DbAdapter dbAdapter;
   
   
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
         //initialize new Task object
         this.task = new Task();
         //change taskJob to add new task
         this.taskJob = this.ADD_TASK;
      }
      
//      notificationReceiver = new NotificationReceiver(this);
//      //bind activity to service
//      notificationReceiver.doBindService();      
   }
   
   
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
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present
      getMenuInflater().inflate(R.menu.modify_task, menu);
      return true;
   }

   //edit current task
   private void editExistingTask() {
      //load data from form to this.task object
      updateTask();
      
      /**
       *check for notification
       */
      
      //call dbAdapter to update task
      dbAdapter.editExistingTask(this.task);
      Toast.makeText(getBaseContext(), "Task updated: " + this.task.getName(), Toast.LENGTH_LONG).show();
   }


   //retrieve data from form and update this.task object 
   private void updateTask() {
      //set task name
      String taskName = ((EditText)findViewById(R.id.modify_edittext_task_name)).getText().toString();
      this.task.setName(taskName);
           
      //set priority 
      int priority = ((Spinner)findViewById(R.id.modify_spinner_priority)).getSelectedItemPosition();
      this.task.setPriority(priority);
      
      //set due date
      DatePicker datePicker = (DatePicker) findViewById(R.id.modify_datepicker_date);
      int day = datePicker.getDayOfMonth();
      int month = datePicker.getMonth();
      int year = datePicker.getYear();
      this.task.getDate().set(Calendar.DATE, day);
      this.task.getDate().set(Calendar.MONTH, month);
      this.task.getDate().set(Calendar.YEAR, year);
      
      //set due time
      TimePicker timePicker = (TimePicker) findViewById(R.id.modify_timepicker_time);
      int hour = timePicker.getCurrentHour();
      int minute = timePicker.getCurrentMinute();
      this.task.getTime().set(Calendar.HOUR_OF_DAY, hour);
      this.task.getTime().set(Calendar.MINUTE, minute);

      //new cal set to the date
      cal = Calendar.getInstance();
      cal.set(year, month, day);
      cal.set(Calendar.HOUR, hour);
      cal.set(Calendar.MINUTE, minute);
      cal.set(Calendar.SECOND, 0);
      
      //service sets alarm through NotificationReceiver that talks to service
      
      /**
      notificationReceiver.setAlarmForNotification(cal);
      Toast.makeText(this, "Notification set for: "+ (month+1) +"/"+ day +"/"+ 
            year + " " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
       */
      
      //set notification 
      CompoundButton notificationSwitch = (Switch)findViewById(R.id.modify_switch_notification);
      if(notificationSwitch.isChecked()){this.task.setNotification(1);}

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
   }

   
   @Override
   protected void onStop() {
       //stop connection to service when activity is stopped
//       if(notificationReceiver != null)
//          notificationReceiver.doUnbindService();
       super.onStop();
   }
  
   
   //add new task to db
   private void addNewTask() {
      //load data from form to this.task object
      updateTask();
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


