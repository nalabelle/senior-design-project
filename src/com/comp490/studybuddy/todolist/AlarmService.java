/**
 * Author: Uyen Nguyen
 * Date started:1/26/15
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

//Service is started for Alarm 
public class AlarmService extends Service {

   public class ServiceBinder extends Binder {
      AlarmService getService() {
         return AlarmService.this;
      }
   }

   //intent extra of service to create notification   
   public static final String INTENT_NOTIFY = "com.comp490.studybuddy.todolist.INTENT_NOTIFY";

   @Override
   public void onCreate() {
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {      
      //if service started by Alarm intent, show notification
      if(intent.getBooleanExtra(INTENT_NOTIFY, false))
         showNotification();
      
      //service should stop running after processing notification
      return START_NOT_STICKY;
   }

   @Override
   public IBinder onBind(Intent intent) {
      return binder;
   }

   private final IBinder binder = new ServiceBinder();

   //create and display notification
   private void showNotification() {
      
      /**
       *replace with system level alert dialog
       */
      Toast.makeText(getBaseContext(), "Task is due!", Toast.LENGTH_LONG).show();

      //stop service when finished
      stopSelf();
   }
}