package com.comp490.studybuddy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="Flashcards")
public class FlashcardModel {
	@DatabaseField(generatedId = true)
	private String id;
	@DatabaseField
	private String front;
	@DatabaseField
	private String back;
	@DatabaseField(foreign=true)
	private DeckModel deck;
	//+ "foreign key ( " + FLASHCARDS_COLUMN_DECK + " ) references " + DECKS_TABLE_NAME + " ( " + DECKS_COLUMN_ID + " ) "
	
}
