package com.comp490.studybuddy.models;

public class TasksModel {
	@Attributes(primaryKey = true, notNull = false)
	private int id;
	@Attributes(notNull = true, primaryKey = false)
	private String title;
	@Attributes(notNull = true, primaryKey = false)
	private int date;
	@Attributes(notNull = true, primaryKey = false)
	private int time;
	@Attributes(notNull = true, primaryKey = false)
	private int priority;
	@Attributes(notNull = true, primaryKey = false)
	private int notification;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getNotification() {
		return notification;
	}

	public void setNotification(int notification) {
		this.notification = notification;
	}

}
