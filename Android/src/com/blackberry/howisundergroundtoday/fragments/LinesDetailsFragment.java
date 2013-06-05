package com.blackberry.howisundergroundtoday.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blackberry.howisundergroundtoday.R;
import com.blackberry.howisundergroundtoday.objects.LineObject;

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
            return new LinesDetailsFragment(new LineObject());
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
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public LinesDetailsFragment(LineObject line) {
        this.line = line;
    }

    public void setLine(LineObject line) {
        this.line = line;
    }
}
