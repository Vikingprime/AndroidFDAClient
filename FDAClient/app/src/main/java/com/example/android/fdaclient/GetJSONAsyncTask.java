package com.example.android.fdaclient;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vikingprime on 3/23/2016.
 */
public class GetJSONAsyncTask extends AsyncTask<Void, Void, String> {

    private String mUrl;
    private String mEmail;
    private JSONParser mParser;

    public GetJSONAsyncTask(String url, String email, JSONParser parser){
        mUrl = url;
        mEmail = email;
        mParser = parser;
    }
    @Override
    protected String doInBackground(Void... params) {
        String wholeUrl = mUrl+"/api/getSurvey";
        try {
            URL url = new URL(wholeUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("Email", mEmail);

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result

            return response.toString();

        } catch (Exception e) {
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result) {
        JSONObject jsonObj=null;
        try {
            jsonObj = new JSONObject(result);
        }catch(Exception e){
           e.printStackTrace();
        }
        mParser.parse(jsonObj);
    }
}
