package com.example.android.fdaclient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vikingprime on 3/27/2016.
 */
public class SurveyAdapter extends BaseAdapter {
    public static class ViewHolder {
        public TextView textView;
    }

    private ArrayList<Survey> mSurvey;
    private LayoutInflater mInflater;

    public SurveyAdapter(ArrayList<Survey> surveys, LayoutInflater inflater) {
        this.mSurvey = surveys;
        this.mInflater = inflater;
    }

    public void setSurvey(ArrayList<Survey> surveys){
        mSurvey = surveys;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSurvey.size();
    }

    @Override
    public Object getItem(int position) {
        return mSurvey.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.survey_listview, null);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(mSurvey.get(position).getSurveyName());
        return convertView;
    }
}
