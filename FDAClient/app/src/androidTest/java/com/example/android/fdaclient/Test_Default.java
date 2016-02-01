package com.example.android.fdaclient;

/**
 * Created by Vikingprime on 2/1/2016.
 */
import com.robotium.solo.Solo;
import junit.framework.Assert;

import static com.example.android.fdaclient.R.*;

public class Test_Default extends Login_Test {
    public void testRun() {
        solo.clickOnView(solo.getView(id.createUser));
        solo.sleep(shortDelay);

        Assert.assertTrue("Test failed: Activity did not load",
                solo.waitForActivity(NewUserActivity.class));

        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(shortDelay);

        Assert.assertTrue("Test failed: Activity did not load",
                solo.waitForActivity(NewUserActivity.class));

        // Rotate the screen back to portrait.
        solo.setActivityOrientation(Solo.PORTRAIT);

        // Give the rotation time to settle.
        solo.sleep(shortDelay);

        // Wait for activity
        Assert.assertTrue("Test failed: Activity did not load",
                solo.waitForActivity(NewUserActivity.class));

        solo.goBackToActivity("LoginActivity");

        Assert.assertTrue("Test failed: Activity did not load",
                solo.waitForActivity(LoginActivity.class));
    }
}