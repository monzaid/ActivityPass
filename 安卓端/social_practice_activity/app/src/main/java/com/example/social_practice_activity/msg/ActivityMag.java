package com.example.social_practice_activity.msg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.myView.UpdataListView;

public class ActivityMag extends AppCompatActivity implements UpdataListView.OnRefreshListener{
    private UpdataListView updataListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_mag);
        updataListView = findViewById(R.id.u);
        updataListView.setOnRefreshListener(this);
    }

    @Override
    public void onPullRefresh() {

    }

    @Override
    public void onLoadingMore() {

    }
}