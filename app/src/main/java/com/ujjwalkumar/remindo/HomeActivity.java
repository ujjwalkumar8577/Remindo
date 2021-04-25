package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;

import java.util.*;
import java.text.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HomeActivity extends AppCompatActivity {

	private static final String CHANNEL_ID = "Reminders";
	private ArrayList<UnitReminder> allReminders = new ArrayList<>();
	private ArrayList<UnitReminder> filtered = new ArrayList<>();

	private Toolbar toolbar;
	private FloatingActionButton fab;
	private DrawerLayout drawer;
	private ListView listview1;
	private LinearLayout nav_view;
	private TextView drawer_textviewreminders;
	private TextView drawer_textviewsettings;
	private TextView drawer_textviewhelp;
	private TextView drawer_textviewabout;

	private Intent inh = new Intent();
	private SharedPreferences sp1;
	private Calendar cal = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		toolbar = findViewById(R.id.toolbar);
		fab = findViewById(R.id.fab);
		drawer = findViewById(R.id.drawer);
		nav_view = findViewById(R.id.nav_view);
		listview1 = findViewById(R.id.listview1);

		drawer_textviewreminders = (TextView) nav_view.findViewById(R.id.textviewreminders);
		drawer_textviewsettings = (TextView) nav_view.findViewById(R.id.textviewsettings);
		drawer_textviewhelp = (TextView) nav_view.findViewById(R.id.textviewhelp);
		drawer_textviewabout = (TextView) nav_view.findViewById(R.id.textviewabout);

		sp1 = getSharedPreferences("reminderData", Activity.MODE_PRIVATE);
		cal = Calendar.getInstance();
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawer, toolbar, R.string.app_name, R.string.app_name);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(drawer.isDrawerOpen(GravityCompat.START))
					onBackPressed();
				else
					drawer.openDrawer(GravityCompat.START);
			}
		});

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				inh.setAction(Intent.ACTION_VIEW);
				inh.setClass(getApplicationContext(), AddActivity.class);
				inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(inh);
				finish();
			}
		});

		drawer_textviewreminders.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				inh.setAction(Intent.ACTION_VIEW);
				inh.setClass(getApplicationContext(), RemindersActivity.class);
				inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(inh);
			}
		});

		drawer_textviewsettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				inh.setAction(Intent.ACTION_VIEW);
				inh.setClass(getApplicationContext(), SettingsActivity.class);
				inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(inh);
			}
		});

		drawer_textviewhelp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				inh.setAction(Intent.ACTION_VIEW);
				inh.setClass(getApplicationContext(), HelpActivity.class);
				inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(inh);
			}
		});

		drawer_textviewabout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				inh.setAction(Intent.ACTION_VIEW);
				inh.setClass(getApplicationContext(), AboutActivity.class);
				inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(inh);
			}
		});

		if (!sp1.getString("allReminders", "").equals("")) {
			allReminders = new Gson().fromJson(sp1.getString("allReminders", ""), new TypeToken<ArrayList<UnitReminder>>(){}.getType());
			loadUpcoming();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		}
		else {
			super.onBackPressed();
		}
	}

	private void loadUpcoming () {
		int t = 0;
		for(int i = 0; i < allReminders.size(); i++) {
			if ((allReminders.get(i).getTime() - cal.getTimeInMillis()) < 2592000000d && (allReminders.get(i).getTime() - cal.getTimeInMillis()) > -86400000) {
				filtered.add(allReminders.get(i));
			}
			t++;
		}
		Collections.sort(filtered, new Comparator<UnitReminder>() {
			@Override
			public int compare(UnitReminder r1, UnitReminder r2) {
				if(r1.getTime() > r2.getTime())
					return 1;
				else
					return 0;
			}
		});

		listview1.setAdapter(new Listview1Adapter(filtered));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
	}

	public class Listview1Adapter extends BaseAdapter {
		ArrayList<UnitReminder> data;
		public Listview1Adapter(ArrayList<UnitReminder> arr) {
			data = arr;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public UnitReminder getItem(int index) {
			return data.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(final int position, View view, ViewGroup viewGroup) {
			LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = view;
			if (v == null) {
				v = inflater.inflate(R.layout.reminderview, null);
			}

			final LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);
			final ImageView imageviewtype = (ImageView) v.findViewById(R.id.imageviewtype);
			final TextView textviewtype = (TextView) v.findViewById(R.id.textviewtype);
			final TextView textviewname = (TextView) v.findViewById(R.id.textviewname);
			final TextView textviewdate = (TextView) v.findViewById(R.id.textviewdate);
			final TextView textviewtime = (TextView) v.findViewById(R.id.textviewtime);

			android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable(); gd1.setColor(Color.parseColor("#4DD0E1")); gd1.setCornerRadius(50); layout.setBackground(gd1);
			android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable(); gd2.setCornerRadius(30); imageviewtype.setBackground(gd2);

			String type = data.get(position).getType();
			String name = data.get(position).getName();
			long startTime = data.get(position).getTime();

			cal.setTimeInMillis(startTime);
			textviewtype.setText(type);
			textviewname.setText(name);
			textviewdate.setText(new SimpleDateFormat("d MMM yyyy").format(cal.getTime()));
			textviewtime.setText(new SimpleDateFormat("h:mm a").format(cal.getTime()));

			switch (type) {
				case "Remind":
					imageviewtype.setImageResource(R.drawable.remind);
					break;
				case "Birthday":
					imageviewtype.setImageResource(R.drawable.birthday);
					break;
				case "Anniversary":
					imageviewtype.setImageResource(R.drawable.anniversary);
					break;
				case "Daily Task":
					imageviewtype.setImageResource(R.drawable.dailytask);
					break;
				case "Vehicle Insurance":
					imageviewtype.setImageResource(R.drawable.vehicleinsurance);
					break;
				case "Vehicle Fitness":
					imageviewtype.setImageResource(R.drawable.vehiclefitness);
					break;
				case "Vehicle Service":
					imageviewtype.setImageResource(R.drawable.vehicleservice);
					break;
				case "Insurance Premium":
					imageviewtype.setImageResource(R.drawable.insurancepremium);
					break;
				case "EMI":
					imageviewtype.setImageResource(R.drawable.emi);
					break;
			}

			return v;
		}
	}

}
