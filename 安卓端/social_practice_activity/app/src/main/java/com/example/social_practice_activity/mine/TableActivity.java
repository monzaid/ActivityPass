package com.example.social_practice_activity.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.bean.myActivityRegister;
import com.example.social_practice_activity.myAdaper.RecordAdaper;
import com.example.social_practice_activity.myView.UpdataListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TableActivity extends AppCompatActivity implements UpdataListView.OnRefreshListener, RecordAdaper.InnerItemOnclickListener {
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "ActivityRegisterServlet";

    private static final int UPDATE_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int INITDATA_TEXT = 3;
    private static final int REINITDATA_TEXT = 4;
    private static final int UNUPDATE_TEXT = 5;
    private static final int UPDATESTUTE_TEXT = 7;
    private static final int UNUPDATESTUTE_TEXT = 8;
    private static final int UNUPDATEJOIN_TEXT = 9;
    private static final int UPDATEDELETE_TEXT = 11;

    private UpdataListView updateListView;
    private List<myActivityRegister> list = new ArrayList<>();
    private List<myActivityRegister> temp = new ArrayList<>();
    private RecordAdaper adapter;
    private AlertDialog alertDialog, alertDialogAC;
    private myActivityRegister tempMyActivityRegister;
    private TextView tv_tips;
    private ImageView ib_title_back;
    private Spinner s_type;
    private Button btn_ac;
    private FloatingActionButton fab;
    private int pos;

    private int page = 1;
    private int MaxPage = 1;
    private int pageSize = 99999;

    private String userId;
    private int ActivityId;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITDATA_TEXT:
                    if (temp.size() == 0){
                        Toast.makeText(TableActivity.this, "没有查询结果", Toast.LENGTH_SHORT).show();
                    }
//                    if (list.size() == 0){
//                        fab.setVisibility(View.GONE);
//                        break;
//                    }
                    list.clear();
                    list.addAll(temp);
                    System.out.println(list.size());
                    fab.setVisibility(View.VISIBLE);
                    updateListView.setVisibility(View.VISIBLE);
                    tv_tips.setVisibility(View.GONE);
                    adapter = new RecordAdaper(list);
                    adapter.setOnInnerItemOnClickListener(TableActivity.this);
                    System.out.println(list);
                    updateListView.setAdapter(adapter);
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
                    Toast.makeText(TableActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATESTUTE_TEXT:
                    adapter.notifyDataSetChanged();
                    break;
                case UNUPDATESTUTE_TEXT:
                    Toast.makeText(TableActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATEDELETE_TEXT:
                    adapter.notifyDataSetChanged();
                    Toast.makeText(TableActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
                case UNUPDATEJOIN_TEXT:
                    Toast.makeText(TableActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(TableActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_table);
        ib_title_back = findViewById(R.id.ib_title_back);
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        updateListView = findViewById(R.id.u);
        updateListView.setOnRefreshListener(this);
        tv_tips = findViewById(R.id.tv_tips);
        tv_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDatas(INITDATA_TEXT);
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        userId = new String(bundle.getString("userId"));
        ActivityId = Integer.parseInt(bundle.getString("ActivityId"));


        alertDialog = new AlertDialog.Builder(this)
                .setTitle("确认要拒绝这个人参加活动吗？")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(tempMyActivityRegister, pos);
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        alertDialogAC = new AlertDialog.Builder(this)
                .setTitle("确认要一键审核吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < list.size(); j++){
                            myActivityRegister ar = list.get(j);
                            if (ar.stute.equals("待审核")){
                                ar.register = 2;
                                update(ar, j);
                            }
                        }
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        s_type = findViewById(R.id.s_type);
        s_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    return;
                String SpItemSelected = s_type.getSelectedItem().toString();
                System.out.println("---" + SpItemSelected);
//                toGetNext(SpItemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        btn_ac = findViewById(R.id.btn_ac);
        btn_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAC.show();
            }
        });
        initDatas(INITDATA_TEXT);

    }



    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        myActivityRegister myActivityRegister = list.get(position);
        switch (v.getId()) {
            case R.id.btn_ac:
                if (myActivityRegister.register == 1){
                    myActivityRegister.register = 2;
                    update(myActivityRegister, position);
                }
                break;
            case R.id.btn_no:
                alertDialog.show();
                tempMyActivityRegister = myActivityRegister;
                pos = position;
                break;
            case R.id.btn_sign:
                if (!myActivityRegister.sign_in_face || !myActivityRegister.sign_in_pos){
                    myActivityRegister.sign_in_face = true;
                    myActivityRegister.sign_in_pos = true;
                    myActivityRegister.register++;
                }else if (!myActivityRegister.sign_out_pos || !myActivityRegister.sign_out_face){
                    myActivityRegister.sign_out_pos = true;
                    myActivityRegister.sign_out_face = true;
                    myActivityRegister.register++;
                }else {
                    return;
                }
                update(myActivityRegister, position);
                break;
            case R.id.btn_call:
                Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + myActivityRegister.telephone));//跳转到拨号界面，同时传递电话号码
                startActivity(dialIntent);
                break;
            default:
                break;
        }

    }

    private void update(myActivityRegister myActivityRegister, int pos) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "update")
                .add("t_a_id", String.valueOf(ActivityId))
                .add("t_u_id", myActivityRegister.userId)
                .add("register", String.valueOf(myActivityRegister.register))
                .add("sign_in_pos", String.valueOf(myActivityRegister.sign_in_pos))
                .add("sign_in_face", String.valueOf(myActivityRegister.sign_in_face))
                .add("sign_in_2", String.valueOf(myActivityRegister.sign_in_2))
                .add("sign_out_pos", String.valueOf(myActivityRegister.sign_out_pos))
                .add("sign_out_face", String.valueOf(myActivityRegister.sign_out_face))
                .add("sing_out_2", String.valueOf(myActivityRegister.sing_out_2))
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
                JSONObject jo1 = null;
                try {
                    jo1 = new JSONObject(response.body().string());
//                    System.out.println(jo1.toString());
                    if (jo1.getString("ac").equals("false")){
                        mHandler.obtainMessage(UNUPDATESTUTE_TEXT).sendToTarget();
                        return;
                    }
                    list.get(pos).register = myActivityRegister.register;
                    list.get(pos).sign_out_pos = myActivityRegister.sign_out_pos;
                    list.get(pos).sign_in_pos = myActivityRegister.sign_in_pos;
                    list.get(pos).sign_out_face = myActivityRegister.sign_out_face;
                    list.get(pos).sign_in_face = myActivityRegister.sign_in_face;
                    if (myActivityRegister.register == 2){
                        if (!myActivityRegister.sign_in_face || !myActivityRegister.sign_in_pos) {
                            list.get(pos).stute = "待签到";
                        }else if (!myActivityRegister.sign_out_face || !myActivityRegister.sign_out_pos){
                            list.get(pos).stute = "待签退";
                        }else {
                            list.get(pos).stute = "已完成";
                        }
                    }
                    mHandler.obtainMessage(UPDATESTUTE_TEXT).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void delete(myActivityRegister myActivityRegister, int pos) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "delete")
                .add("activityId", String.valueOf(ActivityId))
                .add("userId", myActivityRegister.userId)
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
                JSONObject jo1 = null;
                try {
                    jo1 = new JSONObject(response.body().string());
//                    System.out.println(jo1.toString());
                    if (jo1.getString("ac").equals("false")){
                        mHandler.obtainMessage(UNUPDATEJOIN_TEXT).sendToTarget();
                        return;
                    }
                    System.out.println(pos);
                    list.remove(pos);
                    mHandler.obtainMessage(UPDATEDELETE_TEXT).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                .add("action", "manage")
                .add("ActivityId", String.valueOf(ActivityId))
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
                    System.out.println(jo1.toString());
                    JSONArray jsonArray = jo1.getJSONArray("data");
                    MaxPage = jo1.getInt("pageCount");
                    myActivityRegister news = new myActivityRegister();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        news.userId = jsonArray.getJSONObject(i).getString("id");
                        news.register = Integer.parseInt(jsonArray.getJSONObject(i).getString("register"));
                        news.stute = jsonArray.getJSONObject(i).getString("stute");
                        news.username = jsonArray.getJSONObject(i).getString("username");
                        news.telephone = jsonArray.getJSONObject(i).getString("telephone");
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