package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

	private TextView textviewTitle;

	private Timer timer = new Timer();
	private TimerTask splash;
	private Intent in = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		textviewTitle = findViewById(R.id.textviewTitle);
		textviewTitle.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/logofont.ttf"), Typeface.NORMAL);

		splash = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						in.setAction(Intent.ACTION_VIEW);
						in.setClass(getApplicationContext(), HomeActivity.class);
						in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(in);
						finish();
					}
				});
			}
		};
		timer.schedule(splash, 1000);
	}
}
