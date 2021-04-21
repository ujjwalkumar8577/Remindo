package com.ujjwalkumar.remindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.util.*;

import java.util.*;
import java.text.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RemindersActivity extends AppCompatActivity {
	
	
	private Toolbar _toolbar;
	private String stype = "";
	private String sname = "";
	private String stime = "";
	private String srep = "";
	private String sfrequency = "";
	private double t = 0;
	private double count = 0;
	
	private ArrayList<HashMap<String, Object>> reminderList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> allReminderList = new ArrayList<>();
	
	private LinearLayout linear1;
	private ListView listview1;
	
	private Intent inr = new Intent();
	private SharedPreferences sp1;
	private Calendar cal = Calendar.getInstance();
	private AlertDialog.Builder delete;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.reminders);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		listview1 = (ListView) findViewById(R.id.listview1);
		sp1 = getSharedPreferences("reminders", Activity.MODE_PRIVATE);
		delete = new AlertDialog.Builder(this);
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				delete.setTitle("Delete");
				delete.setMessage("Do you want to delete?");
				delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						_deleteID(reminderList.get((int)_position).get("id").toString());
						reminderList.remove((int)(_position));
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
	}
	private void initializeLogic() {
		setTitle("All Reminders");
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
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);

	}
	
	private void _deleteID (final String _ID) {
		t = 0;
		count = allReminderList.size();
		for(int _repeat10 = 0; _repeat10 < (int)(count); _repeat10++) {
			if (allReminderList.get((int)t).containsKey("id")) {
				if (allReminderList.get((int)t).get("id").toString().equals(_ID)) {
					allReminderList.remove((int)(t));
					count--;
					t--;
				}
			}
			t++;
		}
		sp1.edit().putString("allrem", new Gson().toJson(allReminderList)).commit();
		SketchwareUtil.showMessage(getApplicationContext(), "Deleted successfully");
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
			if (reminderList.get((int)_position).containsKey("type")) {
				stype = reminderList.get((int)_position).get("type").toString();
			}
			else {
				stype = "0";
			}
			if (reminderList.get((int)_position).containsKey("name")) {
				sname = reminderList.get((int)_position).get("name").toString();
			}
			else {
				sname = "NA";
			}
			if (reminderList.get((int)_position).containsKey("time")) {
				stime = reminderList.get((int)_position).get("time").toString();
			}
			else {
				stime = String.valueOf((long)(cal.getTimeInMillis()));
			}
			if (reminderList.get((int)_position).containsKey("rep")) {
				srep = reminderList.get((int)_position).get("rep").toString();
			}
			else {
				srep = "0";
			}
			if (reminderList.get((int)_position).containsKey("frequency")) {
				sfrequency = reminderList.get((int)_position).get("frequency").toString();
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
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int[] _location = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int[] _location = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
