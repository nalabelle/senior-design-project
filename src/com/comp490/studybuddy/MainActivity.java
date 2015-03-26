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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.comp490.studybuddy.calendar.CalenActivity;
import com.comp490.studybuddy.flashcards.FlashMain;
import com.comp490.studybuddy.note.NoteActivity;
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
	   case R.id.about_dialog: {
		   Dialog about = new AlertDialog.Builder(this)
	         .setIcon(android.R.drawable.ic_dialog_info)
	         .setTitle(R.string.about_title)
	         .setMessage(R.string.about_msg)
	         .setNegativeButton(R.string.close,
               new NegativeButtonListener())
	         .create();
	      about.show();
		  return true;
	   }
	   /*
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
	           startActivity(new Intent(this, FlashMain.class));
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
	      */
	      default:
	           return super.onOptionsItemSelected(item);
	      }
	   }

	   //event handler for No button 
	   private static class NegativeButtonListener implements OnClickListener {
	      @Override
	      public void onClick(DialogInterface dialog, int val) {
	         //close dialog
	         dialog.dismiss();
	      }
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
   
   public boolean onFlashcardhClick(View view) {
      Intent FlashcardLaunch = new Intent(this.getApplicationContext(), FlashMain.class);
      startActivity(FlashcardLaunch);
      return true;
   }
 }
