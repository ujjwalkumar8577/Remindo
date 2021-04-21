package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

	private LinearLayout layout;
	private TextView textview1;

	private Timer timer = new Timer();
	private TimerTask splash;
	private ObjectAnimator ani = new ObjectAnimator();
	private Intent in = new Intent();

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);

		layout = (LinearLayout) findViewById(R.id.layout);
		textview1 = (TextView) findViewById(R.id.textview1);

		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/logofont.ttf"), Typeface.NORMAL);

		ani.setTarget(layout);
		ani.setPropertyName("alpha");
		ani.setFloatValues((float)(0.2d), (float)(1.0d));
		ani.setInterpolator(new DecelerateInterpolator());
		ani.setDuration((int)(500));
		ani.start();

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
		timer.schedule(splash, (int)(1000));
	}
}
