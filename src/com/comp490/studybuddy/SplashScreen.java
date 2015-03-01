package com.comp490.studybuddy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
 
public class SplashScreen extends Activity 
{
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000; //1750;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_splash_screen);
        
        // Font Apache 2.0 License 
        //http://www.fontsquirrel.com/license/permanent-marker 
        Typeface font = Typeface.createFromAsset(getAssets(), "font/PermanentMarker.ttf");
        TextView tv = (TextView) findViewById(R.id.splashTextView);
        tv.setTypeface(font);
        tv.startAnimation(AnimationUtils.loadAnimation(SplashScreen.this, R.animator.fade));
 
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
