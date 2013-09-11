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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class CameraDetailsActivity extends SherlockFragmentActivity {
	private String tag = getClass().getSimpleName();
	private ArrayList<Bitmap> bitMapList;
	private String cameraId = "0";
	ImageView iv;
	TextView tv;
	private boolean run = true;

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		Log.d(tag, "onAttachFragment");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camera_fragment_detail);

		Log.d(tag, "onCreate");
		bitMapList = new ArrayList<Bitmap>();

		Intent intent = getIntent();
		String[] imgList = intent.getStringExtra("imgList").split(",");
		for (int i = 0; i < imgList.length; i++) {
			new ImageLoader().downloadBitmapToList(imgList[i], bitMapList);
		}
		Log.i(tag, "loading complete");
		String description = intent.getStringExtra("description");
		cameraId = intent.getStringExtra("cameraId");
		iv = (ImageView) findViewById(R.id.cameraDetail);

		tv = (TextView) findViewById(R.id.cameraDescription);
		tv.setText(description);

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(tag, "onPause");

	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(tag, "onStop");
		Log.i(tag, "arr size: " + bitMapList.size());
		run = false;

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(tag, "onResume");

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Log.d(tag, "onAttachedToWindow");

	}

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		Log.d(tag, "onCreateView");

		return super.onCreateView(parent, name, context, attrs);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(tag, "onStart");

		new Thread(new HelloRunnable()).start();

		
		new RequestTask("getRandomStr")
		.execute("http://api.traffy.in.th/apis/getKey.php?appid=abcb6710");

	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String text = (String) msg.obj;
			if (bitMapList.size() >= 5) {
				int index = Integer.parseInt(text) % 5;
				iv.setImageBitmap(bitMapList.get(index));
			}
		}
	};

	public void updateUIThread(String msgStr) {
		Message msg = new Message();
		String textTochange = msgStr;
		msg.obj = textTochange;
		mHandler.sendMessage(msg);
	}

	private class HelloRunnable implements Runnable {

		public void run() {
			int i = 1;
			while (run) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				updateUIThread(i + "");
				System.out.println("Hello from a thread!");
				i++;
			}
		}

	}

	private void reloadViewAfterRequestTaskComplete(){
		
		
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

				
				
				Log.i(tag, "cctv passkey: " + passKey);
				String traffy_request_url = "http://athena.traffy.in.th/apis/apitraffy.php?format="
						+ "png&api=getcctvimg&id="+cameraId+"&key="
						+ passKey
						+ "&appid=" + AppID;					

				new RequestTask("getData").execute(traffy_request_url);
			} else if (requestType.equalsIgnoreCase("getData")) {
				Log.i(tag, "result cctv: " + result);
				// this mean we get real data from traffy already
				//this.traffyCameraXmlParser(result);
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
			/*
			 * System.out.println("Root element :" +
			 * doc.getDocumentElement().getNodeName());
			 * 
			 * 
			 * 
			 * System.out.println("----------------------------" +
			 * nList.getLength());
			 */
			if (nList != null)
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

					// System.out.println("\nCurrent Element :" +
					// nNode.getNodeName());

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
						Log.i(tag, "imgUrl " + imgUrl);

					}
				}

		}// end xml parser

	}// end private request class

}