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
import java.util.Locale;
import com.comp490.studybuddy.R;
import com.comp490.studybuddy.todolist.NavigationHandler;
import com.comp490.studybuddy.todolist.DeleteHandler;
import com.comp490.studybuddy.todolist.DbAdapter;
import com.comp490.studybuddy.todolist.Task;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewTask extends DefaultActivity {

   private Task task;
   //request edit task
   public static final int EDIT_TASK = 1;
   private DbAdapter dbAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_view_task);
      dbAdapter = new DbAdapter(this);
      dbAdapter.open();
      //get task object from bundle
      Bundle taskBundle = this.getIntent().getExtras();
      try {
         this.task = (Task) taskBundle.getSerializable(Task.TASK_BUNDLE);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      //check if task object is null
      if(this.task == null) {
         //close activity
         this.finish();
      } else {
         //show data from task object
         this.displayTask();
      }
   }

   //put task info into view
   private void displayTask() {
      if(this.task == null) {
         this.finish();
      } else {
         // set task data 
         TextView taskNameTextView = (TextView) findViewById(R.id.view_textview_name);
         taskNameTextView.setText(this.task.getName());
         
         // set date
         TextView taskDateTextView = (TextView) findViewById(R.id.view_textview_date);
         Calendar taskDueDate = this.task.getDate();
         int dayOfWeek = taskDueDate.get(Calendar.DAY_OF_WEEK);
         String nameOfDay = "";
         switch(dayOfWeek) {
           case 1: nameOfDay = "Sun";
           break;
           case 2: nameOfDay = "Mon";
           break;
           case 3: nameOfDay = "Tues";
           break;
           case 4: nameOfDay = "Wed";
           break;
           case 5: nameOfDay = "Thurs";
           break;
           case 6: nameOfDay = "Fri";
           break;
           case 7: nameOfDay = "Sat";
           break;
           }
         String dueDateString = nameOfDay + " " + taskDueDate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " "
               + taskDueDate.get(Calendar.DATE) + ", "
               + taskDueDate.get(Calendar.YEAR);
         taskDateTextView.setText(dueDateString);
         
         // set time
         TextView taskTimeTextView = (TextView) findViewById(R.id.view_textview_time);
         Calendar taskDueTime = this.task.getTime();
         String am_pm = "";
         if (taskDueTime.get(Calendar.AM_PM) == Calendar.AM)
             am_pm = "AM";
         else if (taskDueTime.get(Calendar.AM_PM) == Calendar.PM)
             am_pm = "PM";
         String hour = (taskDueTime.get(Calendar.HOUR) == 0) ?"12":taskDueTime.get(Calendar.HOUR)+""; 
         String minute = String.format("%02d", taskDueTime.get(Calendar.MINUTE));
         String dueTimeString = hour + ":" + minute + " " + am_pm;
         taskTimeTextView.setText(dueTimeString);
         
         // set the priority level
         TextView priorityTextView = (TextView) findViewById(R.id.view_textview_priority);
         String priorityString;
         switch (this.task.getPriority()) {
         case Task.YES_PRIORITY:
            priorityString = this.getString(R.string.modify_priority_yes);
            break;
         default:
            priorityString = this.getString(R.string.modify_priority_no);
            break;
         }
         priorityTextView.setText(priorityString);
         
         //set notification
         TextView notificationTextView = (TextView) findViewById(R.id.view_textview_notification);
         String notificationString = "";
         if (this.task.getNotification() == 0) {
            notificationString = "Off";
         }
         else {
            notificationString = "On";
         }
         notificationTextView.setText(notificationString);
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.view_task, menu);
      return true;
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // TODO Auto-generated method stub
      super.onActivityResult(requestCode, resultCode, data);
      if(requestCode == EDIT_TASK) {
         this.task = (Task) data.getExtras().getSerializable(Task.TASK_BUNDLE);
         this.displayTask();
      }
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.view_actionbar_edit:
         NavigationHandler.editTask(this, this.task);
         return true;
      case R.id.view_actionbar_delete:
         DeleteHandler.deleteDialog(this, this.task, this.dbAdapter);
      default:
         return super.onOptionsItemSelected(item);
      }
   }

}
