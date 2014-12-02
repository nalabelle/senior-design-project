package com.comp490.studybuddy.todolist;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.R.id;
import com.comp490.studybuddy.R.layout;
import com.comp490.studybuddy.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ToDoMain extends Activity
{

@Override
protected void onCreate(Bundle savedInstanceState)
{
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_to_do_main);
}

@Override
public boolean onCreateOptionsMenu(Menu menu)
{
   // Inflate the menu; this adds items to the action bar if it is present.
   getMenuInflater().inflate(R.menu.to_do_main, menu);
   return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item)
{
   // Handle action bar item clicks here. The action bar will
   // automatically handle clicks on the Home/Up button, so long
   // as you specify a parent activity in AndroidManifest.xml.
   int id = item.getItemId();
   if (id == R.id.action_settings)
   {
      return true;
   }
   return super.onOptionsItemSelected(item);
}
}
