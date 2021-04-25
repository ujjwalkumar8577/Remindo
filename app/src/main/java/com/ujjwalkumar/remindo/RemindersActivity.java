package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class RemindersActivity extends AppCompatActivity {

	private int t = 0;
	private int count = 0;
	private ArrayList<Reminder> reminders = new ArrayList<>();
	private ArrayList<UnitReminder> allReminders = new ArrayList<>();

	private Toolbar toolbar;
	private ListView listview1;
	
	private Intent inr = new Intent();
	private SharedPreferences sp1;
	private Calendar cal = Calendar.getInstance();
	private AlertDialog.Builder delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminders);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		listview1 = (ListView) findViewById(R.id.listview1);
		sp1 = getSharedPreferences("reminderData", Activity.MODE_PRIVATE);
		delete = new AlertDialog.Builder(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> param1, View param2, int param3, long param4) {
				final int position = param3;
				delete.setTitle("Delete");
				delete.setMessage("Do you want to delete?");
				delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						deleteID(reminders.get(position).getID());
						reminders.remove((position));
						sp1.edit().putString("reminders", new Gson().toJson(reminders)).apply();
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					}
				});
				delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {

					}
				});
				delete.create().show();
				return true;
			}
		});

		if (!sp1.getString("reminders", "").equals("")) {
			reminders = new Gson().fromJson(sp1.getString("reminders", ""), new TypeToken<ArrayList<Reminder>>(){}.getType());
			Collections.sort(reminders, new Comparator<Reminder>() {
				@Override
				public int compare(Reminder r1, Reminder r2) {
					if(r1.getStartTime() > r2.getStartTime())
						return 1;
					else
						return 0;
				}
			});
			listview1.setAdapter(new Listview1Adapter(reminders));
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		}
		if (!sp1.getString("allReminders", "").equals("")) {
			allReminders = new Gson().fromJson(sp1.getString("allReminders", ""), new TypeToken<ArrayList<UnitReminder>>(){}.getType());
		}
	}
	
	private void deleteID (int ID) {
		t = 0;
		count = allReminders.size();
		for(int i=0; i<count; i++) {
			if (allReminders.get(t).getParentID() == ID) {
				allReminders.remove(t);
				count--;
				t--;
			}
			t++;
		}
		sp1.edit().putString("allReminders", new Gson().toJson(allReminders)).apply();
		Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
	}

	private void cancelAlarm(long time, String name, int id) {
		Calendar ctmp = Calendar.getInstance();
		ctmp.setTimeInMillis(time);

		Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
		intent.putExtra("name", name);
		intent.putExtra("id", id);
		intent.putExtra("time", new SimpleDateFormat("h:mm a").format(ctmp.getTime()));
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}
	
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<Reminder> data;
		public Listview1Adapter(ArrayList<Reminder> arr) {
			data = arr;
		}
		
		@Override
		public int getCount() {
			return data.size();
		}
		
		@Override
		public Reminder getItem(int index) {
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
			long startTime = data.get(position).getStartTime();

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
