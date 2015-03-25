package com.comp490.studybuddy.flashcards.templates;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class CardDeckInfo implements InfoInterface {

	private static CardDeckInfo backend;
	private static Context info;
	private ArrayList<Decks> decks;

	private CardDeckInfo() {
		decks = new ArrayList<Decks>();
	}

	public static synchronized CardDeckInfo getInstance(Context ctx) {
		info = ctx;
		if (backend == null) backend = new CardDeckInfo();
		return backend;
	}

	public void setContext(Context ctx) { info = ctx; }

	//saves info to local storage with .io.File
	public boolean save(Object o) 
	{
		Decks deck = (Decks) o;
		String fileName = deck.getName();
		try 
		{
			FileOutputStream fos = info.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(deck);
			os.close();
			return true;
		} 
		
		catch (IOException e) 
		{
			return false;
		}
	}

	// loads one deck
	public Decks load(String fileName) 
	{
		try 
		{
			FileInputStream fis = info.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			Decks deck = (Decks) is.readObject();
			is.close();
			return deck;
		} 
		
		catch (IOException ioe) 
		{
			return null;
		} 
		
		catch (ClassNotFoundException cnfe) 
		{
			return null;
		}
	}

	// loads every deck
	public ArrayList<Decks> loadAll() 
	{
		decks = new ArrayList<Decks>();
		for (File f : info.getFilesDir().listFiles()) 
		{
			Decks deck = load(f.getName());
			decks.add(deck);
		}
		Collections.sort(decks);
		return decks;
	}
	
	public boolean hasDeck(Decks deck) 
	{
		loadAll();
		return decks.contains(deck);
	}
	
	public boolean delete(String fileName) 
	{
		return info.deleteFile(fileName);
	}
	
	public void deleteAll() 
	{
		for (File f : info.getFilesDir().listFiles()) 
		{
			f.delete();
		}
		decks = new ArrayList<Decks>();
	}
}
