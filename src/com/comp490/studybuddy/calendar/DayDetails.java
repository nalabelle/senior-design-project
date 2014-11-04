package com.comp490.studybuddy.calendar;

import com.comp490.studybuddy.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DayDetails extends Activity {
	
	private TextView dayDetailText;
	private Intent prevIntent;
	private String day_mon_yr;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_day_detail);
	    ActionBar actionBar = getActionBar();
	    actionBar.hide();
	    
	    prevIntent = getIntent();
	    day_mon_yr = prevIntent.getStringExtra("Day");
	    
	    dayDetailText = (TextView) this.findViewById(R.id.textView1);
	    
	    dayDetailText.setText(day_mon_yr);
	}

}
