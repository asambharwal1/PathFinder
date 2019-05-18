package com.example.aashishssg.pathfinder;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegistrationEducation extends AppCompatActivity {
    ArrayList<String> al = new ArrayList<String>(){{
        add("BS Computer Science - Software Development");
        add("BS Computer Science - Game Systems"); add("BS Cyber-Physical Systems Engineering");
        add("BS Cybersecurity"); add("BS Data Science");
        add("BS Information Systems"); add("BS Information Technology");
        add("BS Math and Computer Science"); add("BS Network Engineering and Security");
        add("BS Computer Science - Software Engineering");
        add("Other");
    }};

    ArrayList<String> jobs = new ArrayList<String>(){{
        add("None");
        add("Network Security Engineer");
        add("Software Engineer");
        add("Technology Consultant");
        add("Java Developer");
        add("Application Developer");
        add("Security Analyst");
        add("Technology Support Services Analyst");
        add("Game Software Engineer");
        add("Senior Associate Consultant");
        add("Network Operations Tools and Automation Developer");
        add("Associate Solutions Analyst");
        add("Associate Software Developer");
        add("C++ Software Engineer");
        add("Programmer Analyst");
        add("Entry-Level Application Engineer");
        add("Product Data Analyst");
        add("Mobile App Developer");
        add("Client Operations Manager");
        add("Trading Analyst");
        add("Actuarial Analyst");
        add("Engineering Consultant");
        add(   "Embedded Software Engineer");
        add(    "Platform Software Engineer");
        add(   "Software Services Consultant");
        add(    "Entry Level IT Auditor");
        add(   "DevOps Engineer");
        add(   "Product Manager");
        add(   "Senior Analyst");
        add(   "Entry-Level Web Developer");
        add(    "Full-Stack Web Developer");
        add(  "Service Desk Engineer");
        add("Junior Software Implementation Specialist");
        add(   "Lead Website Architecture Engineer");
        add(    "Junior Software Engineer");
        add(    "User Experience Researcher");
        add(   "DevOps Developer");
        add(    "Software Development Consultant");
        add("Cybersecurity Consultant");
        add(".Net Applications Support Specialist");
        add( "C++/C# Programmer");
        add(  "Technology Consultant in Connectivity");
        add("Technology Consultant in User Experience");
        add("Android Developer");
        add("Web Developer");
        add("Demo Engineer");
        add("Senior Systems Analyst");
        add( "Associate Consultant");
        add( "AngularJS Developer");
        add("Systems Administrator");
        add("e-Commerce Consultant");
        add("Platform Services and Product Support");
        add( "Systems Engineer");
        add( "Full-Stack Ruby on Rails Developer");
        add("Implementation Engineer");
        add( "IS Project Manager");
        add( "Financial Reporting Analyst");
        add("Risk-Analyst");
        add("Research Analyst");
        add("Technology Consultant in Digitalization");
        add("Software Developer");
        add("Python Developer");
        add("Trading Systems Support Engineer");
        add("Network Specialist");
        add("Network Support Engineer");
        add("Security Software Engineer");
        add("Information Technology Consultant");
        add("Data Scientist");
        add("Application Analyst");
        add( "Website Architecture Engineer");
        add("Software Application Programmer");
        add( "Algorithm Engineer");
        add("Quality Analyst");
        add("Client Systems Engineer");
        add("Quality Assurance Engineer");
        add("Back-End Developer");
        add( "Back-End Enginerr");
        add( "Front-End Developer");
        add("Front-End Developer");
        add("C++ Software Developer");
        add("Data Science Instructor");
        add("Data Analyst");
        add("Support Engineer");
        add("Java Web Developer");
        add("Information Technology Specialist");
        add("Node.js Developer");
        add("Quantitive Developer");
        add("Statistical Programmer");
        add( "iOS Developer");
        add("Android Engineer");
        add("iOS Engineer");
        add(".Net Developer");
        add("UI Developer");
        add("Project Consultant");
        add("Python Software Engineer");
        add("UI/UX Designer");
        add("IT Coordinator");
        add("Game Developer");
        add("Machine Learning Engineer");
        add("AI Researcher");
        add("UX Manager");
        add("Platform Engineer");
        add("C/C++ Software Engineer");
        add("Technical Writer");
        add("Linux Administration");
        add("Application Development Coordinator");
        add("Applied Research Associate");
        add("Database/SQL Developer");
        add("FX Java Developer");
        add("C# Application Software Developer");
        add("Deep Learning Engineer");
        add("Oracle PL/SQL Developer");
        add("Drupal Developer");
        add("PHP Developer");
        add("Full-Stack Developer");
        add("Software Programmer");
        add("System Admin - Windows");
        add("Full-Stack Software Engineer");
        add("System Admin - Linux");
        add("SQL Developer");
        add("Information Security Engineer");
        add( "C# Software Engineer");
        add( "Database Administrator");

    }};

    ArrayList<String> internships = new ArrayList<String>(){{
        add("None");
        add("Software Developer Intern");
        add("Software Engineering Intern");
        add("Java Developer Intern");
        add("UI/UX Designer Intern");
        add("Cloud Intern");
        add("Quality Assurance IT Intern");
        add("Web Application Development Intern");
        add("E-Commerce Intern");
        add("Research Analyst Intern");
        add("Quantitative Research Intern");
        add("Mobile Developer Intern");
        add("Program Design Intern");
        add("Applied Data Scientist Intern");
        add("Wordpress Developer Intern");
        add("Web Content Intern");
        add("IT Intern");
        add("Data Science Intern");
        add("Data Analytics Intern");
        add("C++ Developer Intern");
        add("Web Developer Intern");
        add("Technical Writer Intern");
        add("MySQL Developer Intern");
        add("Ruby on Rails Developer Intern");
        add("Client Services Intern");
        add("Embedded Software Engineer Intern");
        add("Back-End Software Engineer Intern");
        add("Front-End Software Engineer Intern");
        add("Game Development Intern");
        add("Technology Consultant Intern");
        add("Full-Stack Web Developer Intern");
        add("PHP Developer Intern");
        add("Business Analysis Intern");
    }};

    ArrayList<String> masters = new ArrayList<String>(){{
        add("No preference");
        add("MS Information Systems - Standard");
        add("MS Information Systems - Business Analysis/System Analysis");
        add("MS Information Systems - Business Intelligence");
        add("MS Information Systems - Database Administration");
        add("MS Information Systems - IT Enterprise Management");
        add("MS Computer Science");
    }};

    Spinner sp0;
    Spinner sp1;
    Spinner sp2;
    Spinner sp3;


    FirebaseAuth fba;
    FirebaseDatabase fbd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_education);
        fba = FirebaseAuth.getInstance();
        fbd = FirebaseDatabase.getInstance();

        sp0 = (Spinner) findViewById(R.id.internship_spinner);
        ArrayAdapter<String> spinnerArrayAdapter0 = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, android.R.id.text1, internships);
        spinnerArrayAdapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp0.setAdapter(spinnerArrayAdapter0);

        sp1 = (Spinner) findViewById(R.id.major_spinner);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, android.R.id.text1, al);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(spinnerArrayAdapter1);

        sp2 = (Spinner) findViewById(R.id.job_spinner);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, android.R.id.text1, jobs);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(spinnerArrayAdapter2);

        sp3 = (Spinner) findViewById(R.id.preferred_major_spinner);
        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, android.R.id.text1, masters);
        spinnerArrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp3.setAdapter(spinnerArrayAdapter3);

        Button mEmailSignInButton = (Button) findViewById(R.id.registration_complete);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbr = fbd.getReference();
                DatabaseReference ed = dbr.child(fba.getCurrentUser().getUid()).child("education");
                ed.child("undergrad_major").setValue(sp1.getSelectedItem().toString());
                ed.child("job").setValue(sp2.getSelectedItem().toString());
                ed.child("internship").setValue(sp0.getSelectedItem().toString());
                ed.child("preferred_major").setValue(sp3.getSelectedItem().toString());
                changeActivity();
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

    public void changeActivity(){
        Intent i = new Intent(this, Skills.class);
        startActivityForResult(i, 999);
    }
}

