package com.mimotech.testgmapapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class CameraDetailsActivity extends SherlockFragmentActivity
{
	private String TAG = getClass().getSimpleName();
	private ArrayList<Bitmap> bitMapList;
	ImageView iv;
	TextView tv;
	private boolean run = true;
	private ToggleButton bookMarkImgBtn;
	
	@Override
	public void onAttachFragment(Fragment fragment)
	{
		super.onAttachFragment(fragment);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.camera_fragment_detail);
		
		bitMapList = new ArrayList<Bitmap>();
		
		Intent intent = getIntent();
		String[] imgList = intent.getStringExtra("imgList").split(",");
		for (int i = 0; i < imgList.length; i++)
		{
			new ImageLoader().downloadBitmapToList(imgList[i], bitMapList);
			bitMapList.add(null);
		}
		String description = intent.getStringExtra("description");
		iv = (ImageView) findViewById(R.id.cameraDetail);
		
		tv = (TextView) findViewById(R.id.cameraDescription);
		tv.setText(description);
		
		bookMarkImgBtn = (ToggleButton) findViewById(R.id.bookmarkBookMarkImgBtn);
		bookMarkImgBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Log.i(TAG, "booked it" + bookMarkImgBtn.isChecked());
			}
		});
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.d(TAG, "onStop");
		Log.i(TAG, "arr size: " + bitMapList.size());
		run = false;
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(TAG, "onResume");
		
	}
	
	@Override
	public void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		Log.d(TAG, "onAttachedToWindow");
		
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d(TAG, "onStart");
		
		new Thread(new HelloRunnable()).start();
		
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			String text = (String) msg.obj;
			int index = Integer.parseInt(text) % 5;
			
			if (bitMapList.size() > index && bitMapList.get(index) != null)
			{
				iv.setImageBitmap(bitMapList.get(index));
			}
		}
	};
	
	public void updateUIThread(String msgStr)
	{
		Message msg = new Message();
		String textTochange = msgStr;
		msg.obj = textTochange;
		mHandler.sendMessage(msg);
	}
	
	private class HelloRunnable implements Runnable
	{
		
		public void run()
		{
			int i = 1;
			while (run)
			{
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				updateUIThread(i + "");
				i++;
			}
		}
		
	}
	
	public void emergencyBtnOnClick(View view)
	{
		Log.d(TAG, "emergencyBtnOnClick");
		Intent shareBtn = new Intent(CameraDetailsActivity.this,
				EmergencyCallActivity.class);
		startActivity(shareBtn);
	}
	
	public String readProfiles()
	{
		BufferedReader bufferedReader;
		String read = "undefined";
		
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					getFilesDir() + File.separator + "camera.csv")));
			String temp = "undefined";
			
			while ((temp = bufferedReader.readLine()) != null)
			{
				read = temp;
				Log.i(TAG, "read from read: " + read);
				
			}
			bufferedReader.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return "undefined";
			
		}
		return read;
		
	}
	
}