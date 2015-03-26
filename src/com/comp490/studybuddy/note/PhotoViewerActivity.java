package com.comp490.studybuddy.note;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.comp490.studybuddy.R;

/* This activity launches a PhotoViewer library created by Chris Banes which
 * can be found at https://github.com/chrisbanes/PhotoView (Apache License2).
 * The viewer allows pinch/zoom/swipe/double tap features.
 */

public class PhotoViewerActivity extends Activity {
	PhotoViewAttacher mAttacher;
	ImageView imageView;
	Bitmap bitmap;
	File imgFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.gc(); // try run garbage collector before viewing
		Bundle extras = getIntent().getExtras();
		
		//View to hold our photo
		imageView = new ImageView(getApplicationContext());

		//Make sure the file still exists
		try {
			imgFile = new File(extras.getString("path"));
		} catch (Exception e) {
			Log.e("Note Photo", "Photo file not created");
			e.printStackTrace();
		}
		if (imgFile != null) {
			//The following prevents texture too large errors.
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			
			//this just decodes the bounds
			BitmapFactory.decodeFile(imgFile.getAbsolutePath(), opts);
			int pow = 0;
			
			//these are old, but safe?
			int maxH = 2048;
			int maxW = 2048;
			
			while (opts.outHeight >> pow > maxH || opts.outWidth >> pow > maxW)
			    pow += 1;
			opts.inSampleSize = 1 << pow; 
			opts.inJustDecodeBounds = false;
				
			bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), opts);
			//some devices save photos rotated
			rotateBitmap();
			imageView.setImageBitmap(bitmap);

		} else { //display error image if necessary
			Drawable bitmap = getResources()
					.getDrawable(R.drawable.file_not_found);
			imageView.setImageDrawable(bitmap);
		}

		// Attach a PhotoViewAttacher, which takes care of all of the zooming
		// functionality.
		mAttacher = new PhotoViewAttacher(imageView);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
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

	//Rotates the image if necessary (some devices save images rotated)
	public void rotateBitmap() {
		int rotate = 0;
		try {
			File imageFile = new File(imgFile.getAbsolutePath());
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Matrix matrix = new Matrix();
		matrix.postRotate(rotate);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}
}
