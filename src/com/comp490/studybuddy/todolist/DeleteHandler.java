
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import com.comp490.studybuddy.todolist.DbAdapter;
import com.comp490.studybuddy.todolist.Task;

//Class for delete confirmation dialogs before deleting task
public class DeleteHandler {

   public static void deleteDialog(Activity activity, Task task, DbAdapter databaseAdapter) {
      Dialog confirmDelete = new AlertDialog.Builder(activity)
      .setIcon(android.R.drawable.ic_menu_help)
      .setTitle("Are you sure to want to delete this task?")
      .setPositiveButton("Yes",
            new PositiveButtonListener(activity, task, databaseAdapter))
      .setNegativeButton("No",
            new NegativeButtonListener())
      .create();
      confirmDelete.show();
   }

   //event handler for Yes button 
   private static class PositiveButtonListener implements OnClickListener {
      private Activity activity;
      private Task task;
      private DbAdapter dbAdapter;

      public PositiveButtonListener(Activity activity, Task task, DbAdapter databaseAdapter) {
         this.activity = activity;
         this.task = task;
         this.dbAdapter = databaseAdapter;
      }

      @Override
      public void onClick(DialogInterface dialog, int val) {
         dbAdapter.deleteTask(task);
         Toast.makeText(activity.getBaseContext(), "Task deleted: " + task.getName(), Toast.LENGTH_LONG).show();
         //return to last activity
         activity.finish();
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
