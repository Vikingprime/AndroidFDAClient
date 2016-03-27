package com.example.android.fdaclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends Activity implements JSONParser{
    Button Login;
    TextView newUser;
    EditText e1;
    EditText e2;
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Login = (Button) findViewById(R.id.loginButton);
        newUser = (TextView) findViewById(R.id.createUser);
        e1 = (EditText) findViewById(R.id.username);
        e2 = (EditText) findViewById(R.id.pass);
        error = (TextView) findViewById(R.id.wrongLogin);

        Login.setOnClickListener(makeLoginOnClickListener(this));
        newUser.setOnClickListener(makeNewUserOnClickListener(this));

    }

    private View.OnClickListener makeNewUserOnClickListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, NewUserActivity.class));
            }
        };
    }

    private View.OnClickListener makeLoginOnClickListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String pass = e2.getText().toString();

                new LoginAsyncTask(StudyActivity.url,email,pass,(JSONParser)context).execute();
            }
        };
    }

    public void isValid(Boolean valid) {
        if(valid)
            startActivity(new Intent (this, StudyActivity.class));

        else {
            notValid();
        }
    }

    public void notValid(){
        error.setText(R.string.login_error);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void parse(JSONObject object) {
        ArrayList<Question> surveyList = new ArrayList<Question>();
        try {
            JSONArray Surveys = object.getJSONArray("survey");
            if(Surveys.length()==0){
                Log.d("SURVEYS",Surveys.toString());
                isValid(false);
            }
            else {
                isValid(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
