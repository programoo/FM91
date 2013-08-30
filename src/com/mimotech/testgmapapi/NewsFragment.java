package com.mimotech.testgmapapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class NewsFragment extends SherlockFragment implements
		OnItemClickListener {
	View viewMainFragment;
	View numberContainer;
	ListView lv;
	private Context context;
	String tag = this.getClass().getSimpleName();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.context = getActivity().getApplicationContext();
		this.viewMainFragment = inflater.inflate(R.layout.news_fragment,
				container, false);

		lv = (ListView) viewMainFragment.findViewById(R.id.list1Fragment);

		NewsListViewAdapter ardap = new NewsListViewAdapter(getActivity());

		lv.setAdapter(ardap);

		new RequestTask("getRandomStr")
				.execute("http://api.traffy.in.th/apis/getKey.php?appid=abcb6710");

		Log.d(tag, "onCreateView");

		// handle item click event
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.d(tag, "item click: " + position + "," + id);
				// mark as read
				Info.getNews(id + "").isRead = true;

				Intent mapActivity = new Intent(getActivity(),
						NewsDetailsActivity.class);
				mapActivity.putExtra("newsId", id + "");

				startActivity(mapActivity);

			}

		});

		return viewMainFragment;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(tag, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	public void onStart() {
		super.onStart();
		// after view start complete re-create new view
		Log.d(tag, "onStart");
		// update already read list
		lv = (ListView) viewMainFragment.findViewById(R.id.list1Fragment);
		NewsListViewAdapter ardap = new NewsListViewAdapter(getActivity());
		lv.setAdapter(ardap);

		// update badge count unRead
		TextView tvBadgeCount = (TextView) getActivity().findViewById(
				R.id.badge_count);
		tvBadgeCount.setText(Info.unReadNumber() + "");

	}

	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(tag, "onActivityCreated");

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {

		/*
		 * String asset[] = { "book", "ruler", "rubber", "meter" }; ListView lv
		 * = (ListView) viewMainFragment .findViewById(R.id.list1Fragment);
		 * NewsListViewAdapterDetails ardap = new NewsListViewAdapterDetails(
		 * context, R.layout.news_fragment_detail, asset);
		 * 
		 * lv.setAdapter(ardap);
		 */

	}

	public void reloadViewAfterRequestTaskComplete() {
		Log.d(tag, "reloadViewAfterRequestTaskComplete");

		NewsListViewAdapter ardap = new NewsListViewAdapter(
				getActivity());
		lv.setAdapter(ardap);
		
		TextView tvBadgeCount = (TextView) getActivity().findViewById(
				R.id.badge_count);
		tvBadgeCount.setText(Info.unReadNumber() + "");

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
			Log.d(this.getClass().getSimpleName(), "onPostExecute");
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
				 * 
				 * http://api.traffy.in.th/apis/apitraffy.php?api=getIncident&key
				 * =(คีย์ที่ได้รับจากการลงทะเบียน)&appid=(id
				 * ที่ได้รับจากการลงทะเบียน
				 * )&format=XML&limit=10&offset=5&type=all&from=2011-11-05
				 * 17:41:13&to=2011-11-05 17:45:20
				 */
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				String dateStart = dateFormat.format(date) + "%2000:00:01";
				String dateEnd = dateFormat.format(date) + "%2023:59:59";

				System.out.println(dateStart);

				System.out.println(dateEnd);

				String traffy_request_url = "http://api.traffy.in.th/apis/apitraffy.php?api=getIncident&key="
						+ passKey
						+ "&format=XML&limit=10&offset=5&type=all&from="
						+ dateStart + "&to=" + dateEnd;
				new RequestTask("getData").execute(traffy_request_url);
			} else if (requestType.equalsIgnoreCase("getData")) {
				// this mean we get real data from traffy already
				this.traffyNewsXmlParser(result);
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

		public void traffyNewsXmlParser(String xmlString) {
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

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			// doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("news");

			System.out.println("----------------------------"
					+ nList.getLength());

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					String id = eElement.getAttribute("id");
					String type = eElement.getAttribute("type");
					String primarySource = eElement
							.getAttribute("primarysource");
					String secondarySource = eElement
							.getAttribute("secondarysource");
					String startTime = eElement.getAttribute("starttime");
					String endTime = eElement.getAttribute("endtime");

					String mediaType = eElement.getElementsByTagName("media")
							.item(0).getAttributes().getNamedItem("type")
							.getNodeValue();
					String mediaPath = eElement.getElementsByTagName("media")
							.item(0).getAttributes().getNamedItem("path")
							.getNodeValue();

					String title = eElement.getElementsByTagName("title")
							.item(0).getTextContent();
					String description = eElement
							.getElementsByTagName("description").item(0)
							.getTextContent();

					String locationType = eElement
							.getElementsByTagName("location").item(0)
							.getAttributes().getNamedItem("type")
							.getNodeValue();

					String roadName = getStringValueFromExistElement(eElement,
							"road", "name");
					String startPointName = getStringValueFromExistElement(
							eElement, "startpoint", "name");
					String startPointLat = getStringValueFromExistElement(
							eElement, "startpoint", "latitude");
					String startPointLong = getStringValueFromExistElement(
							eElement, "startpoint", "longitude");

					String endPointName = getStringValueFromExistElement(
							eElement, "endpoint", "name");
					String endPointLat = getStringValueFromExistElement(
							eElement, "endpoint", "latitude");
					String endPointLong = getStringValueFromExistElement(
							eElement, "endpoint", "longitude");
					News n = new News(id, type, primarySource, secondarySource,
							startTime, endTime, mediaType, mediaPath, title,
							description, locationType, roadName,
							startPointName, startPointLat, startPointLong,
							endPointName, endPointLat, endPointLong);

					Info.uniqueAdd(n);

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

	}

}
