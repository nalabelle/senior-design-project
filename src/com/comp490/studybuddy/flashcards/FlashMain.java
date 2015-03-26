/**
* Author: Wesley Liang
* Date started: 
* Date Completed: IP
* Peer Review:  
* Team members: Buddy Corp
* Contribution: Uyen 
*/

/*
* Code is largely adapted from the google developer training tutorial found at:
* http://developer.android.com/training/animation/index.html
* This project is open source, developed in adherence to the guideline found here:
* http://www.apache.org/licenses/LICENSE-2.0
* 
*/

package com.comp490.studybuddy.flashcards;

import com.comp490.studybuddy.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;

import android.view.MenuItem;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ContextMenu;
import android.app.ListActivity;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.view.View.OnClickListener;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.comp490.studybuddy.calendar.CalenActivity;
import com.comp490.studybuddy.flashcards.templates.Decks;
import com.comp490.studybuddy.flashcards.templates.CardDeckInfo;
import com.comp490.studybuddy.handwritingnote.HandwritingMain;
import com.comp490.studybuddy.note.NoteActivity;
import com.comp490.studybuddy.note.PhotoViewerActivity;
import com.comp490.studybuddy.note.VideoViewerActivity;
import com.comp490.studybuddy.todolist.ToDoMain;

public class FlashMain extends ListActivity 
{
	
	public static final int ADD_DECK = 0;
	public static final int EDIT_DECK = 1;

	CardDeckInfo backObj = CardDeckInfo.getInstance(this);
	ListView list;
	ArrayList<Decks> deckObj = new ArrayList<Decks>();

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deck_list);

		list = this.getListView();

		deckObj = backObj.loadAll();
		list.setAdapter(new ArrayAdapter<Decks>(this, R.layout.deck, deckObj));

		TextView addDeck = (TextView) findViewById(R.id.add_deck);
		addDeck.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				addDeck();
			}
		});
		registerForContextMenu(list);
	}
	
	protected void onResume() 
	{
		super.onResume();
		deckObj = backObj.loadAll();
		list.setAdapter(new ArrayAdapter<Decks>(this, R.layout.deck, deckObj));
	}

	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		Decks currDeck = (Decks) getListView().getItemAtPosition(position);
		
		Intent intent = new Intent(this, ViewDecks.class);

		Bundle bundle = new Bundle();
		bundle.putString(ViewDecks.DECK_TITLE, currDeck.getName());

		intent.putExtras(bundle);
		startActivity(intent);
	}

	void addDeck() 
	{
		Intent intent = new Intent(this, AddDecks.class);
		startActivityForResult(intent, ADD_DECK);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) 
	{
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode != RESULT_OK) return;

		Bundle bundle = intent.getExtras();
		if (bundle == null) return;

		String name = bundle.getString(AddDecks.DECK_TITLE);

		if (requestCode == ADD_DECK) 
		{
			Decks newDeck = new Decks(name);
			deckObj.add(newDeck);
			CardDeckInfo.getInstance(this).save(newDeck);
			
			deckObj = backObj.loadAll();
		} 
		
		if (requestCode == EDIT_DECK) 
		{
			String oldDeckName = bundle.getString(AddDecks.PREVIOUS_DECK_TITLE);
			
			Decks newDeck = backObj.load(oldDeckName);
			newDeck.setName(name);
			
			backObj.delete(oldDeckName);
			backObj.save(newDeck);
			
			deckObj = backObj.loadAll();
		}

		list.setAdapter(new ArrayAdapter<Decks>(this, R.layout.deck, deckObj));
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Decks currDeck = (Decks) getListView().getItemAtPosition(info.position);
	    switch (item.getItemId()) 
	    {
	        case R.id.edit:
	        	Intent intent = new Intent(this, AddDecks.class);
	        	Bundle bundle = new Bundle();
	        	bundle.putString(AddDecks.PREVIOUS_DECK_TITLE, currDeck.getName());
	        	intent.putExtras(bundle);
	        	startActivityForResult(intent, EDIT_DECK);
	            return true;
	        case R.id.delete:
	            //delete action
	        	backObj.delete(currDeck.getName());
	        	
	        	deckObj = backObj.loadAll();
	        	list.setAdapter(new ArrayAdapter<Decks>(this, R.layout.deck, deckObj));
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
	   switch(item.getItemId()) {
	      case R.id.action_launch_text: {
	           startActivity(new Intent(this, NoteActivity.class));
              return true;
	      }
	      case R.id.action_launch_calendar: {
	           startActivity(new Intent(this, CalenActivity.class));
              return true;
	      }
	      case R.id.action_launch_todo: {
	           startActivity(new Intent(this, ToDoMain.class));
	           return true;
	      }
	      default:
	           return super.onOptionsItemSelected(item);
	      }
	   }
	  
}
