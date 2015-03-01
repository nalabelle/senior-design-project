package com.comp490.studybuddy.flashcards.templates;

public interface InfoInterface 
{

	Object load(String id);
	Object loadAll();
	boolean save(Object o);
	boolean delete(String id);
}
