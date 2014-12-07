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
import com.comp490.studybuddy.todolist.NavigationHandler;
import com.comp490.studybuddy.todolist.DbAdapter;
import com.comp490.studybuddy.todolist.Task;

import android.os.Bundle;
import android.app.LoaderManager;
//import android.content.CursorLoader;
//import android.content.Loader;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

//Note: have not finished implementing LoaderManager
public class ToDoMain extends DefaultActivity {
   //implements LoaderManager.LoaderCallbacks<Cursor>  
   
   //callbacks through which we will interact with the LoaderManager
   private LoaderManager.LoaderCallbacks<Cursor> callbacks;
 
	private ListView listView;
	private DbAdapter dbAdapter;
	private Cursor cursor;
   //this is the Adapter being used to display the list's data
	private SimpleCursorAdapter listViewAdapter;
	public static final int ADD_NEW_TASK = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_main);
		listView = (ListView) findViewById(R.id.view_all_tasks_listview);
		
	    //action listener for listView
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listViewItemClickHandler(arg0, arg1, arg2);
			}
		});
		
		dbAdapter = new DbAdapter(this);
		dbAdapter.open();		
		initTasksListView();
	}

   //initialize all tasks and add them to listview from db
   @SuppressWarnings("deprecation")
   public void initTasksListView() {
			cursor = dbAdapter.getAllTasks();
			startManagingCursor(cursor);
			
	      //specify which columns go into which views for the cursor adapter
			String[] fromColumns = new String[]{DbAdapter.TASK_COLUMN_NAME};
			//load data into layout components
			int[] toViews = new int[]{R.id.view_listview};
			
		   //create an empty adapter to display the loaded data
	      //we pass null for the cursor, then update it in onLoadFinished()*
			listViewAdapter = new SimpleCursorAdapter(this,
					R.layout.activity_view_listview, cursor, fromColumns, toViews);
			//set adapter for the list view
			this.listView.setAdapter(listViewAdapter);
			
		   //prepare the loader
	      //getLoaderManager().initLoader(0, null, this);
	}
	

	//event handler for item clicked from listView
	private void listViewItemClickHandler(AdapterView<?> adapterView, View listView, int itemId) {
		//new task object with data to be passed to next activity to show detail
		Task taskItem = new Task();
		//move cursor to right position
		cursor.moveToFirst();
		cursor.move(itemId);
		//sets data for task item selected
		taskItem.setId(cursor.getString(cursor.getColumnIndex(DbAdapter.TASK_COLUMN_ID)));
		taskItem.setName(cursor.getString(cursor.getColumnIndex(DbAdapter.TASK_COLUMN_NAME)));
		taskItem.getDate().setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DbAdapter.TASK_COLUMN_DATE)));
		taskItem.getTime().setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DbAdapter.TASK_COLUMN_TIME)));
		taskItem.setPriority(cursor.getInt(cursor.getColumnIndex(DbAdapter.TASK_COLUMN_PRIORITY)));
		taskItem.setNotification(cursor.getInt(cursor.getColumnIndex(DbAdapter.TASK_COLUMN_NOTIFICATION)));
		//start activity
		NavigationHandler.viewTask(this, taskItem);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.view_all_tasks_actionbar_add:
			NavigationHandler.addTask(this, this.dbAdapter);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_main, menu);
		return true;
	}

	/*
   // Called when a new Loader needs to be created
   public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      // Now create and return a CursorLoader that will take care of
      // creating a Cursor for the data being displayed.
      return new CursorLoader(this);
   }

   // Called when a previously created loader has finished loading
   public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       // Swap the new cursor in.  (The framework will take care of closing the
       // old cursor once we return.)
      listViewAdapter.swapCursor(data);
   }

   // Called when a previously created loader is reset, making the data unavailable
   public void onLoaderReset(Loader<Cursor> loader) {
       // This is called when the last Cursor provided to onLoadFinished()
       // above is about to be closed.  We need to make sure we are no
       // longer using it.
      listViewAdapter.swapCursor(null);
   }
*/

}
