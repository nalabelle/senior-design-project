package com.comp490.studybuddy.note;

import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntryModel;

public class VideoBuilder {
	private NoteActivity noteActivity;
	private VideoBuilder videoBuilder = this;
	private NoteEntryModel entry;
	private VideoView vid;
	private int viewID;
	Uri data;	
	
	public VideoBuilder(NoteActivity textNote, Uri data, NoteEntryModel entry){
		this.noteActivity = textNote;
		this.entry = entry;
		this.data = data;
		createVideoView();		
	}
	
	private void createVideoView(){
		vid = new VideoView(noteActivity);
		
		//set IDs for deletions
		viewID = noteActivity.generateViewID();
		vid.setId(viewID);
		entry.setViewID(viewID);
		
		//Insert video
		vid.setVideoURI(data);
		android.widget.FrameLayout.LayoutParams params;
		
		 DisplayMetrics metrics = new DisplayMetrics(); 
		 noteActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 params = new FrameLayout.LayoutParams((int)metrics.xdpi, (int) metrics.ydpi);
       //params.width =  metrics.widthPixels;
       //params.height = metrics.heightPixels;
       //params.leftMargin = 0;
       vid.setLayoutParams(params);

 		LinearLayout layout = (LinearLayout) noteActivity.findViewById(R.id.note_inner_layout);
 		layout.addView(vid);

		vid.setOnTouchListener(new View.OnTouchListener()
	    {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	      	  ActionMode.Callback vidMenu = new VideoMenu(noteActivity, videoBuilder);
					noteActivity.startActionMode(vidMenu);
	            return false;
	        }
	    });	
		
		// Creates playback controls
		MediaController media_Controller = new MediaController(noteActivity);
		media_Controller.setAnchorView(vid);
		vid.setMediaController(media_Controller);
	}
	
	protected int getID(){
		return viewID;
	}
	
	// might be unnecessary
	protected void deleteObject(){
		noteActivity.getNoteModel().remove(entry);
		vid = null;
		videoBuilder = null;
	}
}
