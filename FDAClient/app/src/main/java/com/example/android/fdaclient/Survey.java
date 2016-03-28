package com.example.android.fdaclient;

/**
 * Created by Vikingprime on 3/27/2016.
 */
public class Survey {
    private String surveyID;
    private String surveyName;

    public Survey(String surveyID, String surveyName) {
        this.surveyID = surveyID;
        this.surveyName = surveyName;
    }

    public String getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(String surveyID) {
        this.surveyID = surveyID;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }
}
