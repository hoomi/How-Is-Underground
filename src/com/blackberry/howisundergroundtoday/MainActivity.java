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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */

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
				showDetailsDialog(arg2);

			}
		});
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

	private void showDetailsDialog(int position) {
		animation = null;
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.detailsdialog);
		final LineObject lo = myAdapter.getItem(position);
		TextView message = (TextView) dialog
				.findViewById(R.id.message_textview);
		if (lo.getLineStatusDetails() != null
				&& !lo.getLineStatusDetails().equals(""))
			message.setText(lo.getLineStatusDetails());
		else
			message.setText(lo.getLineStatus());
		dialog.getWindow().getAttributes().windowAnimations = R.style.DetailsDialog;
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if (lo.getLineStatus().equals("Good Service"))
			((ImageView) dialog.findViewById(R.id.dialog_imageview))
					.setImageResource(R.drawable.smileyface);
		else if (lo.getLineStatus().equals("Severe Delays")
				|| lo.getLineStatus().equals("Part Closure")) {
			ImageView iv = (ImageView) dialog
					.findViewById(R.id.dialog_imageview);

			iv.setImageResource(R.drawable.sadfaceanim);
			animation = (AnimationDrawable) iv.getDrawable();

		} else
			((ImageView) dialog.findViewById(R.id.dialog_imageview))
					.setImageResource(R.drawable.normalface);
		dialog.findViewById(R.id.dialogrootview).setBackgroundColor(
				getResources().getColor(getColor(lo.getLineID())));
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (animation != null)
					animation.stop();

			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (animation != null)
					animation.stop();

			}
		});
		dialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialogi) {
				if (lo.getLineStatus().equals("Good Service")) {
					ImageView iv = (ImageView) dialog
							.findViewById(R.id.dialog_imageview);
					iv.setImageResource(R.drawable.happyface);
					animation = (AnimationDrawable) iv.getDrawable();
				} else if (lo.getLineStatus().equals("Severe Delays")
						|| lo.getLineStatus().equals("Part Closure")) {
					ImageView iv = (ImageView) dialog
							.findViewById(R.id.dialog_imageview);

					iv.setImageResource(R.drawable.sadfaceanim);
					animation = (AnimationDrawable) iv.getDrawable();

				} else
					((ImageView) dialog.findViewById(R.id.dialog_imageview))
							.setImageResource(R.drawable.normalface);
				if (animation != null)
					animation.start();

			}
		});

		dialog.show();

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
					lo.setLineID(Integer.parseInt(nameElement.getAttribute("ID")));
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

	private class LineAdapter extends ArrayAdapter<LineObject> {

		public LineAdapter(Context context, int textViewResourceId,
				List<LineObject> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView lineStatusImage;
			TextView lineName;
			AnimationDrawable anim;
			RelativeLayout lineBackground;
			View v = convertView;

			if (v == null)
				v = getLayoutInflater().inflate(R.layout.line_row, null, false);
			LineObject lo = getItem(position);
			lineStatusImage = (ImageView) v
					.findViewById(R.id.linestatusimageview);
			lineName = (TextView) v.findViewById(R.id.linenametextview);
			lineBackground = (RelativeLayout) v
					.findViewById(R.id.linebackground);
			if (lineStatusImage != null)
				if (lo.getLineStatus().equals("Good Service")) {
					lineStatusImage.setImageResource(R.drawable.smileyface);
					
				} else if (lo.getLineStatus().equals("Severe Delays")
						|| lo.getLineStatus().equals("Part Closure")) {
					lineStatusImage.setImageResource(R.drawable.sadface2);
					
				} else if (lo.getLineStatus().equals("Minor Delays")) {
					lineStatusImage.setImageResource(R.drawable.sadface);
				} else
					lineStatusImage.setImageResource(R.drawable.normalface);
			if (lineName != null) {
				lineName.setText(lo.getLineName());
				// lineName.setTextColor(getResources().getColor(getColor(lo.getLinID())));
			}
			if (lineBackground != null) {
				lineBackground.setBackgroundColor(getResources().getColor(
						getColor(lo.getLineID())));
			}
			return v;
		}
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