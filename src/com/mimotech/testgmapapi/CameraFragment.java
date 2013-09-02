package com.mimotech.testgmapapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * @author SAND-AIS To create Dial Fragment Extends {@link SherlockFragment}
 */

public class CameraFragment extends SherlockFragment {
	Activity mainActivity;
	View viewMainFragment;

	View numberContainer;

	private Context context;
	String tag = this.getClass().getSimpleName();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mainActivity = this.getActivity();

		this.context = this.getActivity().getApplicationContext();
		this.viewMainFragment = inflater.inflate(R.layout.camera_fragment, container, false);

		String[] asset = {
				"book ",
				"pencil", "glass", "phone" };
		GridView gv = (GridView) viewMainFragment.findViewById(R.id.cameraGridView);
		CameraCCTVAdapter ardap = new CameraCCTVAdapter(context,asset);

		gv.setAdapter(ardap);
		final RelativeLayout cctvLayout = (RelativeLayout) viewMainFragment.findViewById(R.id.cctvLayout);
		final RelativeLayout positionLayout = (RelativeLayout) viewMainFragment.findViewById(R.id.positionLayout);
		
		//handle click event
		Button positionBtn = (Button) viewMainFragment.findViewById(R.id.positionBtn);
		positionBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag,"setOnClickListener ja");
				positionLayout.setVisibility(View.VISIBLE);
				cctvLayout.setVisibility(View.GONE);
				
			}
		});
		
		Button cctvBtn = (Button) viewMainFragment.findViewById(R.id.cctvBtn);
		cctvBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag,"cctvOnClickListener ja");

				positionLayout.setVisibility(View.GONE);
				cctvLayout.setVisibility(View.VISIBLE);
				
			}
		});
		
		Log.i(tag, "onCreateView");
		return viewMainFragment;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.i(tag, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	public void onStart() {
		super.onStart();
	}

	public void onResume() {
		super.onResume();

	}

	public void positionBtnOnClick(View view) {
		Log.d(tag, "positionBtnOnClick");
		/*
		 * Intent mapActivity = new Intent(MainActivity.this,
		 * BasicMapActivity.class); startActivity(mapActivity);
		 */

	}
	
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
