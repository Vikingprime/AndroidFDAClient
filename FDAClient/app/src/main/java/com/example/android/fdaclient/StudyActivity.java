package com.example.android.fdaclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class StudyActivity extends AppCompatActivity {
    private JSONObject json = new JSONObject();
    ListView mListView;
    SurveyAdapter mSurveyAdapter;
    String url = "ec2-52-207-254-157.compute-1.amazonaws.com:3000";
    String email = "janeDoe@gmail.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        hardCodeJSON();
        makeRequest();
        ArrayList<String> questions = new ArrayList<String>();
        ArrayList<ArrayList<String>> answers = new ArrayList<ArrayList<String>>();
        Log.d("TAG", "ONCREATE AFTER HARDCODE");
        try {

            int NumberSA = json.getInt("NumberSA");
            int NumberMC = json.getInt("NumberMC");
            int NumberCh = json.getInt("NumberCh");
            JSONArray SAArray = null;
            JSONArray MCArray = null;
            JSONArray Answers = null;
            if(NumberSA!=0) SAArray = json.getJSONArray("SAQuestions");
            if(NumberMC!=0) {
                MCArray = json.getJSONArray("MCQuestions");
                Answers = json.getJSONArray("MCAnswers");
            }


            Log.d("TAG", "ONCREATE AFTER JSON RETRIEVAL");
            for(int i = 0;i<NumberSA;i++){
                questions.add((String) SAArray.get(i));
            }
            for(int i = 0;i<NumberMC;i++){
                questions.add((String) MCArray.get(i));
                Log.d("TAG","Added a Question: "+(String) MCArray.get(i));
            }
            for(int i =0;i<NumberMC;i++){
                answers.add(new ArrayList<String>());
                for(int z =0;z<((JSONArray)(Answers.get(i))).length();z++){
                    answers.get(i).add((String) ((JSONArray)(Answers.get(i))).get(z));
                    Log.d("TAG","Added "+(String) ((JSONArray)(Answers.get(i))).get(z));
                }
            }


            Log.d("TAG", "ONCREATE AFTER for loops");
            initializeViewFields(questions, answers, NumberSA, NumberMC, NumberCh);


            Log.d("TAG", "ONCREATE AFTER INIT VIEW");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG", "JSON EXCEPTION "+e.toString());
        }
    }

    private void initializeViewFields(ArrayList<String> questions, ArrayList<ArrayList<String>> answers, int
            numberSA, int numberMC, int numberCh) {
        mListView = (ListView) findViewById(R.id.survey_list);
        mSurveyAdapter = new SurveyAdapter(numberSA,numberMC,numberCh);
        mSurveyAdapter.setQuestions(questions,answers);
        mListView.setAdapter(mSurveyAdapter);
    }


    private void hardCodeJSON(){
        try {
            json.put("NumberSA", 2);
            json.put("NumberMC", 2);
            json.put("NumberCh", 0);
              json.accumulate("SAQuestions","What's your favorite color?");
           json.accumulate("SAQuestions","What city were you born in?");
              json.accumulate("MCQuestions", "How many hours do you sleep?");
                json.accumulate("MCQuestions", "How many meals do you have per day?");
            JSONArray AnswerArray= new JSONArray();
            AnswerArray.put(new JSONArray());
            AnswerArray.put(new JSONArray());
            ((JSONArray) AnswerArray.get(0)).put("<1");
            ((JSONArray) AnswerArray.get(0)).put("2-4");
            ((JSONArray) AnswerArray.get(0)).put("4-6");
            ((JSONArray) AnswerArray.get(0)).put(">6");
            ((JSONArray) AnswerArray.get(1)).put("1");
            ((JSONArray) AnswerArray.get(1)).put("2");
            ((JSONArray) AnswerArray.get(1)).put("3");
            ((JSONArray) AnswerArray.get(1)).put(">3");
            json.accumulate("MCAnswers",AnswerArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void makeRequest(){
        Log.d("TAG", "ENTERED MAKE REQUEST");
        String wholeUrl = url+"/api/getSurvey";
        try {
            URL url = new URL(wholeUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("Email", email);


            Log.d("TAG", "ABOUT TO SEND");
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
           Log.d("TAG", response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private class SurveyAdapter extends BaseAdapter {

        private static final int TYPE_MAX_COUNT = 3;

        private ArrayList<String> mQuestions;
        private ArrayList<ArrayList<String>> MCAnswers;
        private LayoutInflater mInflater;
        private int SA;
        private int MC;
        private int check;
        private final int short_Answer_Question = 0;
        private final int multiple_Choice_Question = 1;
        private final int check_box_Question = 2;


        public SurveyAdapter(int shortAnswer, int multipleChoice, int checkbox) {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            SA = shortAnswer;
            MC = multipleChoice;
            check = checkbox;
        }


        public void setQuestions(ArrayList<String> questions,ArrayList<ArrayList<String>> mcAnswers){
            mQuestions = questions;
            MCAnswers = mcAnswers;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            int offset = ++position;
          if(offset<=SA){
                return short_Answer_Question;
            }
            else if(offset<=SA+MC){
              return multiple_Choice_Question;
          }
            else {
              return check_box_Question;
          }
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

        @Override
        public int getCount() {
            return mQuestions.size();
        }

        @Override
        public String getItem(int position) {
            return mQuestions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            System.out.println("getView " + position + " " + convertView + " type = " + type);
            if (convertView == null) {
                holder = new ViewHolder();
                holder.buttonCreated = false;
                switch (type) {
                    case short_Answer_Question:
                        convertView = mInflater.inflate(R.layout.short_answer_listview, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.question);
                        holder.rGroup = null;
                        break;
                    case multiple_Choice_Question:
                        convertView = mInflater.inflate(R.layout.multiple_choice_listview, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.MCquestion);
                        holder.rGroup = (RadioGroup)convertView.findViewById(R.id.radiogroup);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(mQuestions.get(position));
            if(holder.rGroup!=null && holder.buttonCreated==false){
                LinearLayout linlayout = new LinearLayout(StudyActivity.this);
                linlayout.setOrientation(linlayout.VERTICAL);
               int index =  MCAnswers.get(position-SA).size();
                for (int i = 1; i <= index; i++) {
                    RadioButton rdbtn = new RadioButton(StudyActivity.this);
               //     rdbtn.setId(View.generateViewId());
                    rdbtn.setText(MCAnswers.get(position-SA).get(i-1));
                    Log.d("TAG", "Setting Text "+MCAnswers.get(position-SA).get(i-1));
                    linlayout.addView(rdbtn);
                }
                holder.rGroup.addView(linlayout);
                holder.buttonCreated=true;

            }
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
        public RadioGroup rGroup;
        public boolean buttonCreated;
    }

}
