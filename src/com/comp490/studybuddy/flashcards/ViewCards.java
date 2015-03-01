package com.comp490.studybuddy.flashcards;

import com.comp490.studybuddy.R;
import com.comp490.studybuddy.flashcards.templates.CardDeckInfo;
import com.comp490.studybuddy.flashcards.templates.Cards;
import com.comp490.studybuddy.flashcards.templates.Decks;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.View.OnClickListener;
import android.view.MenuItem.OnMenuItemClickListener;

public class ViewCards extends Activity implements FragmentManager.OnBackStackChangedListener 
{
	private Handler mHandler = new Handler();
	private boolean mShowingBack = false;

	public static final String FRONT_OF_CARD = "frontFragment";
	public static final String BACK_OF_CARD = "backFragment";

	private CardDeckInfo backend = CardDeckInfo.getInstance(this);
	private Decks deck;
	private Cards card;

	private CardFrontFrag frontFragment;
	private CardBackFrag backFragment;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_card_activity);

		Bundle bundle = getIntent().getExtras();
		deck = backend.load(bundle.getString(ViewDecks.DECK_TITLE));
		card = deck.getCard(bundle.getString(FRONT_OF_CARD));

		frontFragment = new CardFrontFrag();
		bundle = new Bundle();
		bundle.putString(FRONT_OF_CARD, card.getFront());
		frontFragment.setArguments(bundle);

		backFragment = new CardBackFrag();
		bundle = new Bundle();
		bundle.putString(BACK_OF_CARD, (String) card.getBack());
		backFragment.setArguments(bundle);

		if (savedInstanceState == null) 
		{
			getFragmentManager().beginTransaction()
					.add(R.id.container, frontFragment).commit();
		} 
		
		else 
		{
			mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
		}

		getFragmentManager().addOnBackStackChangedListener(this);
		View view = (View) findViewById(R.id.container);
		view.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				flip();
			}
		});
	}

	private final int CORRECT_BUTTON = 0;
	private final int INCORRECT_BUTTON = 1;

	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu);

		if (mShowingBack) 
		{
			// added correct / incorrect buttons
			MenuItem right = menu.add(Menu.NONE, CORRECT_BUTTON, Menu.NONE,
					"Correct");
			right.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			right.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				public boolean onMenuItemClick(MenuItem item) {
					card.incrementRight();
					flip();
					backend.save(deck);
					return true;
				}
			});

			MenuItem wrong = menu.add(Menu.NONE, INCORRECT_BUTTON, Menu.NONE,
					"Incorrect");
			wrong.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			wrong.setOnMenuItemClickListener(new OnMenuItemClickListener() 
			{
				public boolean onMenuItemClick(MenuItem item) 
				{
					card.incrementWrong();
					flip();
					backend.save(deck);
					return true;
				}
			});
		} 
		
		else 
		{
			menu.removeItem(CORRECT_BUTTON);
			menu.removeItem(INCORRECT_BUTTON);
		}

		MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
				mShowingBack ? R.string.action_front : R.string.action_back);
		item.setIcon(mShowingBack ? R.drawable.ic_action_photo
				: R.drawable.ic_action_info);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			Intent intent = new Intent(this, ViewDecks.class).putExtra(
					ViewDecks.DECK_TITLE, deck.getName());
			NavUtils.navigateUpTo(this, intent);
			return true;

		case R.id.action_flip:
			flip();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void flip() 
	{
		if (mShowingBack) {
			getFragmentManager().popBackStack();
			return;
		}

		mShowingBack = true;

		getFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.animator.card_flip_right_in,
						R.animator.card_flip_right_out,
						R.animator.card_flip_left_in,
						R.animator.card_flip_left_out)
				.replace(R.id.container, backFragment).addToBackStack(null)
				.commit();

		mHandler.post(new Runnable() {
			public void run() {
				invalidateOptionsMenu();
			}
		});
	}

	public void onBackStackChanged() 
	{
		mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
		invalidateOptionsMenu();
	}

	public static class CardFrontFrag extends Fragment 
	{

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) 
		{
			return inflater.inflate(R.layout.fragment_card_front, container,
					false);
		}

		public void onResume() {
			super.onResume();
			String front = getArguments().getString(FRONT_OF_CARD);
			TextView text = (TextView) getActivity().findViewById(
					R.id.text_front);
			if (text != null)
				text.setText(front);
		}
	}

	public static class CardBackFrag extends Fragment 
	{

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_card_back, container,
					false);
		}

		public void onResume() 
		{
			super.onResume();
			String back = getArguments().getString(BACK_OF_CARD);
			TextView text = (TextView) getActivity().findViewById(
					R.id.text_back);
			if (text != null)
				text.setText(back);
		}
	}
}
