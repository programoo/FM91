package com.mimotech.testgmapapi;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class MainActivity extends SherlockFragmentActivity {
	String tag = getClass().getSimpleName();
	private ViewPager mViewPager;
	private TabHost mTabHost;
	private int badgeCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		Log.d(tag, "onCreate");

		mViewPager = (ViewPager) findViewById(R.id.pager);

		// mViewPager.setId(1);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		TabsAdapter mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		mTabHost.clearAllTabs();

		Bundle tabArgs = null;

		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_news");
		tabArgs.putInt("id", 1);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_home"), getResources()
				.getDrawable(R.drawable.ic_launcher), NewsFragment.class,
				tabArgs, getString(R.string.news_tabbar));

		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_camera");
		tabArgs.putInt("id", 2);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_home"), getResources()
				.getDrawable(R.drawable.ic_launcher), CameraFragment.class,
				tabArgs, getString(R.string.camera_tabbar));

		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_3");
		tabArgs.putInt("id", 2);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_home"), getResources()
				.getDrawable(R.drawable.ic_launcher), NewsFragment.class,
				tabArgs, getString(R.string.hello_world));

	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		TextView v = (TextView) mTabHost.getTabWidget().getChildAt(0)
				.findViewById(R.id.badge_count);
		v.setText("" + badgeCount++);
		return super.onKeyDown(keyCode, event);

	}

	public void newsBtnOnClick(View view) {
		Log.d(tag, "newsBtnOnClick");
	}

	public void eventBtnOnClick(View view) {
		Log.d(tag, "eventBtnOnClick");
	}

	public void positionBtnOnClick(View view) {
		Log.d(tag, "positionBtnOnClick");
		/*
		 * Intent mapActivity = new Intent(MainActivity.this,
		 * BasicMapActivity.class); startActivity(mapActivity);
		 */

	}

	public void emergencyBtnOnClick(View view) {
		Log.d(tag, "emergencyBtnOnClick");

	}

	public void cctvBtnOnClick(View view) {
		Log.d(tag, "positionBtnOnClick");
	}

}
