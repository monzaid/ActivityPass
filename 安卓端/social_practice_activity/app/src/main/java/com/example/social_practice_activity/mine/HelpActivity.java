package com.example.social_practice_activity.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.bean.mySettings;
import com.example.social_practice_activity.myAdaper.SettingsAdaper;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {
    private ListView listView;
    private String[] help;
    private List<com.example.social_practice_activity.bean.mySettings> mySettings = new ArrayList<>();
    private TextView tv_title;
    private ImageView ib_title_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        ib_title_back = findViewById(R.id.ib_title_back);
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView = findViewById(R.id.u);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("帮助与反馈");
        help = getResources().getStringArray(R.array.help);
        for (String h : help){
            mySettings.add(new mySettings(h, null));
        }
        SettingsAdaper adaper = new SettingsAdaper(mySettings);
        listView.setAdapter(adaper);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mySettings settings = (mySettings) adapterView.getItemAtPosition(position);
                String action = settings.name;
                if (action.equals("如何参加")){
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "如何参加");
                    Intent intent = new Intent(HelpActivity.this, SaveActivity.class);///////////////////////
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if (action.equals("如何发布")){
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "如何发布");
                    Intent intent = new Intent(HelpActivity.this, SaveActivity.class);///////////////////////
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if (action.equals("如何签到")){
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "如何签到");
                    Intent intent = new Intent(HelpActivity.this, SaveActivity.class);///////////////////////
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if (action.equals("如何管理")){
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "如何管理");
                    Intent intent = new Intent(HelpActivity.this, SaveActivity.class);///////////////////////
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if (action.equals("反馈")){
                    Intent intent = new Intent(HelpActivity.this, SaveActivity.class);///////////////////////
                    startActivity(intent);
                }
            }
        });
    }
}