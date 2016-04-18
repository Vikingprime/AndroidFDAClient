package com.example.android.fdaclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements JSONParser{
    Button Login;
    TextView newUser;
    EditText e1;
    EditText e2;
    TextView error;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Login = (Button) findViewById(R.id.loginButton);
        newUser = (TextView) findViewById(R.id.createUser);
        e1 = (EditText) findViewById(R.id.username);
        e2 = (EditText) findViewById(R.id.pass);
        error = (TextView) findViewById(R.id.wrongLogin);
        e1.setText("");
        e2.setText("");

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

                intent = new Intent(context,StudyActivity.class);
                String email = e1.getText().toString();
                String pass = e2.getText().toString();
                intent.putExtra("email",email);
                intent.putExtra("password", pass);
                new ValidationAsyncTask(StudyActivity.url,email,pass,(JSONParser)context).execute();
            }
        };
    }

    public void isValid(Boolean valid) {
        if(valid)
            startActivity(intent);

        else {
            notValid();
        }
    }

    public void notValid(){
        intent = null;
        error.setText(R.string.login_error);
    }




    @Override
    public void parse(JSONObject object,String action) {
        JSONArray surveys=null;
        boolean loggedIn = false;
        try {
            surveys = object.getJSONArray("survey");
            String logged = object.getString("login");
            if(logged!=null){
                loggedIn = Boolean.parseBoolean(logged);
            }
            intent.putExtra("JSONString",object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

       isValid(loggedIn);


    }

    public static void makeToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
