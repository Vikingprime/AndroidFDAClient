package com.example.android.fdaclient;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vikingprime on 3/27/2016.
 */
public class LoginAsyncTask extends AsyncTask<Void,Void,String>{


    private String mUrl;
    private String mEmail;
    private String mPassword;
    private JSONParser mParser;

    public LoginAsyncTask(String url, String email,String password, JSONParser surveyParser){
        mUrl = url;
        mEmail = email;
        mPassword = password;
        mParser= surveyParser;
    }
    @Override
    protected String doInBackground(Void... params) {
        String wholeUrl = mUrl+"/api/login";
        try {
            URL url = new URL(wholeUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("email", mEmail);
            con.setRequestProperty("password",mPassword);

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
            //print result

            return response.toString();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("LOGIN", result);
        Log.d("TAG","ABOUT TO REQUEST ACTIVITY"+result);
        JSONObject jsonObj=null;
        try {
            jsonObj = new JSONObject(result);
        }catch(Exception e){
            e.printStackTrace();
        }
        mParser.parse(jsonObj);
    }

}
