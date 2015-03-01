/**
 * Author: Uyen Nguyen
 * Date started:1/26/15
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import java.util.Calendar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

//Service handler for activity service connections
public class NotificationReceiver {
   private NotificationService boundService;
   private Context context;
   private boolean isBound;

   public NotificationReceiver(Context myContext) {
      context = myContext;
   }
   
   //connect to service
   public void doBindService() {
      context.bindService(new Intent(context, NotificationService.class), connection, Context.BIND_AUTO_CREATE);
      isBound = true;
   }
   
   //when connected to service ServiceConnection is created for method calling on it
   private ServiceConnection connection = new ServiceConnection() {
      public void onServiceConnected(ComponentName name, IBinder service) {
         boundService = ((NotificationService.ServiceBinder) service).getService();
      }
      
      public void onServiceDisconnected(ComponentName name) {
         boundService = null;
      }
   };

   //set alarm 
   public void setAlarmForNotification(Calendar cal){
      boundService.setAlarm(cal);
   }
   
   //stop service
   public void doUnbindService() {
      if (isBound) {
         context.unbindService(connection);
         isBound = false;
      }
   }
}