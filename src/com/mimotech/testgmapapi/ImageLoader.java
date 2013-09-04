package com.mimotech.testgmapapi;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	public void download(String url, ImageView imageView) {
		BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
		task.execute(url);
	}

	public void downloadBitmapToList(String url, ArrayList<Bitmap> arr) {
		BitmapDownloaderTask task = new BitmapDownloaderTask(arr, "keepBitmap");
		task.execute(url);
	}
}

class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	private String url;
	private final WeakReference<ImageView> imageViewReference;
	private ArrayList<Bitmap> arr;
	private String type = "undefined";

	public BitmapDownloaderTask(ImageView imageView) {
		imageViewReference = new WeakReference<ImageView>(imageView);
		type = "undefined";
	}

	public BitmapDownloaderTask(ArrayList<Bitmap> arr, String type) {
		imageViewReference = new WeakReference<ImageView>(null);
		this.arr = arr;
		this.type = type;
	}

	@Override
	// Actual download method, run in the task thread
	protected Bitmap doInBackground(String... params) {
		// params comes from the execute() call: params[0] is the url.
		return downloadBitmap(params[0]);
	}

	@Override
	// Once the image is downloaded, associates it to the imageView
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (this.type.equalsIgnoreCase("undefined")) {
			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		} else {
			arr.add(bitmap);
		}

	}

	static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			e.printStackTrace();
			// Log.w("ImageDownloader", "Error while retrieving bitmap from " +
			// url, e.toString());
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}
}