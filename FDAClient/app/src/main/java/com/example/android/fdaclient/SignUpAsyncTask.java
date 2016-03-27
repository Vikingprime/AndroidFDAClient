package com.example.android.fdaclient;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vikingprime on 3/27/2016.
 */
public class SignUpAsyncTask extends AsyncTask<Void,Void,String> {


    private String mUrl;
    private String mEmail;
    private String mPassword;
    private String mName;
    private ValidityCheck mValid;

    public SignUpAsyncTask(String url, String email,String password,String name, ValidityCheck valid){
        mUrl = url;
        mEmail = email;
        mPassword = password;
        mName = name;
        mValid = valid;
    }
    @Override
    protected String doInBackground(Void... params) {
        String wholeUrl = mUrl+"/api/addUser";
        try {
            URL url = new URL(wholeUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("name",mName);
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
        mValid.isValid(Boolean.parseBoolean(result));
    }

}
