package com.mimotech.testgmapapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewsDetailsActivity extends SherlockFragmentActivity {
	private String tag = getClass().getSimpleName();
	private GoogleMap mMap;
	private static final LatLng BANGKOK = new LatLng(13.724714, 100.633111);
	private LatLng accidentLatLng;
	private News currentNews;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.news_fragment_detail);

		Log.d(tag, "onCreate1");

		Intent intent = getIntent();
		String id = intent.getStringExtra("newsId");
		currentNews = Info.getNews(id);
		// set text
		TextView tv = (TextView) findViewById(R.id.newsTextDetail);
		Log.d(tag, "id :" + id);
		tv.setText(Info.getNews(id).description);

		// set accident lat long
		if (Info.getNews(id).endPointLat.equalsIgnoreCase("undefined")
				|| Info.getNews(id).endPointLong.equalsIgnoreCase("undefined")) {
			accidentLatLng = BANGKOK;

		} else {

			accidentLatLng = new LatLng(
					Double.parseDouble(Info.getNews(id).startPointLat),
					Double.parseDouble(Info.getNews(id).startPointLong));

		}

		this.setUpMapIfNeeded();
		this.cameraZoom();

	}

	private void cameraZoom() {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accidentLatLng, 15));

	}

	private void setUpMap() {
		Marker marker = mMap.addMarker(new MarkerOptions()
				.position(accidentLatLng).title(currentNews.title)
				.snippet("Population: 4,137,400"));
		Log.d(tag, "maker not work");
		
		marker.showInfoWindow();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

}