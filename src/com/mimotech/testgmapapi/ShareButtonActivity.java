/*
 ===========================================================================
 Copyright (c) 2012 Three Pillar Global Inc. http://threepillarglobal.com

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sub-license, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ===========================================================================
 */

package com.mimotech.testgmapapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.brickred.socialauth.Photo;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Main class of the ShareButton Example for SocialAuth Android SDK. <br>
 * 
 * The main objective of this example is to access social media providers
 * Facebook, Twitter and others by clicking a single button "Share".On Clicking
 * the button the api will open dialog of providers. User can access the
 * provider from dialog and can update the status.
 * 
 * The class first creates a button in main.xml. It then adds button to
 * SocialAuth Android Library <br>
 * 
 * Then it adds providers Facebook, Twitter and others to library object by
 * addProvider method and finally enables the providers by calling enable method<br>
 * 
 * After successful authentication of provider, it receives the response in
 * responseListener and then update status by updatestatus() method <br>
 * 
 * @author vineet.aggarwal@3pillarglobal.com
 * 
 */

public class ShareButtonActivity extends Activity {

	// SocialAuth Component
	SocialAuthAdapter adapter;
	Profile profileMap;
	List<Photo> photosList;

	// Android Components
	Button update;
	EditText edit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.share_activity);

		// Welcome Message
		TextView textview = (TextView) findViewById(R.id.text);
		textview.setText("Welcome to SocialAuth Demo. Connect any provider and then press Update button to Share Update.");

		// Create Your Own Share Button
		Button share = (Button) findViewById(R.id.sharebutton);
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
			Toast.makeText(ShareButtonActivity.this,
					providerName + " connected", Toast.LENGTH_LONG).show();
			Toast.makeText(ShareButtonActivity.this,
					"DCIM: " + Environment.DIRECTORY_DCIM, Toast.LENGTH_LONG)
					.show();
			update = (Button) findViewById(R.id.update);
			edit = (EditText) findViewById(R.id.editTxt);

			// Please avoid sending duplicate message. Social Media Providers
			// block duplicate messages.

			update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					adapter.updateStatus(edit.getText().toString(),
							new MessageListener(), false);

					// Use your own code here

					String imgUri = "/mnt/sdcard/DCIM/Camera/IMG001.jpg";
					try {

						File bmFile = new File(imgUri);
						Bitmap bitmap = decodeFile(bmFile);

						Toast.makeText(ShareButtonActivity.this,
								"POSTING IMAGE...", Toast.LENGTH_LONG)
								.show();		
								
						adapter.uploadImage("posting image..", imgUri,
								bitmap, 100);
						Toast.makeText(ShareButtonActivity.this,
								"POST IMAGE COMPLETE", Toast.LENGTH_LONG)
								.show();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(ShareButtonActivity.this,
								"ERROR HAPPEN HERE", Toast.LENGTH_LONG).show();

						e.printStackTrace();
					}
					Toast.makeText(ShareButtonActivity.this, "POSTED IMAGE",
							Toast.LENGTH_LONG).show();

					// to share on multiple providers
					// adapter.updateStatus(edit.getText().toString(), new
					// MessageListener(), false);
				}
			});

			// Share via Email Intent
			if (providerName.equalsIgnoreCase("share_mail")) {
				// Use your own code here
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
						Uri.fromParts("mailto",
								"vineet.aggarwal@3pillarglobal.com", null));
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"Test");
				File file = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
						"image5964402.png");
				Uri uri = Uri.fromFile(file);
				emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
				startActivity(Intent.createChooser(emailIntent, "Test"));
			}

			// Share via mms intent
			if (providerName.equalsIgnoreCase("share_mms")) {

				// Use your own code here
				File file = new File(
						Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM
								+ "/Camera/"), "IMG001.jpg.png");
				Uri uri = Uri.fromFile(file);

				Intent mmsIntent = new Intent(Intent.ACTION_SEND, uri);
				mmsIntent.putExtra("sms_body", "Test");
				mmsIntent.putExtra(Intent.EXTRA_STREAM, uri);
				mmsIntent.setType("image/png");
				startActivity(mmsIntent);
			}

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
				return BitmapFactory.decodeStream(new FileInputStream(f), null,
						o2);
			} catch (FileNotFoundException e) {
			}
			return null;
		}

	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {
		@Override
		public void onExecute(String provider, Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204)
				Toast.makeText(ShareButtonActivity.this,
						"Message posted on " + provider, Toast.LENGTH_LONG)
						.show();
			else
				Toast.makeText(ShareButtonActivity.this,
						"Message not posted on " + provider, Toast.LENGTH_LONG)
						.show();
		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}

}