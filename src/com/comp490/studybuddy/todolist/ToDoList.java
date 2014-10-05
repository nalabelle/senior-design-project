package com.comp490.studybuddy.todolist;

import com.comp490.studybuddy.R;
import java.util.ArrayList;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class ToDoList extends ListActivity {

    /** ArrayList to store tasks */
    ArrayList<String> list = new ArrayList<String>();

    /** ArrayAdapter to set items to ListView */
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_to_do_list);
       
       Button addButton = (Button) findViewById(R.id.addBtn);
       Button delButton = (Button) findViewById(R.id.delBtn);

       /** Defining the ArrayAdapter to set items to ListView with check box format*/
       adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list);
   
       /** Mouse click listener for add button */
       OnClickListener addListener = new OnClickListener() {
          @Override
          public void onClick(View v) {
             EditText addText = (EditText) findViewById(R.id.txtItem);
             list.add(addText.getText().toString());
             addText.setText("");
             adapter.notifyDataSetChanged();
             }
       };
               
       /** Mouse click listener for delete button */
       OnClickListener delListener = new OnClickListener() {
          @Override
          public void onClick(View v) {
             /** Get checked items from listview */
             SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
             int count = getListView().getCount();
            
             for(int i = count-1; i >= 0; i--) {
                if(checkedItemPositions.get(i)) {                
                   adapter.remove(list.get(i));
                }
             }  
             checkedItemPositions.clear();
             adapter.notifyDataSetChanged();
          }
       };            
           
   
       /** Set event listener for the add button */
       addButton.setOnClickListener(addListener);
       
       /** Set event listener for the delete button */
       delButton.setOnClickListener(delListener);      
   
       /** Set adapter to the ListView */
       setListAdapter(adapter);
    }
}