/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.todolist;

import com.comp490.studybuddy.database.DBHelper;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

//Default layout configurations used for all activities
public class DefaultActivity extends OrmLiteBaseActivity<DBHelper> {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      ActionBar actionBar = getActionBar();
      actionBar.setDisplayHomeAsUpEnabled(true);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      //set home button as back button
      switch (item.getItemId()) {
      case android.R.id.home:
         this.finish();
         return true;
      default:
         return super.onOptionsItemSelected(item);
      }
   }
   
}

