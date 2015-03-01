package com.comp490.studybuddy.flashcards.templates;

import java.io.Serializable;
import java.util.Calendar;

public class Cards implements Comparable<Cards>, Serializable 
{
	
	private static final long serialVersionUID = 1L;
	private String front;
	private Object back;
	private int correct;
	private int incorrect;
	private Calendar calendar;
	
	public Cards(String front, Object back) 
	{
		this.front = front;
		this.back = back;
		this.calendar = Calendar.getInstance();
	}
	
	public String getFront() 
	{ 
		return front; 
	}
	
	public Object getBack() 
	{ 
		return back; 
	}
	public Calendar getCal() 
	{ 
		return calendar;
	}
	public double getStrength() 
	{ 
		return (double) correct / (correct + incorrect);
	}
	
	public void setFront(String front) 
	{ 
		this.front = front; 
	}
	public void setBack(Object back) 
	{ 
		this.back = back; 
	}
	
	public void incrementRight() 
	{ 
		correct++; 
	}
	
	public void incrementWrong() 
	{ 
		incorrect++; 
	}
	
	public int compareTo(Cards card) 
	{ 
		return calendar.compareTo(card.getCal()); 
	}
	
	public boolean equals(Object o) 
	{
		if (o == null || !(o instanceof Cards)) 
			return false;
		Cards other = (Cards)o;
		return front.equalsIgnoreCase(other.getFront());
	}
	public String toString() 
	{ 
		return front + "\nCorrect Ratio: " + Math.round(getStrength()*100) + "%"; 
	}
}
