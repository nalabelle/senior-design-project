package com.comp490.studybuddy.textnote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.comp490.studybuddy.R;

public class TextNote extends Activity {
	
	boolean micExists;
	MediaRecorder recorder;
	MediaPlayer player;
	int count = 1;
	private static final String LOG_TAG = "Sound Record";

	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	private ImageView a = null;
	@SuppressWarnings("unused")
	private Uri fileUri;
	private static File mediaFile;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_note);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_note, menu);
		View v = (View) menu.findItem(R.id.action_record_sound).getActionView();
		
		//listeners for Record action view menu
		final Button b = (Button) v.findViewById(R.id.bActionSoundRecord);
		b.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v1) {
				if (hasMic()){
					b.setTextColor(Color.RED);
					clickie();
					startRecording();
				}
			}
		});
		v.findViewById(R.id.ibActionSoundPlay).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v1) {
				clickie();
			}
		});
		v.findViewById(R.id.ibActionSoundStop).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v1) {
				b.setTextColor(Color.WHITE);
			}
		});
		v.findViewById(R.id.ibActionSoundPause).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v1) {
				clickie();
			}
		});
		v.findViewById(R.id.ibActionSoundSave).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v1) {
				clickie();
			}
		});		
		return true;
	}
	
	public void clickie(){ //for testing
		Toast.makeText(this, "Listener working", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
		case R.id.action_record_sound: {
			// loads sub menu via xml
			return true;
		}
		//insert other action menu options
		default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void takePhoto(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		a = (ImageView) findViewById(R.id.imageView1);
		if (resultCode == Activity.RESULT_OK) {
				Bundle extras = data.getExtras();
				Bitmap btm = (Bitmap) extras.get("data");
				FileOutputStream b;
				try {
					b = new FileOutputStream(mediaFile);
					btm.compress(Bitmap.CompressFormat.JPEG, 100, b);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				a.setImageBitmap(btm);
		}
	}
	
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	@SuppressLint("SimpleDateFormat")
	private static File getOutputMediaFile(int type) {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} 
		else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} 
		else {
			return null;
		}
		return mediaFile;
	}
	
	protected boolean hasMic(){ // verify device can record sound 
		PackageManager pm = this.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
	}
	
	private void startRecording() {
		try {
			Toast.makeText(this, "Recording", Toast.LENGTH_SHORT).show();
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			String filename = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Sound" + count;
			count++;
			//filename += new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			recorder.setOutputFile(filename);
		} catch (IllegalStateException e) {
			Log.e(LOG_TAG, "startRecording() broke");
		}
	}
}
	
