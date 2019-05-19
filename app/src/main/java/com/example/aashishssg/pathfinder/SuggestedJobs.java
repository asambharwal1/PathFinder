package com.example.aashishssg.pathfinder;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.WIFI_SERVICE;

public class SuggestedJobs extends Fragment implements Updatable{

    float currPay;
    String ip;
    List<BarEntry> entries;
    TextView topJob;
    ListView topJobsList;
    BarChart barChart;
    ArrayList<String> jobList = new ArrayList<>();
    String topJobBarAdd = "";
    String currentJob = "";
    TextView vsText;
    ArrayList<String> percentageHeaders;
    ArrayList<Integer> percentages;

    public SuggestedJobs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_suggested_jobs, container, false);
        topJob = v.findViewById(R.id.job_title);
        topJobsList = v.findViewById(R.id.jobList);
        barChart = v.findViewById(R.id.barChart);
        vsText = v.findViewById(R.id.vsText);

        percentageHeaders = new ArrayList<>();
        percentages = new ArrayList<>();

        //ArrayAdapter<String> aa = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, jobList);

        for(String a: jobList){
            String[] type = a.split("\n");

            percentageHeaders.add(type[0]);
            percentages.add(Integer.parseInt(Math.round(Float.parseFloat(type[1].replace("Match: ", "").replace("%", "").trim()))+""));

            System.out.println(type[0]+"    "+type[1].replace("Match: ", "").replace("%", "").trim());
        }

        PercentCustomAdapter pca = new PercentCustomAdapter(getActivity(), percentageHeaders, percentages);
        topJobsList.setAdapter(pca);

        WifiManager wm = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        int ipAddress = wi.getIpAddress();
        ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        JSONTask jsonTask = new JSONTask();
        jsonTask.execute("http://api.glassdoor.com/api/api.htm?t.p=%T_P%&t.k=%T_K%&userip="+ip+"&useragent=&format=json&v=1&action=jobs-prog&countryId=1&jobTitle="+topJobBarAdd.replace("-", "").replace("\\s+", "%20"));
        JSONTask jsonTaskCurr = new JSONTask();
        jsonTaskCurr.execute("http://api.glassdoor.com/api/api.htm?t.p=%T_P%&t.k=%T_K%&userip="+ip+"&useragent=&format=json&v=1&action=jobs-prog&countryId=1&jobTitle="+currentJob.replace("-", "").replace("\\s+", "%20"));
        try {
            jsonTask.get();
            jsonTaskCurr.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        currPay = (float) jsonTaskCurr.getHighPay();

        entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) jsonTask.getHighPay()));
        entries.add(new BarEntry(1f, currPay));
        BarDataSet set = new BarDataSet(entries, "Salary");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{topJobBarAdd, "", currentJob}));
        vsText.setText(topJobBarAdd+" v. "+currentJob);
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.invalidate();

        topJobsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                topJobBarAdd = jobList.get(i).split("\n")[0];
                JSONTask jsonTask = new JSONTask();
                jsonTask.execute("http://api.glassdoor.com/api/api.htm?t.p=%T_P%&t.k=%T_K%&userip="+ip+"&useragent=&format=json&v=1&action=jobs-prog&countryId=1&jobTitle="+topJobBarAdd.replace("-", "").replace("\\s+", "%20"));
                try {
                    jsonTask.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                entries = new ArrayList<>();
                //System.out.println("Current Job: "+currentJob+"\t"+currPay);
                //System.out.println("New Job: "+topJobBarAdd+"\t"+jsonTask.getHighPay());
                entries.add(new BarEntry(0f, (float) jsonTask.getHighPay()));
                entries.add(new BarEntry(2f, currPay));
                BarDataSet set = new BarDataSet(entries, "Salary");

                BarData data = new BarData(set);
                data.setBarWidth(0.9f);
                barChart.setData(data);
                barChart.setFitBars(true);

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{topJobBarAdd, "", currentJob}));
                vsText.setText(topJobBarAdd+" v. "+currentJob);
                if(jsonTask.getHighPay() == 0){
                    Toast.makeText(getContext(), "No data found for "+topJobBarAdd, Toast.LENGTH_SHORT).show();
                }
                barChart.invalidate();
            }
        });
        return v;
    }

    @Override
    public void update(Bundle bundle) {
        percentageHeaders = new ArrayList<>();
        percentages = new ArrayList<>();
        jobList = bundle.getStringArrayList("topJobs");
        currentJob = bundle.getString("currentJob");
        topJob.setText(jobList.get(0));
        ArrayAdapter<String> aa = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, jobList);
        topJobBarAdd = jobList.get(0).split("\n")[0];
        for(String a: jobList){
            String[] type = a.split("\n");

            percentageHeaders.add(type[0]);
            percentages.add(Integer.parseInt(Math.round(Float.parseFloat(type[1].replace("Match: ", "").replace("%", "").trim()))+""));

            System.out.println(type[0]+"    "+type[1].replace("Match: ", "").replace("%", "").trim());
        }

        PercentCustomAdapter pca = new PercentCustomAdapter(getActivity(), percentageHeaders, percentages);
        topJobsList.setAdapter(pca);


        WifiManager wm = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        int ipAddress = wi.getIpAddress();
        ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        JSONTask jsonTask = new JSONTask();
        jsonTask.execute("http://api.glassdoor.com/api/api.htm?t.p=%T_P%&t.k=%T_K%&userip="+ip+"&useragent=&format=json&v=1&action=jobs-prog&countryId=1&jobTitle="+topJobBarAdd.replace("-", "").replace("\\s+", "%20"));
        JSONTask jsonTaskCurr = new JSONTask();
        jsonTaskCurr.execute("http://api.glassdoor.com/api/api.htm?t.p=%T_P%&t.k=%T_K%&userip="+ip+"&useragent=&format=json&v=1&action=jobs-prog&countryId=1&jobTitle="+currentJob.replace("-", "").replace("\\s+", "%20"));
        try {
            jsonTask.get();
            jsonTaskCurr.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /*try {
            jsonTaskCurr.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        currPay = (float) jsonTaskCurr.getHighPay();

        entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) jsonTask.getHighPay()));
        entries.add(new BarEntry(2f, currPay));
        BarDataSet set = new BarDataSet(entries, "Salary");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f);
        barChart.setData(data);
        barChart.setFitBars(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{topJobBarAdd, "", currentJob}));
        vsText.setText(topJobBarAdd+" v. "+currentJob);
        barChart.invalidate();
    }
}
