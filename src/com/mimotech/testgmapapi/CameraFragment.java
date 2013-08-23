package com.mimotech.testgmapapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * @author SAND-AIS To create Dial Fragment Extends {@link SherlockFragment}
 */

public class CameraFragment extends SherlockFragment {
	Activity mainActivity;
	View viewMainFragment;
	final static protected String LOG_TAG = "CallBack";

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

	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
