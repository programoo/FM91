package com.mimotech.testgmapapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

public class NewsDetailsActivity extends FragmentActivity {
	private String tag = getClass().getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_fragment_detail);
		Log.d(tag, "onCreate1");
		
		Intent intent = getIntent();
		String id = intent.getStringExtra("newsId");
		
		//set text
		TextView tv = (TextView) findViewById(R.id.newsTextDetail);
		Log.d(tag,"id :"+id);
		tv.setText(Info.getNews(id).description);
		
	}

}