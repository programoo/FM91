package com.mimotech.testgmapapi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsListViewAdapter extends ArrayAdapter<String> {
	String tag = getClass().getSimpleName();
	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;

	public NewsListViewAdapter(Context context, int resource, String[] objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.Ids = objects;
		this.rowResourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(tag, "position: " + position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(rowResourceId, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.newsLogo);
		TextView textView = (TextView) rowView.findViewById(R.id.newsText);

		imageView.setImageResource(R.drawable.ic_launcher);
		textView.setText(Ids[position]);

		return rowView;

	}

}