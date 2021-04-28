package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

import java.util.*;
import java.text.*;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AddActivity extends AppCompatActivity {

	private ArrayList<String> types = new ArrayList<>();
	private ArrayList<String> repeats = new ArrayList<>();
	private ArrayList<Reminder> reminders = new ArrayList<>();
	private ArrayList<UnitReminder> allReminders = new ArrayList<>();
	private int i = 0;

	private Toolbar toolbar;
	private LottieAnimationView animationView;
	private LinearLayout layout;
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
	private static Calendar cal = Calendar.getInstance();
	private Intent ina = new Intent();
	private AlertDialog.Builder exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);

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

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		animationView = (LottieAnimationView) findViewById(R.id.animationView);
		layout = (LinearLayout) findViewById(R.id.layout);
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

		sp1 = getSharedPreferences("reminderData", Activity.MODE_PRIVATE);
		exit = new AlertDialog.Builder(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		layout.setVisibility(View.VISIBLE);
		animationView.setVisibility(View.GONE);

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

		spinnerrepeat.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, repeats));
		((ArrayAdapter)spinnerrepeat.getAdapter()).notifyDataSetChanged();
		spinnertype.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, types));
		((ArrayAdapter)spinnertype.getAdapter()).notifyDataSetChanged();

		cal = Calendar.getInstance();
		textviewdated.setText(new SimpleDateFormat("d").format(cal.getTime()));
		textviewdatem.setText(new SimpleDateFormat("MM").format(cal.getTime()));
		textviewdatey.setText(new SimpleDateFormat("yyyy").format(cal.getTime()));

		if (!sp1.getString("reminders", "").equals("")) {
			reminders = new Gson().fromJson(sp1.getString("reminders", ""), new TypeToken<ArrayList<Reminder>>(){}.getType());
		}
		if (!sp1.getString("allReminders", "").equals("")) {
			allReminders = new Gson().fromJson(sp1.getString("allReminders", ""), new TypeToken<ArrayList<UnitReminder>>(){}.getType());
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
		if (!edittextname.getText().toString().equals("")) {
			if (!edittextfreq.getText().toString().equals("")) {

				int frequency = Integer.parseInt(edittextfreq.getText().toString());
				String name = edittextname.getText().toString();
				String type = spinnertype.getSelectedItem().toString();
				String repeat = spinnerrepeat.getSelectedItem().toString();
				long startTime = cal.getTimeInMillis();

				if(frequency<1) {
					frequency = 1;
					edittextfreq.setText("1");
					Toast.makeText(this, "Frequency set as 1", Toast.LENGTH_SHORT).show();
				}

				Reminder reminder = new Reminder(frequency, name, type, repeat, startTime);
				reminders.add(reminder);

				int ID = reminder.getID();
				long[] time = reminder.getTime();

				for(i=0; i<frequency; i++) {
					UnitReminder unitReminder = new UnitReminder(i, ID, frequency, name, type, repeat, time[i]);
					allReminders.add(unitReminder);
					setAlarm(time[i], name, ID+i);
				}

				sp1.edit().putString("reminders", new Gson().toJson(reminders)).apply();
				sp1.edit().putString("allReminders", new Gson().toJson(allReminders)).apply();

				layout.setVisibility(View.GONE);
				animationView.setVisibility(View.VISIBLE);
				animationView.loop(false);
				animationView.playAnimation();
				animationView.addAnimatorListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {

					}

					@Override
					public void onAnimationEnd(Animator animator) {
						ina.setAction(Intent.ACTION_VIEW);
						ina.setClass(getApplicationContext(), HomeActivity.class);
						ina.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(ina);
						finish();
					}

					@Override
					public void onAnimationCancel(Animator animator) {
						ina.setAction(Intent.ACTION_VIEW);
						ina.setClass(getApplicationContext(), HomeActivity.class);
						ina.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(ina);
						finish();
					}

					@Override
					public void onAnimationRepeat(Animator animator) {

					}
				});


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
		Calendar ctmp = Calendar.getInstance();
		ctmp.setTimeInMillis(time);

		Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
		intent.putExtra("name", name);
		intent.putExtra("id", id);
		intent.putExtra("time", new SimpleDateFormat("h:mm a").format(ctmp.getTime()));
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

			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.YEAR, year);
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

			cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
			cal.set(Calendar.MINUTE, minute);
		}
	}
}
