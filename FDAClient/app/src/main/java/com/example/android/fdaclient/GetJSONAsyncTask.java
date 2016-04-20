package com.example.android.fdaclient;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vikingprime on 3/23/2016.
 */
public class GetJSONAsyncTask extends AsyncTask<Void, Void, String> {

    private String mUrl;
    private String mEmail;
    private WeakReference<JSONParser> mParser;
    private String surveyID;
    private String mPassword;

    public GetJSONAsyncTask(String url, String email,String id,String password, JSONParser parser){
        mUrl = url;
        mEmail = email;
        mParser = new WeakReference<JSONParser>(parser);
        surveyID = id;
        mPassword = password;
    }
    @Override
    protected String doInBackground(Void... params) {
        String wholeUrl = mUrl+"/api/getSurvey";
        Log.d("WHOLE URL",wholeUrl);
        try {
            Log.d("TAG","ABOUT TO REQUEST");
            URL url = new URL(wholeUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("email", mEmail);
            con.setRequestProperty("id",surveyID);
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


            Log.d("TAG", "Checking Response " + response.toString());

            return response.toString();

        } catch (Exception e) {
            Log.d("EXCEPTION",e.toString());
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result) {
        Log.d("TAG","ABOUT TO REQUEST ACTIVITY"+result);
        JSONObject jsonObj=null;
        try {
            jsonObj = new JSONObject(result);
        }catch(Exception e){
           e.printStackTrace();
        }
        JSONParser Parse=null;
        if(mParser.get()!=null){
            Parse = mParser.get();
        }
        if(Parse!=null)
        Parse.parse(jsonObj,StudyActivity.QUESTION_ACTION);
    }
}
