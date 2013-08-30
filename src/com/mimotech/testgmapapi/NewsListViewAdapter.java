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
		newsId = new String[Info.newsList.size()];
		for (int i = 0; i < Info.newsList.size(); i++) {
			newsId[i] = Info.newsList.get(i).id;
		}
		Log.d(tag,"NewsListViewAdapter");

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(tag, "position: " + position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = inflater.inflate(R.layout.news_fragment_listview, parent,
				false);

		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.newsLogo);
		TextView description = (TextView) convertView
				.findViewById(R.id.newsText);
		TextView reporter = (TextView) convertView.findViewById(R.id.reporter);

		TextView endTime = (TextView) convertView.findViewById(R.id.newsTime);

		imageView.setImageResource(R.drawable.ic_launcher);
		description.setText(Info.getNews(newsId[position]).description);
		reporter.setText("by: "
				+ Info.getNews(newsId[position]).secondarySource + "("
				+ Info.getNews(newsId[position]).primarySource + ")");
		endTime.setText(Info.getNews(newsId[position]).startTime);

		// hidden isRead marker if already read and
		if (Info.getNews(newsId[position]).isRead) {
			ImageView isReadImg = (ImageView) convertView
					.findViewById(R.id.isRead);
			isReadImg.setVisibility(View.INVISIBLE);
		}

		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Info.newsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		String newsIdStr = Info.getNews(newsId[position]).id;
		int newsId = Integer.parseInt(newsIdStr);
		return newsId;
	}

}