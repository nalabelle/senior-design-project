//Author: Tom, Xintong
package com.comp490.studybuddy.handwritingnote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;

public class HandwritingNote extends View {
	private Bitmap bitmap;
	private Canvas canvas;
	private Path path;
	private Paint bitmapPaint;
	private Paint paint;
	private float tempX, tempY;
	private int screenWidth, screenHeight;
	private DrawPath dp;
	private static final float TOUCH_TOLERANCE = 4;

	//store path history in array list
	private static List<DrawPath> savePath;
	
	private class DrawPath {
		public Path dPath;
		public Paint dPaint;
	}
	
	//constructor
	public HandwritingNote(Context context, int w, int h) {
		super(context);
		screenWidth = w;
		screenHeight = h;

		bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);

		bitmapPaint = new Paint(Paint.DITHER_FLAG);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(5);

		savePath = new ArrayList<DrawPath>();
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
		if (path != null) {
			canvas.drawPath(path, paint);
		}
	}

	private void startDraw(float x, float y) {
		path.moveTo(x, y);
		tempX = x;
		tempY = y;
	}

	private void continueDraw(float x, float y) {
		float dx = Math.abs(x - tempX);
		float dy = Math.abs(tempY - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			path.quadTo(tempX, tempY, (x + tempX) / 2, (y + tempY) / 2);
			tempX = x;
			tempY = y;
		}
	}

	private void finishDraw() {
		path.lineTo(tempX, tempY);
		canvas.drawPath(path, paint);
		savePath.add(dp);
		path = null;
	}

	public void undo() {
		if (savePath != null && savePath.size() > 0) {
			savePath.remove(savePath.size() - 1);
			redraw();
		}
	}

	public void redo() {
		if (savePath != null && savePath.size() > 0) {
			savePath.clear();
			redraw();
		}
	}

	private void redraw() {
		bitmap = Bitmap.createBitmap(screenWidth, screenHeight,Bitmap.Config.ARGB_8888);
		canvas.setBitmap(bitmap);
		Iterator<DrawPath> iter = savePath.iterator();
		while (iter.hasNext()) {
			DrawPath drawPath = iter.next();
			canvas.drawPath(drawPath.dPath, drawPath.dPaint);
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path = new Path();
			dp = new DrawPath();
			dp.dPath = path;
			dp.dPaint = paint;
			startDraw(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			continueDraw(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			finishDraw();
			invalidate();
			break;
		}
		return true;
	}
	
	public void setPenWidth(int w) {
		paint.setStrokeWidth(w);
	}
	
	public void setPenColor(int c){
		if(c == 1){
			paint.setColor(Color.BLACK);
		}
		else if(c == 2){
			paint.setColor(Color.BLUE);
		}
		else if(c == 3){
			paint.setColor(Color.CYAN);
		}
		else if(c == 4){
			paint.setColor(Color.GREEN);
		}
		else if(c == 5){
			paint.setColor(Color.RED);
		}
		else if(c == 6){
			paint.setColor(Color.YELLOW);
		}
		else
			paint.setColor(Color.WHITE);
	}
	
	public void saveFile() {
		String fileUrl = Environment.getExternalStorageDirectory().toString()
				+ "/handwriting.png";

		try {
			FileOutputStream fos = new FileOutputStream(new File(fileUrl));
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}