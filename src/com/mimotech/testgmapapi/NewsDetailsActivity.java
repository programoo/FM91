package com.mimotech.testgmapapi;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewsDetailsActivity extends SherlockFragmentActivity {
	private String tag = getClass().getSimpleName();
	private GoogleMap mMap;
	private static final LatLng BANGKOK = new LatLng(0, 0);
	private LatLng accidentLatLng;
	private News currentNews;
	private NewsDetailsPagerAdapter mPagerAdapter;
	private ViewPager pager;
	private Bitmap mSnapshot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.news_details_activity);

		Log.d(tag, "onCreate1");

		Intent intent = getIntent();
		int index = Integer.parseInt(intent.getStringExtra("index"));
		currentNews = Info.newsList.get(index);

		final List<Fragment> fragments = new Vector<Fragment>();
		for (int i = 0; i < Info.newsList.size(); i++) {
			NewsDetailsFragment ndf = NewsDetailsFragment.newInstance(i);
			fragments.add(ndf);

		}

		this.mPagerAdapter = new NewsDetailsPagerAdapter(
				getSupportFragmentManager(), fragments);

		final ViewPager pager = (ViewPager) findViewById(R.id.viewPagerNewsDetails);
		Log.i(tag, "pager: " + pager);
		Log.i(tag, "mPagerAdapter: " + this.mPagerAdapter);
		pager.setAdapter(this.mPagerAdapter);
		pager.setCurrentItem(index, true);

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth(); // deprecated
		int height = display.getHeight();

		pager.getLayoutParams().width = width;
		pager.getLayoutParams().height = 200;

		myMarker(index);
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageScrollStateChanged(int state) {
			}

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			public void onPageSelected(int position) {
				// Check if this is the page you want.
				Log.d(tag, "page num: " + position);
				myMarker(position);

			}
		});

	}

	public void myMarker(int index) {

		currentNews = Info.newsList.get(index);

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

	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		//if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		//}
	}

	public void onScreenshot() {
		if (mMap == null) {
			return;
		}

		final ImageView snapshotHolder = (ImageView) findViewById(R.id.snapshot_holder);
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
		mMap.getUiSettings().setZoomControlsEnabled(true);
		marker.showInfoWindow();

	}

	private void cameraZoom() {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accidentLatLng, 16));
	}

}