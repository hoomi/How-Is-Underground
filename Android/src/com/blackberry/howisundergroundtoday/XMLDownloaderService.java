package com.blackberry.howisundergroundtoday;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.blackberry.howisundergroundtoday.objects.UndergroundStatusObject;
import com.blackberry.howisundergroundtoday.tools.InternetHelper;
import com.blackberry.howisundergroundtoday.tools.Logger;
import com.blackberry.howisundergroundtoday.tools.XMLHelper;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

public class XMLDownloaderService extends IntentService {

    public class XMLServiceBinder extends Binder {
        public XMLDownloaderService getXMLService() {
            return XMLDownloaderService.this;
        }
    }

    public static final String DOWNLOAD_CACHE_ACTION = "com.howisunderground.downcache";
    public static final String ERROR_MESSAGE_ROAMING = "Roaming";
    public static final String ERROR_MESSAGE_NO_CONNECTION = "No_Connection";
    public static final String ERROR_MESSAGE_GENERAL_ERROR = "General_Error";
    public static final String MESSAGE_PARSED = "Parsed";
    public static final String MESSAGE_USING_CACHE = "Using_Cache";
    public static final String MESSAGE_ONLINE = "Online";

    private static final String LAST_TIME_DOWNLOAD_KEY = "last_time_download";
    private static final String url = "http://cloud.tfl.gov.uk/TrackerNet/LineStatus";
    private long lastTimeCached = 0;
    private XMLServiceBinder mBinder = null;
    private Handler mHandler = null;
    private AlarmManager mAlarmManager = null;
    private PendingIntent mPendingIntent = null;

    private XMLHelper mXMLHelper = null;

    public XMLDownloaderService() {
        super("How_Is_Underground_Baby");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        if (this.mXMLHelper == null) {
            this.mXMLHelper = new XMLHelper(this, url, true);
            this.mXMLHelper.setParserObject(UndergroundStatusObject.getInstance());
        }
        if (mBinder == null) {
            mBinder = new XMLServiceBinder();
        }
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, XMLDownloaderService.class);
        intent.setAction(DOWNLOAD_CACHE_ACTION);
        mPendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 10 * 60 * 1000, AlarmManager.INTERVAL_FIFTEEN_MINUTES, mPendingIntent);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        this.mXMLHelper = null;
        mAlarmManager.cancel(this.mPendingIntent);
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(DOWNLOAD_CACHE_ACTION)) {
            Logger.i(XMLDownloaderService.class, "onHandleIntent Intent: " + intent.getAction());
            SharedPreferences mSharedPrefrences = getSharedPreferences("How_Is_Underground", MODE_PRIVATE);
            lastTimeCached = mSharedPrefrences.getLong(LAST_TIME_DOWNLOAD_KEY, 0);

            if (this.mXMLHelper != null) {
                // Use cache if the last successful download was less than 10 minutes ago
                boolean useCache = (lastTimeCached > System.currentTimeMillis() - 10 * 60 * 1000) && this.mXMLHelper.isThereACache() ? true : false;
                boolean connected = false, roaming = false;
                try {
                    if (useCache) {
                        sendMessageToHandler(0, MESSAGE_USING_CACHE);
                        this.mXMLHelper.getCachedXMLObject();
                        if (!UndergroundStatusObject.getInstance().isLinesArrayEmpty()) {
                            sendMessageToHandler(0, MESSAGE_PARSED);
                        } else {
                            sendMessageToHandler(1, ERROR_MESSAGE_GENERAL_ERROR);
                        }
                    } else if (!useCache) {
                        connected = InternetHelper.isConnectedToInternet(this);
                        //TODO Add an option in the settings if they want to enable roaming
                        roaming = InternetHelper.isItRoaming(this);
                        if (connected && !roaming) {
                            sendMessageToHandler(0, MESSAGE_ONLINE);
                            this.mXMLHelper.startDownloading();
                            if (!UndergroundStatusObject.getInstance().isLinesArrayEmpty()) {
                                SharedPreferences.Editor mSPEditor = mSharedPrefrences.edit();
                                mSPEditor.putLong(LAST_TIME_DOWNLOAD_KEY, System.currentTimeMillis());
                                mSPEditor.commit();
                                sendMessageToHandler(0, MESSAGE_PARSED);
                            } else {
                                sendMessageToHandler(1, ERROR_MESSAGE_GENERAL_ERROR);
                            }
                        } else if (connected && roaming) {
                            sendMessageToHandler(1, ERROR_MESSAGE_ROAMING);
                        } else {
                            sendMessageToHandler(1, ERROR_MESSAGE_NO_CONNECTION);
                        }
                    }
                } catch (URISyntaxException e) {
                    Logger.printStackTrace(e);
                    sendMessageToHandler(1, ERROR_MESSAGE_GENERAL_ERROR);
                } catch (IOException e) {
                    Logger.printStackTrace(e);
                    sendMessageToHandler(1, ERROR_MESSAGE_GENERAL_ERROR);
                } catch (ParserConfigurationException e) {
                    Logger.printStackTrace(e);
                    sendMessageToHandler(1, ERROR_MESSAGE_GENERAL_ERROR);
                } catch (Exception e) {
                    Logger.printStackTrace(e);
                    sendMessageToHandler(1, ERROR_MESSAGE_GENERAL_ERROR);
                }
            }
        }
    }

    /**
     * IT is used to receive message from the background thread
     *
     * @param mHandler Handler which receives the messages
     */
    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    /**
     * Messages are send to the handler if one is set
     *
     * @param errorOrNormal 0 indicates normal message and 1 indicates if it is an error message
     * @param message       It is a string message to be snet to the handler
     */
    private void sendMessageToHandler(int errorOrNormal, String message) {
        if (mHandler == null) {
            return;
        }
        Message msg = mHandler.obtainMessage();
        msg.obj = message;
        msg.arg1 = errorOrNormal;
        mHandler.sendMessage(msg);
    }

}
