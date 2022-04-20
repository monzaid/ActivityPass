package com.example.social_practice_activity.origin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.Utils.ActivityCollector;
import com.example.social_practice_activity.Utils.NotificationUtils;
import com.example.social_practice_activity.bean.myActivity;
import com.example.social_practice_activity.discover.ActivityDetail;
import com.example.social_practice_activity.discover.SearchActivity;
import com.example.social_practice_activity.discover.discoverFragment;
import com.example.social_practice_activity.mine.mineFragment;
import com.example.social_practice_activity.msg.msgFragment;
import com.example.social_practice_activity.myBaseClass.BaseFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "ActivityRegisterServlet";

    private int position;
    private List<BaseFragment> fragments;
    private BaseFragment tempFragemnt;
    
    private RadioButton rb_discover;
    private RadioButton rb_msg;
    private RadioButton rb_mine;
    private RadioGroup rg_main;
    private ImageView iv_search;

    private String username;
    private String id;
    private String imageUrl;

    private String showTitle;
    private int activityId;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    System.out.println("ccc");
                    Log.i("print","执行了");
                    //需要执行的代码放这里
                    NotificationUtils notificationUtils = new NotificationUtils(MainActivity.this);
                    notificationUtils.sendNotification(showTitle, "你参加的活动即将开始签到", toMainActivity());
                    break;
            }
        }
    };

    private PendingIntent toMainActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("id", String.valueOf(activityId));
        bundle.putString("userId", id);
        //点击广播监听
        Intent intentClick = null;
        intentClick = new Intent(this, ActivityDetail.class);
        intentClick.putExtras(bundle);
        //如果跳转的activity 是一个普通的设置FLAG_ACTIVITY_NEW_TASK即可，
        //如果是一个singletask activity 则需要添加FLAG_ACTIVITY_CLEAR_TOP ，否则启动可能失败
        intentClick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentClick.addCategory("android.intent.category.LAUNCHER");
        intentClick.setAction("android.intent.action.MAIN");
        return PendingIntent.getActivity(this, 0, intentClick, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCollector.addActivity(this);

        Bundle bundle = this.getIntent().getExtras();
        username = bundle.getString("username");
        id = bundle.getString("id");
        imageUrl = bundle.getString("imageUrl");
        showTitle = new String("活动通");

        rb_discover = findViewById(R.id.rb_discover);
        rb_msg = findViewById(R.id.rb_msg);
        rb_mine = findViewById(R.id.rb_mine);
        rg_main = findViewById(R.id.rg_main);
        iv_search = findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        initFragment();

        initListener();

        initActivityRegister();
    }

    private void initActivityRegister() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "getTime")
                .add("userId", id)
                .build();
        final Request request = new Request.Builder()
                .url(Url)
                .post(requestBody)
                .build();
        System.out.println(Url);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jo1 = new JSONObject(response.body().string());
                    System.out.println(jo1);
                    JSONArray jsonArray = jo1.getJSONArray("data");
                    myActivity news = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Timestamp start_time = Timestamp.valueOf(jsonArray.getJSONObject(i).getString("start_time"));
                        Timestamp end_time = Timestamp.valueOf(jsonArray.getJSONObject(i).getString("end_time"));
                        String title = new String(jsonArray.getJSONObject(i).getString("title"));
                        int activityIds = jsonArray.getJSONObject(i).getInt("activityId");
                        System.out.println(start_time + " " + end_time);
                        if (start_time.getTime() - 30 * 60 * 1000 <= System.currentTimeMillis() && start_time.getTime() + 10 * 60 * 1000 >= System.currentTimeMillis()){
                            System.out.println("a");
                            initTimer(System.currentTimeMillis() + 10000, title, activityIds);
                        }else if (start_time.getTime() - 30 * 60 * 1000 >= System.currentTimeMillis()){
                            System.out.println("aa");
                            initTimer(start_time.getTime() - 30 * 60 * 1000, title, activityIds);

                        }
                        if (end_time.getTime() - 30 * 60 * 1000 <= System.currentTimeMillis() && end_time.getTime() + 10 * 60 * 1000 >= System.currentTimeMillis()){
                            System.out.println("aaa");
                            initTimer(System.currentTimeMillis() + 10000, title, activityIds);
                        }else if (end_time.getTime() - 30 * 60 * 1000 >= System.currentTimeMillis()){
                            System.out.println("aaaa");
                            initTimer(end_time.getTime() - 30 * 60 * 1000, title, activityIds);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTimer(long timestamp, String title, int activityIds) {
        System.out.println("bbb");
        TimerTask task = new TimerTask() {
         @Override
            public void run() {
             System.out.println("vvvvv");
             showTitle = new String(title);
             activityId = activityIds;
             handler.sendEmptyMessage(1);
             System.out.println("qqqq");
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task, new Date(timestamp));
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        final discoverFragment discoverFragment = new discoverFragment();
        Bundle discoverFragmentBundle = new Bundle();
        discoverFragmentBundle.putString("id", id);
        discoverFragment.setArguments(discoverFragmentBundle);
        final msgFragment msgFragment = new msgFragment();
        Bundle msgFragmentBundle = new Bundle();
        msgFragmentBundle.putString("id", id);
        msgFragment.setArguments(msgFragmentBundle);
        final mineFragment mineFragment = new mineFragment();
        Bundle mineFragmentBundle = new Bundle();
        mineFragmentBundle.putString("id", id);
        mineFragmentBundle.putString("username", username);
        mineFragmentBundle.putString("imageUrl", imageUrl);
        mineFragment.setArguments(mineFragmentBundle);
        fragments.add(discoverFragment);
        fragments.add(msgFragment);
        fragments.add(mineFragment);
    }

    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(RadioGroup group, int checkedId) {
                      switch (checkedId) {
                          case R.id.rb_discover:
                              position = 0;
                              iv_search.setVisibility(View.VISIBLE);
                              break;
                          case R.id.rb_msg:
                              position = 1;
                              iv_search.setVisibility(View.INVISIBLE);
                              break;
                          case R.id.rb_mine:
                              position = 2;
                              iv_search.setVisibility(View.INVISIBLE);
                              break;
                      }
                      BaseFragment baseFragment = getFragment(position);
                      switchFragment(tempFragemnt, baseFragment);
                  }
              });
        //默认设置首页
        rg_main.check(R.id.rb_discover);
    }

    private BaseFragment getFragment(int position) {
        if (fragments != null && fragments.size() > 0) {
            BaseFragment baseFragment = fragments.get(position);
            return baseFragment;
        }
        return null;
    }

    private void switchFragment(Fragment fromFragment, BaseFragment
            nextFragment) {
        if (tempFragemnt != nextFragment) {
            tempFragemnt = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction =
                        getSupportFragmentManager().beginTransaction();
                //判断 nextFragment 是否添加
                if (!nextFragment.isAdded()) {
                    //隐藏当前 Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.add(R.id.frameLayout, nextFragment).commit();
                } else {
                    //隐藏当前 Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(nextFragment).commit();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //从活动管理器删除当前Activity
        ActivityCollector.removeActivity(this);
    }

}