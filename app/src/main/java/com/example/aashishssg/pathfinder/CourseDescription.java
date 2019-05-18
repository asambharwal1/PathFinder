package com.example.aashishssg.pathfinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CourseDescription extends AppCompatActivity {

    TextView titleTV;
    TextView descriptionTV;
    TextView offeredTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleTV = (TextView) findViewById(R.id.course_title);
        descriptionTV = (TextView) findViewById(R.id.course_description);
        offeredTV = (TextView) findViewById(R.id.course_offered);
        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("Title"));
        String title = extras.getString("Title");
        String courseNumber = "N/A";
        String courseName = "N/A";
        if(title.startsWith("CSC")){
            courseNumber = title.substring(0, 7).trim();
            courseName = title.substring(8).trim();
        } else {
            courseNumber = title.substring(0, 6).trim();
            courseName = title.substring(7).trim();
        }
        setTitle(courseNumber);
        titleTV.setText(courseNumber+"\n"+courseName);
        descriptionTV.setText(extras.getString("Description"));
        offeredTV.setText("Typically Offered:\n"+extras.getString("Typically_Offered"));

    }
}
