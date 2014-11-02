//Author: Xintong Shi (Summer)
package com.comp490.studybuddy.handwritingnote;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HandwritingNoteDetail {
	private String createdDate;
	
	private Bitmap thumbnail;
	private String fileName;
	
	//Constructor
	public HandwritingNoteDetail(final Context context, final String name){
		if(context == null){
			//TODO: print error message (context cannot be null)
			throw new IllegalArgumentException();
		}
		
		if(name == null){
			//TODO: print error message (name cannot be null)
			throw new IllegalArgumentException();
		}
		
		fileName = name;
		
		//get data from filename
		if(fileName.contains(".")){
			fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		
		final Date date = new Date(Long.parseLong(fileName));
		//final SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.date_time_format));
		//createdDate = sdf.format(date);
		
		final File imageFile = new File(context.getFilesDir() + HandwritingNote.saveThumbnail + fileName + HandwritingNote.jpgExtension);
		if(imageFile.exists()){
			thumbnail = BitmapFactory.decodeFile(imageFile.toString());
		}
		
	}
	
	public final void recycle(){
		thumbnail.recycle();
	}
	public final String getCreatedDate(){
		return createdDate;
	}
	public final Bitmap getThumbnail(){
		return thumbnail;
	}
	public final String getFileName(){
		return fileName;
	}
}
