package com.comp490.studybuddy.models;

public class DeckModel {
	@Attributes(primaryKey=true, notNull = false)
	private String id;
	@Attributes(notNull=true, primaryKey = false)
	private String name;
}
