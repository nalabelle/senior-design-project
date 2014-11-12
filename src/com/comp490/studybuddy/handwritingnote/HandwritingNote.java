//Author: Tom, Xintong
package com.comp490.studybuddy.handwritingnote;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class HandwritingNote extends View {
	public Button btnEraseAll;
	public LayoutParams params;
	private Paint brush = new Paint();
	private Path path = new Path();
	private Canvas canvas;
	//Will hold a history of previous paths to enable undo functionality.
	private final List<Path> pathList = new ArrayList<Path>();
    //Will hold a history of previous colors to enable undo functionality.
	private final List<Paint> paintList = new ArrayList<Paint>();
	//Stores the last coordinates for building a path.
	private float lastX, lastY;

	public HandwritingNote(Context context) {
		super(context);
		brush.setAntiAlias(true);
		brush.setDither(true);
		brush.setColor(Color.BLACK);
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeCap(Paint.Cap.ROUND);
		brush.setStrokeJoin(Paint.Join.ROUND);
		brush.setStrokeWidth(15f);

		btnEraseAll = new Button(context);
		btnEraseAll.setText("Erase Everything!!");
		params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		btnEraseAll.setLayoutParams(params);
		btnEraseAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// reset the path
				path.reset();
				//clear undo array list
				clearUndoCache();
				// invalidate the view
				postInvalidate();

			}
		});
	}

	@Override
	protected void onDraw(Canvas drawCanvas) {
		//Fill the bitmap with default background color
		drawCanvas.drawColor(Color.WHITE);
		//Draw path
		drawCanvas.drawPath(path, brush);
	}
	
	public final void startDraw(final float x, final float y) {
		path.reset();
		path.moveTo(x, y);
		lastX = x;
		lastY = y;
	}
	
	public final void stopDraw() {
		// end the path and draw it
		path.lineTo(lastX, lastY);
		canvas.drawPath(path, brush);

		// storing undo information
		pathList.add(new Path(path));
		paintList.add(new Paint(brush));

		path.reset();
		invalidate();
	}
	
	//undo the paths
	public final void undo() {
		// check if there is a path
		if (pathList.isEmpty()) {
			return;
		}

		pathList.remove(pathList.size() - 1);
		paintList.remove(paintList.size() - 1);

		redraw();
	}
	
	//Redraws the saved Paths
	public final void redraw() {
		// check if there is a path
		if (pathList.isEmpty()) {
			return;
		}

		final int plsize = pathList.size();
		for (int i = 0; i < plsize; i++) {
			canvas.drawPath(pathList.get(i), paintList.get(i));
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float pointX = event.getX();
		float pointY = event.getY();

		// Checks for the event that occurs
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(pointX, pointY);

			return true;
		case MotionEvent.ACTION_MOVE:
			path.lineTo(pointX, pointY);
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			return false;
		}
		// Force a view to draw.
		//postInvalidate();
		return false;

	}
	
	//Setter for pen color
	public final void setPaintColor(final int color) {
		brush.setColor(color);
	}
	//Getter for pen color
	public final int getPaintColor() {
		return brush.getColor();
	}
	
	//Setter for pen width
	public final void setPenWidth(final float width) {
		brush.setStrokeWidth(width);
	}
	//Getter for pen width
	public final float getPenWidth() {
		return brush.getStrokeWidth();
	}
	
	//clear undo cache
	public final void clearUndoCache() {
		pathList.clear();
		paintList.clear();
	}
}

