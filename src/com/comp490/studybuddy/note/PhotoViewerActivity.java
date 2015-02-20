package com.comp490.studybuddy.note;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.comp490.studybuddy.R;

public class PhotoViewerActivity extends Activity {
	PhotoViewAttacher mAttacher;
	ImageView imageView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.gc(); //try run garbage collector before viewing
		Bundle extras = getIntent().getExtras();
		imageView = new ImageView(getApplicationContext());
		
		
		File imgFile = null;
		try {
			imgFile = new File(extras.getString("path"));
		} catch (Exception e) {
			Log.e("Note Photo", "Photo file not created");
			e.printStackTrace();
		}
		if(imgFile != null){
		    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    imageView.setImageBitmap(bitmap);
		    // or imageView.setImageDrawable(bitmap);?

		} else { 
			Drawable bitmap = getResources().getDrawable(R.drawable.file_not_found);
			imageView.setImageDrawable(bitmap);
		}
		
		//Drawable bitmap = getResources().
	    //imageView.setImageDrawable(bitmap);

	    // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
	    mAttacher = new PhotoViewAttacher(imageView);
	    Toast.makeText(this, extras.getString("path"), Toast.LENGTH_LONG).show();
		
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(
          WindowManager.LayoutParams.FLAG_FULLSCREEN,  
           WindowManager.LayoutParams.FLAG_FULLSCREEN);
      setContentView(imageView);
      
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

/* ImageView mImageView;
PhotoViewAttacher mAttacher;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Any implementation of ImageView can be used!
    mImageView = (ImageView) findViewById(R.id.iv_photo);

    // Set the Drawable displayed
    Drawable bitmap = getResources().getDrawable(R.drawable.wallpaper);
    mImageView.setImageDrawable(bitmap);

    // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
    mAttacher = new PhotoViewAttacher(mImageView);
}


// If you later call mImageView.setImageDrawable/setImageBitmap/setImageResource/etc then you just need to call
attacher.update(); */
