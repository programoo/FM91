package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class CameraDetailsActivity extends SherlockFragmentActivity {
	private String tag = getClass().getSimpleName();
	private ArrayList<Bitmap> bitMapList;
	ImageView iv;
	TextView tv;
	private boolean run = true;

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		Log.d(tag, "onAttachFragment");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camera_fragment_detail);

		Log.d(tag, "onCreate");
		bitMapList = new ArrayList<Bitmap>();

		Intent intent = getIntent();
		String[] imgList = intent.getStringExtra("imgList").split(",");
		for (int i = 0; i < imgList.length; i++) {
			new ImageLoader().downloadBitmapToList(imgList[i], bitMapList);
			bitMapList.add(null);
		}
		Log.i(tag, "loading complete");
		String description = intent.getStringExtra("description");
		iv = (ImageView) findViewById(R.id.cameraDetail);

		tv = (TextView) findViewById(R.id.cameraDescription);
		tv.setText(description);

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(tag, "onPause");

	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(tag, "onStop");
		Log.i(tag, "arr size: " + bitMapList.size());
		run = false;

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(tag, "onResume");

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Log.d(tag, "onAttachedToWindow");

	}

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		Log.d(tag, "onCreateView");

		return super.onCreateView(parent, name, context, attrs);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(tag, "onStart");

		new Thread(new HelloRunnable()).start();

	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String text = (String) msg.obj;
			int index = Integer.parseInt(text) % 5;

			if (bitMapList.size() >index && bitMapList.get(index) != null ) {
				iv.setImageBitmap(bitMapList.get(index));
			}
		}
	};

	public void updateUIThread(String msgStr) {
		Message msg = new Message();
		String textTochange = msgStr;
		msg.obj = textTochange;
		mHandler.sendMessage(msg);
	}

	private class HelloRunnable implements Runnable {

		public void run() {
			int i = 1;
			while (run) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				updateUIThread(i + "");
				i++;
			}
		}

	}

}