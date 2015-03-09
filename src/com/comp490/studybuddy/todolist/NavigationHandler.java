
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import com.comp490.studybuddy.models.Task;
import com.comp490.studybuddy.todolist.ToDoMain;
import com.comp490.studybuddy.todolist.ModifyTask;
import com.comp490.studybuddy.todolist.ViewTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

//Activity handler to start an activity when called by an event 
public class NavigationHandler {

   //go to MainActivity to see list of all tasks
   public static void viewAllTasks(Activity activity) {
      Intent viewAllTasksIntent = new Intent(activity, ToDoMain.class);
      activity.startActivity(viewAllTasksIntent);
   }

   //go to ModifyTask to add new task
   public static void addTask(Activity activity) {
      // Start the activity for user to add task
      Intent addNewTaskIntent = new Intent(activity, ModifyTask.class);
      activity.startActivityForResult(addNewTaskIntent, ToDoMain.ADD_NEW_TASK);
   }
     
   //go to ViewTask to view task details
   public static void viewTask(Activity activity, Task task) {
      Intent viewTaskIntent = new Intent(activity, ViewTask.class);
      //put the Task object into bundle
      Bundle viewTaskBundle = new Bundle();
      viewTaskBundle.putSerializable(Task.TASK_BUNDLE, task);
      //put bundle into intent
      viewTaskIntent.putExtras(viewTaskBundle);
      activity.startActivity(viewTaskIntent);
   }
   
   //go to ModifyTask to edit an existing Task
   public static void editTask(Activity activity, Task task) {
      Intent editTaskIntent = new Intent(activity, ModifyTask.class);
      //put task to edit into bundle
      Bundle editTaskBundle = new Bundle();
      editTaskBundle.putSerializable(Task.TASK_BUNDLE, task);
      //put bundle into intent
      editTaskIntent.putExtras(editTaskBundle);
      activity.startActivityForResult(editTaskIntent, ViewTask.EDIT_TASK);
   }

}
