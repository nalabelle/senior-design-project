package com.comp490.studybuddy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="Decks")
public class DeckModel {
	@DatabaseField(generatedId = true)
	private String id;
	@DatabaseField
	private String name;
	
	DeckModel() {
		
	}
}
