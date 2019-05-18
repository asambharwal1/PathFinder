package com.example.aashishssg.pathfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CourseSearch extends ListFragment implements Updatable{

    HashMap<String, HashMap<String, String>> hm;
    SearchView sv;
    ArrayList<String> courses;
    ArrayAdapter<String> ad;
    ListView lv;
    Context mContext;
    ActivityUpdatable actUpd;

    public CourseSearch() {
        // Required empty public constructor
    }

    public void setHm(HashMap<String, HashMap<String, String>> a){
        hm = a;
    }

    public void setCourses(ArrayList<String> a){
        courses = a;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actUpd = (ActivityUpdatable) context;
    }

    public static CourseSearch newInstance(String param1, String param2) {
        CourseSearch fragment = new CourseSearch();
        return fragment;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String key = (String) l.getItemAtPosition(position);
        changeToDescription(key,
                hm.get(key).get("description"),
                hm.get(key).get("Typically_Offered"));
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_course_search, container, false);
        lv = (ListView) v.findViewById(android.R.id.list);
        setHasOptionsMenu(true);

        ad = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, courses);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        lv.setTextFilterEnabled(true);
        return v;
    }

    public void changeToDescription(String key, String description, String offered){
        Intent change = new Intent(getContext(), CourseDescription.class);
        change.putExtra("Title", key);
        change.putExtra("Description", description);
        change.putExtra("Typically_Offered", offered);
        startActivity(change);
    }

    public void resetAdapter(ArrayList<String> al){
        ad = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(ad);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(s.equals("")) {
                    resetAdapter(courses);
                } else{ArrayList<String> filteredValues = new ArrayList<String>(courses);
                    for(String val: courses){
                        if(!val.toLowerCase().contains(s.toLowerCase())){
                            filteredValues.remove(val);
                        }
                    }
                    resetAdapter(filteredValues);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")) {
                    resetAdapter(courses);
                } else{ArrayList<String> filteredValues = new ArrayList<String>(courses);
                    for(String val: courses){
                        if(!val.toLowerCase().contains(s.toLowerCase())){
                            filteredValues.remove(val);
                        }
                    }
                    resetAdapter(filteredValues);
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void update(Bundle bundle) {

    }
}
