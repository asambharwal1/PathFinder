package com.example.aashishssg.pathfinder;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aashish on 7/18/2017.
 */

public class JSONTask extends AsyncTask<String, String, String>{

    double medPay = 0;
    double highPay = 0;

    public double getHighPay() {
        return highPay;
    }

    public double getMedPay() {
        return medPay;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        //System.out.println(params[0]);

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buff = new StringBuffer();

            String line = "";

            while((line = reader.readLine())!=null){
                buff.append(line);
            }
            String stringedJson = buff.toString();
            JSONObject parentObject = new JSONObject(stringedJson);
            JSONObject pays = parentObject.getJSONObject("response");
            medPay = pays.getDouble("payMedian");
            highPay = pays.getDouble("payHigh");
            //System.out.println("Median Pay: "+medPay+"\tHigh Pay: "+highPay);
            return buff.toString();
        } catch(Exception e){}
        return null;
    }
}
