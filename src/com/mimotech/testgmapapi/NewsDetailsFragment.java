package com.mimotech.testgmapapi;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewsDetailsFragment extends Fragment {
	private String tag = getClass().getSimpleName();
	private News currentNews;
	private int pageId;
	private View v;
	private TextView tv;
	private GoogleMap mMap;
	private Bitmap mSnapshot;
	private LatLng accidentLatLng;
	private static final LatLng BANGKOK = new LatLng(0, 0);

	private void setPage(int page) {
		pageId = page;
	}

	public static NewsDetailsFragment newInstance(int someInt) {
		NewsDetailsFragment myFragment = new NewsDetailsFragment();

		Bundle args = new Bundle();
		args.putInt("news_index", someInt);
		myFragment.setPage(someInt);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(tag, "onCreateView");
		v = inflater.inflate(R.layout.news_fragment_detail, null);
		tv = (TextView) v.findViewById(R.id.newsTextDetail);
		currentNews = Info.newsList.get(pageId);

		tv.setText(currentNews.description + ",page id: " + this.pageId + ","
				+ Info.newsList.get(this.pageId).startPointLat + ","
				+ Info.newsList.get(this.pageId).startPointLong);
		Log.i(tag, "page id: " + this.pageId);

		return v;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		/*
		 * currentNews = Info.newsList.get(this.pageId);
		 * 
		 * // set accident lat long if
		 * (currentNews.endPointLat.equalsIgnoreCase("undefined") ||
		 * currentNews.endPointLong.equalsIgnoreCase("undefined")) {
		 * accidentLatLng = BANGKOK;
		 * 
		 * } else {
		 * 
		 * accidentLatLng = new LatLng(
		 * Double.parseDouble(currentNews.startPointLat),
		 * Double.parseDouble(currentNews.startPointLong));
		 * 
		 * }
		 * 
		 * setUpMapIfNeeded(); cameraZoom(); onScreenshot();
		 */
	}

	public void snapShotProcess() {

		currentNews = Info.newsList.get(this.pageId);

		// set accident lat long
		if (currentNews.endPointLat.equalsIgnoreCase("undefined")
				|| currentNews.endPointLong.equalsIgnoreCase("undefined")) {
			accidentLatLng = BANGKOK;

		} else {

			accidentLatLng = new LatLng(
					Double.parseDouble(currentNews.startPointLat),
					Double.parseDouble(currentNews.startPointLong));

		}

		setUpMapIfNeeded();
		cameraZoom();
		onScreenshot();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(tag, "onCreate");

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

	public void onScreenshot() {
		if (mMap == null) {
			return;
		}

		final ImageView snapshotHolder = (ImageView) getActivity()
				.findViewById(R.id.snapshot_holder);
		Log.d(tag, "onScreenshot");

		SnapshotReadyCallback callback = new SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap snapshot) {
				// Callback is called from the main thread, so we can modify the
				// ImageView safely.
				Log.d(tag, "onSnapshotReady");

				snapshotHolder.setImageBitmap(snapshot);
				// Use the same bitmap for the following snapshots.
				Log.d(tag, "afterSnapshotReady");
				mSnapshot = snapshot;
			}
		};

		// mSnapshot is null on the first call. It is then set in the callback
		// to reuse the same
		// Bitmap object for all the following snapshots thus avoiding creating
		// a new bitmap for
		// every snapshot.
		Log.d(tag, "setup callBack");

		mMap.snapshot(callback, mSnapshot);
		Log.d(tag, "setup callBack complete");

	}

	private void setUpMap() {
		Marker marker = mMap.addMarker(new MarkerOptions()
				.position(accidentLatLng)
				.title(currentNews.title)
				.snippet(
						currentNews.startPointLat + ","
								+ currentNews.startPointLong));
		Log.d(tag, "setUpMarker");
		mMap.getUiSettings().setZoomControlsEnabled(false);
		marker.showInfoWindow();

	}

	private void cameraZoom() {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accidentLatLng, 15));
	}

}