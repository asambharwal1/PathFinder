package com.example.aashishssg.pathfinder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by Aashish SSG on 11/14/2017.
 */

public class PercentCustomAdapter extends BaseAdapter{

    ArrayList<String> percentHeader;
    ArrayList<Integer> percentages;
    Context context;
    LayoutInflater inflater = null;
    Activity act;
    PathFinder activitySkills;

    public PercentCustomAdapter(Activity activity, ArrayList<String> percentHeader, ArrayList<Integer> percent){
        act = activity;
        this.percentHeader = percentHeader;
        this.percentages = percent;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activitySkills = (PathFinder) act;
    }

    @Override
    public int getCount() {
        return percentages.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder{
        TextView percentHeader;
        CircleProgress circleProgress;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final PercentCustomAdapter.Holder hold = new PercentCustomAdapter.Holder();
        View rowView = inflater.inflate(R.layout.percentage_custom_list, viewGroup, false);
        hold.percentHeader = rowView.findViewById(R.id.percentageHeader);
        hold.circleProgress = rowView.findViewById(R.id.percentageMatch);
        hold.percentHeader.setText(percentHeader.get(i));
        hold.circleProgress.setMax(100);
        hold.circleProgress.setProgress(percentages.get(i));
        return rowView;
    }

}
