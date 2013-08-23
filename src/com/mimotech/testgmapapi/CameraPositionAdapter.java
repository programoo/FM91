package com.mimotech.testgmapapi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CameraPositionAdapter extends BaseAdapter {
	String tag = this.getClass().getSimpleName();
	Context mainContext;
	String[] data_str;

	public CameraPositionAdapter(Context context, String[] data_str) {
		Log.i(tag, "GridViewAdapte");
		mainContext = context;
		this.data_str = data_str;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data_str.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i(tag, "getView:  " + position);
		LayoutInflater lf = (LayoutInflater) mainContext
				.getSystemService(mainContext.LAYOUT_INFLATER_SERVICE);

		View v = lf.inflate(R.layout.camera_fragment_position, null);

		return v;
	}

}
