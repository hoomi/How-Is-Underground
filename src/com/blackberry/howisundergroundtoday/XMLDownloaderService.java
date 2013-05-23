package com.blackberry.howisundergroundtoday;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.blackberry.howisundergroundtoday.objects.UndergroundStatusObject;
import com.blackberry.howisundergroundtoday.tools.InternetHelper;
import com.blackberry.howisundergroundtoday.tools.Logger;
import com.blackberry.howisundergroundtoday.tools.XMLHelper;

public class XMLDownloaderService extends IntentService {

	public static final String DOWNLOAD_CACHE_ACTION = "com.howisunderground.downcache";
	private static final String LAST_TIME_DOWNLOAD_KEY = "last_time_download";
	private static final String url = "http://cloud.tfl.gov.uk/TrackerNet/LineStatus";
	private XMLHelper mXMLHelper = null;
	private long lastTimeCached = 0;
	
	
	public XMLDownloaderService() {
		super("How_Is_Underground_Baby");
	}

	@Override
	public void onCreate() {
		if (this.mXMLHelper == null) {
			this.mXMLHelper = new XMLHelper(this,url, true);
			this.mXMLHelper.setParserObject(UndergroundStatusObject.getInstance());
		}
		super.onCreate();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		if(intent.getAction().equals(DOWNLOAD_CACHE_ACTION)) {
			Logger.i(XMLDownloaderService.class, "onHandleIntent");
			SharedPreferences mSharedPrefrences = getSharedPreferences("How_Is_Underground", MODE_PRIVATE);
			lastTimeCached = mSharedPrefrences.getLong(LAST_TIME_DOWNLOAD_KEY, 0);
			// Use cache if the last successfull download was less than 10 mins ago
			boolean useCache = (lastTimeCached > System.currentTimeMillis() - 10 * 60 * 1000) ? true : false;
			if (InternetHelper.isConnectedToInternet(this) && !InternetHelper.isItRoaming(this)) {
				useCache = true;
			}
			if (this.mXMLHelper != null) {
				try {
					if (useCache && this.mXMLHelper.isThereACache()) {
						this.mXMLHelper.getCachedXMLObject();
						useCache = false;
					} else {
						this.mXMLHelper.startDownloading();
						if (!UndergroundStatusObject.getInstance().isLinesArrayEmpty()) {
							SharedPreferences.Editor mSPEditor = mSharedPrefrences.edit();
							mSPEditor.putLong(LAST_TIME_DOWNLOAD_KEY, System.currentTimeMillis());
							mSPEditor.commit();
						}
					}
				} catch (URISyntaxException e) {
					Logger.printStackTrace(e);
				} catch (IOException e) {
					Logger.printStackTrace(e);
				} catch (ParserConfigurationException e) {
					Logger.printStackTrace(e);
				} catch (Exception e) {
					Logger.printStackTrace(e);
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		this.mXMLHelper = null;
		super.onDestroy();
	}

}
