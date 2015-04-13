package com.comp490.studybuddy.note;

import java.sql.SQLException;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.models.NoteEntry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class UpdateView {
	static int _xDelta;
	static int _yDelta;

	public static void move(View view, NoteActivity noteActivity) {
		final ViewGroup parent = (ViewGroup) noteActivity
				.findViewById(R.id.note_layout);

		OnTouchListener listen = new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				final int X = (int) event.getRawX();
				final int Y = (int) event.getRawY();

				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view
							.getLayoutParams();
					_xDelta = X - lParams.leftMargin;
					_yDelta = Y - lParams.topMargin;
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					break;
				case MotionEvent.ACTION_POINTER_UP:
					break;
				case MotionEvent.ACTION_MOVE:
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
							.getLayoutParams();
					//no protection for moving views offscreen
					layoutParams.leftMargin = X - _xDelta;
					layoutParams.topMargin = Y - _yDelta;
					layoutParams.rightMargin = -250;
					layoutParams.bottomMargin = -250;
					view.setLayoutParams(layoutParams);
					break;
				}
				parent.invalidate();
				return true;
			}
		};

		view.setOnTouchListener(listen);
	}
	
	public static void setXY(NoteEntry entry, NoteActivity noteActivity, View view){
			entry.setXY(view.getX(), view.getY());	
			try {
				noteActivity.getHelper().getNoteEntryDao().update(entry);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	
/* Just in case
	protected static void deleteObject(NoteEntry entry, NoteActivity noteActivity){
		try {
			noteActivity.deleteNote(entry);
			noteActivity.getHelper().getNoteEntryDao().delete(entry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	//Removes view and updates DB
	protected static void deleteView(final NoteEntry entry, final NoteActivity noteActivity, final View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(noteActivity);
      builder.setIcon(android.R.drawable.ic_dialog_alert);
      builder.setTitle("Delete item?");
		builder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						try { // clicked yes
							((ViewGroup) view.getParent())
									.removeView(view);
							try {
								noteActivity.deleteNote(entry);
								noteActivity.getHelper().getNoteEntryDao().delete(entry);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (Exception e1) {
							Log.e(entry.getType().toString(), "Delete of View failed");
						}
					}
				});
		builder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
