/**
 * Author: Uyen Nguyen 
 * Date started: 
 * Date Completed: 
 * Team members: 
 * Contribution: 
 */
package com.comp490.studybuddy;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.comp490.studybuddy.calendar.CalenActivity;
import com.comp490.studybuddy.flashcards.FlashMain;
import com.comp490.studybuddy.handwritingnote.HandwritingMain;
import com.comp490.studybuddy.models.NoteEntry;
import com.comp490.studybuddy.note.AudioBuilder;
import com.comp490.studybuddy.note.NoteActivity;
import com.comp490.studybuddy.note.PhotoViewerActivity;
import com.comp490.studybuddy.note.PictureBuilder;
import com.comp490.studybuddy.note.TextBuilder;
import com.comp490.studybuddy.note.VideoViewerActivity;
import com.comp490.studybuddy.todolist.ToDoMain;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
	   switch(item.getItemId()) {
	      case R.id.action_launch_sound: {
	         startActivity(new Intent(this, NoteActivity.class));
            return true;
	      }
	      case R.id.action_launch_photo: {
	         startActivity(new Intent(this, PhotoViewerActivity.class));
            return true;
	      }
	      case R.id.action_launch_video: {
	         startActivity(new Intent(this, VideoViewerActivity.class));
            return true;
	      }
	      case R.id.action_launch_text: {
	           startActivity(new Intent(this, NoteActivity.class));
              return true;
	      }
	      case R.id.action_launch_handwriting: {
	           startActivity(new Intent(this, HandwritingMain.class));
              return true;
	      }
	      case R.id.action_launch_flashcards: {
	           //startActivity(new Intent(this, FlashMain.class));
            return true;
	      }
	      case R.id.action_launch_calendar: {
	           startActivity(new Intent(this, CalenActivity.class));
              return true;
	      }
	      case R.id.action_launch_todo: {
	           startActivity(new Intent(this, ToDoMain.class));
	           return true;
	      }
	      default:
	           return super.onOptionsItemSelected(item);
	      }
	   }
	  
	  
	public void onClickStartService(View V)
	{
		startService(new Intent(this,MyService.class));
	}
	
	public void onClickStopService(View V)
	{
		stopService(new Intent(this,MyService.class));
	}
	
	public boolean onTextNoteClick(View view) {
		Intent noteLaunch = new Intent(this.getApplicationContext(), NoteActivity.class);
		startActivity(noteLaunch);
		return true;
	}
	
	public boolean onCalClick(View view) {
		Intent calLaunch = new Intent(this.getApplicationContext(), CalenActivity.class);
		startActivity(calLaunch);
		return true;
	}
	
   public boolean onTodoClick(View view) {
      Intent todoLaunch = new Intent(this.getApplicationContext(), ToDoMain.class);
      startActivity(todoLaunch);
      return true;
   }
   
   public boolean onHandwritingClick(View view) {
	      Intent handwritingLaunch = new Intent(this.getApplicationContext(), HandwritingMain.class);
	      startActivity(handwritingLaunch);
	      return true;
	   }
   
   public boolean onFlashcardhClick(View view) {
      Intent FlashcardLaunch = new Intent(this.getApplicationContext(), FlashMain.class);
      startActivity(FlashcardLaunch);
      return true;
   }
 }
