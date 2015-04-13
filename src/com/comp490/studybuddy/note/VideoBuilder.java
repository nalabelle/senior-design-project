package com.comp490.studybuddy.note;

import java.sql.SQLException;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;

/* This class creates an button for a recorded video. It updates the model 
 *  entry with the button's ID and the video Uri (stored as a string in 
 *  filepath field). The button when clicked spawns a menu to allow playback.
 */
public class VideoBuilder {
	private NoteActivity noteActivity;
	private VideoBuilder videoBuilder = this;
	private NoteEntry entry;
	private ImageButton videoButton;

	public VideoBuilder(NoteActivity noteActivity, NoteEntry entry) {
		this.noteActivity = noteActivity;
		this.entry = entry;
		createVideoButton();
	}

	private void createVideoButton() {
		videoButton = new ImageButton(noteActivity);
		videoButton.setImageResource(R.drawable.ic_action_video_dark);
		LayoutParams llParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		videoButton.setLayoutParams(llParams);	

		// Save relevant info in data structure
		int viewID = noteActivity.generateViewID();
		videoButton.setId(viewID);
		entry.setViewID(viewID);
		if(entry.getX() != 0) {
			videoButton.setX(entry.getX());
			videoButton.setY(entry.getY());
		}

		ViewGroup layout = (ViewGroup) noteActivity
				.findViewById(R.id.note_layout);
		layout.addView(videoButton);
		
		Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail( Uri.parse(entry.getFilePath()).getPath(), MediaStore.Video.Thumbnails.MINI_KIND );
		//this should work but doesn't ^
		//videoButton.setImageBitmap(thumbnail);

		videoButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// videoBuilder has Uri data
				ActionMode.Callback vidMenu = new VideoMenu(noteActivity,
						videoBuilder);
				noteActivity.startActionMode(vidMenu);
				return true;
			}
		});
		
		//creates the note in the DB
		try {
			this.noteActivity.getHelper().getNoteEntryDao().createOrUpdate(entry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPath() {
		return this.entry.getFilePath();
	}
	
	protected NoteEntry getEntry(){
		return entry;
	}
}
