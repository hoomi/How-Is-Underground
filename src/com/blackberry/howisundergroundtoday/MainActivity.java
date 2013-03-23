package com.blackberry.howisundergroundtoday;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	private final static String TAG = "MainActivity";
	private ListView linesList;
	private ArrayList<LineObject> lines;
	private LineAdapter myAdapter;
	private Timer updateTimer;
	private final MyUpdateHandler handler = new MyUpdateHandler();
	private NodeList lineNames;
	private NodeList lineStatus;
	private NodeList lineLineStatus;
	private UndergroundApplication application;

	private final int NEW_UPDATE_ARRIVED = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		application = (UndergroundApplication) getApplicationContext();
		linesList = (ListView) findViewById(R.id.linesList);
		lines = application.getLines();
		myAdapter = new LineAdapter(this, R.layout.line_row, lines);
		linesList.setAdapter(myAdapter);
		updateTimer = new Timer();

		linesList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				flipTheView(arg1, (LineObject) arg0.getItemAtPosition(arg2));
			}
		});
	}

	private void flipTheView(final View theViewToBeAnimated,
			final LineObject line) {
		final View lineStatus = theViewToBeAnimated
				.findViewById(R.id.linestatustextview);
		final View lineName = theViewToBeAnimated
				.findViewById(R.id.linenametextview);
		final boolean flipped = line.isLineShowingStatus();
		float startingDegree = flipped ? 180 : 0;
		float endingDegree = flipped ? 0 : 180;
		float startingAlpha = flipped ? 1 : 0;
		float endingAlpha = flipped ? 0 : 1;
		AnimatorSet animSet = new AnimatorSet();
		animSet.playTogether(ObjectAnimator.ofFloat(lineName, View.ROTATION_X,
				startingDegree, endingDegree), ObjectAnimator.ofFloat(
				lineStatus, View.ROTATION_X, endingDegree, startingDegree),
				ObjectAnimator.ofFloat(lineName, View.ALPHA, endingAlpha,
						startingAlpha), ObjectAnimator.ofFloat(lineStatus,
						View.ALPHA, startingAlpha, endingAlpha));
		animSet.setDuration(1000);
		animSet.setInterpolator(new DecelerateInterpolator());
		animSet.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationCancel(Animator animation) {

				super.onAnimationCancel(animation);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (!flipped) {

				}
				line.setLineShowingStatus(!flipped);
			}

			@Override
			public void onAnimationStart(Animator animation) {
				if (flipped) {
					lineStatus.setVisibility(View.GONE);
					lineName.setVisibility(View.VISIBLE);
				} else {
					lineStatus.setVisibility(View.VISIBLE);
				}
				super.onAnimationStart(animation);
			}

		});
		animSet.start();
	}

	AnimationDrawable animation;

	@Override
	protected void onStart() {
		super.onStart();
		updateTimer.scheduleAtFixedRate(myTimerTask, 10 * 60 * 1000,
				10 * 60 * 1000);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (updateTimer != null)
			updateTimer.cancel();
	}

	private final TimerTask myTimerTask = new TimerTask() {

		@Override
		public void run() {
			// Document doc = XMLfromString(getXML());
			Document doc = XMLfromString();
			if (doc != null) {
				lineStatus = doc.getElementsByTagName("Status");
				lineNames = doc.getElementsByTagName("Line");
				lineLineStatus = doc.getElementsByTagName("LineStatus");
				LineObject lo;
				lines.clear();
				for (int i = 0; i < lineStatus.getLength(); i++) {
					Element statusElement = (Element) lineStatus.item(i);
					Element nameElement = (Element) lineNames.item(i);
					Element lineStatusElement = (Element) lineLineStatus
							.item(i);
					lo = new LineObject(nameElement.getAttribute("Name"),
							statusElement.getAttribute("Description"),
							R.drawable.ic_launcher);
					lo.setLineID(Integer.parseInt(nameElement
							.getAttribute("ID")));
					lo.setLineStatusDetails(lineStatusElement
							.getAttribute("StatusDetails"));
					lines.add(lo);
				}
				application.setDoc(doc);
				application.setLines(lines);
				handler.sendEmptyMessage(NEW_UPDATE_ARRIVED);

			}
		}
	};

	private class MyUpdateHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NEW_UPDATE_ARRIVED:
				myAdapter.notifyDataSetChanged();
			}
		}

	}

	static ViewHolder holder;

	private class LineAdapter extends ArrayAdapter<LineObject> {

		public LineAdapter(Context context, int textViewResourceId,
				List<LineObject> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.line_row,
						null, false);
				holder = new ViewHolder();
				holder.lineName = (TextView) convertView
						.findViewById(R.id.linenametextview);
				holder.lineStatus = (TextView) convertView
						.findViewById(R.id.linestatustextview);
				holder.logo = (ImageView) convertView
						.findViewById(R.id.linestatusimageview);
				holder.background = convertView
						.findViewById(R.id.linebackground);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LineObject lo = getItem(position);
			if (lo.getLineStatus().equals("Good Service")) {
				holder.logo.setImageResource(R.drawable.smileyface);
			} else if (lo.getLineStatus().equals("Severe Delays")
					|| lo.getLineStatus().equals("Part Closure")) {
				holder.logo.setImageResource(R.drawable.sadface2);
			} else if (lo.getLineStatus().equals("Minor Delays")) {
				holder.logo.setImageResource(R.drawable.sadface);
			} else {
				holder.logo.setImageResource(R.drawable.normalface);
			}
			holder.lineName.setText(lo.getLineName());
			holder.lineStatus.setText(lo.getLineStatusDetails().equals("") ? lo.getLineStatus() : lo.getLineStatusDetails());
			holder.background.setBackgroundColor(getResources().getColor(getColor(lo.getLineID())));
			if (lo.isLineShowingStatus()) {
				holder.lineStatus.setVisibility(View.VISIBLE);
				holder.lineName.setVisibility(View.GONE);
				holder.lineName.setAlpha(0);
				holder.lineStatus.setAlpha(1);
				holder.lineName.setRotationX(180);
				holder.lineStatus.setRotationX(0);
			} else {
				holder.lineName.setAlpha(1);
				holder.lineStatus.setAlpha(0);
				holder.lineStatus.setVisibility(View.GONE);
				holder.lineName.setVisibility(View.VISIBLE);
				holder.lineName.setRotationX(0);
				holder.lineStatus.setRotationX(180);
			}
			convertView.setTag(holder);
			return convertView;
		}

	}

	static class ViewHolder {
		TextView lineName, lineStatus;
		ImageView logo;
		View background;
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

	private int getColor(int lineID) {
		switch (lineID) {
		case 1:
			return R.color.bak_color;
		case 2:
			return R.color.cen_color;
		case 7:
			return R.color.cir_color;
		case 8:
			return R.color.ham_color;
		case 4:
			return R.color.jub_color;
		case 11:
			return R.color.met_color;
		case 5:
			return R.color.nor_color;
		case 6:
			return R.color.pic_color;
		case 3:
			return R.color.vic_color;
		case 12:
			return R.color.wat_color;
		case 82:
			return R.color.ove_color;
		case 81:
			return R.color.dlr_color;
		case 9:
			return R.color.dis_color;
		default:
			return 0;

		}
	}
}