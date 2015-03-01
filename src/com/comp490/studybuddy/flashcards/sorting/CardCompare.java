package com.comp490.studybuddy.flashcards.sorting;

import java.util.Comparator;
import com.comp490.studybuddy.flashcards.templates.Cards;

public class CardCompare implements Comparator<Cards> 
{
	
	public int compare(Cards first, Cards second) 
	{
		return first.getFront().compareToIgnoreCase(second.getFront());
	}
}
