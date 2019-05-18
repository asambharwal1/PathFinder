package com.example.aashishssg.pathfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SuggestedMajor extends Fragment implements Updatable{

    FirebaseAuth fba;
    FirebaseDatabase fbd;
    ArrayList<String> skills = new ArrayList<>();
    HashMap<String, Float> tableSkills = new HashMap<>();
    HashMap<String, ArrayList<String>> jobMajor = new LinkedHashMap<>();
    HashMap<String, Float> majorJobs = new LinkedHashMap<>();
    ArrayList<String> top10 = new ArrayList<>();
    ArrayList<String> topMajors = new ArrayList<>();
    ListView majors;
    TextView topMajor;
    DonutProgress dp;
    String Internship;
    String Job;
    ActivityUpdatable actUpd;
    String UID = "";
    HashMap<String, HashMap<String, String>> hmList;

    public SuggestedMajor() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actUpd = (ActivityUpdatable) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_suggested_major, container, false);
        fba = FirebaseAuth.getInstance();
        fbd = FirebaseDatabase.getInstance();
        topMajor = v.findViewById(R.id.majorMatch);
        majors = v.findViewById(R.id.majorsMatched);
        dp = v.findViewById(R.id.percentageMatch);

        /*for(int j=1; j<tableGlob.length; j++){
            t_b_skills.add(tableGlob[j][0]);
            System.out.println(tableGlob[j][0]);
        }

        for(int i=1; i<tableGlob[0].length; i++){
            for(int j=0; j<tableGlob.length; j++){
                if(!tableSkills.containsKey(tableGlob[0][i])){
                    tableSkills.put(tableGlob[0][i], new ArrayList<String>());
                } else {
                    if(tableSkills.get(tableGlob[0][i]).size()<42)tableSkills.get(tableGlob[0][i]).add(tableGlob[j][i]);
                }
            }
        }

        for(String job: tableSkills.keySet()) {
            System.out.println("TABLE SKILLS JOBS: "+job);
        }

        HashMap<String, ArrayList<String>> hmGrads = new HashMap<String, ArrayList<String>>();

        for(int i=1; i<tableMatch[0].length; i++){
            for(int j=1; j<tableMatch.length; j++){
                if(tableMatch[j][i].equals("Match") && hmGrads.containsKey(tableMatch[0][i])){
                    ArrayList<String> al = hmGrads.get(tableMatch[0][i]);
                    al.add(tableMatch[j][0]);
                    hmGrads.put(tableMatch[0][i], al);
                } else if (tableMatch[j][i].equals("Match") && !hmGrads.containsKey(tableMatch[0][i])){
                    ArrayList al = new ArrayList<String>();
                    al.add(tableMatch[j][0]);
                    hmGrads.put(tableMatch[0][i], al);
                }
            }
        }

        for(String e: hmGrads.keySet()){
            System.out.println(e);
            for(String e1: hmGrads.get(e)){
                System.out.println("\t"+e1);
            }
            System.out.println();
        }

        DatabaseReference jobSkills = fbd.getReference("jobSkillList");
        for(String job: hmGrads.keySet()){
            String jobber = (job.contains("."))?job.replace(".", "(dot)"):job;
            DatabaseReference job1 = jobSkills.child(jobber.replace("#", "(hash)").replace("/", "-"));
            int size = hmGrads.get(job).size();
            //System.out.println("JOB: "+job+" SIZE OF JOB SKILLS:  "+size+"  SIZE OF SKILL LIST "+t_b_skills.size());
            for(int i=0; i<size; i++){
                job1.child("relatedMajors").child("Major_"+i).setValue(hmGrads.get(job).get(i));
            }
        }*/
        skills = new ArrayList<>();
        tableSkills = new HashMap<>();
        jobMajor = new LinkedHashMap<>();
        majorJobs = new LinkedHashMap<>();
        top10 = new ArrayList<>();
        topMajors = new ArrayList<>();
        setValues();
        return v;
    }

    public void setValues(){
        System.out.println("FAC LOG\t"+fba.getCurrentUser().getUid());
        final DatabaseReference dbr = fbd.getReference(UID.equals("")?fba.getCurrentUser().getUid().toString():UID);
        dbr.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot jobIntern = dataSnapshot.child("education");
                Internship = jobIntern.child("internship").getValue().toString();
                Job = jobIntern.child("job").getValue().toString();

                Iterator<DataSnapshot> d = dataSnapshot.child("skills").getChildren().iterator();
                while (d.hasNext()) {
                    DataSnapshot ds = d.next();
                    String skill = (String) ds.getValue();
                    skills.add(skill);
                }

                DatabaseReference getMajorfromJob = fbd.getReference("jobSkillList");
                getMajorfromJob.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            tableSkills.put(ds.getKey(), 0f);
                            jobMajor.put(ds.getKey(), new ArrayList<String>());
                            int totalSkills = 0;
                            int recSkills = 0;
                            int totalRecommendedSkills = 0;
                            for (DataSnapshot skill : ds.getChildren()) {
                                if (skill.getKey().toString().equals("relatedMajors")) {
                                    for (DataSnapshot major : skill.getChildren()) {
                                        ArrayList<String> al = jobMajor.get(ds.getKey());
                                        al.add(major.getValue().toString());
                                        jobMajor.put(ds.getKey(), al);
                                    }
                                } else if (skill.getValue().toString().contains("BS Required") || skill.getValue().toString().contains("TS Required")) {
                                    totalSkills++;
                                    if(skills.contains(skill.getKey())){
                                        //System.out.println(skill.getKey());
                                        tableSkills.put(ds.getKey(), tableSkills.get(ds.getKey()) + 1);
                                    }
                                } else if (skill.getValue().toString().contains("BS Recommended") || skill.getValue().toString().contains("TS Recommended")){
                                    totalRecommendedSkills++;
                                    if(skills.contains(skill.getKey())){
                                        recSkills++;
                                    }
                                }
                            }
                            if(totalRecommendedSkills == 0) totalRecommendedSkills = 1;
                            //System.out.println(ds.getKey()+" size of related majors: "+jobMajor.get(ds.getKey()).size());
                            //System.out.println(tableSkills.get(ds.getKey()));
                            tableSkills.put(ds.getKey(), (((tableSkills.get(ds.getKey()) / totalSkills)*0.9f) + ((recSkills/totalRecommendedSkills)*0.1f))*100);

                        }

                        Set<Map.Entry<String, Float>> me = tableSkills.entrySet();
                        Comparator<Map.Entry<String, Float>> cmp = new Comparator<Map.Entry<String, Float>>() {
                            @Override
                            public int compare(Map.Entry<String, Float> stringFloatEntry, Map.Entry<String, Float> t1) {
                                Float v1 = stringFloatEntry.getValue();
                                Float v2 = t1.getValue();
                                return v1.compareTo(v2) * -1;
                            }
                        };
                        List<Map.Entry<String, Float>> loE = new ArrayList<Map.Entry<String, Float>>(me);
                        Collections.sort(loE, cmp);
                        LinkedHashMap<String, Float> hmforJobs = new LinkedHashMap<String, Float>();
                        int top10Count = 0;

                        for (Map.Entry<String, Float> e : loE) {
                            hmforJobs.put(e.getKey(), e.getValue());
                        }

                        for(String e: hmforJobs.keySet()){
                            if (top10Count <= 10) {
                                for (String str : jobMajor.get(e)) {
                                    majorJobs.put(str, majorJobs.containsKey(str) ? majorJobs.get(str) + 1 : 1);
                                }
                                top10.add(e+"\nMatch: "+new DecimalFormat("#.##").format(hmforJobs.get(e))+"%");
                                top10Count++;
                            }
                        }

                        for(String e: majorJobs.keySet()){
                            majorJobs.put(e, majorJobs.get(e)/10);
                            if(jobMajor.get(Job)!=null && jobMajor.get(Job.replace(".", "(dot)")).contains(e)) majorJobs.put(e, majorJobs.get(e)*0.6f +0.4f);
                        }


                        List<Map.Entry<String, Float>> loE1 = new ArrayList<Map.Entry<String, Float>>(majorJobs.entrySet());
                        Collections.sort(loE1, cmp);
                        LinkedHashMap<String, Float> lhmforMajors = new LinkedHashMap<String, Float>();
                        top10Count = 0;
                        ArrayList<String> percentageHeaders = new ArrayList<String>();
                        ArrayList<Integer> percentages = new ArrayList<Integer>();
                        for (Map.Entry<String, Float> e : loE1) {
                            lhmforMajors.put(e.getKey(), e.getValue());
                            if (top10Count < 1) {
                                float s = (e.getValue())*100;
                                changeProgress((int)s, e.getKey());
                                top10Count++;
                            }
                            percentageHeaders.add(e.getKey());
                            percentages.add((int)(e.getValue()*100));
                            topMajors.add(e.getKey() + "\nPercentage: " + new DecimalFormat("#.##").format(e.getValue() * 100) + "%");
                        }

                        ArrayAdapter<String> aa = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, topMajors);
                        PercentCustomAdapter pca = new PercentCustomAdapter(getActivity(), percentageHeaders, percentages);
                        majors.setAdapter(pca);

                        majors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                changeAct(topMajors.get(i).split("\n")[0]);
                            }
                        });
                        dbr.child("suggested_major").setValue(topMajor.getText().toString().trim());
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("topJobs", top10);
                        bundle.putString("currentJob", Job);
                        actUpd.update(R.id.suggestedJobs, bundle);
                        return;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setHmList(HashMap<String, HashMap<String, String>> a){
        hmList = a;
    }

    public void changeAct(String title){
        final Intent i = new Intent(getContext(), mCoursesRequired.class);
        i.putExtra("Title", title);
        DatabaseReference dbr = fbd.getReference("majorCourses").child(title.replace(" - ", "-").replace("/", "-"));
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> al = new ArrayList<String>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    al.add(ds.getValue().toString());
                }
                i.putExtra("Courses", al);
                Bundle bundle = new Bundle();
                bundle.putSerializable("courseDesc",hmList);
                i.putExtra("hmList", bundle);
                startActivity(i);
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void changeProgress(int val, String text){
        topMajor.setText(text);
        dp.setMax(100);
        dp.setDonut_progress(val+"");
    }

    @Override
    public void update(Bundle bundle) {

    }
}
