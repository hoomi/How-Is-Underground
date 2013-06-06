package com.blackberry.howisundergroundtoday.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.blackberry.howisundergroundtoday.R;
import com.blackberry.howisundergroundtoday.objects.LineObject;
import com.blackberry.howisundergroundtoday.tools.Logger;

import java.util.HashMap;

/**
 * Created by Hooman on 03/06/13.
 */
public class LinesDetailsFragment extends Fragment {

    private LineObject line = null;
    private static HashMap<Integer, LinesDetailsFragment> linesFragmentMap = new HashMap<Integer, LinesDetailsFragment>();

    /**
     * Returns a new instance of the LinesDetailsFragment
     *
     * @param line It is the object which sets the UI of the Fragment
     * @return Returns an instance of LinesDetailsFragment
     */
    public static LinesDetailsFragment newInstance(LineObject line) {
        if (line == null) {
            return new LinesDetailsFragment();
        }
        int key = line.getLineId();
        LinesDetailsFragment lineFragment = null;
        if (LinesDetailsFragment.linesFragmentMap.containsKey(key)) {
            lineFragment = LinesDetailsFragment.linesFragmentMap.get(key);
            lineFragment.setLine(line);
        } else {
            lineFragment = new LinesDetailsFragment(line);
            LinesDetailsFragment.linesFragmentMap.put(key, lineFragment);
        }
        return lineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.linedetailsfragment, null, false);
        TextView tv = (TextView) v.findViewById(R.id.lineTitleTextview);
        tv.setText(this.line.getLineName());
        tv = (TextView) v.findViewById(R.id.lineStatusTextView);
        tv.setText(this.line.getLineStatus().getStatusCssClass());
        tv = (TextView) v.findViewById(R.id.lineStatusDetails);
        tv.setText(this.line.getLineStatusDetails());
        Logger.i(LinesDetailsFragment.class, this.line.getLineColor() + " line color");
        (v.findViewById(R.id.parent_RelativeLayout)).setBackgroundColor(getResources().getColor(this.line.getLineColor()));
        ImageView iv = (ImageView) v.findViewById(R.id.lineIconImageview);
        iv.setImageResource(line.getLineStatus().getStatusResoureImage());
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public LinesDetailsFragment(LineObject line) {
        this.line = line;
    }

    public LinesDetailsFragment() {
        this.line = new LineObject();
    }

    public void setLine(LineObject line) {
        this.line = line;
    }
}
