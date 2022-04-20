package com.example.social_practice_activity.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.myBaseClass.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class mineFragment extends BaseFragment {
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "ActivityRegisterServlet";
    private static final String TAG =
            mineFragment.class.getSimpleName();

    private TextView tv_username;
    private TextView tv_account;
    private TextView tv_activity_join;
    private TextView tv_activity_create;
    private TextView tv_complete;
    private TextView tv_password;
    private TextView tv_about;
    private TextView tv_help;
    private TextView tv_settings;

    private String username;
    private String id;

    private static final int UPDATE_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int UNUPDATE_TEXT = 5;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @SuppressLint("ResourceAsColor")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TEXT:
                    tv_username.setText(username);
                    break;
                case UNUPDATE_TEXT:
                    Toast.makeText(root.getContext(), "服务器脱机了", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(root.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    View root;
    @Override
    public View initView() {
        Log.e(TAG,"主页视图被初2134324始化了");
        root = View.inflate(mContext, R.layout.fragment_mine,null);
        Bundle bundle = this.getArguments();
        username = new String(bundle.getString("username"));
        id = new String(bundle.getString("id"));

        tv_username = root.findViewById(R.id.tv_username);
        tv_account = root.findViewById(R.id.tv_account);
        tv_account.setText(id);
        tv_username.setText(username);

        tv_activity_join = root.findViewById(R.id.tv_activity_join);
        tv_activity_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                Intent intent = new Intent(root.getContext(), ActivityJoinList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tv_activity_create = root.findViewById(R.id.tv_activity_create);
        tv_activity_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                Intent intent = new Intent(root.getContext(), ActivitySendList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tv_complete = root.findViewById(R.id.tv_complete);
        tv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                Intent intent = new Intent(root.getContext(), ConpleteActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                //TODO:返回用户名&头像
            }
        });

        tv_password = root.findViewById(R.id.tv_password);
        tv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                Intent intent = new Intent(root.getContext(), PasswordActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        tv_about = root.findViewById(R.id.tv_about);
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        tv_help = root.findViewById(R.id.tv_help);
        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        tv_settings = root.findViewById(R.id.tv_settings);
        tv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
    @Override
    public void initData() {
        super.initData();
        Log.e(TAG,"主页数据被初始化了");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK){
            toUpdate();
        }
    }

    private void toUpdate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //发送请求
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("action", "find")
                            .add("id", id)
                            .build();
                    final Request request = new Request.Builder()
                            .url(Url)
                            .post(requestBody)
                            .build();
                    System.out.println(Url);
                    //异步处理
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        //请求失败
                        public void onFailure(Call call, IOException e) {
                            Log.d("请求失败","，返回码："+e.getMessage());
                            mHandler.obtainMessage(FAIL_TEXT).sendToTarget();
                        }

                        @Override
                        //请求成功
                        public void onResponse(Call call, final Response response) throws IOException {
                            JSONObject jSONObject = null;
                            try {
                                jSONObject = new JSONObject(response.body().string());
                                //获取登录返回状态
                                String ac = jSONObject.getString("ac");
                                if (ac.equals("true")){
                                    Log.d("登录成功","后台返回数据：" + jSONObject.toString());
                                    username = new String(jSONObject.getString("username"));
                                    mHandler.obtainMessage(UPDATE_TEXT).sendToTarget();
                                }else{
                                    Log.d("登录失败","后台返回数据：" + jSONObject.toString());
                                    mHandler.obtainMessage(UNUPDATE_TEXT).sendToTarget();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}