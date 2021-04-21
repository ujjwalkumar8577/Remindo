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
	
	private Toolbar toolbar;
	private HashMap<String, Object> mp = new HashMap<>();
	private double t = 0;
	private HashMap<String, Object> newmp = new HashMap<>();
	private String stype = "";
	private String sname = "";
	private String stime = "";
	private String srep = "";
	private String sfrequency = "";
	
	private ArrayList<String> types = new ArrayList<>();
	private ArrayList<String> repeats = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> reminderList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> allReminderList = new ArrayList<>();

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
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (_position == 0) {
					spinnerrepeat.setSelection((int)(0));
					edittextfreq.setText("1");
				}
				if (_position == 3) {
					spinnerrepeat.setSelection((int)(1));
					edittextfreq.setText("365");
				}
				if (_position == 8) {
					spinnerrepeat.setSelection((int)(3));
					edittextfreq.setText("12");
				}
				if (_position == 6) {
					spinnerrepeat.setSelection((int)(4));
					edittextfreq.setText("12");
				}
				if ((_position == 1) || ((_position == 2) || ((_position == 4) || ((_position == 5) || (_position == 7))))) {
					spinnerrepeat.setSelection((int)(5));
					edittextfreq.setText("5");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> _param1) {

			}
		});

		spinnerrepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;

			}

			@Override
			public void onNothingSelected(AdapterView<?> _param1) {

			}
		});

		buttonsave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				cal.set(Calendar.DAY_OF_MONTH, (int)(Double.parseDouble(textviewdated.getText().toString())));
				cal.set(Calendar.MONTH, (int)(Double.parseDouble(textviewdatem.getText().toString()) - 1));
				cal.set(Calendar.YEAR, (int)(Double.parseDouble(textviewdatey.getText().toString())));
				cal.set(Calendar.HOUR, (int)(Double.parseDouble(textviewtimeh.getText().toString())));
				cal.set(Calendar.MINUTE, (int)(Double.parseDouble(textviewtimem.getText().toString())));
				cal.add(Calendar.HOUR, (int)(-12));
				SketchwareUtil.showMessage(getApplicationContext(), new SimpleDateFormat("d/MM/yyyy    hh:mm").format(cal.getTime()));
				if (!edittextname.getText().toString().equals("")) {
					if (!edittextfreq.getText().toString().equals("")) {
						stype = String.valueOf((long)(spinnertype.getSelectedItemPosition()));
						sname = edittextname.getText().toString();
						stime = String.valueOf((long)(cal.getTimeInMillis()));
						srep = String.valueOf((long)(spinnerrepeat.getSelectedItemPosition()));
						sfrequency = edittextfreq.getText().toString();
						mp = new HashMap<>();
						mp.put("id", stime);
						mp.put("type", stype);
						mp.put("name", sname);
						mp.put("time", stime);
						mp.put("rep", srep);
						mp.put("frequency", sfrequency);
						reminderList.add(mp);
						sp1.edit().putString("rem", new Gson().toJson(reminderList)).commit();
						t = 0;
						for(int _repeat82 = 0; _repeat82 < (int)(Double.parseDouble(edittextfreq.getText().toString())); _repeat82++) {
							tmp.setTimeInMillis((long)(cal.getTimeInMillis()));
							if (srep.equals("1") || srep.equals("0")) {
								tmp.add(Calendar.DAY_OF_MONTH, (int)(t));
								newmp = new HashMap<>();
								newmp.put("id", stime);
								newmp.put("type", stype);
								newmp.put("name", sname);
								newmp.put("rep", srep);
								newmp.put("frequency", sfrequency);
								newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
								allReminderList.add(newmp);
							}
							else {
								if (srep.equals("2")) {
									tmp.add(Calendar.DAY_OF_MONTH, (int)(7 * t));
									newmp = new HashMap<>();
									newmp.put("id", stime);
									newmp.put("type", stype);
									newmp.put("name", sname);
									newmp.put("rep", srep);
									newmp.put("frequency", sfrequency);
									newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
									allReminderList.add(newmp);
								}
								else {
									if (srep.equals("3")) {
										tmp.add(Calendar.MONTH, (int)(t));
										newmp = new HashMap<>();
										newmp.put("id", stime);
										newmp.put("type", stype);
										newmp.put("name", sname);
										newmp.put("rep", srep);
										newmp.put("frequency", sfrequency);
										newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
										allReminderList.add(newmp);
									}
									else {
										if (srep.equals("4")) {
											tmp.add(Calendar.MONTH, (int)(3 * t));
											newmp = new HashMap<>();
											newmp.put("id", stime);
											newmp.put("type", stype);
											newmp.put("name", sname);
											newmp.put("rep", srep);
											newmp.put("frequency", sfrequency);
											newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
											allReminderList.add(newmp);
										}
										else {
											if (srep.equals("5")) {
												tmp.add(Calendar.YEAR, (int)(t));
												newmp = new HashMap<>();
												newmp.put("id", stime);
												newmp.put("type", stype);
												newmp.put("name", sname);
												newmp.put("rep", srep);
												newmp.put("frequency", sfrequency);
												newmp.put("time", String.valueOf((long)(tmp.getTimeInMillis())));
												allReminderList.add(newmp);
											}
										}
									}
								}
							}
							t++;
						}
						sp1.edit().putString("allrem", new Gson().toJson(allReminderList)).commit();
						SketchwareUtil.showMessage(getApplicationContext(), "Reminder added");
						ina.setAction(Intent.ACTION_VIEW);
						ina.setClass(getApplicationContext(), HomeActivity.class);
						ina.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(ina);
						finish();
					}
					else {
						SketchwareUtil.showMessage(getApplicationContext(), "Frequency required");
					}
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Name required");
				}
			}
		});

		setTitle("Add");
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

	public static class DatePickerFragment extends androidx.appcompat.app.AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		public void onDateSet(DatePicker view, int year, int month, int day) {
			int mon = month +1;
			String date = day + " / " + mon + " / " + year;
			
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
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			((TextView)getActivity().findViewById(R.id.textviewtimeh)).setText(hourOfDay+"");
			((TextView)getActivity().findViewById(R.id.textviewtimem)).setText(minute+"");
		}
	}
}
