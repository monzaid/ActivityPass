package com.example.social_practice_activity.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.Utils.ActivityCollector;
import com.example.social_practice_activity.Utils.CacheUtil;
import com.example.social_practice_activity.bean.mySettings;
import com.example.social_practice_activity.myAdaper.SettingsAdaper;
import com.example.social_practice_activity.origin.MainActivity;
import com.example.social_practice_activity.sign.login;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private ListView listView;
    private String[] settings;
    private List<com.example.social_practice_activity.bean.mySettings> mySettings = new ArrayList<>();
    private TextView tv_title;
    private AlertDialog alertDialog, alertDialogExit;
    private ImageView ib_title_back;

    private String totalCacheSize;

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
        tv_title.setText("设置");

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("确认要清空缓存吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CacheUtil.clearAllCache(getApplicationContext());
                        totalCacheSize = null;
                        try {
                            totalCacheSize = CacheUtil.getTotalCacheSize(getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mySettings.add(new mySettings(settings[0], totalCacheSize));
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();

        alertDialogExit = new AlertDialog.Builder(this)
                .setTitle("确认要退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "exit");
                        Intent intent = new Intent(SettingsActivity.this, login.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //关闭MainActivity
                        ActivityCollector.finishOneActivity(MainActivity.class.getName());
                        finish();
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();

        settings = getResources().getStringArray(R.array.settings);
        totalCacheSize = null;
        try {
            totalCacheSize = CacheUtil.getTotalCacheSize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mySettings.add(new mySettings(settings[0], totalCacheSize));
        mySettings.add(new mySettings(settings[1], null));
        SettingsAdaper adaper = new SettingsAdaper(mySettings);
        listView.setAdapter(adaper);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mySettings settings = (mySettings) adapterView.getItemAtPosition(position);
                String action = settings.name;
                if (action.equals("清空缓存")){
                    alertDialog.show();
                }else if (action.equals("退出登录")){
                    alertDialogExit.show();
                }
            }
        });
    }
}