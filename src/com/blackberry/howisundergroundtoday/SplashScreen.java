package com.blackberry.howisundergroundtoday;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blackberry.howisundergroundtoday.objects.UndergroundStatusObject;

public class SplashScreen extends Activity {


	private ProgressBar splashProgressBar;
	private TextView splashProgressStatus;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);
		splashProgressBar = (ProgressBar) findViewById(R.id.splash_progressbar);
		splashProgressStatus = (TextView) findViewById(R.id.splashstatus_textview);
		new SplashScreenPlayer().execute();
	}

	private class SplashScreenPlayer extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			splashProgressBar.setMax(10);
			splashProgressBar.setProgress(0);
			splashProgressStatus.setText(R.string.internet_connection_check);
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			Intent mIntent = new Intent(SplashScreen.this, XMLDownloaderService.class);
			mIntent.setAction(XMLDownloaderService.DOWNLOAD_CACHE_ACTION);
			startService(mIntent);
			UndergroundStatusObject mUSO = UndergroundStatusObject.getInstance();
			int i = 10;
			while (mUSO.isLinesArrayEmpty() && i >= 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return false;
				}
				i++;
				publishProgress(1);
			}
			if (mUSO.isLinesArrayEmpty()) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		protected void onCancelled() {
			splashProgressStatus.setText(R.string.cancelled);
			stopService(new Intent(SplashScreen.this, XMLDownloaderService.class));
			finish();
			super.onCancelled();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			splashProgressBar.incrementProgressBy(values[0]);
			super.onProgressUpdate(values);
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



	}
}
