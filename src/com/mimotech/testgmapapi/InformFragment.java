package com.mimotech.testgmapapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InformFragment extends Fragment implements OnClickListener
{
	private String TAG = this.getClass().getSimpleName();
	private View v;
	private RelativeLayout mainLayout;
	private RelativeLayout detailLayout;
	private TextView tv;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		this.v = inflater.inflate(R.layout.inform_fragment, container, false);
		
		mainLayout = (RelativeLayout) v.findViewById(R.id.informMainLayout);
		detailLayout = (RelativeLayout) v.findViewById(R.id.informDetailLayout);
		
		tv = (TextView) v.findViewById(R.id.typeOfInformTv);
		
		ImageButton imgBtn = (ImageButton) v.findViewById(R.id.trafficImgBtn);
		imgBtn.setOnClickListener(this);
		imgBtn.setTag("traffic");
		
		
		this.mainLayout.setVisibility(View.VISIBLE);
		this.detailLayout.setVisibility(View.GONE);

		
		Log.d(TAG, "onCreateView");
		
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}
	

	
	public boolean readProfiles()
	{
		BufferedReader bufferedReader;
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					getActivity().getFilesDir() + File.separator
							+ "profile.csv")));
			String read = "";
			
			while ((read = bufferedReader.readLine()) != null)
			{
				Log.i(TAG, read);
				
			}
			bufferedReader.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
			
		}
		return true;
		
	}
	
	@Override
	public void onClick(View v)
	{
		if (this.readProfiles())
		{
			//have user profile 
			this.mainLayout.setVisibility(View.GONE);
			this.detailLayout.setVisibility(View.VISIBLE);
			tv.setText(v.getTag().toString());
			
			Log.i(TAG,v.getTag().toString() );
		} else
		{
			// redirect to user profile page
			AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
			builder1.setMessage(getString(R.string.insert_your_data));
			builder1.setCancelable(true);
			builder1.setPositiveButton("Yes",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							dialog.cancel();
							
							Intent it = new Intent(getActivity(),InsertProfileActivity.class);
							startActivity(it);
						}
					});
			builder1.setNegativeButton("No",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							dialog.cancel();
						}
					});
			
			AlertDialog alert11 = builder1.create();
			alert11.show();
		}
		
	}
	
}
