//Author: Xintong Shi(Summer)
package com.comp490.studybuddy.handwritingnote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class DrawingNote extends View implements View.OnTouchListener {
	private Bitmap bitmap;
	private Canvas canvas;
	private Paint paint;

	//if it has been modified
	private boolean isModified = false;
	//whether its cleared or not
	private boolean isCleared = false;
	//whether its new or loaded
	private boolean isNewHwNote = true;
	
	
	public interface DrawingNoteListener{
		void hasModified();
		void hasTODO();
	}
	
	private final DrawingNoteListener dnListener;
	
	public DrawingNote(final Context context, final DrawingNoteListener listener){
		super(context);
		dnListener = listener;
		initialize();
	}
	
	//for undo purpose
	private final List<Path> pathList = new ArrayList<Path>();
	private final List<Paint> paintList = new ArrayList<Paint>();
	
	//initialize
	public final void initialize(){
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		//paint.setStrokeWidth(   );
		paint.setStyle(Paint.Style.STROKE);
		
		setOnTouchListener(this);
		setBackgroundColor(Color.WHITE);
		setWillNotDraw(true);
	}
	
	@Override
	public final void onDraw(final Canvas drawCanvas){
		drawCanvas.drawColor(Color.WHITE);
		drawCanvas.drawBitmap(bitmap, 0f, 0f, paint);
		drawCanvas.drawPath(path, paint);
	}
	
	@Override
	public final boolean onTouch(final View view, final MotionEvent event){
		switch (event.getAction()){
		case MotionEvent.ACTION_MOVE:
			continueDrawing(event.getX(), event.getY());
			break;
			
		case MotionEvent.ACTION_DOWN:
			startDrawing(event.getX(), event.getY());
			break;
		
		case MotionEvent.ACTION_UP:
			stopDrawing();
			break;
		
		default:
			break;
		}
		return true;
	}
		
	private final Path path = new Path();
	
	private float lastX, lastY;
	
	public final void startDrawing(final float x, final float y){
		path.reset();
		path.moveTo(x, y);
		lastX = x;
		lastY = y;
		
		isModified = true;
		dnListener.hasModified();
	}
	
	public final void continueDrawing(final float x, final float y){
		
	}
	
	public final void stopDrawing(){
		//end path
		path.lineTo(lastX, lastY);
		canvas.drawPath(path, paint);
		//save to arraylist
		pathList.add(new Path(path));
		paintList.add(new Paint(paint));
		//clear path
		path.reset();
	}
	
	public final void undoDrawing(){
		if(pathList.isEmpty()){
			return;
		}
		clearDrawing(true);
		
		pathList.remove(pathList.size()-1);
		paintList.remove(paintList.size()-1);
		
		reDrawing();
	}
	
	public final void clearDrawing(final boolean clear){
		
	}
	
	public final void reDrawing(){
		if(pathList.isEmpty()){
			return;
		}
		
		final int pathListSize = pathList.size();
		for(int i = 0; i < pathListSize; i++){
			canvas.drawPath(pathList.get(i),paintList.get(i));
		}
	}
	
	public final Bitmap getBitmap(){
		return bitmap;
	}
	
	public final void setBitmap(){
		
	}
	
	public final void loadBitmap(){
		
	}
	
	public final int getPenColor(){
		return paint.getColor();
	}
	public final void setPenColor(final int color){
		paint.setColor(color);
	}
	public final float getPenWidth(){
		return paint.getStrokeWidth();
	}
	public final void setPenWidth(final float width){
		paint.setStrokeWidth(width);
	}
	public final Paint getPaint(){
		return paint;
	}
	public final void setPaint(final Paint newPaint){
		this.paint = newPaint;
	}
	public final boolean getIsModified(){
		return isModified;
	}
	public final void serIsModified(final boolean YorN){
		this.isModified = YorN;
	}
	public final boolean clearStatus(){
		return !isModified && isCleared;
	}
	public final void recycle(){
		if(bitmap != null){
			bitmap.recycle();
		}
	}
}
