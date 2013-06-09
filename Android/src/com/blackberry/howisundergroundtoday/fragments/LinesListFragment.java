package com.blackberry.howisundergroundtoday.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import com.blackberry.howisundergroundtoday.MainActivity;
import com.blackberry.howisundergroundtoday.R;
import com.blackberry.howisundergroundtoday.adapter.LineAdapter;
import com.blackberry.howisundergroundtoday.objects.LineObject;
import com.blackberry.howisundergroundtoday.objects.UndergroundStatusObject;

public class LinesListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private LineAdapter mLineAdapter = null;
    private ListView mLinesListView = null;

    public LinesListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.linelistfragment, null, false);
        this.mLinesListView = (ListView) v.findViewById(R.id.lineListView);
        this.mLinesListView.setAdapter(this.mLineAdapter);
        this.mLinesListView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mLineAdapter = new LineAdapter(getActivity(), R.layout.linelistfragment, UndergroundStatusObject.getInstance().getLinesArray());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MainActivity mActivity = (MainActivity) getActivity();
        mActivity.changeFragmentDetails(i);
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




}
