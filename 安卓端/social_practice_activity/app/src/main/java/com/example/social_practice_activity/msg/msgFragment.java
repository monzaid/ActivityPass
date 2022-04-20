package com.example.social_practice_activity.msg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.bean.myMsg;
import com.example.social_practice_activity.discover.ActivityDetail;
import com.example.social_practice_activity.myAdaper.msgAdaper;
import com.example.social_practice_activity.myBaseClass.BaseFragment;
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

public class msgFragment extends BaseFragment implements UpdataListView.OnRefreshListener{

    private static final String TAG =
            msgFragment.class.getSimpleName();
    View root;
    private UpdataListView updateListView;
    private TextView tv_activityMag;
    private TextView tv_informMag;

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "MsgServlet";

    private static final int UPDATE_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int INITDATA_TEXT = 3;
    private static final int REINITDATA_TEXT = 4;
    private static final int UNUPDATE_TEXT = 5;
    private List<myMsg> list = new ArrayList<>();
    private List<myMsg> temp = new ArrayList<>();
    private msgAdaper adapter;
    private TextView tv_tips;

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
                    adapter = new msgAdaper(list);
                    updateListView.setAdapter(adapter);
                    updateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            myMsg msg = (myMsg) adapterView.getItemAtPosition(position);
                            Bundle bundle = new Bundle();
                            bundle.putString("id", String.valueOf(msg.t_a_id));
                            bundle.putString("userId", userId);
                            Intent intent = new Intent(root.getContext(), ActivityDetail.class);
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
                    Toast.makeText(root.getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(root.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public View initView() {
        Log.e(TAG,"主页视图被3425324初始化了");
        root = View.inflate(mContext, R.layout.fragment_msg,null);
//        tv_activityMag = root.findViewById(R.id.tv_activity);

        Bundle bundle = this.getArguments();
        userId = new String(bundle.getString("id"));

//        tv_activityMag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(root.getContext(), ActivityMag.class);
//                startActivity(intent);
//            }
//        });
//        tv_informMag = root.findViewById(R.id.tv_inform);
//        tv_informMag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(root.getContext(), InformMag.class);
//                startActivity(intent);
//            }
//        });
        updateListView = root.findViewById(R.id.u);
        updateListView.setOnRefreshListener(this);
        tv_tips = root.findViewById(R.id.tv_tips);
        tv_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDatas(INITDATA_TEXT);
            }
        });
        fab = root.findViewById(R.id.fab);
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

        initDatas(INITDATA_TEXT);
        return root;
    }
    @Override
    public void initData() {
        super.initData();
        Log.e(TAG,"主页数据被初始化了");
    }

    private void initDatas(int msg) {
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
                .add("id", userId)
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
                    System.out.println(jo1.toString());
                    JSONArray jsonArray = jo1.getJSONArray("data");
                    MaxPage = jo1.getInt("pageCount");
                    myMsg news = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int id = Integer.parseInt(jsonArray.getJSONObject(i).getString("t_a_id"));
                        int type = Integer.parseInt(jsonArray.getJSONObject(i).getString("type"));
                        String title = jsonArray.getJSONObject(i).getString("title");
                        Timestamp time = Timestamp.valueOf(jsonArray.getJSONObject(i).getString("time"));
                        String content = null;
                        if (type == 1){
                            content = new String("有用户参加活动");
                        }else if (type == 2){
                            content = new String("审核通过");
                        }else if (type == -1){
                            content = new String("活动已被发布者删除");
                        }else if (type == -2){
                            content = new String("活动有新的修改");
                        }else if (type == 0){
                            content = new String("审核不通过");
                        }

                        news = new myMsg(id, userId, type, time, title, content);
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
                initDatas(REINITDATA_TEXT);
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
                initDatas(UPDATE_TEXT);
                //通知listView刷新数据完毕
                updateListView.completeRefresh();
            }
        }, 2000);
    }
}