package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class OtherFragment extends Fragment implements OnItemClickListener
{
	private String TAG = this.getClass().getSimpleName();
	private View v;
	private ArrayList<String> strList;
	private ListView lv;
	
	// configuration variable
	private View viewSelected;
	private String latLnConfig;
	private boolean crimTick = false;
	private boolean accidentTick = false;
	private boolean otherTick = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		latLnConfig = "";
		strList = new ArrayList<String>();
		
		strList.add("Profile");
		strList.add("facebook");
		strList.add("twitter");
		strList.add("Setting");
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.v = inflater.inflate(R.layout.other_fragment, container, false);
		
		lv = (ListView) this.v.findViewById(R.id.otherLv);
		Log.i(TAG, "" + strList.size());
		OtherListViewAdapter adapter = new OtherListViewAdapter(getActivity(),
				strList,R.layout.other_fragment_listview);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(this);
		
		Log.i(TAG, "setting listview complete");
		
		return this.v;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		String dialogName = (String) this.lv.getItemAtPosition(arg2);
		if (dialogName.equalsIgnoreCase("setting"))
		{
			final Dialog settingDialog = new Dialog(getActivity(),
					android.R.style.Theme_Light_NoTitleBar);
			settingDialog.getWindow();
			settingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			settingDialog.setContentView(R.layout.setting_dialog);
			settingDialog.setCancelable(true);
			settingDialog.show();
			
			final ListView lv = (ListView) settingDialog
					.findViewById(R.id.settingDialogLv);
			
			ArrayList<String> strSettingList = new ArrayList<String>();
			strSettingList.add(getString(R.string.place_setting_text));
			strSettingList.add(getString(R.string.radious_setting_text));
			strSettingList.add(getString(R.string.rewind_setting_text));
			
			OtherListViewAdapter settingsAdapter = new OtherListViewAdapter(
					getActivity(), strSettingList,R.layout.other_fragment_settings_listview);
			lv.setAdapter(settingsAdapter);
			
			final ToggleButton crimTg = (ToggleButton) settingDialog
					.findViewById(R.id.crimeTgBtn);
			final ToggleButton accidentTg = (ToggleButton) settingDialog
					.findViewById(R.id.accidentTgBtn);
			final ToggleButton otherTg = (ToggleButton) settingDialog
					.findViewById(R.id.otherTgBtn);
			
			crimTg.setChecked(this.crimTick);
			accidentTg.setChecked(this.accidentTick);
			otherTg.setChecked(this.otherTick);
			
			Log.i(TAG, "is check: " + crimTg.isChecked());
			Log.i(TAG, "is check: " + accidentTg.isChecked());
			Log.i(TAG, "is check: " + otherTg.isChecked());
			
			settingDialog.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface dialog)
				{
					crimTick = crimTg.isChecked();
					accidentTick = accidentTg.isChecked();
					otherTick = otherTg.isChecked();
					
					Log.i(TAG, "is check: " + crimTg.isChecked());
					Log.i(TAG, "is check: " + accidentTg.isChecked());
					Log.i(TAG, "is check: " + otherTg.isChecked());
				}
			});
			
			lv.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					// show select view 
					viewSelected = view;
					String setType = (String) lv.getItemAtPosition(position);
					if (setType
							.equalsIgnoreCase(getString(R.string.place_setting_text)))
					{
						Intent i = new Intent(getActivity(),
								InformMapSelectorActivity.class);
						startActivityForResult(i, Info.RESULT_SELECTED_POSITION);
					} else if (setType
							.equalsIgnoreCase(getString(R.string.radious_setting_text)))
					{
						Log.i(TAG, "radius text setting click");
						
						final Dialog radiusSettingDialog = new Dialog(
								getActivity());
						radiusSettingDialog.getWindow();
						radiusSettingDialog
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
						radiusSettingDialog
								.setContentView(R.layout.radious_setting_dialog);
						radiusSettingDialog.setCancelable(true);
						radiusSettingDialog.show();
						
						final RadioButton oneKm = (RadioButton) radiusSettingDialog.findViewById(R.id.oneKilometerRadio);
						oneKm.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Log.i(TAG,oneKm.isChecked()+"");
							}
						});
						
						final RadioButton threeKm = (RadioButton) radiusSettingDialog.findViewById(R.id.threeKilometerRadio);
						threeKm.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Log.i(TAG,threeKm.isChecked()+"");
							}
						});
						final RadioButton fiveKm = (RadioButton) radiusSettingDialog.findViewById(R.id.fiveKilometerRadio);
						fiveKm.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Log.i(TAG,fiveKm.isChecked()+"");
							}
						});
						
					}
					
				}
			});
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Info.RESULT_SELECTED_POSITION)
		{
			if (resultCode == Info.RESULT_OK)
			{
				String result = data.getStringExtra("result");
				Log.i(TAG, "result from selector" + result);
				latLnConfig = result;
				TextView tv = (TextView) this.viewSelected.findViewById(R.id.otherSelectedDataTv);
				tv.setText(result);
			}
			if (resultCode == Info.RESULT_CANCELED)
			{
				Log.e(TAG, "result onActivityResult error");
			}
		}
	}// onActivityResult
	
}
