package com.comp490.studybuddy.note;

import android.net.Uri;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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
		vid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		vid.setVideoURI(data);
		android.widget.FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
				300, 300); //Need to fix this to appropriate dimensions based on device
		vid.setLayoutParams(fl);

		vid.setOnLongClickListener(new View.OnLongClickListener() {			
			@Override
			public boolean onLongClick(View v) {
				ActionMode.Callback textMenu = new VideoMenu(noteActivity, videoBuilder);
				noteActivity.startActionMode(textMenu);
				return true;
			}
		});
		
		MediaController media_Controller = new MediaController(noteActivity);
		media_Controller.setAnchorView(vid);
		vid.setMediaController(media_Controller);
		LinearLayout layout = (LinearLayout) noteActivity.findViewById(R.id.note_inner_layout);
		layout.addView(vid);
		

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
