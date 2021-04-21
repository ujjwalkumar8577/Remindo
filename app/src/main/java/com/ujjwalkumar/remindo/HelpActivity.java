package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.net.*;

public class HelpActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private LinearLayout layout;
	private Button send;
	private EditText name;
	private EditText feed;
	
	private Intent inf = new Intent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});

		layout = (LinearLayout) findViewById(R.id.layout);
		send = (Button) findViewById(R.id.send);
		name = (EditText) findViewById(R.id.name);
		feed = (EditText) findViewById(R.id.feed);

		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!name.getText().toString().equals("")) {
					if (!feed.getText().toString().equals("")) {
						inf.setAction(Intent.ACTION_VIEW);
						inf.setData(Uri.parse("mailto:".concat("na@gmail.com")));
						inf.putExtra("body", "Name:".concat(name.getText().toString().concat("\n".concat("Feedback:\n".concat(feed.getText().toString())))));
						startActivity(inf);
						SketchwareUtil.showMessage(getApplicationContext(), "Press send button.");
						finish();
					}
					else {
						SketchwareUtil.showMessage(getApplicationContext(), "Please enter the message");
					}
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Please enter your name");
				}
			}
		});

		setTitle("Help & Feedback");
	}
}
