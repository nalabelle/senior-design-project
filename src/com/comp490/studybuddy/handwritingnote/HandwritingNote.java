//Author: Tom, Xintong
package com.comp490.studybuddy.handwritingnote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
	private static final float TOUCH_TOLERANCE = 4;
	
	private int screenWidth, screenHeight;

	//store path history in array list for undo use
	private static ArrayList<DrawPath> savePath;
	//store deleted path in array list for redo use
	private static ArrayList<DrawPath> deletedPath;
	private DrawPath dp;
	
	private class DrawPath {
		public Path dPath;
		public Paint dPaint;
	}
	
	private int currStyle = 1;
	private int currColor = Color.BLACK;
	private int currWidth = 5;
	
	//constructor
	public HandwritingNote(Context context, int w, int h) {
		super(context);
		screenWidth = w;
		screenHeight = h;		
		
		initCanvas();
		
		savePath = new ArrayList<DrawPath>();
		deletedPath = new ArrayList<DrawPath>();
	}
	
	public void initCanvas(){	
		setPaint();
		bitmapPaint = new Paint(Paint.DITHER_FLAG);	
		bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);	
		canvas = new Canvas(bitmap);
		
		canvas.drawColor(Color.WHITE);
		path = new Path();
		bitmapPaint = new Paint(Paint.DITHER_FLAG);	
	}
	
	public void setPaint(){
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(currWidth);
		if(currStyle == 1){
			paint.setColor(currColor);
		}
		else
			paint.setColor(Color.WHITE);
	}
	
	public void setPenStyle(int s){
		if(s == 0){
			currStyle = 1;
			setPaint();
		}
		if(s == 1){
			currStyle = 2;
			setPaint();
			paint.setStrokeWidth(20);
		}
	}
	
	public void setPenWidth(int w) {
		currWidth = w;
		setPaint();
	}
	
	public void setPenColor(int c){
		if(c == 1){
			currColor = Color.BLACK;
		}
		else if(c == 2){
			currColor = Color.BLUE;
		}
		else if(c == 3){
			currColor = Color.CYAN;
		}
		else if(c == 4){
			currColor = Color.GREEN;
		}
		else if(c == 5){
			currColor = Color.RED;
		}
		else if(c == 6){
			currColor = Color.YELLOW;
		}
		else{
			currColor = Color.WHITE;
		}
		setPaint();
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
		if (path != null) {
			canvas.drawPath(path, paint);
		}
	}

	private void startDraw(float x, float y) {
		path.reset();
		path.moveTo(x, y);
		tempX = x;
		tempY = y;
	}

	private void continueDraw(float x, float y) {
		float dx = Math.abs(x - tempX);
		float dy = Math.abs(y - tempY);
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
	
	private void redraw() {
		bitmap = Bitmap.createBitmap(screenWidth, screenHeight,Bitmap.Config.ARGB_8888);
		canvas.setBitmap(bitmap);
		Iterator<DrawPath> iter = savePath.iterator();
		while (iter.hasNext()) {
			DrawPath tempDp = iter.next();
			canvas.drawPath(tempDp.dPath, tempDp.dPaint);
		}
		invalidate();
	}
	
	public void undo() {
		if (savePath != null && savePath.size() > 0) {
			initCanvas();
			
			//deleted the latest draw path from save path and store it in deleted draw path
			DrawPath tempDp = savePath.get(savePath.size() - 1);
			deletedPath.add(tempDp);
			savePath.remove(savePath.size() - 1);
			
			redraw();
		}
	}

	public void redo() {
		//get the latest draw path from deleted path and reverted back to save path
		if (deletedPath.size() > 0) {
			DrawPath tempDp = deletedPath.get(deletedPath.size() - 1);
			savePath.add(tempDp);
			canvas.drawPath(tempDp.dPath,tempDp.dPaint);
			deletedPath.remove(deletedPath.size() - 1);
			invalidate();
		}
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