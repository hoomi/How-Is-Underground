package com.blackberry.howisundergroundtoday.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.blackberry.howisundergroundtoday.R;
import com.blackberry.howisundergroundtoday.objects.LineObject;

import java.util.List;

public class LineAdapter extends ArrayAdapter<LineObject> {
    private static ViewHolder holder;
    private final Activity parentActivity;
    public LineAdapter(Activity parentActivity, int textViewResourceId,
                       List<LineObject> objects) {
        super(parentActivity.getBaseContext(), textViewResourceId, objects);
        this.parentActivity = parentActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.parentActivity.getLayoutInflater().inflate(R.layout.line_row,
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
        holder.logo.setImageResource(lo.getLineStatus().getStatusResoureImage());
        holder.lineStatus.setText(lo.getLineStatus().getStatusDescription());
        holder.background.setBackgroundColor(getContext().getResources().getColor(lo.getLineColor()));
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

    static class ViewHolder {
        TextView lineName, lineStatus;
        ImageView logo;
        View background;
    }
}

