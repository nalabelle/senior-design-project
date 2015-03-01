package com.comp490.studybuddy.flashcards;

import java.util.ArrayList;
import com.comp490.studybuddy.R;
import com.comp490.studybuddy.flashcards.templates.CardDeckInfo;
import com.comp490.studybuddy.flashcards.templates.Cards;
import com.comp490.studybuddy.flashcards.templates.Decks;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewDecks extends ListActivity 
{

	public static final String DECK_TITLE = "deckTitle";
	public static final int ADD_CARD = 0;
	public static final int EDIT_CARD = 1;

	CardDeckInfo backend = CardDeckInfo.getInstance(this);
	ListView listView;
	ArrayList<Cards> cards = new ArrayList<Cards>();
	Decks deck;
	String deckTitle;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_deck_activity);

		Bundle bundle = getIntent().getExtras();
		deckTitle = bundle.getString(DECK_TITLE);

		deck = backend.load(deckTitle);
		cards = deck.getCards();

		listView = this.getListView();

		listView.setAdapter(new ArrayAdapter<Cards>(this, R.layout.card, cards));

		TextView addCard = (TextView) findViewById(R.id.add_card);
		addCard.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				addCard();
			}
		});
		registerForContextMenu(listView);
	}

	protected void onResume() 
	{
		super.onResume();
		deck = backend.load(deckTitle);
		cards = deck.getCards();
		listView.setAdapter(new ArrayAdapter<Cards>(this, R.layout.card, cards));
	}

	private void addCard() 
	{
		Intent intent = new Intent(this, AddCards.class);
		Bundle bundle = new Bundle();
		bundle.putString(DECK_TITLE, deck.getName());
		intent.putExtras(bundle);
		startActivityForResult(intent, ADD_CARD);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		Cards currCard = (Cards) getListView().getItemAtPosition(position);

		Intent intent = new Intent(this, ViewCards.class);

		Bundle bundle = new Bundle();
		bundle.putString(ViewDecks.DECK_TITLE, deck.getName());
		bundle.putString(ViewCards.FRONT_OF_CARD, currCard.getFront());
		bundle.putString(ViewCards.BACK_OF_CARD,
				(String) currCard.getBack());
		intent.putExtras(bundle);

		startActivity(intent);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
	}

	public boolean onContextItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Cards currCard = (Cards) getListView().getItemAtPosition(info.position);
		switch (item.getItemId()) 
		{
		case R.id.edit:
			Intent intent = new Intent(this, AddCards.class);
			Bundle bundle = new Bundle();
			bundle.putString(DECK_TITLE, deck.getName());
			bundle.putString(AddCards.OLD_CARD_FRONT,
					currCard.getFront());
			bundle.putString(AddCards.OLD_CARD_BACK,
					(String) currCard.getBack());
			intent.putExtras(bundle);
			startActivityForResult(intent, EDIT_CARD);
			return true;
		case R.id.delete:
			deck.deleteCard(currCard.getFront());
			backend.save(deck);
			cards = deck.getCards();
			listView.setAdapter(new ArrayAdapter<Cards>(this, R.layout.card,
					cards));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) 
	{

		MenuItem sortByFront = menu.add("Sort by name");
		MenuItem sortByStrength = menu.add("Sort by correctness");
		MenuItem sortByTimeCreated = menu.add("Sort by date");

		sortByFront.setOnMenuItemClickListener(new OnMenuItemClickListener() 
		{
			public boolean onMenuItemClick(MenuItem item) 
			{
				deck.sortByFront();
				backend.save(deck);
				cards = deck.getCards();
				listView.setAdapter(new ArrayAdapter<Cards>(
						ViewDecks.this, R.layout.card, cards));
				return true;
			}
		});

		sortByStrength
				.setOnMenuItemClickListener(new OnMenuItemClickListener() 
				{
					public boolean onMenuItemClick(MenuItem item) 
					{
						deck.sortByStrength();
						backend.save(deck);
						cards = deck.getCards();
						listView.setAdapter(new ArrayAdapter<Cards>(
								ViewDecks.this, R.layout.card, cards));
						return true;
					}
				});

		sortByTimeCreated
				.setOnMenuItemClickListener(new OnMenuItemClickListener() 
				{
					public boolean onMenuItemClick(MenuItem item) 
					{
						deck.sortByCalendar();
						backend.save(deck);
						cards = deck.getCards();
						listView.setAdapter(new ArrayAdapter<Cards>(
								ViewDecks.this, R.layout.card, cards));
						return true;
					}
				});

		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) 
	{
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode != RESULT_OK)
			return;

		Bundle bundle = intent.getExtras();
		if (bundle == null)
			return;

		String front = bundle.getString(AddCards.CARD_FRONT);
		String back = bundle.getString(AddCards.CARD_BACK);

		if (requestCode == ADD_CARD) 
		{
			Cards newCard = new Cards(front, back);
			cards.add(newCard);
			backend.save(deck);
		}

		if (requestCode == EDIT_CARD) 
		{
			String oldCardFront = bundle
					.getString(AddCards.OLD_CARD_FRONT);

			deck.deleteCard(oldCardFront);

			Cards newCard = new Cards(front, back);
			deck.addCard(newCard);
			backend.save(deck);

			cards = deck.getCards();
		}

		listView.setAdapter(new ArrayAdapter<Cards>(this, R.layout.card, cards));
	}

}
