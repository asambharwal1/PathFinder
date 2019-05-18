package com.example.aashishssg.pathfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class mCoursesRequired extends AppCompatActivity implements Updatable{

    TextView MajorName;
    TextView majorDescription;
    ListView CoursesNeeded;
    HashMap<String, HashMap<String, String>> lister = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_courses_required);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        majorDescription = (TextView) findViewById(R.id.major_description);
        MajorName = (TextView) findViewById(R.id.major_titleAct);
        CoursesNeeded = (ListView) findViewById(R.id.sub_list_courses);
        StringBuffer sb = new StringBuffer("");
        Bundle bundle = getIntent().getExtras();
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(this.getAssets().open("majorsCourses.json")));
            String temp;
            while ((temp = br.readLine())!=null){
                sb.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        try {
            JSONObject obj = new JSONObject(sb.toString());
            System.out.println(obj.toString());
            String description = obj.getJSONObject(bundle.getString("Title").replace(" - ", "-")).getString("description");
            majorDescription.setText(description);
        } catch (Exception e){

        }

        MajorName.setText(bundle.getString("Title"));
        setTitle(MajorName.getText().toString().split("-")[0]);
        final ArrayList<String> list = bundle.getStringArrayList("Courses");
        Bundle b = bundle.getBundle("hmList");
        lister = (HashMap<String, HashMap<String,String>>) b.getSerializable("courseDesc");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        CoursesNeeded.setAdapter(aa);
        CoursesNeeded.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = "";
                for(String e: lister.keySet()){
                    if(e.contains(list.get(i))){
                        info = e;
                        break;
                    }
                }
                changeAct(info, lister.get(info)==null?"N/A":lister.get(info).get("description"), lister.get(info)==null?"N/A":lister.get(info).get("Typically_Offered"));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void changeAct(String key, String description, String offered){
        Intent change = new Intent(this, CourseDescription.class);
        change.putExtra("Title", key);
        change.putExtra("Description", description);
        change.putExtra("Typically_Offered", offered);
        startActivity(change);
    }

    @Override
    public void update(Bundle bundle) {
        //System.out.println("UPDATES THE HASHMAP");
        lister = (HashMap<String, HashMap<String,String>>) bundle.getSerializable("courseDesc");
    }
}
