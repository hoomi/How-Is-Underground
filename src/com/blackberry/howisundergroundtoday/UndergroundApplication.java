package com.blackberry.howisundergroundtoday;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

public class UndergroundApplication extends Application {

	private final String LASTIMEDOWNLOADKEY = "last_time_download";
	private String cachePath = "";
	private SharedPreferences sp;
	private long lastTimeDownload;

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		cachePath = getCacheDir() + "/xmlCache.xml";
		sp = getSharedPreferences("How_Is_Underground", Context.MODE_PRIVATE);
		lastTimeDownload = sp.getLong(LASTIMEDOWNLOADKEY, 0);
	}

	private Document doc;

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	private ArrayList<LineObject> lines;

	public ArrayList<LineObject> getLines() {
		return lines;
	}

	public void setLines(ArrayList<LineObject> lines) {
		this.lines = lines;
	}
	
	/**
	 * Downloads the xml from the server and caches it in a file
	 * @param remoteUrl server Url
	 * @return returns the path to the cached file
	 * @throws IOException
	 */
	public String downloadAndCache(String stringUrl) throws IOException {
		InputStream is;
		File cacheFile;
		OutputStream os;
		SharedPreferences.Editor editor = sp.edit();
		final byte[] buffer = new byte[1024];
		URL url;
		url = new URL(stringUrl);
		cacheFile = new File(cachePath);
		if (cacheFile.exists()) {
			cacheFile.delete();
		}
		cacheFile = new File(cachePath);
		os = new FileOutputStream(cacheFile);
		is = new InputSource(url.openStream()).getByteStream();
		int read;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
		os.flush();
		is.close();
		os.close();
		editor.putLong(LASTIMEDOWNLOADKEY, System.currentTimeMillis());
		editor.commit();
		return "file://" + cachePath;
	}

	/**
	 * Checks whether there is a valid cache file. It ha to be downloaded less than 5 minutes ago
	 * @return true if cache file exists and false otherwise
	 */
	public boolean isThereAnyCache() {
		SharedPreferences.Editor editor = sp.edit();
		File file = new File(cachePath);
		Log.i("TAG", "difference: " + (System.currentTimeMillis() - lastTimeDownload) + " file exists: " + file.exists());
		if (file.exists()
				&& (System.currentTimeMillis() - lastTimeDownload) < 5 * 60 * 1000) {
			return true;
		}
		file.delete();
		editor.remove(LASTIMEDOWNLOADKEY);
		editor.commit();
		return false;
	}
	
	/**
	 * Checks whether the device is connected to the internet or not
	 * @return true if the device is connected to the internet and false otherwise
	 */
	public boolean checkInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() == null
				|| !cm.getActiveNetworkInfo().isConnected())
			return false;
		return true;
	}


	public Document XMLfromString(String url, boolean useCache) {
		Document doc;
		URI uri;
		String cacheURI;
		try {
			if (!useCache) {
				cacheURI = downloadAndCache(url);
			} else {
				cacheURI = "file://" + cachePath;
				Log.i("Application", "used cache");
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
}
