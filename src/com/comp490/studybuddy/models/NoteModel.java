package com.comp490.studybuddy.models;

import java.util.ArrayList;

import android.content.Context;
import com.comp490.studybuddy.database.DBAdapter;
import com.comp490.studybuddy.models.NoteEntryModel.NoteType;

public class NoteModel {
	//I store collections of NoteEntries! Essentially holds the data of
	//our current note composed of notentries.
	private ArrayList<NoteEntryModel> entries = new ArrayList<NoteEntryModel>();
	
	public NoteEntryModel add(NoteType type) {
		NoteEntryModel note = new NoteEntryModel(type);
		entries.add(note);
		return note;
	}

	public boolean add(NoteEntryModel note) {
		return entries.add(note);
	}
	
	//Not 100% this works
	public boolean remove(NoteEntryModel note){
		return entries.remove(note);
	}
	
	public void getEntriesFromDatabase(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		
		dbAdapter.getAllNotes();
		
		android.database.Cursor cursor = dbAdapter.getAllTasks();
		if(!cursor.moveToFirst()) return; //no data
		
		while(!cursor.isAfterLast()) {			
			int id = cursor.getInt(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_ID));
			String name = cursor.getString(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_NAME));
			String type = cursor.getString(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_TYPE));
			String path = cursor.getString(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_PATH));
			int viewId = cursor.getInt(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_VIEWID));
			int secondaryViewId = cursor.getInt(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_SECONDARY_VIEWID));
			
			NoteType noteType = null;
			for(NoteType nt : NoteType.values()) {
				if(nt.toString().equalsIgnoreCase(type)) noteType = nt;
			}
			
			if(noteType == null) return; //something went wrong
	
			NoteEntryModel note = new NoteEntryModel(noteType);
			note.setName(name);
			note.setFilePath(path);
			note.setViewID(viewId);
			note.setSecondaryViewID(secondaryViewId);
			note.setID(id);
			this.add(note);
			
			cursor.moveToNext();
		}
		
		dbAdapter.close();
	}
	
	public boolean saveNote(NoteEntryModel note, Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		
		boolean res = dbAdapter.insertNote(note.getID(), note.getName(),
				note.getType().toString(), note.getFilePath(),
				note.getViewID(), note.getSecondaryViewID());
		dbAdapter.close();
		return res;
	}
	
	public void saveEntriesToDatabase(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		
		dbAdapter.getAllNotes();
		
		android.database.Cursor cursor = dbAdapter.getAllTasks();
		if(!cursor.moveToFirst()) return; //no data
		
		while(!cursor.isAfterLast()) {			
			int id = cursor.getInt(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_ID));
			String name = cursor.getString(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_NAME));
			String type = cursor.getString(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_TYPE));
			String path = cursor.getString(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_PATH));
			int viewId = cursor.getInt(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_VIEWID));
			int secondaryViewId = cursor.getInt(cursor.getColumnIndex(DBAdapter.NOTE_COLUMN_SECONDARY_VIEWID));
			
			NoteType noteType = null;
			for(NoteType nt : NoteType.values()) {
				if(nt.toString().equalsIgnoreCase(type)) noteType = nt;
			}
			
			if(noteType == null) return; //something went wrong
	
			NoteEntryModel note = new NoteEntryModel(noteType);
			note.setName(name);
			note.setFilePath(path);
			note.setViewID(viewId);
			note.setSecondaryViewID(secondaryViewId);
			note.setID(id);
			this.add(note);
			
			cursor.moveToNext();
		}
		
		dbAdapter.close();
	}
}
