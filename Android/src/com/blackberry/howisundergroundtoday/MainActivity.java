package com.blackberry.howisundergroundtoday;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.blackberry.howisundergroundtoday.adapter.LineAdapter;
import com.blackberry.howisundergroundtoday.adapter.LineFragmentAdapter;
import com.blackberry.howisundergroundtoday.fragments.LinesListFragment;
import com.blackberry.howisundergroundtoday.objects.LineObject;
import com.blackberry.howisundergroundtoday.objects.UndergroundStatusObject;

public class MainActivity extends FragmentActivity {
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String message = (String) msg.obj;
                if (message
                        .equalsIgnoreCase(XMLDownloaderService.MESSAGE_PARSED)) {
//                    myAdapter.notifyDataSetChanged();
                }
            }
            super.handleMessage(msg);
        }

    };
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder iBinder) {
            XMLDownloaderService.XMLServiceBinder mBinder = (XMLDownloaderService.XMLServiceBinder) iBinder;
            mXMLServiceDownload = mBinder.getXMLService();
            mXMLServiceDownload.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
    /**
     * Called when the activity is first created.
     */
    AnimationDrawable animation;
    private XMLDownloaderService mXMLServiceDownload = null;
    private Intent mIntent = null;
    private LineFragmentAdapter mFragmentAdapter = null;
    private FragmentManager mFragmentManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.mFragmentManager = getSupportFragmentManager();
        this.mIntent = new Intent(this, XMLDownloaderService.class);
        bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
        this.mFragmentAdapter = new LineFragmentAdapter(this.mFragmentManager,  UndergroundStatusObject.getInstance().getLinesArray());
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
    protected void onDestroy() {
        unbindService(this.mServiceConnection);
        mXMLServiceDownload.setHandler(null);
        stopService(mIntent);
        super.onDestroy();

    }
}