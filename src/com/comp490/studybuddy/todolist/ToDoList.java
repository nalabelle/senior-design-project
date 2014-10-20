/**
 * Author: Uyen Nguyen
 * Date started: 9/26/2014
 * Date Completed: IP
 * Peer Review: IP 
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;
import com.comp490.studybuddy.R;
import com.comp490.studybuddy.todolist.DBHelper;
import com.comp490.studybuddy.todolist.AddTask;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;

public class ToDoList extends ListActivity {
   public static final String TASK_ID = "taskId";
   public static final String TASK_NAME = "taskName";
   public static final String TASK_DATE = "taskDate";
   Intent intent;
   TextView task;
   DBHelper db = new DBHelper(this);
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_to_do_list);
      ArrayList<HashMap<String, String>> taskList =  db.getAllTasks();
      if(taskList.size()!=0) {
         ListView lv = getListView();
         lv.setOnItemClickListener(new OnItemClickListener() {
            @Override 
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               task = (TextView) view.findViewById(R.id.taskId);
               String task_tag = task.getText().toString();               
               Intent  intent = new Intent(getApplicationContext(), UpdateTask.class);
               intent.putExtra(TASK_ID, task_tag); 
               startActivity(intent); 
            }
         }); 
         ListAdapter adapter = new SimpleAdapter(ToDoList.this, taskList, R.layout.task_view, new String[] {TASK_ID, TASK_NAME}, new int[] {R.id.taskId, R.id.taskName}); 
         setListAdapter(adapter);
      }
   }
   
   public void displayAddTask(View view) {
      Intent intent = new Intent(getApplicationContext(), AddTask.class);
      startActivity(intent);
   }
   
}
