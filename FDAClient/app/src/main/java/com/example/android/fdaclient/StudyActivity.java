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

public class StudyActivity extends AppCompatActivity implements JSONParser{
    private JSONObject json = new JSONObject();
    ListView mListView;
    SurveyAdapter mSurveyAdapter;
    String url = "http://ec2-52-207-254-157.compute-1.amazonaws.com:3000";
    String email = "janeDoe@gmail.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        hardCodeJSON();
        makeRequest();

        }

    private void initializeViewFields(ArrayList<Question> questions) {
        mListView = (ListView) findViewById(R.id.survey_list);
        mSurveyAdapter = new SurveyAdapter();
        mSurveyAdapter.setQuestions(questions);
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
        new GetJSONAsyncTask(url,email,this).execute();

    }

    @Override
    public void parse(JSONObject object) {
        ArrayList<Question> questionList = new ArrayList<Question>();
        Log.d("TAG", "ONCREATE AFTER HARDCODE");
        try {
            JSONArray Questions = object.getJSONArray("questions");

            for (int i = 0; i < Questions.length(); i++) {
                String prompt = ((JSONObject) Questions.get(i)).getString("prompt");
                String type = ((JSONObject) Questions.get(i)).getString("type");
                JSONArray answers = ((JSONObject) Questions.get(i)).getJSONArray("option");
                Question question = new Question(type, prompt, answers);
                questionList.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initializeViewFields(questionList);

    }

    private class SurveyAdapter extends BaseAdapter {

        private static final int TYPE_MAX_COUNT = 3;

        private ArrayList<Question> mQuestions;
        private LayoutInflater mInflater;
        private final int short_Answer_Question = 0;
        private final int multiple_Choice_Question = 1;
        private final int check_box_Question = 2;


        public SurveyAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public void setQuestions(ArrayList<Question> questions){
            mQuestions = questions;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            String type = getItem(position).getType();
          if(type.equals("txt")){
                return short_Answer_Question;
            }
            else if(type.equals("MC")){
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
        public Question getItem(int position) {
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
            holder.textView.setText(mQuestions.get(position).getPrompt());
            if(holder.rGroup!=null && holder.buttonCreated==false){
                LinearLayout linlayout = new LinearLayout(StudyActivity.this);
                linlayout.setOrientation(linlayout.VERTICAL);
               int index =  mQuestions.get(position).getAnswers().length();
                for (int i = 0; i < index; i++) {
                    RadioButton rdbtn = new RadioButton(StudyActivity.this);
               //     rdbtn.setId(View.generateViewId());
                    try {
                        rdbtn.setText(mQuestions.get(position).getAnswers().getString(i));
                    }catch(Exception e){
                        rdbtn.setText("Null question");
                    }
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
