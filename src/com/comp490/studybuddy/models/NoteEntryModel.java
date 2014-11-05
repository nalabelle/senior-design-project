package com.comp490.studybuddy.models;

public class NoteEntryModel {
	//I store NoteEntries!
	private NoteType type;
	private String name;
	private String description;
	private String filePath; //May not have one, who knows?
	
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
}
