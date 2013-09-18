package com.mimotech.testgmapapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class NewsDetailsActivity extends SherlockFragmentActivity implements
		OnClickListener {
	private String tag = getClass().getSimpleName();
	private GoogleMap mMap;
	private SocialAuthAdapter adapter;
	private String description;
	private RelativeLayout newsDetailNormalLayout;
	private RelativeLayout newsDetailShareLayout;
	private EditText newsDetailsShareEditText;

	private static final int REQUEST_CODE = 6384;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.news_fragment_detail);

		Log.d(tag, "onCreate1");

		newsDetailNormalLayout = (RelativeLayout) findViewById(R.id.news_detail_normal_layout);
		newsDetailShareLayout = (RelativeLayout) findViewById(R.id.news_detail_share_layout);

		newsDetailNormalLayout.setVisibility(View.VISIBLE);
		newsDetailShareLayout.setVisibility(View.GONE);

		Intent intent = getIntent();
		String sLat = intent.getStringExtra("startPointLat");
		String sLng = intent.getStringExtra("startPointLong");
		String title = intent.getStringExtra("title");
		description = intent.getStringExtra("description");

		// normal layout
		TextView tv = (TextView) findViewById(R.id.newsTextDetail);
		tv.setText(description);
		this.myMarker(sLat, sLng, title);
		Button share = (Button) findViewById(R.id.shareBtn);
		adapter = new SocialAuthAdapter(new ResponseListener());
		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		// Providers require setting user call Back url
		adapter.addCallBack(Provider.TWITTER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
		// Enable Provider
		adapter.enable((Button) share);

		// share layout
		TextView shareDetailTv = (TextView) findViewById(R.id.news_details_msg);
		shareDetailTv.setText(description);
		Button shareButton = (Button) findViewById(R.id.newsDetailsShareBtn);
		shareButton.setOnClickListener(this);

		newsDetailsShareEditText = (EditText) findViewById(R.id.newsDetailsShareEditText);

		ImageButton uploadImgBtn = (ImageButton) findViewById(R.id.uploadImgBtn);
		uploadImgBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				Log.i(tag, "upload image click");
				showChooser();
			}
		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	public void myMarker(String sLat, String sLng, String title) {

		LatLng accidentLatLng = new LatLng(0, 0);
		Log.i(tag, "set up map");
		// set accident lat long
		if (!sLat.equalsIgnoreCase("undefined")
				&& !sLng.equalsIgnoreCase("undefined")) {

			try {
				accidentLatLng = new LatLng(Double.parseDouble(sLat),
						Double.parseDouble(sLng));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

		}

		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.newsMap)).getMap();
		}

		if (mMap != null) {
			Log.i(tag, "Map not null");

			// calculate distance between user and event
			double howFar = (int) (new Info().distance(accidentLatLng.latitude,
					accidentLatLng.longitude, Info.lat, Info.lng, "K") * 100) / 100.0;

			if (accidentLatLng.latitude == 0 || accidentLatLng.longitude == 0) {
				howFar = 0;
			}
			// news marker
			String titileDetail = getString(R.string.farfromyou_msg) + ": "
					+ howFar + " km";
			Marker marker = mMap.addMarker(new MarkerOptions()
					.position(accidentLatLng).title(title)
					.snippet(titileDetail));
			Log.d(tag, "setUpMarkerNewsMarker");

			// user marker
			new LatLng(Info.lat, Info.lng);
			titileDetail = getString(R.string.farfromyou_msg) + ": " + howFar
					+ " km";
			Marker myMarker = mMap.addMarker(new MarkerOptions()
					.position(new LatLng(Info.lat, Info.lng))
					.title(getString(R.string.you_here_msg))
					.snippet(Info.reverseGpsName)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

			Log.d(tag, "setUpMarkerNewsMarker");

			mMap.getUiSettings().setZoomControlsEnabled(true);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accidentLatLng,
					11));
			// myMarker.showInfoWindow();
			marker.showInfoWindow();

		}
	}

	private void showChooser() {
		// Use the GET_CONTENT intent from the utility class
		Intent target = FileUtils.createGetContentIntent();
		// Create the chooser Intent
		Intent intent = Intent.createChooser(target,
				getString(R.string.chooser_label));
		try {
			startActivityForResult(intent, REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			// The reason for the existence of aFileChooser
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE:
			// If the file selection was successful
			if (resultCode == RESULT_OK) {
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();

					try {
						// Create a file instance from the URI
						final File file = FileUtils.getFile(uri);
						Log.i(tag,"path: "+file.getAbsolutePath());
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error",
								e);
					}
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

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

			newsDetailNormalLayout.setVisibility(View.GONE);
			newsDetailShareLayout.setVisibility(View.VISIBLE);

			// after connected complete redirect to share page
			Log.d(tag, "emergencyBtnOnClick");
			/*
			 * Intent shareBtn = new Intent(NewsDetailsActivity.this,
			 * ShareButtonActivity.class); startActivity(shareBtn);
			 */

			// adapter.updateStatus(description, new MessageListener(), false);

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

	@Override
	public void onClick(View v) {

		String quotMsg = newsDetailsShareEditText.getText().toString() + "\n"
				+ description;
		adapter.updateStatus(quotMsg, new MessageListener(), false);

		// prevent re-post return to first step
		newsDetailNormalLayout.setVisibility(View.VISIBLE);
		newsDetailShareLayout.setVisibility(View.GONE);

		try {

			String imgUri = "/mnt/sdcard/DCIM/Camera/IMG001.jpg";
			File bmFile = new File(imgUri);
			Bitmap bitmap = decodeFile(bmFile);

			Toast.makeText(NewsDetailsActivity.this, "POSTING IMAGE...",
					Toast.LENGTH_LONG).show();

			adapter.uploadImage(quotMsg, imgUri, bitmap, 100);

			Toast.makeText(NewsDetailsActivity.this, "POST IMAGE COMPLETE",
					Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 256;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

}