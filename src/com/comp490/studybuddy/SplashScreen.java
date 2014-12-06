package com.comp490.studybuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
 
public class SplashScreen extends Activity 
{
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1750;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
 
        new Handler().postDelayed(new Runnable() 
        {
 
            @Override
            public void run() 
            {
                //starts main activity after timer is done
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
 
}
