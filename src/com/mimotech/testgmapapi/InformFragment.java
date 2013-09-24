package com.mimotech.testgmapapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class InformFragment extends Fragment implements OnClickListener,
		OnMarkerClickListener, OnInfoWindowClickListener, OnMapClickListener
{
	private String TAG = this.getClass().getSimpleName();
	private View v;
	private RelativeLayout mainLayout;
	private RelativeLayout detailLayout;
	private TextView tv;
	private AQuery aq;
	private Context context;
	private ArrayList<Nearby> nearbyList;
	private GoogleMap mMap;
	private Dialog dialog;
	private ImageButton addImgImgBtn ;
	private ImageButton addPlaceImgBtn ;
	private Button sendBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		aq = new AQuery(getActivity());
		nearbyList = new ArrayList<Nearby>();
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		this.v = inflater.inflate(R.layout.inform_fragment, container, false);
		
		mainLayout = (RelativeLayout) v.findViewById(R.id.informMainLayout);
		detailLayout = (RelativeLayout) v.findViewById(R.id.informDetailLayout);
		
		sendBtn = (Button) v.findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String url = "http://203.185.131.171/CrimeMap/Json/inform.php?tel=xxxx&title=xxxx&name=xxxx&description&Lat=xxxx&Lng=xxxx&image=xxxx";

				aq.ajax(url, String.class, new AjaxCallback<String>() {

				        @Override
				        public void callback(String url, String html, AjaxStatus status) {
				                Log.i(TAG,"traffy post data result: "+html);
				        }
				        
				});
				
			}
		});
		
		tv = (TextView) v.findViewById(R.id.typeOfInformTv);
		
		ImageButton imgBtn = (ImageButton) v.findViewById(R.id.trafficImgBtn);
		imgBtn.setOnClickListener(this);
		imgBtn.setTag("traffic");
		
		
		addImgImgBtn = (ImageButton) v
				.findViewById(R.id.addImgInformImgBtn);
		addImgImgBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				
				showChooser();
			}
			
		});
		
		
		
		
		
		addPlaceImgBtn = (ImageButton) v
				.findViewById(R.id.addPlaceInformImgBtn);
		addPlaceImgBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				
				Intent i = new Intent(getActivity(),
						InformMapSelectorActivity.class);
				startActivityForResult(i, Info.RESULT_SELECTED_POSITION);
			}
			
		});
		
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
	
	private void markAll()
	{
		
		Log.i(TAG, "set up map marker");
		
		// re-draw marker again
		for (int i = 0; i < this.nearbyList.size(); i++)
		{
			myMarker(this.nearbyList.get(i).lat, this.nearbyList.get(i).lng,
					this.nearbyList.get(i).title);
		}
		
		Log.i(TAG, "nearby num: " + nearbyList.size());
		
	}
	
	private void myMarker(String sLat, String sLng, String title)
	{
		Log.i(TAG, "re-mark num: " + nearbyList.size());
		
		LatLng accidentLatLng;
		// set accident lat long
		if (sLat.equalsIgnoreCase("undefined")
				|| sLng.equalsIgnoreCase("undefined"))
		{
			accidentLatLng = new LatLng(0, 0);
			
		} else
		{
			
			accidentLatLng = new LatLng(Double.parseDouble(sLat),
					Double.parseDouble(sLng));
			
		}
		
		if (mMap == null)
		{
			
			if (mMap == null)
			{
				mMap = ((SupportMapFragment) getActivity()
						.getSupportFragmentManager().findFragmentById(
								R.id.insertPositionMap)).getMap();
				mMap.setOnMarkerClickListener(this);
				mMap.setOnInfoWindowClickListener(this);
				mMap.setOnMapClickListener(this);
				
			}
			
		}
		
		if (mMap != null)
		{
			mMap.clear();
			// calculate distance between user and event
			double howFar = (int) (new Info().distance(accidentLatLng.latitude,
					accidentLatLng.longitude, Info.lat, Info.lng, "K") * 100) / 100.0;
			// news marker
			String titileDetail = getString(R.string.farfromyou_msg) + ": "
					+ howFar + " km";
			
			Marker marker = mMap.addMarker(new MarkerOptions()
					.position(accidentLatLng).title(title)
					.snippet(titileDetail));
			
			mMap.getUiSettings().setZoomControlsEnabled(true);
			// when load complete mark our position
			Marker myMarker = mMap.addMarker(new MarkerOptions()
					.position(new LatLng(Info.lat, Info.lng))
					.title(getString(R.string.you_here_msg))
					.snippet(Info.reverseGpsName)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			
			myMarker.showInfoWindow();
			
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					Info.lat, Info.lng), 15));
			
		}
		
	}
	
	class RetreiveFeedTask extends AsyncTask<String, String, String>
	{
		protected String doInBackground(String... urls)
		{
			HttpResponse response = null;
			String inputStreamAsString = "undefined";
			try
			{
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(
						"https://maps.googleapis.com/maps/api/place/nearbysearch/xml?location="
								+ Info.lat
								+ ","
								+ Info.lng
								+ "&rankby=prominence&radius=500&sensor=false&key=AIzaSyCGwL4iF8lgumHDZvWmwArYtZknFZeGuYY"));
				response = client.execute(request);
				inputStreamAsString = convertStreamToString(response
						.getEntity().getContent());
				
			} catch (URISyntaxException e)
			{
				e.printStackTrace();
			} catch (ClientProtocolException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return inputStreamAsString;
		}
		
		protected void onPostExecute(String inputStreamAsString)
		{
			// TODO: check this.exception
			// TODO: do something with the feed
			Log.i(TAG, "inputStreamAsString: " + inputStreamAsString);
			try
			{
				XmlDom xmlJa = new XmlDom(inputStreamAsString);
				nearByParsingToObj(xmlJa);
				// after get nearby obj then draw to gMap
				markAll();
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	public String convertStreamToString(InputStream inputStream)
			throws IOException
	{
		if (inputStream != null)
		{
			Writer writer = new StringWriter();
			
			char[] buffer = new char[1024];
			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
				}
			} finally
			{
				inputStream.close();
			}
			return writer.toString();
		} else
		{
			return "";
		}
	}
	
	public void nearByParsingToObj(XmlDom xml)
	{
		
		List<XmlDom> entries;
		// tempList = new ArrayList<News>();
		// List<String> titles = new ArrayList<String>();
		
		try
		{
			entries = xml.tags("result");
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			return;
		}
		
		for (XmlDom entry : entries)
		{
			
			String name = entry.text("name");
			String vicinity = entry.text("vicinity");
			String type = entry.text("type");
			String geometry = entry.child("geometry").text();
			String lat = entry.child("geometry").child("location").child("lat")
					.text();
			String lng = entry.child("geometry").child("location").child("lng")
					.text();
			
			uniqueAddBearby(new Nearby(name + " " + vicinity + " " + type, lat,
					lng));
			
			Log.i(TAG, "nearby Obj: " + name + "," + vicinity + "," + type
					+ "," + geometry + "," + lat + "," + lng);
		}
	}
	
	public void uniqueAddBearby(Nearby nb)
	{
		for (int i = 0; i < this.nearbyList.size(); i++)
		{
			if (this.nearbyList.get(i).title.equalsIgnoreCase(nb.title))
			{
				return;
			}
		}
		this.nearbyList.add(nb);
	}
	
	public String readProfiles()
	{
		BufferedReader bufferedReader;
		String read = "undefined";
		
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					getActivity().getFilesDir() + File.separator
							+ "profile.csv")));
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
	
	@Override
	public void onClick(View v)
	{
		
		if (!this.readProfiles().equalsIgnoreCase("undefined"))
		{
			// have user profile
			this.mainLayout.setVisibility(View.GONE);
			this.detailLayout.setVisibility(View.VISIBLE);
			tv.setText(v.getTag().toString());
			
			Log.i(TAG, v.getTag().toString());
		} else
		{
			// redirect to user profile page
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					getActivity());
			builder1.setMessage(getString(R.string.insert_your_data));
			builder1.setCancelable(true);
			builder1.setPositiveButton("Yes",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							dialog.cancel();
							
							Intent it = new Intent(getActivity(),
									InsertProfileActivity.class);
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
	
	private void showChooser()
	{
		// Use the GET_CONTENT intent from the utility class
		Intent target = FileUtils.createGetContentIntent();
		// Create the chooser Intent
		Intent intent = Intent.createChooser(target,
				getString(R.string.chooser_label));
		try
		{
			startActivityForResult(intent, Info.RESULT_SELECTED_IMAGE);
		} catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Info.RESULT_SELECTED_POSITION)
		{
			
			if (resultCode == Info.RESULT_OK)
			{
				String result = data.getStringExtra("result");
				addPlaceImgBtn.setImageResource(R.drawable.map_selected);

				Log.i(TAG,"result from selector"+result);
			}
			if (resultCode == Info.RESULT_CANCELED)
			{
				// Write your code if there's no result
				Log.e(TAG,"result onActivityResult error");
			}
		}
		else if(requestCode == Info.RESULT_SELECTED_IMAGE){
			//RESULT_OK == -1
			if (resultCode == -1)
			{
				if (data != null)
				{
					// Get the URI of the selected file
					final Uri uri = data.getData();
					
					try
					{
						// Create a file instance from the URI
						File file = FileUtils.getFile(uri);
						Log.i(TAG, "path: " + file.getAbsolutePath());
						String pathImgSelected = file.getAbsolutePath();
						Bitmap bitmapSelected = Info.decodeFile(file,128);
						addImgImgBtn.setImageBitmap(bitmapSelected);
					} catch (Exception e)
					{
						Log.e("FileSelectorTestActivity",
								"File select error", e);
					}
				}
			}
			
		}
	}// onActivityResult
	
	@Override
	public boolean onMarkerClick(Marker marker)
	{
		return false;
	}
	
	@Override
	public void onInfoWindowClick(Marker marker)
	{
		Log.i(TAG, "i select this" + marker.getTitle());
		dialog.dismiss();
	}
	
	@Override
	public void onMapClick(LatLng point)
	{
		Log.i(TAG, "Select: " + point.toString());
		mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
		dialog.dismiss();
	}
	
}
