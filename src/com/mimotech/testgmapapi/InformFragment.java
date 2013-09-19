package com.mimotech.testgmapapi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class InformFragment extends SherlockFragment {
	private String tag = this.getClass().getSimpleName();
	private View viewMainFragment;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.viewMainFragment = inflater.inflate(R.layout.inform_fragment,
				container, false);

		Log.d(tag, "onCreateView");
		return viewMainFragment;
	}


}
