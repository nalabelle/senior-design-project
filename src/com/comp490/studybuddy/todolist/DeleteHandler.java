
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import java.sql.SQLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.comp490.studybuddy.models.Task;

//Class for delete confirmation dialogs before deleting task
public class DeleteHandler {

   public static void deleteDialog(Activity activity, Task task) {
      Dialog confirmDelete = new AlertDialog.Builder(activity)
      .setIcon(android.R.drawable.ic_menu_help)
      .setTitle("Are you sure to want to delete this task?")
      .setPositiveButton("Yes",
            new PositiveButtonListener(activity, task))
      .setNegativeButton("No",
            new NegativeButtonListener())
      .create();
      confirmDelete.show();
   }

   //event handler for Yes button 
   private static class PositiveButtonListener implements OnClickListener {
      private Activity activity;
      private Task task;

      public PositiveButtonListener(Activity activity, Task task) {
         this.activity = activity;
         this.task = task;
      }

      @Override
      public void onClick(DialogInterface dialog, int val) {
    	  //it seems that ToDoMain and ViewTask both call this, so this cast should be okay.
    	  DefaultActivity act = (DefaultActivity) activity;
    	  if(task == null) {
    		  try {
    			  act.getHelper().getTaskDao().notifyChanges();
    		  } catch (SQLException e) {
    			  e.printStackTrace();
    		  }
    		  return;
    	  }
    	  
    	  String name = task.getName(); //this won't exist after we delete it.
    	  try {
			act.getHelper().getTaskDao().delete(task);
			act.getHelper().getTaskDao().notifyChanges();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //TODO: Check to make sure this returned 1, as it should have.
         Toast.makeText(activity.getBaseContext(), "Task deleted: " + name, Toast.LENGTH_LONG).show();
         //return to last activity
         //activity.finish();
         activity.recreate();
      }
   }

   //event handler for No button 
   private static class NegativeButtonListener implements OnClickListener {
      @Override
      public void onClick(DialogInterface dialog, int val) {
         //close dialog
         dialog.dismiss();
      }
   }

}
