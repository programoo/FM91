package com.mimotech.testgmapapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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

public class CameraFragment extends SherlockFragment {
	View viewMainFragment;
	public ArrayList<Camera> camList = new ArrayList<Camera>();
	View numberContainer;
	String tag = this.getClass().getSimpleName();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.viewMainFragment = inflater.inflate(R.layout.camera_fragment,
				container, false);
		String[] asset = { "book ", "pencil", "glass", "phone" };
		GridView gv = (GridView) viewMainFragment
				.findViewById(R.id.cameraGridView);
		CameraCCTVAdapter ardap = new CameraCCTVAdapter(getActivity()
				.getApplicationContext(), asset);

		gv.setAdapter(ardap);
		final RelativeLayout cctvLayout = (RelativeLayout) viewMainFragment
				.findViewById(R.id.cctvLayout);
		final RelativeLayout positionLayout = (RelativeLayout) viewMainFragment
				.findViewById(R.id.positionLayout);

		// handle click event
		Button positionBtn = (Button) viewMainFragment
				.findViewById(R.id.positionBtn);
		positionBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag, "setOnClickListener ja");
				positionLayout.setVisibility(View.VISIBLE);
				cctvLayout.setVisibility(View.GONE);

			}
		});

		Button cctvBtn = (Button) viewMainFragment.findViewById(R.id.cctvBtn);
		cctvBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(tag, "cctvOnClickListener ja");

				positionLayout.setVisibility(View.GONE);
				cctvLayout.setVisibility(View.VISIBLE);

			}
		});

		Log.i(tag, "onCreateView");
		return viewMainFragment;
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
				 * getCCTV&key=(passkey from combination)&appid=(appId from registation)
				 */
				//http://athena.traffy.in.th/apis/apitraffy.php?format=…&api=…&available=…&key=…&appid=.. .
				Log.i(tag,"cctv passkey: "+passKey);
				String traffy_request_url = 
				"http://athena.traffy.in.th/apis/apitraffy.php?format="+"CSV&api=getCCTV&available=t&key="+passKey+"&appid="+AppID;
						
				new RequestTask("getData").execute(traffy_request_url);
			} else if (requestType.equalsIgnoreCase("getData")) {
				Log.i(tag,"result cctv: "+result);
				// this mean we get real data from traffy already
				this.traffyCameraCsvParser(result);
				// reloadViewAfterRequestTaskComplete();

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

		public void traffyCameraCsvParser(String csvString) {
			String camObj[] = csvString.split("[\n]");
			for(int i=0;i<camObj.length;i++){
				Log.i(tag,"index: "+i+","+camObj[i]);
				String cam[] = camObj[i].split("[,]");
				if(cam.length>=10){
					Camera cm = new Camera(cam[0],cam[1],cam[2],cam[3],cam[4],cam[5],cam[6],cam[7],cam[8],cam[9]);
				}
			}

		}// end xml parser

	}

}
