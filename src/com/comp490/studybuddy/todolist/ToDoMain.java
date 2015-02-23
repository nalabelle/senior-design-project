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
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.database.DBAdapter;

public class ToDoMain extends DefaultActivity {

 	private DBAdapter dbAdapter;
	private Cursor cursor;
	private ListView listView;
   //adapter to display the list's data
	private SimpleCursorAdapter listViewAdapter;
	public static final int ADD_NEW_TASK = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_main);
		listView = (ListView) findViewById(R.id.view_all_tasks_listview);
				
      //short click to open task
		listView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
               long arg3) {
            listViewItemClickHandler(arg0, arg1, arg2);
         }
      });
		
		//long click to delete task
      listView.setOnItemLongClickListener(new OnItemLongClickListener() {
          public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, 
                long arg3) {
             listViewItemLongClickHandler(arg0, arg1, arg2);
            return true;
          }
      }); 
		
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();		
		initTasksListView();
	}

   //initialize all tasks and add them to listview from db
   @SuppressWarnings("deprecation")
   public void initTasksListView() {
			cursor = dbAdapter.getAllTasks();
			startManagingCursor(cursor);
	      //specify which columns go into which views for the cursor adapter
			String[] fromColumns = new String[]{DBAdapter.TASK_COLUMN_NAME};
			//load data into layout components
			int[] toViews = new int[]{R.id.view_listview};		
		   //create an adapter to display the loaded data
			listViewAdapter = new SimpleCursorAdapter(this,
					R.layout.activity_view_listview, cursor, fromColumns, toViews, 1);
			//set adapter for the list view
			this.listView.setAdapter(listViewAdapter);
	}

	//event handler for item shortn clicked from listView
	private void listViewItemClickHandler(AdapterView<?> adapterView, View listView, int itemId) {
		//new task object with data to be passed to next activity to show detail
		Task taskItem = new Task();
		//move cursor to right position
		cursor.moveToFirst();
		cursor.move(itemId);
		//sets data for task item selected
	   taskItem.setId(cursor.getInt(cursor.getColumnIndex(DBAdapter.TASK_COLUMN_ID)));
		taskItem.setName(cursor.getString(cursor.getColumnIndex(DBAdapter.TASK_COLUMN_NAME)));
		taskItem.getDate().setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DBAdapter.TASK_COLUMN_DATE)));
		taskItem.getTime().setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DBAdapter.TASK_COLUMN_TIME)));
		taskItem.setPriority(cursor.getInt(cursor.getColumnIndex(DBAdapter.TASK_COLUMN_PRIORITY)));
		taskItem.setNotification(cursor.getInt(cursor.getColumnIndex(DBAdapter.TASK_COLUMN_NOTIFICATION)));
		//start activity
		NavigationHandler.viewTask(this, taskItem);
	}
	
	//event handler for item long clicked from listView
   @SuppressWarnings("deprecation")
   private void listViewItemLongClickHandler(AdapterView<?> adapterView, View listView, int itemId) {
      Task taskItem = new Task();
      //move cursor to right position
      cursor.moveToFirst();
      cursor.move(itemId);
      //sets data for task item selected
      taskItem.setId(cursor.getInt(cursor.getColumnIndex(DBAdapter.TASK_COLUMN_ID)));
      taskItem.setName(cursor.getString(cursor.getColumnIndex(DBAdapter.TASK_COLUMN_NAME)));
      DeleteHandler.deleteDialog(this, taskItem, this.dbAdapter);
      cursor.requery();    
      listViewAdapter.notifyDataSetChanged();
   }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.view_all_tasks_actionbar_add:
			NavigationHandler.addTask(this, this.dbAdapter);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.to_do_main, menu);
		return true;
	}

}

