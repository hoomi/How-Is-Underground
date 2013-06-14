package com.blackberry.howisundergroundtoday;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import com.blackberry.howisundergroundtoday.adapter.LineFragmentAdapter;
import com.blackberry.howisundergroundtoday.fragments.LinesDetailsFragment;
import com.blackberry.howisundergroundtoday.fragments.LinesListFragment;
import com.blackberry.howisundergroundtoday.objects.LineObject;
import com.blackberry.howisundergroundtoday.objects.UndergroundStatusObject;

public class MainActivity extends FragmentActivity {
    private final static String VIEW_PAGER_ITEM_KEY = "view_pager_item";
    private final static String BEEN_PORTRAIT_KEY = "has_been_portrait";
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String message = (String) msg.obj;
                if (message.equalsIgnoreCase(XMLDownloaderService.MESSAGE_PARSED)) {
                    if (mFragmentAdapter != null) {
                        mFragmentAdapter.setLineObjects(UndergroundStatusObject.getInstance().getLinesArray());
                    } else if (mLinesListFragment != null) {
                        mLinesListFragment.notifyDataSetChanged();
                    }
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
    private XMLDownloaderService mXMLServiceDownload = null;
    private Intent mIntent = null;
    private LineFragmentAdapter mFragmentAdapter = null;
    private FragmentManager mFragmentManager = null;
    private ViewPager mViewPager = null;
    private int selectedLineIndex = 0;
    private FrameLayout mFrameLayout = null;
    private LinesListFragment mLinesListFragment = null;
    private boolean mPortrait = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.mFragmentManager = getSupportFragmentManager();
        this.mIntent = new Intent(this, XMLDownloaderService.class);
        bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
        if (savedInstanceState != null) {
            this.selectedLineIndex = savedInstanceState.getInt(VIEW_PAGER_ITEM_KEY, 0);
            this.mPortrait = savedInstanceState.getBoolean(BEEN_PORTRAIT_KEY, false);
        }
        if (findViewById(R.id.line_details_pager) != null) {
            this.mFragmentAdapter = new LineFragmentAdapter(this.mFragmentManager, UndergroundStatusObject.getInstance().getLinesArray());
            this.mViewPager = (ViewPager) findViewById(R.id.line_details_pager);
            this.mViewPager.setAdapter(mFragmentAdapter);
            this.mViewPager.setCurrentItem(this.selectedLineIndex);
        } else if (findViewById(R.id.fragment_container_FrameLayout) != null) {
            if (!this.mPortrait) {
                this.mPortrait = true;
                this.mLinesListFragment = new LinesListFragment();
                this.mFragmentManager.beginTransaction().add(R.id.fragment_container_FrameLayout, this.mLinesListFragment, "listFragment").commit();
            }
        }
    }

    /**
     * Changes the fragment in the viewAdapter to the correct fragment
     */
    public void changeFragmentDetails(int index) {
        // If the index is greater negative or greater than the length of the strings
        if (index < 0 && index >= this.mFragmentAdapter.getCount()) {
            return;
        }
        if (this.mViewPager != null) {
            this.mViewPager.setCurrentItem(index);
        } else {
            LineObject lo = UndergroundStatusObject.getInstance().getLinesArray().get(index);
            FragmentTransaction ft = this.mFragmentManager.beginTransaction();

//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out, R.anim.card_flip_left_in, R.anim.card_flip_left_out);
            ft.add(R.id.fragment_container_FrameLayout, LinesDetailsFragment.newInstance(lo), lo.getLineName());
            ft.addToBackStack(lo.getLineName());
            ft.commit();
        }
        this.selectedLineIndex = index;
    }

    @Override
    protected void onDestroy() {
        unbindService(this.mServiceConnection);
        mXMLServiceDownload.setHandler(null);
        stopService(mIntent);
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (this.mViewPager != null) {
            this.selectedLineIndex = this.mViewPager.getCurrentItem();
        }
        outState.putBoolean(BEEN_PORTRAIT_KEY, this.mPortrait);
        outState.putInt(VIEW_PAGER_ITEM_KEY, this.selectedLineIndex);
        super.onSaveInstanceState(outState);
    }
}