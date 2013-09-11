package com.mimotech.testgmapapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class BlankActivity extends SherlockFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View viewMainFragment = inflater.inflate(R.layout.blank_activity,
				container, false);
		
		return viewMainFragment;
	}
	
}
