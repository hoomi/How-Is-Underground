package com.blackberry.howisundergroundtoday.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.blackberry.howisundergroundtoday.R;
import com.blackberry.howisundergroundtoday.adapter.LineAdapter;
import com.blackberry.howisundergroundtoday.objects.UndergroundStatusObject;

public class LinesListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private LineAdapter mLineAdapter = null;
    private ListView mLinesListView = null;

    public LinesListFragment() {
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

    }
}
