package com.example.android.fdaclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    public static String url = "http://ec2-52-90-83-176.compute-1.amazonaws.com:3000";
    private Button submitButton;
    private  String lastClickedSurveyID= null;
    public static String QUESTION_ACTION = "question";
    public static String SURVEY_ACTION = "survey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "ONCREATE AFTER HARDCODE");
        setContentView(R.layout.activity_study);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        JSONObject object=null;
        String json = getIntent().getStringExtra("JSONString");
        try {
            if(json!=null)
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mListView = (ListView) findViewById(R.id.survey_list);
        submitButton = (Button) findViewById(R.id.SubmitButton);
        submitButton.setOnClickListener(makeOnClickListener());
        submitButton.setVisibility(View.GONE);
        if(object!=null)
        initializeSurveyFields(object);

    }

    private View.OnClickListener makeOnClickListener() {
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Log.d("TrySend","Will try sending");
                boolean valid = true;
                JSONArray answers = new JSONArray();
                String json;
                for(int i=0;i<mQuestionAdapter.getCount();i++) {
                    String answer = null;
                    Log.d("TrySend","Question Type is: "+((Question) mQuestionAdapter.getItem(i)).getType());
                    if (((Question) mQuestionAdapter.getItem(i)).getType().equals("Txt")) {
                        answer= ((EditText)
                                (getViewByPosition(i,mListView).findViewById(R.id.answer))).getText().toString();
                    }
                    else if(((Question) mQuestionAdapter.getItem(i)).getType().equals("Mc")){
                        RadioGroup rGroup = ((RadioGroup)(getViewByPosition(i,mListView).findViewById(R.id.radiogroup)));
                        int radioButtonID = rGroup.getCheckedRadioButtonId();
                        Log.d("RADIO","RADIOID "+radioButtonID);
                        RadioButton radioButton = (RadioButton) rGroup.findViewById(radioButtonID);
                        if(radioButton!=null)
                        answer = radioButton.getText().toString();
                    }
                    else if(((Question) mQuestionAdapter.getItem(i)).getType().equals("Num")){
                        answer= ((EditText)
                                (getViewByPosition(i,mListView).findViewById(R.id.answer))).getText().toString();
                    }
                    else {
                        valid = false;
                        break;
                    }
                    if(answers==null){
                        valid = false;
                        break;
                    }
                    answers.put(answer);
                }
                if(valid) {
                    sendResults(lastClickedSurveyID, answers);
                    onBackPressed();
                }
                else
                    LoginActivity.makeToast(getBaseContext(),"Make sure to answer all Questions");
            }
        };
    }
    public void sendResults(String id,JSONArray answers){
        new sendResult(url,id,answers,password,email).execute();
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void initializeQuestionFields(ArrayList<Question> questions) {
        mQuestionAdapter = new QuestionAdapter();
        submitButton.setVisibility(View.VISIBLE);
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
                lastClickedSurveyID = ((Survey)(mSurveyAdapter.getItem(position))).getSurveyID();
                makeRequest(lastClickedSurveyID,email,password);
            }
        };
    }

    private void makeRequest(String surveyID,String email,String password){
        new GetJSONAsyncTask(url,email,surveyID,password,this).execute();
    }

    @Override
    public void parse(JSONObject object, String action) {
       if(action.equals(QUESTION_ACTION)){
           getQuestions(object);
       }
        else if(action.equals(SURVEY_ACTION)){
            initializeSurveyFields(object);
       }

    }
    private void getQuestions(JSONObject object){
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

    @Override
    public void onBackPressed()
    {
        if(mQuestionAdapter==null)
        super.onBackPressed();

        else {
            mQuestionAdapter = null;
            submitButton.setVisibility(View.GONE);
            new ValidationAsyncTask(StudyActivity.url,email,password,(JSONParser)this).execute();
        }
    }

    private class QuestionAdapter extends BaseAdapter {

        private static final int TYPE_MAX_COUNT = 3;

        private ArrayList<Question> mQuestions;
        private LayoutInflater mInflater;
        private final int short_Answer_Question = 0;
        private final int multiple_Choice_Question = 1;
        private final int check_box_Question = 2;
        private final int num_Question = 3;


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
           else if(type.equals("Num")){
              return num_Question;
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
                        holder.edit = (EditText) convertView.findViewById(R.id.answer);
                        holder.edit.setInputType(InputType.TYPE_CLASS_TEXT);
                        holder.rGroup = null;
                        break;
                    case multiple_Choice_Question:
                        convertView = mInflater.inflate(R.layout.multiple_choice_listview, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.MCquestion);
                        holder.rGroup = (RadioGroup)convertView.findViewById(R.id.radiogroup);
                        break;
                    //TODO: CHANGE TO ALLOW FOR NUM QUESTIONS
                    case num_Question:
                        convertView = mInflater.inflate(R.layout.short_answer_listview,null);
                        holder.textView = (TextView)convertView.findViewById(R.id.question);
                        holder.edit = (EditText) convertView.findViewById(R.id.answer);
                        holder.edit.setInputType(InputType.TYPE_CLASS_NUMBER);
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
               int index =  mQuestions.get(position).getAnswers().length();
                for (int i = 0; i < index; i++) {
                    RadioButton rdbtn = new RadioButton(StudyActivity.this);
                    try {
                        rdbtn.setText(mQuestions.get(position).getAnswers().getString(i));
                    }catch(Exception e){
                        rdbtn.setText("Null Question");
                    }
                    holder.rGroup.setPadding(0,80,0,0);
                    holder.rGroup.addView(rdbtn);
                }
             //   holder.rGroup.addView(linlayout);
                holder.buttonCreated=true;

            }
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
        public EditText edit;
        public RadioGroup rGroup;
        public boolean buttonCreated;
    }

}
