package com.example.social_practice_activity.sign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.mine.ConpleteActivity;

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

public class register extends AppCompatActivity {
    //组件
    private EditText et_account;
    private EditText et_password;
    private EditText et_confirmPassword;
    private Button btn_register;
    private TextView tv_toLogin;
    private Button btn_login;
    private Button btn_complete;
    private RelativeLayout rl_register;
    private RelativeLayout rl_registerSuccessful;
    private ImageView ib_title_back;
    //服务器地址
    static private final String[] serverHost = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = serverHost[INTERNET] + "RegisServlet";

    //登录请求状态
    private static final int SUCCESSFUL_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int UNSUCCESSFUL_TEXT = 3;

    private String account;
    private String password;
    private String confirmpassword;

    private boolean et_acount_empty, et_password_empty, et_confirmpassword_empty;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESSFUL_TEXT:
                    //TODO:保存账号密码
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //隐藏软键盘
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    rl_register.setVisibility(View.GONE);
                    rl_registerSuccessful.setVisibility(View.VISIBLE);
                    break;
                case UNSUCCESSFUL_TEXT:
                    Toast.makeText(register.this, "账号已存在", Toast.LENGTH_SHORT).show();
                    et_account.requestFocus();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(register.this, "网络错误", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_register);
        //初始化页面组件
        initView();
    }

    private void initView() {
        et_acount_empty = true;
        et_password_empty = true;
        et_confirmpassword_empty = true;
        ib_title_back = (ImageView) findViewById(R.id.ib_title_back);
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rl_register = (RelativeLayout) findViewById(R.id.rl_register);
        rl_registerSuccessful = (RelativeLayout) findViewById(R.id.rl_registerSuccessful);
        rl_registerSuccessful.setVisibility(View.GONE);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //单击事件，获取账号密码
                account = et_account.getText().toString().trim();
                password = et_password.getText().toString().trim();
                confirmpassword = et_confirmPassword.getText().toString().trim();
                //检查账号密码是否正确
                //TODO:账号密码合法性
                if(!password.equals(confirmpassword)){
                    Toast.makeText(register.this, "密码和确认密码不一致", Toast.LENGTH_SHORT).show();
                    et_password.requestFocus();
                    return;
                }

                //TODO:给密码加密 MD5
                //发送注册请求
                toRegister();
            }
        });
        tv_toLogin = (TextView) findViewById(R.id.tv_login);
        tv_toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_complete = (Button) findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", account);
                Intent intent = new Intent(register.this, ConpleteActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        et_account.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                String temp = et_account.getText().toString().trim();
                et_acount_empty = (temp == null || temp.isEmpty());
                btn_register.setEnabled(!et_acount_empty && !et_password_empty && !et_confirmpassword_empty);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听

            }
        });
        et_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                String temp = et_password.getText().toString().trim();
                et_password_empty = (temp == null || temp.isEmpty());
                btn_register.setEnabled(!et_acount_empty && !et_password_empty && !et_confirmpassword_empty);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听

            }
        });
        et_confirmPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                String temp = et_confirmPassword.getText().toString().trim();
                et_confirmpassword_empty = (temp == null || temp.isEmpty());
                btn_register.setEnabled(!et_acount_empty && !et_password_empty && !et_confirmpassword_empty);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听

            }
        });
    }

    private void toRegister(){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("id", account)
                .add("password", password)
                .build();
        final Request request = new Request.Builder()
                .url(Url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            //请求失败
            public void onFailure(Call call, IOException e) {
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
                        mHandler.obtainMessage(SUCCESSFUL_TEXT).sendToTarget();
                    }else{
                        mHandler.obtainMessage(UNSUCCESSFUL_TEXT).sendToTarget();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}