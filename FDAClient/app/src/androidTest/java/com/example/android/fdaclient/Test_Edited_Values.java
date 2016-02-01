package com.example.android.fdaclient;

/**
 * Created by Vikingprime on 2/1/2016.
 */
import android.widget.TextView;

import com.robotium.solo.Solo;

import junit.framework.Assert;

public class Test_Edited_Values extends Login_Test {
    public void testRun() {
        solo.clickOnView(solo.getView(R.id.loginButton));
        TextView textView = (TextView) solo.getView(R.id.wrongLogin);
        Assert.assertTrue("Test failed, message did not print",
                textView.getText().toString().equals("Incorrect Login"));


        solo.enterText((android.widget.EditText)
                solo.getView(R.id.username), "Vikingprime");
        solo.enterText((android.widget.EditText)
                solo.getView(R.id.pass), "CompSci");
        solo.clickOnView(solo.getView(R.id.loginButton));

        Assert.assertTrue("Test failed: Activity did not load",
                solo.waitForActivity(StudyActivity.class));

        // Rotate the screen back to portrait.
        solo.setActivityOrientation(Solo.PORTRAIT);

        // Give the rotation time to settle.
        solo.sleep(shortDelay);

        // Wait for activity
        Assert.assertTrue("Test failed: Activity did not",
                solo.waitForActivity(StudyActivity.class));
    }
}
