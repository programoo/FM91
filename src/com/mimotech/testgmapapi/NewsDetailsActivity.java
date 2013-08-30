package com.mimotech.testgmapapi;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class NewsDetailsActivity extends SherlockFragmentActivity {
	private String tag = getClass().getSimpleName();
	private GoogleMap mMap;
	private static final LatLng BANGKOK = new LatLng(13.724714, 100.633111);
	private LatLng accidentLatLng;
	private News currentNews;
	private NewsDetailsPagerAdapter mPagerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.news_details_activity);

		Log.d(tag, "onCreate1");

		Intent intent = getIntent();
		String id = intent.getStringExtra("newsId");
		currentNews = Info.getNews(id);

		List<Fragment> fragments = new Vector<Fragment>();
		for (int i = 0; i < 10; i++) {
			NewsDetailsFragment ndf = new NewsDetailsFragment();
			ndf.setPageId(i);
			fragments.add(ndf);

		}

		this.mPagerAdapter = new NewsDetailsPagerAdapter(
				getSupportFragmentManager(), fragments);

		ViewPager pager = (ViewPager) findViewById(R.id.viewPagerNewsDetails);
		Log.i(tag, "pager: " + pager);
		Log.i(tag, "mPagerAdapter: " + this.mPagerAdapter);
		pager.setAdapter(this.mPagerAdapter);
		pager.setCurrentItem(3, true);

	}

}