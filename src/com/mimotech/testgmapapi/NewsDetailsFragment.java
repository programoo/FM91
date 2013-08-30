package com.mimotech.testgmapapi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewsDetailsFragment extends Fragment {
	private String tag = getClass().getSimpleName();
	private GoogleMap mMap;
	private static final LatLng BANGKOK = new LatLng(13.724714, 100.633111);
	private LatLng accidentLatLng;
	private News currentNews;
	private int pageId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(tag, "onCreateView");
		View v = inflater.inflate(R.layout.news_fragment_detail, container,
				false);
		TextView tv = (TextView) v.findViewById(R.id.newsTextDetail);
		tv.setText(currentNews.description);
		Log.i(tag, "page id: " + pageId);
		Log.i(tag, currentNews.startPointLat + "," + currentNews.startPointLong);
		Log.i(tag, "acciden lat: " + accidentLatLng);
		this.cameraZoom();
		this.setUpMap();
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(tag, "onCreate");

		currentNews = Info.newsList.get(pageId);
		// set accident lat long
		if (currentNews.endPointLat.equalsIgnoreCase("undefined")
				|| currentNews.endPointLong.equalsIgnoreCase("undefined")) {
			accidentLatLng = BANGKOK;

		} else {

			accidentLatLng = new LatLng(
					Double.parseDouble(currentNews.startPointLat),
					Double.parseDouble(currentNews.startPointLong));

		}

		this.setUpMapIfNeeded();
		this.cameraZoom();

	}

	private void cameraZoom() {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accidentLatLng, 15));

	}

	private void setUpMap() {
		Marker marker = mMap.addMarker(new MarkerOptions()
				.position(accidentLatLng)
				.title(currentNews.title)
				.snippet(
						currentNews.startPointLat + ","
								+ currentNews.startPointLong));
		Log.d(tag, "setUpMaker");
		mMap.getUiSettings().setZoomControlsEnabled(false);
		marker.showInfoWindow();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getActivity()
					.getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
		Log.d(tag, "update view ja");

	}

}