package com.comp490.studybuddy.note;

import android.net.Uri;
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
	private int viewID;
	private ImageButton videoButton;
	Uri data;

	public VideoBuilder(NoteActivity noteActivity, Uri data, NoteEntry entry) {
		this.noteActivity = noteActivity;
		this.entry = entry;
		this.data = data;
		createVideoButton();
		this.entry.setType(NoteEntry.NoteType.VIDEO);
	}

	private void createVideoButton() {
		videoButton = new ImageButton(noteActivity);
		videoButton.setImageResource(R.drawable.ic_action_video_dark);
		LayoutParams llParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		videoButton.setLayoutParams(llParams);	

		// Save relevant info in data structure
		viewID = noteActivity.generateViewID();
		videoButton.setId(viewID);
		entry.setViewID(viewID);
		entry.setFilePath(data.toString()); // When loading, need to parse back
														// into an Uri with Uri.parse(

		ViewGroup layout = (ViewGroup) noteActivity
				.findViewById(R.id.note_layout);
		layout.addView(videoButton);

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
	}

	protected int getID() {
		return viewID;
	}

	// might be unnecessary
	protected void deleteObject() {
		noteActivity.deleteNote(entry);
		videoButton = null;
		videoBuilder = null;
	}
}
