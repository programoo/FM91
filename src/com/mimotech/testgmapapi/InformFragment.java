package com.mimotech.testgmapapi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InformFragment extends Fragment {
	private String tag = this.getClass().getSimpleName();
	private View viewMainFragment;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.viewMainFragment = inflater.inflate(R.layout.inform_fragment,
				container, false);

		Log.d(tag, "onCreateView");
		return viewMainFragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}


}
