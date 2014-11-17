package com.comp490.studybuddy.models;

import android.util.Log;

public class NoteEntryModel {
	//I store NoteEntries!
	private NoteType type;
	private String name;
	private String description;
	private String filePath = null; //May not have one, who knows?
	private int viewID = -1;
	
	public NoteEntryModel(NoteType type) {
		this.type = type;
	}
	
	public enum NoteType {
		TEXT, AUDIO, PICTURE;
	}

	public void addFile(String file) {
		this.filePath = file;
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
	
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	
	public String getFilePath(){
		if (filePath == null){
			Log.e("NoteEntryModel", "invalid model filepath or was not set");
		}
		return filePath;
	}
}
