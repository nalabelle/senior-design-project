package com.comp490.studybuddy.flashcards.sorting;

import java.util.Comparator;

import com.comp490.studybuddy.flashcards.templates.Cards;

public class CorrectCompare implements Comparator<Cards> 
{
	
	public int compare(Cards first, Cards second) 
	{
		double aStrength = first.getStrength(), bStrength = second.getStrength();
		if (aStrength == bStrength) return 0;
		return aStrength < bStrength ? -1 : 1;
	}
}
