
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import com.comp490.studybuddy.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

//Class to handle all Cancel Confirmation Dialogs in application
//Used in Cancel button and back button press of Add and Modify task activities
public class CancelHandler {
   
   public static void cancelDialog(Activity activity) {
      Dialog confirmCancel = new AlertDialog.Builder(activity)
         .setIcon(android.R.drawable.ic_menu_help)
         .setTitle(R.string.modify_cancel_textview)
         .setPositiveButton(R.string.modify_cancel_posbutton,
               new PositiveButtonListener(activity))
         .setNegativeButton(R.string.modify_cancel_negbutton,
               new NegativeButtonListener())
         .create();
      confirmCancel.show();
   }
      
   //event handler for Yes button 
   private static class PositiveButtonListener implements OnClickListener {
      private Activity activity;
      public PositiveButtonListener(Activity activity){
         this.activity = activity;
      }
      @Override
      public void onClick(DialogInterface dialog, int val) {
         //return to the last activity
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
