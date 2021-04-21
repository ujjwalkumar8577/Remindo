package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.app.*;
import android.media.RingtoneManager;
import android.net.Uri;
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
	private Toolbar toolbar;
	private FloatingActionButton fab;
	private DrawerLayout drawer;
	private String stype = "";
	private String sname = "";
	private String stime = "";
	private String srep = "";
	private String sfrequency = "";
	private HashMap<String, Object> tmpmap = new HashMap<>();
	
	private ArrayList<String> types = new ArrayList<>();
	private ArrayList<String> repeats = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> allReminderList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> tmplistmap = new ArrayList<>();

	private ListView listview1;
	private LinearLayout nav_view;
	private TextView drawer_textviewreminders;
	private TextView drawer_textviewsettings;
	private TextView drawer_textviewhelp;
	private TextView drawer_textviewabout;
	
	private Intent inh = new Intent();
	private SharedPreferences sp1;
	private Calendar cal = Calendar.getInstance();
	private Calendar calNow = Calendar.getInstance();
	private Calendar tmp = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);

		toolbar = (Toolbar) findViewById(R.id._toolbar);
		fab = (FloatingActionButton) findViewById(R.id._fab);
		drawer = (DrawerLayout) findViewById(R.id._drawer);
		nav_view = (LinearLayout) findViewById(R.id._nav_view);
		listview1 = (ListView) findViewById(R.id.listview1);

		drawer_textviewreminders = (TextView) nav_view.findViewById(R.id.textviewreminders);
		drawer_textviewsettings = (TextView) nav_view.findViewById(R.id.textviewsettings);
		drawer_textviewhelp = (TextView) nav_view.findViewById(R.id.textviewhelp);
		drawer_textviewabout = (TextView) nav_view.findViewById(R.id.textviewabout);

		sp1 = getSharedPreferences("reminders", Activity.MODE_PRIVATE);
		cal = Calendar.getInstance();
		calNow = Calendar.getInstance();
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawer, toolbar, R.string.app_name, R.string.app_name);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Reminders", NotificationManager.IMPORTANCE_HIGH);
			channel.setDescription("For reminding users");
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}

		types.add("Remind");
		types.add("Birthday");
		types.add("Anniversary");
		types.add("Daily Task");
		types.add("Vehicle Insurance");
		types.add("Vehicle Fitness");
		types.add("Vehicle Service");
		types.add("Insurance Premium");
		types.add("EMI");

		repeats.add("Does not repeat");
		repeats.add("Every Day");
		repeats.add("Every Week");
		repeats.add("Every Month");
		repeats.add("Every Quarter");
		repeats.add("Every Year");

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

		if (!sp1.getString("allrem", "").equals("")) {
			tmplistmap = new Gson().fromJson(sp1.getString("allrem", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			loadUpcoming();
		}
	}
	
	@Override
	public void onBackPressed() {
		showNotification(0,"Title","My first notification");
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		}
		else {
			super.onBackPressed();
		}
	}

	private void showNotification(int id, String title, String text) {
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setSmallIcon(R.drawable.app_icon);
		builder.setOngoing(true);
		builder.setAutoCancel(false);
		builder.setSound(alarmSound);

		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(id, builder.build());
	}

	private void loadUpcoming () {
		int t = 0;
		for(int i = 0; i < tmplistmap.size(); i++) {
			tmp.setTimeInMillis((long)(Double.parseDouble(tmplistmap.get((int)t).get("time").toString())));
			if (((long)(tmp.getTimeInMillis() - calNow.getTimeInMillis()) < 2592000000d) && ((long)(tmp.getTimeInMillis() - calNow.getTimeInMillis()) > -86400000)) {
				tmpmap = tmplistmap.get(t);
				allReminderList.add(tmpmap);
			}
			t++;
		}
		Collections.sort(allReminderList, new Comparator<Map<String, Object>>() {
			         public int compare(final Map<String, Object> o1, final Map<String, Object> o2) {
				             return ((String) o1.get("time")).compareTo((String) o2.get("time"));
				         }
			     });
		listview1.setAdapter(new Listview1Adapter(allReminderList));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
	}

	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.reminderview, null);
			}
			
			final LinearLayout layout = (LinearLayout) _v.findViewById(R.id.layout);
			final ImageView imageviewtype = (ImageView) _v.findViewById(R.id.imageviewtype);
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final TextView textviewtype = (TextView) _v.findViewById(R.id.textviewtype);
			final TextView textviewname = (TextView) _v.findViewById(R.id.textviewname);
			final LinearLayout linear3 = (LinearLayout) _v.findViewById(R.id.linear3);
			final TextView textviewdate = (TextView) _v.findViewById(R.id.textviewdate);
			final TextView textviewtime = (TextView) _v.findViewById(R.id.textviewtime);
			
			android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable(); gd1.setColor(Color.parseColor("#4DD0E1")); gd1.setCornerRadius(50); layout.setBackground(gd1);
			android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable(); gd2.setCornerRadius(30); imageviewtype.setBackground(gd2);
			if (allReminderList.get((int)_position).containsKey("type")) {
				stype = allReminderList.get((int)_position).get("type").toString();
			}
			else {
				stype = "0";
			}
			if (allReminderList.get((int)_position).containsKey("name")) {
				sname = allReminderList.get((int)_position).get("name").toString();
			}
			else {
				sname = "NA";
			}
			if (allReminderList.get((int)_position).containsKey("time")) {
				stime = allReminderList.get((int)_position).get("time").toString();
			}
			else {
				stime = String.valueOf((long)(cal.getTimeInMillis()));
			}
			if (allReminderList.get((int)_position).containsKey("rep")) {
				srep = allReminderList.get((int)_position).get("rep").toString();
			}
			else {
				srep = "0";
			}
			if (allReminderList.get((int)_position).containsKey("frequency")) {
				sfrequency = allReminderList.get((int)_position).get("frequency").toString();
			}
			else {
				sfrequency = "1";
			}
			cal.setTimeInMillis((long)(Double.parseDouble(stime)));
			textviewname.setText(sname);
			textviewdate.setText(new SimpleDateFormat("d MMM yyyy").format(cal.getTime()));
			textviewtime.setText(new SimpleDateFormat("h:mm a").format(cal.getTime()));
			if (stype.equals("0")) {
				imageviewtype.setImageResource(R.drawable.remind);
				textviewtype.setText("To do");
			}
			else {
				if (stype.equals("1")) {
					imageviewtype.setImageResource(R.drawable.birthday);
					textviewtype.setText("Happy Birthday");
				}
				else {
					if (stype.equals("2")) {
						imageviewtype.setImageResource(R.drawable.anniversary);
						textviewtype.setText("Happy Anniversary");
					}
					else {
						if (stype.equals("3")) {
							imageviewtype.setImageResource(R.drawable.dailytask);
							textviewtype.setText("Daily Task");
						}
						else {
							if (stype.equals("4")) {
								imageviewtype.setImageResource(R.drawable.vehicleinsurance);
								textviewtype.setText("Vehicle Insurance");
							}
							else {
								if (stype.equals("5")) {
									imageviewtype.setImageResource(R.drawable.vehiclefitness);
									textviewtype.setText("Vehicle Fitness");
								}
								else {
									if (stype.equals("6")) {
										imageviewtype.setImageResource(R.drawable.vehicleservice);
										textviewtype.setText("Vehicle Service");
									}
									else {
										if (stype.equals("7")) {
											imageviewtype.setImageResource(R.drawable.insurancepremium);
											textviewtype.setText("Insurance Premium");
										}
										else {
											if (stype.equals("8")) {
												imageviewtype.setImageResource(R.drawable.emi);
												textviewtype.setText("EMI");
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			return _v;
		}
	}

}
