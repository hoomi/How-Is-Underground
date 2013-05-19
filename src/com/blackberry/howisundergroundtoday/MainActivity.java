package com.blackberry.howisundergroundtoday;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
import android.widget.Toast;

import com.blackberry.howisundergroundtoday.objects.LineObject;
import com.blackberry.howisundergroundtoday.objects.UndergroundStatusObject;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    static ViewHolder holder;
    private final int NEW_UPDATE_ARRIVED = 1;
    private final int NOTIFY_USER = 2;
    private final TimerTask myTimerTask = new TimerTask() {

        @Override
        public void run() {
            Document doc;
            if (application.checkInternetConnection()) {
                if (!application.isThereAnyCache()) {
                    doc = application.XMLfromString("http://cloud.tfl.gov.uk/TrackerNet/LineStatus", true);
                } else {
                    return;
                }
            } else {
                Message msg = new Message();
                msg.what = NOTIFY_USER;
                msg.obj = getString(R.string.internet_connection_unavailable_string);
                handler.sendMessage(msg);
                return;
            }
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
                            R.drawable.ic_launcher);
                    lo.setLineId(Integer.parseInt(nameElement
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
    AnimationDrawable animation;
    private ListView linesList;
    private ArrayList<LineObject> lines;
    private LineAdapter myAdapter;
    private final Timer updateTimer = null;
    private MyUpdateHandler handler;
    private NodeList lineNames;
    private NodeList lineStatus;
    private NodeList lineLineStatus;
    private UndergroundApplication application;
    private UndergroundStatusObject undergroundStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        application = (UndergroundApplication) getApplicationContext();
//        handler = new MyUpdateHandler(this);
        undergroundStatus = UndergroundStatusObject.getInstance();
        linesList = (ListView) findViewById(R.id.linesList);
        lines = undergroundStatus.getLinesArray();
        myAdapter = new LineAdapter(this, R.layout.line_row, lines);
        linesList.setAdapter(myAdapter);
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

    @Override
    protected void onStart() {
        super.onStart();
//        if (updateTimer == null) {
//            updateTimer = new Timer();
//        }
//        updateTimer.scheduleAtFixedRate(myTimerTask, 10 * 60 * 1000,
//                10 * 60 * 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (updateTimer != null)
//            updateTimer.cancel();
//        updateTimer = null;
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

    static class ViewHolder {
        TextView lineName, lineStatus;
        ImageView logo;
        View background;
    }

    private class MyUpdateHandler extends Handler {
        private final Context context;

        public MyUpdateHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEW_UPDATE_ARRIVED:
                    myAdapter.notifyDataSetChanged();
                    break;
                case NOTIFY_USER:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

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
            //TODO Add actual data
            holder.logo.setImageResource(R.drawable.normalface);
            holder.lineStatus.setText(lo.getLineStatus().getStatusDescription());
            holder.background.setBackgroundColor(getResources().getColor(R.color.bak_color));
            ////////////////////////////
            holder.lineName.setText(lo.getLineName());
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
}