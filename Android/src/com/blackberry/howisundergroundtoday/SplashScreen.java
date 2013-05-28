package com.blackberry.howisundergroundtoday;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blackberry.howisundergroundtoday.tools.Logger;

public class SplashScreen extends Activity {

    private ProgressBar splashProgressBar;
    private TextView splashProgressStatus;
    private XMLDownloaderService mXMLService = null;
    private Intent mIntent = null;
    private final AsyncTask<Void, Void, Void> splashPlayer = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
        	mXMLService.setHandler(null);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.printStackTrace(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!this.isCancelled()) {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
            super.onPostExecute(aVoid);
        }
    };
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            XMLDownloaderService.XMLServiceBinder mBinder = (XMLDownloaderService.XMLServiceBinder) iBinder;
            mXMLService = mBinder.getXMLService();
            mXMLService.setHandler(mHandler);
            mIntent.setAction(XMLDownloaderService.DOWNLOAD_CACHE_ACTION);
            startService(mIntent);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.arg1) {
                case 0:
                    String message = (String) msg.obj;
                    updateUIMessage(message);
                    if (message.equals(XMLDownloaderService.MESSAGE_PARSED)) {
                        splashPlayer.execute();
                    }
                    break;
                case 1:
                    updateUIMessage((String) msg.obj);
                    stopService(mIntent);
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashscreen);
        splashProgressBar = (ProgressBar) findViewById(R.id.splash_progressbar);
        splashProgressStatus = (TextView) findViewById(R.id.splashstatus_textview);
        splashProgressBar.setMax(10);
        splashProgressBar.setProgress(0);
        splashProgressStatus.setText(R.string.internet_connection_check_string);
        this.mIntent = new Intent(SplashScreen.this, XMLDownloaderService.class);
        bindService(this.mIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void updateUIMessage(String message) {
        if (message == null || message == "") {
            return;
        }
        if (message == XMLDownloaderService.MESSAGE_PARSED) {
            splashProgressStatus.setText(R.string.sucessful_download_string);
            splashProgressBar.setProgress(10);
        } else if (message.equals(XMLDownloaderService.MESSAGE_ONLINE)) {
            splashProgressStatus.setText(R.string.contacting_tfl_string);
            splashProgressBar.setProgress(5);
        } else if (message.equals(XMLDownloaderService.MESSAGE_USING_CACHE)) {
            splashProgressStatus.setText(R.string.preparing_linestatus_string);
            splashProgressBar.setProgress(5);
        } else if (message.equals(XMLDownloaderService.ERROR_MESSAGE_NO_CONNECTION)) {
            Toast.makeText(SplashScreen.this, R.string.internet_connection_unavailable_error_string, Toast.LENGTH_LONG).show();
        } else if (message.equals(XMLDownloaderService.ERROR_MESSAGE_ROAMING)) {
            //TODO Have a different behaviour for when roaming is disabled
            Toast.makeText(SplashScreen.this, R.string.internet_connection_roaming_disabled_error_string, Toast.LENGTH_LONG).show();
        } else if (message.equals(XMLDownloaderService.ERROR_MESSAGE_GENERAL_ERROR)) {
            Toast.makeText(SplashScreen.this, R.string.error_occured_string, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        stopService(this.mIntent);
        super.onBackPressed();
    }
}
