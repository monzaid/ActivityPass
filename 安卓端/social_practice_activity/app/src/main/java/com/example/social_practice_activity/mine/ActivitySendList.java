package com.example.social_practice_activity.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.bean.myActivity;
import com.example.social_practice_activity.discover.ActivityDetail;
import com.example.social_practice_activity.myAdaper.UpdataListViewAdaper;
import com.example.social_practice_activity.myView.UpdataListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivitySendList extends AppCompatActivity implements UpdataListView.OnRefreshListener{

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "ActivityRegisterServlet";

    private static final int UPDATE_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int INITDATA_TEXT = 3;
    private static final int REINITDATA_TEXT = 4;
    private static final int UNUPDATE_TEXT = 5;

    private UpdataListView updateListView;
    private List<myActivity> list = new ArrayList<>();
    private List<myActivity> temp = new ArrayList<>();
    private UpdataListViewAdaper adapter;
    private TextView tv_create;
    private TextView tv_tips;
    private ImageView ib_title_back;

    private int page = 1;
    private int MaxPage = 1;
    private int pageSize = 10;

    private String userId;

    private FloatingActionButton fab;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITDATA_TEXT:
                    if (temp.size() == 0){
                        break;
                    }
                    fab.setVisibility(View.VISIBLE);
                    updateListView.setVisibility(View.VISIBLE);
                    tv_tips.setVisibility(View.GONE);
                    list.clear();
                    list.addAll(temp);
                    adapter = new UpdataListViewAdaper(list);
                    updateListView.setAdapter(adapter);
                    updateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            myActivity msg = (myActivity) adapterView.getItemAtPosition(position);
                            Bundle bundle = new Bundle();
                            bundle.putString("id", String.valueOf(msg.id));
                            bundle.putString("userId", userId);
                            Intent intent = new Intent(ActivitySendList.this, ActivityDetail.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    break;
                case REINITDATA_TEXT:
                    list.clear();
                    list.addAll(temp);
                    adapter.notifyDataSetChanged();
                    break;
                case UPDATE_TEXT:
                    list.addAll(temp);
                    adapter.notifyDataSetChanged();
                    break;
                case UNUPDATE_TEXT:
                    Toast.makeText(ActivitySendList.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(ActivitySendList.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_create);


        Bundle bundle = this.getIntent().getExtras();
        userId = bundle.getString("id");

        ib_title_back = findViewById(R.id.ib_title_back);
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        updateListView = findViewById(R.id.u);
        updateListView.setOnRefreshListener(this);
        tv_create = findViewById(R.id.create);
        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", userId);
                Intent intent = new Intent(ActivitySendList.this, CreateActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tv_tips = findViewById(R.id.tv_tips);
        tv_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity(INITDATA_TEXT);
            }
        });
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= 8) {
                    updateListView.smoothScrollToPosition(0);
                } else {
                    updateListView.setSelection(0);
                }
            }
        });
        getActivity(INITDATA_TEXT);
    }

    private void getActivity(int msg) {
        if (msg == UPDATE_TEXT){
            if (MaxPage == page){
                mHandler.obtainMessage(UNUPDATE_TEXT).sendToTarget();
                return;
            }
            page++;
        }else {
            page = 1;
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "pageBySenderId")
                .add("userId", userId)
                .add("pageNo", String.valueOf(page))
                .add("pageSize", String.valueOf(pageSize))
                .build();
        final Request request = new Request.Builder()
                .url(Url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(FAIL_TEXT).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    temp.clear();
                    JSONObject jo1 = new JSONObject(response.body().string());
                    JSONArray jsonArray = jo1.getJSONArray("data");
                    MaxPage = jo1.getInt("pageCount");
                    myActivity news = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int id = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                        String imageUrl = jsonArray.getJSONObject(i).getString("image");
                        String title = jsonArray.getJSONObject(i).getString("title");
                        String introduction = jsonArray.getJSONObject(i).getString("introduction");
                        String place = jsonArray.getJSONObject(i).getString("place");
                        Timestamp start_time = Timestamp.valueOf(jsonArray.getJSONObject(i).getString("start_time"));
                        int people = Integer.parseInt(jsonArray.getJSONObject(i).getString("people"));
                        news = new myActivity(id, title, introduction, imageUrl, place, people, start_time);
                        temp.add(news);
                        System.out.println("news1: " + temp.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage(msg).sendToTarget();
            }
        });
    }

    @Override
    public void onPullRefresh() {
        // TODO Auto-generated method stub
        updateListView.setEnabled(false);
        //handler设置刷新延时效果
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取最新数据
                getActivity(REINITDATA_TEXT);
                //通知listView刷新数据完毕
                updateListView.completeRefresh();
                updateListView.setEnabled(true);
            }
        }, 2000);
    }

    @Override
    public void onLoadingMore() {
        // TODO Auto-generated method stub
        //handler设置刷新延时效果
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取最新数据
                getActivity(UPDATE_TEXT);
                //通知listView刷新数据完毕
                updateListView.completeRefresh();
            }
        }, 2000);
    }
}