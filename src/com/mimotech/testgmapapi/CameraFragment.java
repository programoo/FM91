package com.mimotech.testgmapapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CameraFragment extends SherlockFragment implements
		OnMarkerClickListener, OnInfoWindowClickListener {
	private View v;
	private ArrayList<Camera> camList = new ArrayList<Camera>();
	private String tag = this.getClass().getSimpleName();
	private GoogleMap mMap;
	private boolean isMark = false;
	private LocationManager locationManager;
	private LocationListener locationListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		locationListener = new MyLocationListener();
		// get current location by gps
		Log.d(tag, "Request location");
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, locationListener);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.v = inflater.inflate(R.layout.camera_fragment, container, false);

		final GridView gv = (GridView) v.findViewById(R.id.cameraGridView);
		CameraGridViewAdapter ardap = new CameraGridViewAdapter(getActivity()
				.getApplicationContext(), camList);

		gv.setAdapter(ardap);
		final RelativeLayout cctvLayout = (RelativeLayout) v
				.findViewById(R.id.cctvLayout);
		final RelativeLayout positionLayout = (RelativeLayout) v
				.findViewById(R.id.positionLayout);

		// handle gridview click
		gv.setOnItemClickListener(

		new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				Log.d(tag, "arg2: " + arg2 + "," + "arg3: " + arg3);
				Camera cam = camList.get(arg2);

				Intent cameraDetail = new Intent(getActivity(),
						CameraDetailsActivity.class);

				cameraDetail.putExtra("description", cam.thaiName + ","
						+ cam.englishName);
				cameraDetail.putExtra("imgList", cam.imgList);
				startActivity(cameraDetail);

			}
		});

		// handle click event
		Button positionBtn = (Button) v.findViewById(R.id.positionBtn);
		positionBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				markAll();
				Log.d(tag, "positionBtnsetOnClickListener ja");
				positionLayout.setVisibility(View.VISIBLE);
				cctvLayout.setVisibility(View.GONE);

			}
		});

		Button cctvBtn = (Button) v.findViewById(R.id.cctvBtn);
		cctvBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(tag, "cctvOnClickListener ja");

				positionLayout.setVisibility(View.GONE);
				cctvLayout.setVisibility(View.VISIBLE);
				CameraGridViewAdapter ardap = new CameraGridViewAdapter(
						getActivity().getApplicationContext(), camList);

				gv.setAdapter(ardap);

			}
		});

		Log.i(tag, "onCreateView");
		return v;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.i(tag, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);

		new RequestTask("getRandomStr")
				.execute("http://api.traffy.in.th/apis/getKey.php?appid=abcb6710");

	}

	public void onStart() {
		super.onStart();
		Log.d(tag, "Request location");
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, locationListener);
	}

	public void onResume() {
		super.onResume();

	}

	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void markAll() {
		// draw marker only first time
		if (!this.isMark) {
			Log.i(tag, "set up map marker");

			for (int i = 0; i < camList.size(); i++) {
				myMarker(camList.get(i).lat, camList.get(i).lng,
						camList.get(i).thaiName, camList.get(i).id);
			}
			Log.i(tag, "camera num: " + camList.size());
			this.isMark = true;

		}

	}

	public double distance(double lat1, double lon1, double lat2, double lon2,
			String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit.equalsIgnoreCase("K")) {
			dist = dist * 1.609344;
		} else if (unit.equalsIgnoreCase("N")) {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {
			/*
			 * Toast.makeText( getActivity().getBaseContext(),
			 * "Location changed: Lat: " + loc.getLatitude() + " Lng: " +
			 * loc.getLongitude(), Toast.LENGTH_SHORT).show();
			 */
			String longitude = "Longitude: " + loc.getLongitude();
			String latitude = "Latitude: " + loc.getLatitude();

			Log.i(tag, "your current location:" + latitude + "," + longitude);

			Info.lat = loc.getLatitude();
			Info.lng = loc.getLongitude();

		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	private void myMarker(String sLat, String sLng, String title, String id) {

		LatLng accidentLatLng;
		// set accident lat long
		if (sLat.equalsIgnoreCase("undefined")
				|| sLng.equalsIgnoreCase("undefined")) {
			accidentLatLng = new LatLng(0, 0);

		} else {

			accidentLatLng = new LatLng(Double.parseDouble(sLat),
					Double.parseDouble(sLng));

		}

		if (mMap == null) {
			mMap = ((SupportMapFragment) getActivity()
					.getSupportFragmentManager().findFragmentById(
							R.id.cameraMap)).getMap();
			mMap.setOnMarkerClickListener(this);
			mMap.setOnInfoWindowClickListener(this);

		}

		if (mMap != null) {

			// calculate distance between user and event
			double howFar = (int) (new Info().distance(accidentLatLng.latitude,
					accidentLatLng.longitude, Info.lat, Info.lng, "K") * 100) / 100.0;
			// news marker
			String titileDetail = getString(R.string.farfromyou_msg) + ": "
					+ howFar + " km";

			Marker marker = mMap.addMarker(new MarkerOptions()
					.position(accidentLatLng).title(id + ":" + title)
					.snippet(titileDetail)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.camera_gmap_icon)));
					
			mMap.getUiSettings().setZoomControlsEnabled(true);
			marker.showInfoWindow();

			//
			
			
			// when load complete mark our position
			Marker myMarker = mMap.addMarker(new MarkerOptions()
					.position(new LatLng(Info.lat, Info.lng))
					.title("You here")
					.snippet("You here")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			
			
			myMarker.showInfoWindow();
			
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					Info.lat, Info.lng), 10));

		}

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		Log.i(tag, marker.getTitle() + marker.getSnippet());
		return false;
	}

	public Camera getCamById(String id) {
		for (int i = 0; i < camList.size(); i++) {
			if (camList.get(i).id.equalsIgnoreCase(id)) {
				return camList.get(i);
			}
		}
		return null;
	}

	private void reloadViewAfterRequestTaskComplete() {
		this.markAll();
	}

	private class RequestTask extends AsyncTask<String, String, String> {
		private String tag = getClass().getSimpleName();
		public String AppID = "abcb6710";
		public String hiddenkey = "TxLYP6j1Ee";
		public String randomStr = "undefined";
		public String requestType = "";

		public RequestTask(String requestType) {
			this.requestType = requestType;
		}

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d(this.getClass().getSimpleName(), "cctv onPostExecute");
			// Log.i(tag, "result: " + result);

			// Do anything with response..
			if (requestType.equalsIgnoreCase("getRandomStr")) {
				randomStr = result;
				String passKey = "";
				try {
					passKey = convertToMD5(AppID + randomStr)
							+ convertToMD5(hiddenkey + randomStr);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}

				/*
				 * http://athena.traffy.in.th/apis/apitraffy.php?format=XML&api=
				 * getCCTV&key=(passkey from combination)&appid=(appId from
				 * registation)
				 */
				// http://athena.traffy.in.th/apis/apitraffy.php?format=…&api=…&available=…&key=…&appid=..
				// .
				Log.i(tag, "cctv passkey: " + passKey);
				String traffy_request_url = "http://athena.traffy.in.th/apis/apitraffy.php?format="
						+ "XML&api=getCCTV&available=t&key="
						+ passKey
						+ "&appid=" + AppID;

				new RequestTask("getData").execute(traffy_request_url);
			} else if (requestType.equalsIgnoreCase("getData")) {
				// Log.i(tag, "result cctv: " + result);
				// this mean we get real data from traffy already
				this.traffyCameraXmlParser(result);
				reloadViewAfterRequestTaskComplete();
			}

		}

		public String convertToMD5(String msg) throws NoSuchAlgorithmException {
			String plaintext = msg;
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}

			return hashtext;
		}

		public void traffyCameraXmlParser(String xmlString) {
			if (xmlString == null) {
				Log.e(tag, "traffyNewsXmlParser: nullString");
				return;
			}

			Document doc = null;
			NodeList nList = null;
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(xmlString));
				doc = builder.parse(is);
				nList = doc.getElementsByTagName("cctv");

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			if (nList != null)
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;
						String id = eElement.getAttribute("no");
						String nameEng = eElement.getAttribute("name");
						String nameTH = eElement.getAttribute("name_th");

						String lat = eElement.getElementsByTagName("point")
								.item(0).getAttributes().getNamedItem("lat")
								.getNodeValue();
						String lng = eElement.getElementsByTagName("point")
								.item(0).getAttributes().getNamedItem("lng")
								.getNodeValue();
						String available = eElement.getAttribute("available");
						String imgUrl = eElement.getElementsByTagName("url")
								.item(0).getTextContent();

						String lastupdate = eElement
								.getElementsByTagName("lastupdate").item(0)
								.getTextContent();
						String src = eElement.getElementsByTagName("src")
								.item(0).getTextContent();
						String description = eElement
								.getElementsByTagName("desc").item(0)
								.getTextContent();
						String imgList = eElement.getElementsByTagName("list")
								.item(0).getTextContent();

						Camera cam = new Camera(id, nameEng, nameTH, lat, lng,
								available, imgUrl, lastupdate, src,
								description, imgList);
						this.uniqueAdd(cam);

					}
				}

		}// end xml parser

		public void uniqueAdd(Camera cam) {

			for (int i = 0; i < camList.size(); i++) {
				if (cam.id.equalsIgnoreCase(camList.get(i).id)) {
					return;
				}
			}
			camList.add(cam);
		}

	}// end private request class

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		Log.i(tag, marker.getTitle() + marker.getSnippet());
		Camera cam = getCamById(marker.getTitle().split("[:]")[0]);
		Intent cameraDetail = new Intent(getActivity(),
				CameraDetailsActivity.class);
		//in case of user point
		try{
			cameraDetail.putExtra("description", cam.thaiName + ","
					+ cam.englishName);
			cameraDetail.putExtra("cameraId", cam.id);
			cameraDetail.putExtra("imgList", cam.imgList);
			startActivity(cameraDetail);
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		

	}

}
