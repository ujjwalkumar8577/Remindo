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
	private Button send;
	private EditText name;
	private EditText feed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		toolbar = (Toolbar) findViewById(R.id._toolbar);
		send = (Button) findViewById(R.id.send);
		name = (EditText) findViewById(R.id.name);
		feed = (EditText) findViewById(R.id.feed);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});

		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!name.getText().toString().equals("")) {
					if (!feed.getText().toString().equals("")) {
						String emailAddress = "na@gmail.com";
						String body = "Name:" + name.getText().toString() + "\n" + "Feedback:\n" + feed.getText().toString();

						Intent inf = new Intent();
						inf.setAction(Intent.ACTION_VIEW);
						inf.setData(Uri.parse("mailto:"+emailAddress));
						inf.putExtra("body", body);
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
