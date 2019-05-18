package com.example.aashishssg.pathfinder;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PathFinder extends AppCompatActivity implements ActivityUpdatable{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    SuggestedMajor suggestedMajor;
    SuggestedJobs suggestedJobs;
    CourseSearch courseSearch;
    FirebaseAuth fba;

    boolean justRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Path Finder");
        fba = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_major);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_work);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_action_name);

        HashMap<String, HashMap<String, String>> hm = new HashMap<String, HashMap<String, String>>();
        ArrayList<String> courses = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(this.getAssets().open("classes.json")));
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
            obj = obj.getJSONObject("courses");
            Iterator<String> iter = obj.keys();
            while (iter.hasNext()){
                String key = iter.next();
                JSONObject jsonObject = obj.getJSONObject(key);
                courses.add(key);
                //System.out.println(key);
                String description = jsonObject.getString("description");
                String typically_offered= "";
                if(jsonObject.has("Typically_Offered"))
                    typically_offered = jsonObject.getString("Typically_Offered");
                hm.put(key, new HashMap<String, String>());
                hm.get(key).put("description", description);
                hm.get(key).put("Typically_Offered", typically_offered.isEmpty()?"N/A":typically_offered);
            }
        } catch (Exception e){

        }

        String UID = getIntent().hasExtra("UID")?getIntent().getStringExtra("UID"):"";

        suggestedMajor = new SuggestedMajor();
        suggestedJobs = new SuggestedJobs();
        courseSearch = new CourseSearch();
        suggestedMajor.UID = UID;
        courseSearch.setHm(hm);
        courseSearch.setCourses(courses);
        suggestedMajor.setHmList(hm);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {

            if(fba.getCurrentUser().getEmail().equals("admin@faculty.edu")){
                setResult(404);
            } else if (getIntent().getBooleanExtra("userJustRegistered", false)){
                setResult(999);
            }
            fba.signOut();
            finish();
            return true;
        } else if (id == R.id.app_bar_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(int id, Bundle bundle) {
        switch(id){
            case R.id.suggestedMajor:
                suggestedMajor.update(bundle);
                break;
            case R.id.suggestedJobs:
                suggestedJobs.update(bundle);
                break;
            case R.id.courseSearch:
                courseSearch.update(bundle);
                break;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0:
                    return suggestedMajor;
                case 1:
                    return suggestedJobs;
                case 2:
                    return courseSearch;
            }

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Major";
                case 1:
                    return "Jobs";
                case 2:
                    return "Courses";
            }
            return null;
        }
    }
}
