package com.comp490.studybuddy.models;

import android.util.Log;

/* NoteEntryModel stores all relevant data about the note objects we create.
 * Includes absolute file paths to media files, ViewIds of corresponding
 * buttons, media type, etc. Eventually will probably store data about
 * x/y positions. 
 */

public class NoteEntryModel {
	//I store NoteEntries!
	@Attributes(primaryKey = true, notNull = false)
	private int id; //do something with this?
	@Attributes(notNull = true, primaryKey = false)
	private String name;
	@Attributes(notNull = true, primaryKey = false)
	private NoteType type;
	//private String description;
	private String filePath = null; //May not have one, who knows?
	private int viewID = -1;
	private int secondaryViewID = -1;

	
	public NoteEntryModel(NoteType type) {
		this.type = type;
	}
	
	public enum NoteType {
		TEXT, AUDIO, PICTURE, VIDEO;
	}

	public void setName(String text) {
		this.name = text;
	}

	public NoteType getType() {
		return type;
	}

	public String getName() {
		return this.name;
	}
	
	public void setViewID(int viewID){ 
		this.viewID = viewID;
	}
	
	public int getViewID(){
		if (viewID < 0){
			Log.e("NoteEntryModel", "invalid view ID or was not set");
		}
		return viewID; 
	}
	
	//Right now this is only used to rename the title of sound onscreen
	//button, which is stored in a textview
	public void setSecondaryViewID(int viewID){ 
		this.secondaryViewID = viewID;
	}
	
	public int getSecondaryViewID(){
		if (viewID < 0){
			Log.e("NoteEntryModel", "invalid view ID or was not set");
		}
		return secondaryViewID; 
	}
	
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	
	public String getFilePath(){
		if (filePath == null){
			Log.e("NoteEntryModel", "invalid model filepath or was not set");
		}
		return filePath;
	}

	public void setType(NoteType newType) {
		this.type = newType;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}
}
