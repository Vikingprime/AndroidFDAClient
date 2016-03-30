package com.example.android.fdaclient;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vikingprime on 3/30/2016.
 */
public class sendResult extends AsyncTask<Void, Void, Void> {
    private String mUrl;
    private String mAnswers;

    public sendResult(String url, String answers) {
        mUrl = url;
        mAnswers = answers;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String wholeUrl = mUrl + "/api/saveSurvey";
        try {
            URL url = new URL(wholeUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("answer", mAnswers);

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Log.d("HERE", "GOT HERE");

        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
