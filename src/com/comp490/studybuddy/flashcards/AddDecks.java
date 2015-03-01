package com.comp490.studybuddy.flashcards;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.flashcards.templates.Decks;
import com.comp490.studybuddy.flashcards.templates.CardDeckInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.support.v4.app.FragmentActivity;


public class AddDecks extends FragmentActivity 
{

	public static final String DECK_TITLE = "deckTitle";
	public static final String PREVIOUS_DECK_TITLE = "prevDeckTitle";

	Bundle bundle;
	EditText deckTitle;
	String prevDeckTitle;
	Button deckCancel, deckSave;
	String unfinishedMSG = "", duplicateMSG = "";
	CardDeckInfo backend = CardDeckInfo.getInstance(this);

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_deck);

		deckTitle = (EditText) findViewById(R.id.deck_name);

		deckSave = (Button) findViewById(R.id.deck_save);
		deckCancel = (Button) findViewById(R.id.deck_cancel);

		deckCancel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		bundle = getIntent().getExtras();
		
		if (bundle == null) 
		{
			addDeck();
		} 
		
		else 
		{
			editDeck();
		}
	}

	private void addDeck() 
	{
		deckSave.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{

				String name = deckTitle.getText().toString();

				if (name == null || name.length() == 0) 
				{
					unfinishedMSG += "Name";
					incompleteMessage();
					return;
				}

				// duplicate deck titles
				if (backend.hasDeck(new Decks(name))) 
				{
					duplicateMSG += name;
					duplicateMessage();
					return;
				}

				Bundle bundle = new Bundle();
				bundle.putString(DECK_TITLE, name.trim());

				Intent intent = new Intent();
				intent.putExtras(bundle);

				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void editDeck() 
	{
		prevDeckTitle = bundle.getString(PREVIOUS_DECK_TITLE);
		deckTitle.setHint(prevDeckTitle);

		deckSave.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{

				String name = deckTitle.getText().toString();

				if (name == null || name.length() == 0) 
				{
					unfinishedMSG += "Name";
					incompleteMessage();
					return;
				}

				// duplicate deck name
				if (backend.hasDeck(new Decks(name))) 
				{
					duplicateMSG += name;
					duplicateMessage();
					return;
				}

				Bundle bundle = new Bundle();
				bundle.putString(PREVIOUS_DECK_TITLE, prevDeckTitle);
				bundle.putString(DECK_TITLE, name.trim());

				Intent intent = new Intent();
				intent.putExtras(bundle);

				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void incompleteMessage() 
	{
		unfinishedMSG += " required!";
		DeckInfoDialogFragment newFragment = new DeckInfoDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(DeckInfoDialogFragment.MESSAGE, unfinishedMSG);
		newFragment.setArguments(bundle);

		newFragment.show(AddDecks.this.getSupportFragmentManager(),
				"incomplete message");
		return;
	}

	private void duplicateMessage() 
	{
		duplicateMSG += " already exists!";
		DeckInfoDialogFragment newFragment = new DeckInfoDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(DeckInfoDialogFragment.MESSAGE, duplicateMSG);
		newFragment.setArguments(bundle);

		newFragment.show(AddDecks.this.getSupportFragmentManager(),
				"duplicate message");
		return;
	}
}
