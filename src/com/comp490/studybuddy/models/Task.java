
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.models;

import java.io.Serializable;
import java.util.Calendar;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//Model class for a Task with id, name, date, time, priority and notification attributes
@DatabaseTable(tableName="Tasks")
public class Task implements Serializable {

	@DatabaseField(generatedId = true)
   private int id;
	@DatabaseField
   private String name;
	@DatabaseField(dataType=DataType.SERIALIZABLE)
   private Calendar dueDate;
	@DatabaseField(dataType=DataType.SERIALIZABLE)
   private Calendar dueTime;
	@DatabaseField
   private int priorityLevel;
	@DatabaseField
   private int notification;
	
   public static final int NO_PRIORITY = 0;
   public static final int YES_PRIORITY = 1;
   public static final int NOTIFICATION_UNCHECKED = 0;
   public static final int NOTIFICATION_CHECKED = 1;
   public static final String TASK_BUNDLE = "task_bundle";

   public Task() {
      this.id = 0;
      this.name = "";
      this.dueDate = Calendar.getInstance();
      this.dueTime = Calendar.getInstance();
      this.priorityLevel = NO_PRIORITY;
      this.notification = NOTIFICATION_UNCHECKED;
   }
   
   public Task(int id, String title, Calendar date, Calendar time, int priorityLevel, int notify) {
      super();
      this.id = id;
      this.name = title;
      this.dueDate = date;
      this.dueDate = time;
      this.priorityLevel = priorityLevel;
      this.notification = notify;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String title) {
      this.name = title;
   }

   public Calendar getDate() {
      return dueDate;
   }

   public void setDate(Calendar date) {
      this.dueDate = date;
   }

   public Calendar getTime() {
      return dueTime;
   }

   public void setTime(Calendar time) {
      this.dueTime = time;
   }  
   
   public int getPriority() {
      return priorityLevel;
   }

   public void setPriority(int priorityLevel) {
      this.priorityLevel = priorityLevel;
   }
   
   public int getNotification() {
      return notification;
   }

   public void setNotification(int notify) {
      this.notification = notify;
   }  
}
