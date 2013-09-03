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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.news_fragment_detail);

		Log.d(tag, "onCreate1");

		Intent intent = getIntent();
		String sLat = intent.getStringExtra("startPointLat");
		String sLng = intent.getStringExtra("startPointLong");
		String title = intent.getStringExtra("title");
		String description = intent.getStringExtra("description");

		TextView tv = (TextView) findViewById(R.id.newsTextDetail);
		tv.setText(description);

		this.myMarker(sLat, sLng, title);

	}

	public void myMarker(String sLat, String sLng, String title) {

		LatLng accidentLatLng;
		Log.i(tag, "set up map");
		// set accident lat long
		if (sLat.equalsIgnoreCase("undefined")
				|| sLng.equalsIgnoreCase("undefined")) {
			accidentLatLng = new LatLng(0, 0);

		} else {

			accidentLatLng = new LatLng(Double.parseDouble(sLat),
					Double.parseDouble(sLng));

		}
		setUpMapIfNeeded(accidentLatLng, title);
	}

	private void setUpMapIfNeeded(LatLng accidentLatLng, String title) {

		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}

		if (mMap != null) {
			Log.i(tag, "Map not null");

			Marker marker = mMap.addMarker(new MarkerOptions()
					.position(accidentLatLng).title(title)
					.snippet(accidentLatLng.toString()));
			Log.d(tag, "setUpMarker");
			mMap.getUiSettings().setZoomControlsEnabled(true);
			marker.showInfoWindow();

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accidentLatLng,
					16));

		}

	}

}