package com.blackberry.howisundergroundtoday.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.blackberry.howisundergroundtoday.fragments.LinesDetailsFragment;
import com.blackberry.howisundergroundtoday.objects.LineObject;

import java.util.ArrayList;
import java.util.Collection;

public class LineFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<LineObject> linesList = null;

    public LineFragmentAdapter(FragmentManager fm, ArrayList<LineObject> linesList) {
        super(fm);
        if (linesList != null) {
            this.linesList = linesList;
        } else {
            this.linesList = new ArrayList<LineObject>();
        }
    }

    @Override
    public Fragment getItem(int i) {
        if (this.linesList == null || this.linesList.size() == 0) {
            return null;
        }
        return LinesDetailsFragment.newInstance(this.linesList.get(i));
    }

    @Override
    public int getCount() {
        if (this.linesList != null) {
            return this.linesList.size();
        }
        return 0;
    }
}
