package com.blackberry.howisundergroundtoday;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import android.net.ConnectivityManager;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);
		application=(UndergroundApplication) getApplicationContext();
		splashProgressBar=(ProgressBar) findViewById(R.id.splash_progressbar);
		splashProgressStatus=(TextView) findViewById(R.id.splashstatus_textview);
		new SplashScreenPlayer().execute();
	}

	
	private class SplashScreenPlayer extends AsyncTask<Void, Integer, Void>{

		@Override
		protected void onCancelled() {
			splashProgressStatus.setText(R.string.cancelled);
			finish();
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			if(checkInternetConnection()){
			startActivity(new Intent(SplashScreen.this,MainActivity.class));
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
			int addedProgress=values[0]-splashProgressBar.getProgress();
			splashProgressBar.incrementProgressBy(addedProgress);
			switch(values[0]){
			case 1:
				splashProgressStatus.setText(R.string.contacting_tfl);
				break;
			case 2:
				splashProgressStatus.setText(R.string.preparing_linestatus);
				break;
			case 10:
				splashProgressStatus.setText(R.string.error_occured);
				Toast.makeText(SplashScreen.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
				break;
			case 11:
				splashProgressStatus.setText(R.string.sucessful_download);
				break;
			}
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
			if(checkInternetConnection())
				publishProgress(1);
			else{
				publishProgress(10);
				return null;
			}
			Thread.sleep(1000);
			Document doc= XMLfromString();
			if(doc==null){
				publishProgress(10);
				return null;
			}else{
				application.setDoc(doc);
			}
			Thread.sleep(1000);
			publishProgress(2);
			application.setLines(extractLinesFromDocument(doc));
			publishProgress(11);
			
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	
	private boolean checkInternetConnection(){
		ConnectivityManager cm=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if(cm.getActiveNetworkInfo()==null || !cm.getActiveNetworkInfo().isConnected())
			return false;
		return true;
	}
	
	public Document XMLfromString() {
		Document doc;
		URL url;
		try {
			url = new URL("http://cloud.tfl.gov.uk/TrackerNet/LineStatus");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();
			return doc;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// URL url = new URL(myUrl);
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	
	private ArrayList<LineObject> extractLinesFromDocument(Document doc){
		ArrayList<LineObject> lines=new ArrayList<LineObject>();
		NodeList lineStatus = doc.getElementsByTagName("Status");
		NodeList lineNames = doc.getElementsByTagName("Line");
		NodeList lineLineStatus=doc.getElementsByTagName("LineStatus");
		LineObject lo;
		for(int i=0;i<lineStatus.getLength();i++){
			Element statusElement=(Element)lineStatus.item(i);
			Element nameElement=(Element)lineNames.item(i);
			Element lineStatusElement=(Element)lineLineStatus.item(i);
			lo=new LineObject(nameElement.getAttribute("Name"),statusElement.getAttribute("Description") , R.drawable.ic_launcher);
			lo.setLineID(Integer.parseInt(nameElement.getAttribute("ID")));
			lo.setLineStatusDetails(lineStatusElement.getAttribute("StatusDetails"));
			lines.add(lo);
		}
		
		return lines;
	}
	
}
