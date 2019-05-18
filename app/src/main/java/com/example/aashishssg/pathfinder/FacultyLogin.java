package com.example.aashishssg.pathfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FacultyLogin extends AppCompatActivity {

    ListView lv;
    FirebaseDatabase fbd;
    HashMap<String, HashMap<String, String>> mapHashMap;
    ArrayList<SpannableString> userNames = new ArrayList<>();
    ArrayAdapter<SpannableString> adapter;
    FacultyLogin login = this;
    FirebaseAuth fba;
    HashMap<SpannableString, String> textToUID = new HashMap<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.faculty_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.faculty_logout){
            setResult(404);
            fba.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);
        //System.out.println("THIS WAS CALLED HERE AGAIN.");
        setTitle("Faculty View");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fba = FirebaseAuth.getInstance();
        lv = (ListView) findViewById(R.id.users);
        fbd = FirebaseDatabase.getInstance();
        mapHashMap = new HashMap<>();
        DatabaseReference dbr = fbd.getReference();
        //System.out.println("FAC LOG\t"+fba.getCurrentUser().getUid());
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UID = "";
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    //System.out.println(ds.getKey().toString());
                    if(!ds.getKey().toString().equals("majorCourses") && !ds.getKey().toString().equals("jobSkillList")){
                        UID = ds.getKey().toString();
                        mapHashMap.put(UID, new HashMap<String, String>());
                        System.out.println(UID);
                        for(DataSnapshot d: ds.getChildren()){
                            //System.out.println("\t"+d.getKey());
                            if(d.getKey().toString().equals("full_name")) mapHashMap.get(UID).put("full_name", d.getValue().toString());
                            if(d.getKey().toString().equals("suggested_major")) mapHashMap.get(UID).put("suggested_major", d.getValue().toString());
                            if(d.getKey().toString().equals("email")) mapHashMap.get(UID).put("email", d.getValue().toString());
                        }
                    }
                }

                for(String key: mapHashMap.keySet()){
                    HashMap<String, String> values = mapHashMap.get(key);
                    String name = values.get("full_name");
                    String email = "E-mail: "+values.get("email");
                    String major = "Major: "+values.get("suggested_major");
                    SpannableString ss0 = new SpannableString(name);
                    ss0.setSpan(new RelativeSizeSpan(1.5f), 0, name.length(), 0);
                    SpannableString ss1 = new SpannableString(email);
                    ss1.setSpan(new RelativeSizeSpan(1f), 0, email.length(), 0);
                    SpannableString ss2 = new SpannableString(major);
                    ss2.setSpan(new RelativeSizeSpan(1f), 0, major.length(), 0);
                    CharSequence cs = TextUtils.concat(ss0, "\n", ss1, "\n", ss2);
                    userNames.add(new SpannableString(cs));
                    textToUID.put(new SpannableString(cs), key);
                }
                adapter = new ArrayAdapter<SpannableString>(getApplicationContext(), android.R.layout.simple_list_item_1, userNames);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String UID = textToUID.get(userNames.get(i));
                changeActivity(UID);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==234 && resultCode==404){
            finish();
        }
    }

    public void changeActivity(String UID){
        Intent intent = new Intent(this, PathFinder.class);
        intent.putExtra("UID", UID);
        startActivityForResult(intent, 234);
    }

}
