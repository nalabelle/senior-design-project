package com.comp490.studybuddy.models;

import java.util.ArrayList;

import com.comp490.studybuddy.models.NoteEntryModel.NoteType;

public class NoteModel {
	//I store collections of NoteEntries!
	private ArrayList<NoteEntryModel> entries = new ArrayList<NoteEntryModel>();
	
	public NoteEntryModel add(NoteType type) {
		NoteEntryModel note = new NoteEntryModel(type);
		entries.add(note);
		return note;
	}

	public boolean add(NoteEntryModel note) {
		return entries.add(note);
	}
}
