package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;

public class AboutActivity extends AppCompatActivity {
	
	private Toolbar toolbar;
	private TextView textview1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		toolbar = (Toolbar) findViewById(R.id._toolbar);
		textview1 = (TextView) findViewById(R.id.textview1);

		setTitle("About the App");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/logofont.ttf"), Typeface.NORMAL);
	}
}
