package com.mimotech.testgmapapi;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;


public class CameraDetailsActivity extends SherlockFragmentActivity{
	private String tag = getClass().getSimpleName();
	private ArrayList<Bitmap> arr;
	ImageView iv;
	TextView tv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camera_fragment_detail);

		Log.d(tag, "onCreate1");
		arr = new ArrayList<Bitmap>();
		
		Intent intent = getIntent();
		String []imgList = intent.getStringExtra("imgList").split(",");
		for(int i=0;i<imgList.length;i++){
			new ImageLoader().downloadBitmapToList(imgList[i],arr);
		}
		Log.i(tag,"loading complete");
		String description = intent.getStringExtra("description");

		iv = (ImageView) findViewById(R.id.cameraDetail);
		
		tv = (TextView) findViewById(R.id.cameraDescription);
		tv.setText(description);
		

		
		
		

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
        new Thread(new HelloRunnable()).start();


	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String text = (String) msg.obj;
			tv.setText(text);
			// call setText here
			/*
			ShowListViewAdapter ad = new ShowListViewAdapter(mainContext, Info.hostMsg);
			lv.setAdapter(ad);
			scrollMyListViewToBottom();
			*/
		}
	};
	
	public void updateUIThread(String msgStr){
		Message msg = new Message();
		String textTochange = msgStr;
		msg.obj = textTochange;
		mHandler.sendMessage(msg);
	}
	
	private class HelloRunnable implements Runnable {

	    public void run() {
	    	int i=0;
	    	while(true){
	    		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		updateUIThread(i+"");
	    	    System.out.println("Hello from a thread!");
	    	    i++;
	    	}
	    }


	}



}