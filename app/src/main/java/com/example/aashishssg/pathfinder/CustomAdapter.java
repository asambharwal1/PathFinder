package com.example.aashishssg.pathfinder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aashish SSG on 9/1/2017.
 */

public class CustomAdapter extends BaseAdapter {
    ArrayList<String> skill;
    Context context;
    LayoutInflater inflater = null;
    Activity act;
    Skills activitySkills;

    public CustomAdapter(Activity activity, ArrayList<String> skillInterest){
        act = activity;
        skill = skillInterest;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activitySkills = (Skills) act;
    }

    @Override
    public int getCount() {
        return skill.size();
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
        TextView skillInterest;
        Button delete;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Holder hold = new Holder();
        View rowView = inflater.inflate(R.layout.custom_list, null);
        hold.delete = rowView.findViewById(R.id.delete_skill);
        hold.skillInterest = rowView.findViewById(R.id.description);
        hold.skillInterest.setText(skill.get(i));
        final int val = i;
        hold.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skill.remove(val);
                activitySkills.resetValues();
            }
        });
        return rowView;
    }

}
