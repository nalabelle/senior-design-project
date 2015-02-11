/**
 * Author: Uyen Nguyen
 * Date started:1/26/15
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;
import com.comp490.studybuddy.todolist.AlarmService;
import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

//Set alarm using AlarmService
public class Alarm implements Runnable{
   private final Calendar alarmCal;
   private final AlarmManager alarmManager;
   private final Context context;

   public Alarm(Context context, Calendar cal) {
      this.context = context;
      this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      this.alarmCal = cal;
   }
   
   @Override
   public void run() {
      //start service on alarm time
      Intent intent = new Intent(context, AlarmService.class);
      intent.putExtra(AlarmService.INTENT_NOTIFY, true);
      PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
      //sets alarm
      alarmManager.set(AlarmManager.RTC, alarmCal.getTimeInMillis(), pendingIntent);
   }
}
