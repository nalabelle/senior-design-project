package com.comp490.studybuddy.flashcards;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.flashcards.templates.Cards;
import com.comp490.studybuddy.flashcards.templates.Decks;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.support.v4.app.FragmentActivity;
import com.comp490.studybuddy.flashcards.templates.CardDeckInfo;

public class AddCards extends FragmentActivity 
{

	public static final String CARD_FRONT = "cardFront";
	public static final String CARD_BACK = "cardBack";
	public static final String OLD_CARD_FRONT = "oldCardFront";
	public static final String OLD_CARD_BACK = "oldCardBack";

	CardDeckInfo backend = CardDeckInfo.getInstance(this);
	EditText cardFront, cardBack;
	Button cardCancel, cardSave;
	String incompleteMessage = "", duplicateMessage = "";
	String deckName, oldCardFront, oldCardBack;
	Bundle bundle;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_card);

		bundle = getIntent().getExtras();
		deckName = bundle.getString(ViewDecks.DECK_TITLE);

		cardFront = (EditText) findViewById(R.id.front_name);
		cardBack = (EditText) findViewById(R.id.back_name);

		cardSave = (Button) findViewById(R.id.deck_save);
		cardCancel = (Button) findViewById(R.id.deck_cancel);

		cardCancel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		if (bundle.containsKey(OLD_CARD_FRONT)) 
		{
			editCard();
		} else {
			addCard();
		}
		
	}

	private void addCard() 
	{
		cardSave.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{

				String front = cardFront.getText().toString();
				String back = cardBack.getText().toString();

				if (front == null || front.length() == 0 || back == null
						|| back.length() == 0) {
					incompleteMessage += "Front/Back";
					incompleteMessage();
					return;
				}

				// duplicate card front
				Decks deck = backend.load(deckName);
				if (deck.hasCard(new Cards(front, back))) 
				{
					duplicateMessage += "Card with front: " + front;
					duplicateMessage();
					return;
				}

				Bundle bundle = new Bundle();
				bundle.putString(CARD_FRONT, front.trim());
				bundle.putString(CARD_BACK, back.trim());

				Intent intent = new Intent();
				intent.putExtras(bundle);

				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void editCard() 
	{
		oldCardFront = bundle.getString(OLD_CARD_FRONT);
		oldCardBack = bundle.getString(OLD_CARD_BACK);
		cardFront.setHint(oldCardFront);
		cardBack.setHint(oldCardBack);

		cardSave.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				String front = cardFront.getText().toString();
				String back = cardBack.getText().toString();

				if (front == null || front.length() == 0 || back == null
						|| back.length() == 0) {
					incompleteMessage += "Front and Back";
					incompleteMessage();
					return;
				}

				// duplicate card front
				Decks deck = backend.load(deckName);
				if (deck.hasCard(new Cards(front, back))) 
				{
					duplicateMessage += "Card";
					duplicateMessage();
					return;
				}

				Bundle bundle = new Bundle();
				bundle.putString(OLD_CARD_FRONT, oldCardFront);
				bundle.putString(OLD_CARD_BACK, oldCardBack);
				bundle.putString(CARD_FRONT, front.trim());
				bundle.putString(CARD_BACK, back.trim());

				Intent intent = new Intent();
				intent.putExtras(bundle);

				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void incompleteMessage() 
	{
		incompleteMessage += " please fill out info";
		DeckInfoDialogFragment newFragment = new DeckInfoDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(DeckInfoDialogFragment.MESSAGE, incompleteMessage);
		newFragment.setArguments(bundle);

		newFragment.show(AddCards.this.getSupportFragmentManager(),
				"incomplete message");
		return;
	}

	private void duplicateMessage() 
	{
		duplicateMessage += " already created";
		DeckInfoDialogFragment newFragment = new DeckInfoDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(DeckInfoDialogFragment.MESSAGE, duplicateMessage);
		newFragment.setArguments(bundle);

		newFragment.show(AddCards.this.getSupportFragmentManager(),
				"duplicate message");
		return;
	}
}
