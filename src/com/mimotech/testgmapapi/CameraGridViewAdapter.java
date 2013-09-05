package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CameraGridViewAdapter extends BaseAdapter {
	String tag = this.getClass().getSimpleName();
	Context mainContext;
	private ArrayList<Camera> camList;

	public CameraGridViewAdapter(Context context, ArrayList<Camera> camList) {
		Log.d(tag, "GridViewAdapte");
		mainContext = context;
		this.camList = camList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return camList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return camList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i(tag, "getView:  " + position);
		LayoutInflater lf = (LayoutInflater) mainContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View v = lf.inflate(R.layout.camera_fragment_gridview, null);

		TextView tv = (TextView) v.findViewById(R.id.cameraTextView1);
		tv.setText(camList.get(position).thaiName);
		
		ImageView iv = (ImageView) v.findViewById(R.id.cameraImageView1);
		iv.setImageResource(R.drawable.ic_launcher);
		String imgUrl = camList.get(position).imgUrl;
		Log.i(tag,"ImgUrl: "+camList.get(position).thaiName);
		new ImageLoader().download(imgUrl, iv);

		return v;
	}

}
