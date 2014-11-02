//Author: Xintong Shi (Summer)
package com.comp490.studybuddy.handwritingnote;

import android.app.Application;

public class HandwritingNote extends Application {
	//application name
	public static final String appName = "StudyBuddy";
	//thumbnail saved in sub folder
	public static final String saveThumbnail = "/thumbnail/";
	//cache saved in sub folder
	public static final String saveCache = "/studybuddy/.chache/";
	
	public static final String handwritingNoteName = "saveHwNoteName";
	//error message
	public static final String fileError = "FILE ERROR";
	
	public static final int jpgQuality = 70;
	
	public static final int pngQuality = 100;
	
	public static final String jpgExtension = ".jpg";
	
	public static final String pngExtension = ".png";
	
	public static final int maxHwNotePage = 2;
	
	//TODO: color and pen width
}
