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
	
	private Toolbar toolbar;
	private String stype = "";
	private String sname = "";
	private String stime = "";
	private String srep = "";
	private String sfrequency = "";
	private double t = 0;
	private double count = 0;
	private ArrayList<HashMap<String, Object>> reminderList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> allReminderList = new ArrayList<>();

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
		sp1 = getSharedPreferences("reminders", Activity.MODE_PRIVATE);
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
						deleteID(reminderList.get(position).get("id").toString());
						reminderList.remove((position));
						sp1.edit().putString("rem", new Gson().toJson(reminderList)).commit();
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

		if (!sp1.getString("rem", "").equals("")) {
			reminderList = new Gson().fromJson(sp1.getString("rem", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			Collections.sort(reminderList, new Comparator<Map<String, Object>>() {
				public int compare(final Map<String, Object> o1, final Map<String, Object> o2) {
					return ((String) o1.get("time")).compareTo((String) o2.get("time"));
				}
			});
			listview1.setAdapter(new Listview1Adapter(reminderList));
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		}
		if (!sp1.getString("allrem", "").equals("")) {
			allReminderList = new Gson().fromJson(sp1.getString("allrem", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
	}
	
	private void deleteID (final String ID) {
		t = 0;
		count = allReminderList.size();
		for(int i= 0; i< (int)(count); i++) {
			if (allReminderList.get((int)t).containsKey("id")) {
				if (allReminderList.get((int)t).get("id").toString().equals(ID)) {
					allReminderList.remove((int)(t));
					count--;
					t--;
				}
			}
			t++;
		}
		sp1.edit().putString("allrem", new Gson().toJson(allReminderList)).commit();
		Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
	}
	
	
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> arr) {
			data = arr;
		}
		
		@Override
		public int getCount() {
			return data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int index) {
			return data.get(index);
		}
		
		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(final int position, View view, ViewGroup viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = view;
			if (v == null) {
				v = _inflater.inflate(R.layout.reminderview, null);
			}
			
			final LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);
			final ImageView imageviewtype = (ImageView) v.findViewById(R.id.imageviewtype);
			final TextView textviewtype = (TextView) v.findViewById(R.id.textviewtype);
			final TextView textviewname = (TextView) v.findViewById(R.id.textviewname);
			final TextView textviewdate = (TextView) v.findViewById(R.id.textviewdate);
			final TextView textviewtime = (TextView) v.findViewById(R.id.textviewtime);
			
			android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable(); gd1.setColor(Color.parseColor("#4DD0E1")); gd1.setCornerRadius(50); layout.setBackground(gd1);
			android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable(); gd2.setCornerRadius(30); imageviewtype.setBackground(gd2);

			if (reminderList.get(position).containsKey("type")) {
				stype = reminderList.get(position).get("type").toString();
			}
			else {
				stype = "0";
			}
			if (reminderList.get(position).containsKey("name")) {
				sname = reminderList.get(position).get("name").toString();
			}
			else {
				sname = "NA";
			}
			if (reminderList.get(position).containsKey("time")) {
				stime = reminderList.get(position).get("time").toString();
			}
			else {
				stime = String.valueOf((long)(cal.getTimeInMillis()));
			}
			if (reminderList.get(position).containsKey("rep")) {
				srep = reminderList.get(position).get("rep").toString();
			}
			else {
				srep = "0";
			}
			if (reminderList.get(position).containsKey("frequency")) {
				sfrequency = reminderList.get(position).get("frequency").toString();
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
			else if (stype.equals("1")) {
				imageviewtype.setImageResource(R.drawable.birthday);
				textviewtype.setText("Happy Birthday");
			}
			else if (stype.equals("2")) {
				imageviewtype.setImageResource(R.drawable.anniversary);
				textviewtype.setText("Happy Anniversary");
			}
			else if (stype.equals("3")) {
				imageviewtype.setImageResource(R.drawable.dailytask);
				textviewtype.setText("Daily Task");
			}
			else if (stype.equals("4")) {
				imageviewtype.setImageResource(R.drawable.vehicleinsurance);
				textviewtype.setText("Vehicle Insurance");
			}
			else if (stype.equals("5")) {
				imageviewtype.setImageResource(R.drawable.vehiclefitness);
				textviewtype.setText("Vehicle Fitness");
			}
			else if (stype.equals("6")) {
				imageviewtype.setImageResource(R.drawable.vehicleservice);
				textviewtype.setText("Vehicle Service");
			}
			else if (stype.equals("7")) {
				imageviewtype.setImageResource(R.drawable.insurancepremium);
				textviewtype.setText("Insurance Premium");
			}
			else if (stype.equals("8")) {
				imageviewtype.setImageResource(R.drawable.emi);
				textviewtype.setText("EMI");
			}
			
			return v;
		}
	}
}
