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

import android.os.AsyncTask;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CameraFragment extends SherlockFragment {
	private View v;
	private ArrayList<Camera> camList = new ArrayList<Camera>();
	private String tag = this.getClass().getSimpleName();
	private GoogleMap mMap;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.v = inflater.inflate(R.layout.camera_fragment, container, false);

		final GridView gv = (GridView) v.findViewById(R.id.cameraGridView);
		CameraCCTVAdapter ardap = new CameraCCTVAdapter(getActivity()
				.getApplicationContext(), camList);

		gv.setAdapter(ardap);
		final RelativeLayout cctvLayout = (RelativeLayout) v
				.findViewById(R.id.cctvLayout);
		final RelativeLayout positionLayout = (RelativeLayout) v
				.findViewById(R.id.positionLayout);

		// handle click event
		Button positionBtn = (Button) v.findViewById(R.id.positionBtn);
		positionBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag, "positionBtnsetOnClickListener ja");
				positionLayout.setVisibility(View.VISIBLE);
				cctvLayout.setVisibility(View.GONE);
				markAll();

			}
		});

		Button cctvBtn = (Button) v.findViewById(R.id.cctvBtn);
		cctvBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag, "cctvOnClickListener ja");

				positionLayout.setVisibility(View.GONE);
				cctvLayout.setVisibility(View.VISIBLE);
				CameraCCTVAdapter ardap = new CameraCCTVAdapter(getActivity()
						.getApplicationContext(), camList);

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
	}

	public void onResume() {
		super.onResume();

	}

	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void markAll() {
		Log.i(tag, "set up map marker");

		for (int i = 0; i < camList.size(); i++) {
			myMarker(camList.get(i).lat, camList.get(i).lng,
					camList.get(i).thaiName);
		}
		Log.i(tag, "camera num: " + camList.size());
	}

	public void myMarker(String sLat, String sLng, String title) {

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
		}

		if (mMap != null) {

			Marker marker = mMap.addMarker(new MarkerOptions()
					.position(accidentLatLng).title(title)
					.snippet(accidentLatLng.toString()));
			mMap.getUiSettings().setZoomControlsEnabled(true);
			marker.showInfoWindow();

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(accidentLatLng,
					10));

		}
	}

	public void reloadViewAfterRequestTaskComplete() {

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
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d(this.getClass().getSimpleName(), "cctv onPostExecute");
			Log.i(tag, "result: " + result);

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
				Log.i(tag, "result cctv: " + result);
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
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(xmlString));
				doc = builder.parse(is);

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			NodeList nList = doc.getElementsByTagName("cctv");
			/*
			 * System.out.println("Root element :" +
			 * doc.getDocumentElement().getNodeName());
			 * 
			 * 
			 * 
			 * System.out.println("----------------------------" +
			 * nList.getLength());
			 */

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					String id = eElement.getAttribute("no");
					String nameEng = eElement.getAttribute("name");
					String nameTH = eElement.getAttribute("name_th");

					String lat = eElement.getElementsByTagName("point").item(0)
							.getAttributes().getNamedItem("lat").getNodeValue();
					String lng = eElement.getElementsByTagName("point").item(0)
							.getAttributes().getNamedItem("lng").getNodeValue();
					String available = eElement.getAttribute("available");
					String imgUrl = eElement.getElementsByTagName("url")
							.item(0).getTextContent();
					
					
					String lastupdate = eElement.getAttribute("lastupdate");
					String src = eElement.getAttribute("src");
					String desc = eElement.getAttribute("desc");
					String imgList = eElement.getAttribute("list");

					Camera cam = new Camera(id, nameEng, nameTH, lat, lng,
							available,imgUrl, lastupdate, src, desc, imgList);
					this.uniqueAdd(cam);
					Log.i(tag,"imgUrl "+imgUrl);
					/*
					 * String roadName =
					 * getStringValueFromExistElement(eElement, "road", "name");
					 * String startPointName = getStringValueFromExistElement(
					 * eElement, "startpoint", "name"); String startPointLat =
					 * getStringValueFromExistElement( eElement, "startpoint",
					 * "latitude"); String startPointLong =
					 * getStringValueFromExistElement( eElement, "startpoint",
					 * "longitude");
					 * 
					 * String endPointName = getStringValueFromExistElement(
					 * eElement, "endpoint", "name"); String endPointLat =
					 * getStringValueFromExistElement( eElement, "endpoint",
					 * "latitude"); String endPointLong =
					 * getStringValueFromExistElement( eElement, "endpoint",
					 * "longitude"); News n = new News(id, type, primarySource,
					 * secondarySource, startTime, endTime, mediaType,
					 * mediaPath, title, description, locationType, roadName,
					 * startPointName, startPointLat, startPointLong,
					 * endPointName, endPointLat, endPointLong);
					 */

				}
			}

		}// end xml parser

		public String getStringValueFromExistElement(Element eElement,
				String elementName, String attributeName) {
			try {
				String valueString = eElement.getElementsByTagName(elementName)
						.item(0).getAttributes().getNamedItem(attributeName)
						.getNodeValue();
				return valueString;
			} catch (NullPointerException e) {
				Log.d(tag, "element not found: " + elementName + ","
						+ attributeName);
				return "undefined";
			}

		}

		public void uniqueAdd(Camera cam) {

			for (int i = 0; i < camList.size(); i++) {
				if (cam.id.equalsIgnoreCase(camList.get(i).id)) {
					return;
				}
			}
			camList.add(cam);
		}

	}// end private request class

}
