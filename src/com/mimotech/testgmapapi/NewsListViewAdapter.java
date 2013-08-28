package com.mimotech.testgmapapi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsListViewAdapter extends BaseAdapter {
	String tag = getClass().getSimpleName();
	private Context context;
	String newsId[];

	public NewsListViewAdapter(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		newsId = new String[Info.newsList.keySet().size()];
		int index = 0;
		for (String key : Info.newsList.keySet()) {

			newsId[index] = key;
			++index;
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(tag, "position: " + position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.news_fragment_listview,
				parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.newsLogo);
		TextView description = (TextView) rowView.findViewById(R.id.newsText);
		TextView reporter = (TextView) rowView.findViewById(R.id.reporter);

		imageView.setImageResource(R.drawable.ic_launcher);
		description.setText(Info.newsList.get(newsId[position]).description);
		reporter.setText("by: "
				+ Info.newsList.get(newsId[position]).secondarySource + "("
				+ Info.newsList.get(newsId[position]).primarySource + ")");
		return rowView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Info.newsList.keySet().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		String newsIdStr = Info.newsList.get(newsId[position]).id;
		int newsId = Integer.parseInt( newsIdStr  );
		return newsId;
	}

}