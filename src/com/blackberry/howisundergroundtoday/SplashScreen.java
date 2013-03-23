package com.blackberry.howisundergroundtoday;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends Activity {


	private ProgressBar splashProgressBar;
	private TextView splashProgressStatus;
	private UndergroundApplication application;
	private final String cachePath = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);
		application = (UndergroundApplication) getApplicationContext();
		splashProgressBar = (ProgressBar) findViewById(R.id.splash_progressbar);
		splashProgressStatus = (TextView) findViewById(R.id.splashstatus_textview);
		new SplashScreenPlayer().execute();
	}

	private class SplashScreenPlayer extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected void onCancelled() {
			splashProgressStatus.setText(R.string.cancelled);
			finish();
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result && !this.isCancelled()) {
				startActivity(new Intent(SplashScreen.this, MainActivity.class));
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
			finish();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			splashProgressBar.setMax(10);
			splashProgressBar.setProgress(0);
			splashProgressStatus.setText(R.string.internet_connection_check);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int addedProgress = values[0] - splashProgressBar.getProgress();
			splashProgressBar.incrementProgressBy(addedProgress);
			switch (values[0]) {
			case 1:
				splashProgressStatus.setText(R.string.contacting_tfl);
				break;
			case 2:
				splashProgressStatus.setText(R.string.preparing_linestatus);
				break;
			case 3:
				splashProgressStatus.setText(R.string.used_cache_string);
				break;
			case 10:
				splashProgressStatus.setText(R.string.error_occured);
				Toast.makeText(SplashScreen.this,
						"Please check your internet connection",
						Toast.LENGTH_LONG).show();
				break;
			case 11:
				splashProgressStatus.setText(R.string.sucessful_download);
				break;
			}
			super.onProgressUpdate(values);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean useCache = false;
			try {
				if (application.checkInternetConnection()) {
					useCache = application.isThereAnyCache() ? true : false;
					publishProgress(1);
				} else {
					if (!application.isThereAnyCache()) {
						publishProgress(10);
						return false;
					} else {
						publishProgress(3);
						useCache = true;
					}
				}
				Thread.sleep(1000);
				Document doc = XMLfromString(
						"http://cloud.tfl.gov.uk/TrackerNet/LineStatus",
						useCache);
				if (doc == null) {
					publishProgress(10);
					return false;
				} else {
					application.setDoc(doc);
				}
				Thread.sleep(1000);
				publishProgress(2);
				application.setLines(extractLinesFromDocument(doc));
				publishProgress(11);

				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

	}

	public Document XMLfromString(String url, boolean useCache) {
		Document doc;
		URI uri;
		String cacheURI;
		try {
			if (!useCache) {
				cacheURI = application.downloadAndCache(url);
			} else {
				cacheURI = "file://" + cachePath;
			}
			uri = new URI(cacheURI);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new InputSource(uri.toURL().openStream()));
			doc.getDocumentElement().normalize();
			return doc;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Creates an array of Line object from the xmlDoc
	 * @param xmlDoc
	 * @return an array of Line Objects
	 */
	public ArrayList<LineObject> extractLinesFromDocument(Document doc) {
		ArrayList<LineObject> lines = new ArrayList<LineObject>();
		NodeList lineStatus = doc.getElementsByTagName("Status");
		NodeList lineNames = doc.getElementsByTagName("Line");
		NodeList lineLineStatus = doc.getElementsByTagName("LineStatus");
		LineObject lo;
		for (int i = 0; i < lineStatus.getLength(); i++) {
			Element statusElement = (Element) lineStatus.item(i);
			Element nameElement = (Element) lineNames.item(i);
			Element lineStatusElement = (Element) lineLineStatus.item(i);
			lo = new LineObject(nameElement.getAttribute("Name"),
					statusElement.getAttribute("Description"),
					R.drawable.ic_launcher);
			lo.setLineID(Integer.parseInt(nameElement.getAttribute("ID")));
			lo.setLineStatusDetails(lineStatusElement
					.getAttribute("StatusDetails"));
			lines.add(lo);
		}

		return lines;
	}
}
