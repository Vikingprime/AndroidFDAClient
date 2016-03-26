package com.example.android.fdaclient;

import org.json.JSONArray;

/**
 * Created by Vikingprime on 3/26/2016.
 */
public class Question {
    private String type;
    private String prompt;
    private JSONArray answers;
    private JSONParser mParser;

    public Question(String type, String prompt, JSONArray answers) {
        this.type = type;
        this.prompt = prompt;
        this.answers = answers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public JSONArray getAnswers() {
        return answers;
    }

    public void setAnswers(JSONArray answers) {
        this.answers = answers;
    }
}
