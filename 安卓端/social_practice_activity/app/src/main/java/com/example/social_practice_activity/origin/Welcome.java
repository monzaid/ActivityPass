package com.example.social_practice_activity.origin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.sign.login;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    //使程序休眠2秒
                    sleep(2000);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "init");
                    Intent intent = new Intent(Welcome.this,login.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //关闭当前活动
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}