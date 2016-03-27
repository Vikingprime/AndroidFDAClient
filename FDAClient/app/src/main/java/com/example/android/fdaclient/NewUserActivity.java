package com.example.android.fdaclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewUserActivity extends Activity implements ValidityCheck{

    Button mSignup;
    EditText e1;
    EditText e2;
    EditText e3;
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        mSignup = (Button) findViewById(R.id.SignUpButton);
        e1 = (EditText) findViewById(R.id.Email);
        e2 = (EditText) findViewById(R.id.Password);
        e3 = (EditText) findViewById(R.id.Name);
        error = (TextView) findViewById(R.id.badSignUp);

        mSignup.setOnClickListener(makeSignUpOnClickListener(this));
    }

    private View.OnClickListener makeSignUpOnClickListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String pass = e2.getText().toString();
                String name = e3.getText().toString();

                new SignUpAsyncTask(StudyActivity.url,email,pass,name,(ValidityCheck)context).execute();
            }
        };
    }

    @Override
    public void isValid(Boolean valid) {
        if(valid)
            startActivity(new Intent(this, StudyActivity.class));

        else {
            notValid();
        }
    }

    @Override
    public void notValid(){
        error.setText(R.string.signup_error);
    }
}
