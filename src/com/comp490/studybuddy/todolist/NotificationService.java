/**
 * Author: Uyen Nguyen
 * Date started:1/26/15
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;
import com.comp490.studybuddy.todolist.Alarm;
import java.util.Calendar;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class NotificationService extends Service {

   //binder class to access service
   public class ServiceBinder extends Binder {
      NotificationService getService() {
         return NotificationService.this;
      }
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      //service started and stopped as needed
      return START_STICKY;
   }

   @Override
   public IBinder onBind(Intent intent) {
      return binder;
   }

   //gets services
   private final IBinder binder = new ServiceBinder();

   //set alarm 
   public void setAlarm(Calendar cal) {
      //new thread for alarm
      new Alarm(this, cal).run();
   }
}