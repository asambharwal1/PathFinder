package com.example.aashishssg.pathfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A login screen that offers login via email/password.
 */
public class Skills extends AppCompatActivity {

    ListView listSkills;
    ListView listInterests;

    CustomAdapter lSadapter;
    CustomAdapter lIadapter;

    FirebaseAuth fba;
    FirebaseDatabase fbd;

    ArrayList<String> lSarray = new ArrayList<>();
    ArrayList<String> lIarray = new ArrayList<>();
    ArrayList<String> skillsList = new ArrayList<String>(){{
        add("None");
        add("Python");
        add("Java");
        add("C");
        add("C++");
        add("C#");
        add("HTML");
        add("CSS");
        add("JavaScript");
        add("AngularJS");
        add("Node.js");
        add("SQL");
        add("Oracle SQL Dev.");
        add("PL/SQL");
        add("SPSS");
        add("Microsoft Word");
        add("Microsoft Powerpoint");
        add("Microsoft Excel");
        add("XML");
        add(".Net");
        add("Swift");
        add("MySQL");
        add("Machine Learning");
        add("Linux");
        add("Unix");
        add("Ruby");
        add("Ruby on Rails");
        add("PHP");
        add("Git");
        add("JQuery");
        add("System Security");
        add("Coordinate Projects");
        add("Upgrade/Maintain Tech");
        add("Analyze/Implement Tech");
    }};

    ArrayList<String> interestsList = new ArrayList<String>(){{
        add("None");
        add("Collaborative");
        add("Verbal and Communication");
        add("Presentation");
        add("Coordinate Staff");
        add("Supervise Staff");
        add("Organize Staff");
        add("Problem Solving");
        add("Customer Service");
        add("Leadership");
        add("Time management");
    }};

    public void resetValues(){
        lSadapter.notifyDataSetChanged();
        lIadapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);
        final Spinner skills = (Spinner) findViewById(R.id.skills_spinner);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, android.R.id.text1, skillsList);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skills.setAdapter(ad);

        fba = FirebaseAuth.getInstance();
        fbd = FirebaseDatabase.getInstance();

        lSadapter = new CustomAdapter(this, lSarray);
        lIadapter = new CustomAdapter(this, lIarray);

        System.out.println(lSarray.hashCode());

        listSkills = (ListView) findViewById(R.id.list_skills);
        listSkills.setAdapter(lSadapter);
        listInterests = (ListView) findViewById(R.id.list_interests);
        listInterests.setAdapter(lIadapter);

        skills.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0 && !lSarray.contains(skillsList.get(i))) lSarray.add(skillsList.get(i));
                lSadapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Spinner interests = (Spinner) findViewById(R.id.interests_spinner);
        ArrayAdapter<String> ad1 = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, android.R.id.text1, interestsList);
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interests.setAdapter(ad1);

        interests.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0 && !lIarray.contains(interestsList.get(i))) lIarray.add(interestsList.get(i));
                lIadapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );


        Button logIn = (Button) findViewById(R.id.logged_in_button);
        logIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbr = fbd.getReference();
                DatabaseReference sk = dbr.child(fba.getCurrentUser().getUid()).child("skills");
                for(int i=0; i<lSarray.size(); i++){
                    sk.child("tech_"+i).setValue(lSarray.get(i));
                }
                for(int i=0; i<lIarray.size(); i++){
                    sk.child("business_"+i).setValue(lIarray.get(i));
                }
                changeAct();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 999){
            setResult(999);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void changeAct(){
        Intent i = new Intent(this, PathFinder.class);
        i.putExtra("userJustRegistered", true);
        startActivityForResult(i, 999);
    }
}

