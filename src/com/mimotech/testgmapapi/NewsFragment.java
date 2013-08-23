package com.mimotech.testgmapapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * @author SAND-AIS To create Dial Fragment Extends {@link SherlockFragment}
 */

public class NewsFragment extends SherlockFragment {
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
		this.viewMainFragment = inflater.inflate(R.layout.news_fragment, container, false);

		String[] asset = {
				"pillow...",
				"pencil", "glass", "phone" };
		ListView lv = (ListView) viewMainFragment.findViewById(R.id.list1Fragment);
		Log.d(tag, "list: " + lv);
		NewsListViewAdapter ardap = new NewsListViewAdapter(context,
				R.layout.news_fragment_listview, asset);

		lv.setAdapter(ardap);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(tag, "item click: " + position + "," + id);
				String asset[]={"book","ruler","rubber","meter"};
				ListView lv = (ListView) viewMainFragment.findViewById(R.id.list1Fragment);
				NewsListViewAdapterDetails ardap = new NewsListViewAdapterDetails(context,
						R.layout.news_fragment_detail, asset);

				lv.setAdapter(ardap);

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

	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
