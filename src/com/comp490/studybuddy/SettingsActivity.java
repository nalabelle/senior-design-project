package com.comp490.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SettingsActivity extends ActionBarActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.comp490.studybuddy.R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	switch (item.getItemId()) {
        case com.comp490.studybuddy.R.id.action_settings:
            openSettings();
            return true;    
        default:
        return super.onOptionsItemSelected(item);
    	}
    }

	private void openSettings() {
		// Open settings when menu item settings is selected
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	private void openHelp() {
		// Open settings when menu item Help is selected
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}
	
	private void openAbout() {
		// Open settings when menu item About is selected
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}	
}

		