package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

import java.util.*;
import java.text.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AddActivity extends AppCompatActivity {

	private ArrayList<String> types = new ArrayList<>();
	private ArrayList<String> repeats = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> reminderList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> allReminderList = new ArrayList<>();
	private HashMap<String, Object> mp = new HashMap<>();
	private HashMap<String, Object> newmp = new HashMap<>();
	private int i = 0;
	private String remType = "";
	private String remName = "";
	private String remTime = "";
	private String remRepeat = "";
	private String remFrequency = "";

	private Toolbar toolbar;
	private LinearLayout linear4;
	private LinearLayout linear5;
	private Spinner spinnertype;
	private EditText edittextname;
	private TextView textviewdated;
	private TextView textviewdatem;
	private TextView textviewdatey;
	private TextView textviewtimeh;
	private TextView textviewtimem;
	private Spinner spinnerrepeat;
	private EditText edittextfreq;
	private Button buttonsave;
	
	private SharedPreferences sp1;
	private Calendar cal = Calendar.getInstance();
	private Calendar tmp = Calendar.getInstance();
	private Intent ina = new Intent();
	private AlertDialog.Builder exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		spinnertype = (Spinner) findViewById(R.id.spinnertype);
		edittextname = (EditText) findViewById(R.id.edittextname);
		textviewdated = (TextView) findViewById(R.id.textviewdated);
		textviewdatem = (TextView) findViewById(R.id.textviewdatem);
		textviewdatey = (TextView) findViewById(R.id.textviewdatey);
		textviewtimeh = (TextView) findViewById(R.id.textviewtimeh);
		textviewtimem = (TextView) findViewById(R.id.textviewtimem);
		spinnerrepeat = (Spinner) findViewById(R.id.spinnerrepeat);
		edittextfreq = (EditText) findViewById(R.id.edittextfreq);
		buttonsave = (Button) findViewById(R.id.buttonsave);

		sp1 = getSharedPreferences("reminders", Activity.MODE_PRIVATE);
		exit = new AlertDialog.Builder(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});

		linear4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				androidx.appcompat.app.AppCompatDialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "Date Picker");
			}
		});

		linear5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getFragmentManager(), "timePicker");
			}
		});

		spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> param1, View param2, int param3, long param4) {
				final int position = param3;
				if (position == 0) {
					spinnerrepeat.setSelection((int)(0));
					edittextfreq.setText("1");
				}
				if (position == 3) {
					spinnerrepeat.setSelection((int)(1));
					edittextfreq.setText("365");
				}
				if (position == 8) {
					spinnerrepeat.setSelection((int)(3));
					edittextfreq.setText("12");
				}
				if (position == 6) {
					spinnerrepeat.setSelection((int)(4));
					edittextfreq.setText("12");
				}
				if ((position == 1) || ((position == 2) || ((position == 4) || ((position == 5) || (position == 7))))) {
					spinnerrepeat.setSelection((int)(5));
					edittextfreq.setText("5");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> param1) {

			}
		});

		spinnerrepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> param1, View param2, int param3, long param4) {
				final int position = param3;
			}

			@Override
			public void onNothingSelected(AdapterView<?> param1) {

			}
		});

		buttonsave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				saveReminder();
			}
		});

		types.add("Remind");
		types.add("Birthday");
		types.add("Anniversary");
		types.add("Daily Task");
		types.add("Vehicle Insurance");
		types.add("Vehicle Fitness");
		types.add("Vehicle Service");
		types.add("Insurance Premium");
		types.add("EMI");
		spinnertype.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, types));
		((ArrayAdapter)spinnertype.getAdapter()).notifyDataSetChanged();
		repeats.add("Does not repeat");
		repeats.add("Every Day");
		repeats.add("Every Week");
		repeats.add("Every Month");
		repeats.add("Every Quarter");
		repeats.add("Every Year");
		spinnerrepeat.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, repeats));
		((ArrayAdapter)spinnerrepeat.getAdapter()).notifyDataSetChanged();
		cal = Calendar.getInstance();
		textviewdated.setText(new SimpleDateFormat("d").format(cal.getTime()));
		textviewdatem.setText(new SimpleDateFormat("MM").format(cal.getTime()));
		textviewdatey.setText(new SimpleDateFormat("yyyy").format(cal.getTime()));

		if (!sp1.getString("rem", "").equals("")) {
			reminderList = new Gson().fromJson(sp1.getString("rem", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
		if (!sp1.getString("allrem", "").equals("")) {
			allReminderList = new Gson().fromJson(sp1.getString("allrem", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
	}

	@Override
	public void onBackPressed() {
		exit.setTitle("Exit");
		exit.setMessage("Do you want to cancel?");
		exit.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				ina.setAction(Intent.ACTION_VIEW);
				ina.setClass(getApplicationContext(), HomeActivity.class);
				ina.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(ina);
				finish();
			}
		});
		exit.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		exit.create().show();
	}

	private void saveReminder() {
		cal.set(Calendar.DAY_OF_MONTH, (Integer.parseInt(textviewdated.getText().toString())));
		cal.set(Calendar.MONTH, (Integer.parseInt(textviewdatem.getText().toString()) - 1));
		cal.set(Calendar.YEAR, (Integer.parseInt(textviewdatey.getText().toString())));
		cal.set(Calendar.HOUR_OF_DAY, (Integer.parseInt(textviewtimeh.getText().toString())));
		cal.set(Calendar.MINUTE, (Integer.parseInt(textviewtimem.getText().toString())));

		if (!edittextname.getText().toString().equals("")) {
			if (!edittextfreq.getText().toString().equals("")) {
				remType = String.valueOf((long)(spinnertype.getSelectedItemPosition()));
				remName = edittextname.getText().toString();
				remTime = String.valueOf((long)(cal.getTimeInMillis()));
				remRepeat = String.valueOf((long)(spinnerrepeat.getSelectedItemPosition()));
				remFrequency = edittextfreq.getText().toString();
				mp = new HashMap<>();
				mp.put("id", remTime);
				mp.put("type", remType);
				mp.put("name", remName);
				mp.put("time", remTime);
				mp.put("rep", remRepeat);
				mp.put("frequency", remFrequency);
				reminderList.add(mp);
				sp1.edit().putString("rem", new Gson().toJson(reminderList)).commit();
				for(i=0; i<Integer.parseInt(edittextfreq.getText().toString()); i++) {
					tmp.setTimeInMillis((long)(cal.getTimeInMillis()));
					if (remRepeat.equals("1") || remRepeat.equals("0")) {
						tmp.add(Calendar.DAY_OF_MONTH, (int)(i));
						newmp = new HashMap<>();
						newmp.put("id", remTime);
						newmp.put("type", remType);
						newmp.put("name", remName);
						newmp.put("rep", remRepeat);
						newmp.put("frequency", remFrequency);
						newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
						allReminderList.add(newmp);
						setAlarm(tmp.getTimeInMillis(), remName, (int)tmp.getTimeInMillis()%1000000007);
					}
					else if (remRepeat.equals("2")) {
						tmp.add(Calendar.DAY_OF_MONTH, (int)(7 * i));
						newmp = new HashMap<>();
						newmp.put("id", remTime);
						newmp.put("type", remType);
						newmp.put("name", remName);
						newmp.put("rep", remRepeat);
						newmp.put("frequency", remFrequency);
						newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
						allReminderList.add(newmp);
						setAlarm(tmp.getTimeInMillis(), remName, (int)tmp.getTimeInMillis()%1000000007);
					}
					else if (remRepeat.equals("3")) {
						tmp.add(Calendar.MONTH, (int)(i));
						newmp = new HashMap<>();
						newmp.put("id", remTime);
						newmp.put("type", remType);
						newmp.put("name", remName);
						newmp.put("rep", remRepeat);
						newmp.put("frequency", remFrequency);
						newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
						allReminderList.add(newmp);
						setAlarm(tmp.getTimeInMillis(), remName, (int)tmp.getTimeInMillis()%1000000007);
					}
					else if (remRepeat.equals("4")) {
						tmp.add(Calendar.MONTH, (int)(3 * i));
						newmp = new HashMap<>();
						newmp.put("id", remTime);
						newmp.put("type", remType);
						newmp.put("name", remName);
						newmp.put("rep", remRepeat);
						newmp.put("frequency", remFrequency);
						newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
						allReminderList.add(newmp);
						setAlarm(tmp.getTimeInMillis(), remName, (int)tmp.getTimeInMillis()%1000000007);
					}
					else if (remRepeat.equals("5")) {
						tmp.add(Calendar.YEAR, (int)(i));
						newmp = new HashMap<>();
						newmp.put("id", remTime);
						newmp.put("type", remType);
						newmp.put("name", remName);
						newmp.put("rep", remRepeat);
						newmp.put("frequency", remFrequency);
						newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
						allReminderList.add(newmp);
						setAlarm(tmp.getTimeInMillis(), remName, (int)tmp.getTimeInMillis()%1000000007);
					}
					i++;
				}
				sp1.edit().putString("allrem", new Gson().toJson(allReminderList)).commit();
				ina.setAction(Intent.ACTION_VIEW);
				ina.setClass(getApplicationContext(), HomeActivity.class);
				ina.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(ina);
				finish();
			}
			else {
				Toast.makeText(this, "Frequency required", Toast.LENGTH_SHORT).show();
			}
		}
		else {
			Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show();
		}
	}

	private void setAlarm(long time, String name, int id) {
		Intent intent = new Intent(this,AlarmBroadcastReceiver.class);
		intent.putExtra("name",name);
		intent.putExtra("id", id);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), id, intent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
		}
		else {
			alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
		}
	}

	public static class DatePickerFragment extends androidx.appcompat.app.AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			int mon = month +1;
			TextView textviewdated = (TextView) getActivity().findViewById(R.id.textviewdated);
			TextView textviewdatem = (TextView) getActivity().findViewById(R.id.textviewdatem);
			TextView textviewdatey = (TextView) getActivity().findViewById(R.id.textviewdatey);
			textviewdated.setText(day+"");
			textviewdatem.setText(mon+"");
			textviewdatey.setText(year+"");
		}
	}

	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		 @Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			return new TimePickerDialog(getActivity(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		 	TextView textviewTimeHr = (TextView) getActivity().findViewById(R.id.textviewtimeh);
		 	TextView textviewTimeMin = (TextView) getActivity().findViewById(R.id.textviewtimem);
		 	textviewTimeHr.setText(hourOfDay+"");
		 	textviewTimeMin.setText(minute+"");
		}
	}
}
