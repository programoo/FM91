package com.mimotech.testgmapapi;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
	private SocialAuthAdapter adapter;
	private String description;

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
		description = intent.getStringExtra("description");

		TextView tv = (TextView) findViewById(R.id.newsTextDetail);
		tv.setText(description);

		this.myMarker(sLat, sLng, title);

		// share button

		// Create Your Own Share Button
		Button share = (Button) findViewById(R.id.shareBtn);
		share.setText("Share");
		share.setTextColor(Color.WHITE);
		// share.setBackgroundResource(R.drawable.button_gradient);

		// Add it to Library
		adapter = new SocialAuthAdapter(new ResponseListener());

		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);

		// Providers require setting user call Back url
		adapter.addCallBack(Provider.TWITTER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		// Enable Provider
		adapter.enable(share);

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

		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.newsMap)).getMap();
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

	/**
	 * Listens Response from Library
	 * 
	 */

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			Log.d("ShareButton", "Authentication Successful");

			// Get name of provider after authentication
			final String providerName = values
					.getString(SocialAuthAdapter.PROVIDER);
			Log.d("ShareButton", "Provider Name = " + providerName);
			Toast.makeText(NewsDetailsActivity.this,
					providerName + " connected", Toast.LENGTH_LONG).show();

			adapter.updateStatus(description, new MessageListener(), false);

		}

		@Override
		public void onError(SocialAuthError error) {
			Log.d("ShareButton", "Authentication Error: " + error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("ShareButton", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			Log.d("Share-Button", "Dialog Closed by pressing Back Key");
		}

	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {
		@Override
		public void onExecute(String provider, Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204)
				Toast.makeText(NewsDetailsActivity.this,
						"Message posted on " + provider, Toast.LENGTH_LONG)
						.show();
			else
				Toast.makeText(NewsDetailsActivity.this,
						"Message not posted on " + provider, Toast.LENGTH_LONG)
						.show();
		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}

}