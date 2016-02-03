package com.example.android.fdaclient;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import junit.framework.TestCase;

/**
 * Created by Vikingprime on 2/1/2016.
 */
public class Login_Test extends ActivityInstrumentationTestCase2<LoginActivity> {

    /**
     * Some useful delay/timer values.
     */
    public static int MILLISECOND = 500;
    public static int shortDelay = 2 * MILLISECOND;
    public static int mediumDelay = 5 * MILLISECOND;
    public static int longDelay = 10 * MILLISECOND;
    public static int veryLongDelay = 30 * MILLISECOND;
    protected Solo solo;

    public Login_Test() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp()throws Exception {
        super.setUp();
        solo=new Solo(getInstrumentation(),getActivity());
    }

    public void testScreen() {
    }

    @Override
    protected void tearDown()throws Exception {
        solo.finishOpenedActivities();
    }
}