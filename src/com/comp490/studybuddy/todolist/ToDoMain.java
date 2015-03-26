/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */


package com.comp490.studybuddy.todolist;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.calendar.CalenActivity;
import com.comp490.studybuddy.flashcards.FlashMain;
import com.comp490.studybuddy.models.Task;
import com.comp490.studybuddy.note.NoteActivity;
import com.j256.ormlite.dao.Dao;

public class ToDoMain extends DefaultActivity implements Dao.DaoObserver {

	private ListView listView;
   //adapter to display the list's data
	private ArrayAdapter<Task> listViewAdapter;
	public static final int ADD_NEW_TASK = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_main);
		listView = (ListView) findViewById(R.id.view_all_tasks_listview);
		listView.setSelector(R.drawable.selector);
		
		//Register ourselves as a DAO observer so we can update on change.
		try {
			getHelper().getTaskDao().registerObserver(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
			
		initTasksListView();
	}

   //initialize all tasks and add them to listview from db
   public void initTasksListView() {
			List<Task> list = null;
			try {
				list = getHelper().getTaskDao().queryForAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			if(list == null) return;
		   //create an adapter to display the loaded data
			listViewAdapter = new ArrayAdapter<Task>(this,
					R.layout.activity_view_listview, R.id.view_listview, list);
			//set adapter for the list view
			this.listView.setAdapter(listViewAdapter);
	}

	//event handler for item short clicked from listView
	private void listViewItemClickHandler(AdapterView<?> adapterView, View listView, int itemId) {
		//new task object with data to be passed to next activity to show detail
		Task taskItem = this.listViewAdapter.getItem(itemId);
		//start activity
		NavigationHandler.viewTask(this, taskItem);
	}
	
	//event handler for item long clicked from listView
   private void listViewItemLongClickHandler(AdapterView<?> adapterView, View listView, int itemId) {
      Task taskItem = this.listViewAdapter.getItem(itemId);
      DeleteHandler.deleteDialog(this, taskItem);
   }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.view_all_tasks_actionbar_add:
			NavigationHandler.addTask(this);
			return true;
	      case R.id.action_launch_text: {
	           startActivity(new Intent(this, NoteActivity.class));
             return true;
	      }
		case R.id.action_launch_flashcards: {
			startActivity(new Intent(this, FlashMain.class));
			return true;
		      }
		case R.id.action_launch_calendar: {
			startActivity(new Intent(this, CalenActivity.class));
			return true;
			}   
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

	@Override
	//This should update the list menu when the Task DAO is changed.
	public void onChange() {
		this.initTasksListView();
	}
	
	@Override
	protected void onDestroy() {
		try {
			getHelper().getTaskDao().unregisterObserver(this);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

}

