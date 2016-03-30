package com.example.android.fdaclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudyActivity extends AppCompatActivity implements JSONParser{
    private JSONObject json = new JSONObject();

    ListView mListView;
    QuestionAdapter mQuestionAdapter;
    SurveyAdapter mSurveyAdapter;
    private String email;
    private String password;
    public static String url = "http://ec2-54-165-195-77.compute-1.amazonaws.com:3000";
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "ONCREATE AFTER HARDCODE");
        setContentView(R.layout.activity_study);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        JSONObject object=null;
        try {
            object = new JSONObject(getIntent().getStringExtra("JSONString"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mListView = (ListView) findViewById(R.id.survey_list);
        submitButton = (Button) findViewById(R.id.SignUpButton);
        submitButton.setOnClickListener(makeOnClickListener());
        initializeSurveyFields(object);

    }

    private View.OnClickListener makeOnClickListener() {
        return View.OnClickListener;
    }

    private void initializeQuestionFields(ArrayList<Question> questions) {
        mQuestionAdapter = new QuestionAdapter();
        mQuestionAdapter.setQuestions(questions);
        mListView.setAdapter(mQuestionAdapter);
    }

    private void initializeSurveyFields(JSONObject json){
        ArrayList<Survey> surveyList = new ArrayList<Survey>();
        try {
            JSONArray surveys = json.getJSONArray("survey");
            Log.d("ARRAY",surveys.toString());

            for (int i = 0; i < surveys.length(); i++) {
                String name = ((JSONObject) surveys.get(i)).getString("name");
                String id = ((JSONObject) surveys.get(i)).getString("_id");
                Survey survey = new Survey(id,name);
                surveyList.add(survey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSurveyAdapter = new SurveyAdapter(surveyList,(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        mListView.setAdapter(mSurveyAdapter);
        mListView.setOnItemClickListener(makeOnItemClickListener());

    }

    private AdapterView.OnItemClickListener makeOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makeRequest(((Survey)(mSurveyAdapter.getItem(position))).getSurveyID(),email,password);
            }
        };
    }

    private void makeRequest(String surveyID,String email,String password){
        new GetJSONAsyncTask(url,email,surveyID,password,this).execute();

    }

    @Override
    public void parse(JSONObject object) {
        ArrayList<Question> questionList = new ArrayList<Question>();
        try {
            JSONArray Questions = object.getJSONArray("questions");
            Log.d("ARRAY",Questions.toString());

            for (int i = 0; i < Questions.length(); i++) {
                String prompt = ((JSONObject) Questions.get(i)).getString("prompt");
                String type = ((JSONObject) Questions.get(i)).getString("type");
                JSONArray answers = ((JSONObject) Questions.get(i)).getJSONArray("options");
                Question question = new Question(type, prompt, answers);
                questionList.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initializeQuestionFields(questionList);

    }

    private class QuestionAdapter extends BaseAdapter {

        private static final int TYPE_MAX_COUNT = 3;

        private ArrayList<Question> mQuestions;
        private LayoutInflater mInflater;
        private final int short_Answer_Question = 0;
        private final int multiple_Choice_Question = 1;
        private final int check_box_Question = 2;


        public QuestionAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public void setQuestions(ArrayList<Question> questions){
            mQuestions = questions;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            String type = getItem(position).getType();
          if(type.equals("Txt")){
                return short_Answer_Question;
            }
            else if(type.equals("Mc")){
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
                    default:
                        convertView = mInflater.inflate(R.layout.short_answer_listview, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.question);
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
                        rdbtn.setText("Null Question");
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
