package com.comp490.studybuddy.models;

public class FlashcardModel {
	@Attributes(primaryKey=true, notNull = false)
	private String id;
	@Attributes(notNull=true, primaryKey = false)
	private String front;
	private String back;
	@Attributes(notNull = false, primaryKey = false)
	private DeckModel deck;
	//+ "foreign key ( " + FLASHCARDS_COLUMN_DECK + " ) references " + DECKS_TABLE_NAME + " ( " + DECKS_COLUMN_ID + " ) "
	
}
