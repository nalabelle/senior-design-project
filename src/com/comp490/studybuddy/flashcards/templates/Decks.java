package com.comp490.studybuddy.flashcards.templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import com.comp490.studybuddy.flashcards.sorting.CardCompare;
import com.comp490.studybuddy.flashcards.sorting.CorrectCompare;

public class Decks implements Comparable<Decks>, Serializable 
{
	
	private static final long serialVersionUID = 1L;
	private String title;
	private ArrayList<Cards> cards;
	
	public Decks(String name) 
	{
		this.title = name;
		cards = new ArrayList<Cards>();
	}
	
	public String getName() 
	{ 
		return title; 
	}
	
	public ArrayList<Cards> getCards() 
	{ 
		return cards; 
	}
	
	public int size() 
	{ 
		return cards.size(); 
	}
	
	public void setName(String name) 
	{ 
		this.title = name; 
	}
	
	public boolean hasCard(Cards card) 
	{ 
		return cards.contains(card); 
	}
	
	public void addCard(Cards card) 
	{ 
		if (!(cards.contains(card))) cards.add(card);
	} 
	
	public Cards getCard(String front) 
	{
		return cards.get(cards.indexOf(new Cards(front, "")));
	}
	
	public boolean deleteCard(String front) 
	{
		Cards temp = new Cards(front, null);
		return cards.remove(temp);
	}
	
	public Cards searchForCard(String front) 
	{
		Cards temp = new Cards(front, null);
		int index = cards.indexOf(temp);
		return cards.get(index);
	}
	
	public void sortByCalendar() 
	{
		Collections.sort(cards);
	}
	
	public void sortByFront() 
	{
		Collections.sort(cards, new CardCompare());
	}
	
	public void sortByStrength() 
	{ 
		Collections.sort(cards, new CorrectCompare());
	}
	
	public int compareTo(Decks deck) 
	{
		return title.compareToIgnoreCase(deck.getName());
	}

	public boolean equals(Object o) 
	{
		if (o == null || !(o instanceof Decks)) return false;
		Decks other = (Decks)o;
		return title.equalsIgnoreCase(other.getName());
	}
	
	public String toString() 
	{
		return title + "\n" + size() + " card" + (size() == 1 ? "" : "s");
	}
}
