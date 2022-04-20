package com.example.social_practice_activity.sign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.Utils.MySQLiteOpenHelper;
import com.example.social_practice_activity.origin.MainActivity;

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

public class login extends AppCompatActivity{
    //组件
    private EditText et_acount;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_toRegister;
    //服务器地址
    static private final String[] serverHost = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = serverHost[INTERNET] + "LoginServlet";
    //登录请求状态
    private static final int SUCCESSFUL_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int UNSUCCESSFUL_TEXT = 3;
    private static final int NOUSER_TEXT = 4;

    private String username;
    private String id;

    private CheckBox cb_mm;
    private String type;
    MySQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase wdb;
    SQLiteDatabase rdb;

    private boolean et_acount_empty, et_password_empty;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESSFUL_TEXT:
                    Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    bundle.putString("username", username);
                    Intent intent = new Intent(login.this, MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    break;
                case UNSUCCESSFUL_TEXT:
                    Toast.makeText(login.this, "账号错误或者是密码错误", Toast.LENGTH_SHORT).show();
                    et_acount.requestFocus();
                    break;
                case NOUSER_TEXT:
                    Toast.makeText(login.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    et_acount.requestFocus();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(login.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sqLiteOpenHelper = MySQLiteOpenHelper.getInstance(login.this);
        wdb = sqLiteOpenHelper.getWritableDatabase();
        rdb = sqLiteOpenHelper.getReadableDatabase();
        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");
        //初始化页面组件
        initView();
    }

    private void  initView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        cb_mm = findViewById(R.id.cb_mm);
        if (type.equals("init")){
            // Cursor 就是ResultSet
            Cursor cursor = rdb.rawQuery("select id,password from user_table;", null);
            // 往下查找只，继续移动的查询下一个
            while(cursor.moveToNext()) {
                @SuppressLint("Range") String acount = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
                toLogin(acount, password);
                break;
            }
        }
        et_acount_empty = true;
        et_password_empty = true;

        et_acount = (EditText)findViewById(R.id.et_account);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //单击事件，获取账号密码
                final String accout = et_acount.getText().toString().trim();
                final String password = et_password.getText().toString().trim();
                //TODO:给密码加密 MD5
                //发送登录请求
                toLogin(accout, password);
            }
        });
        et_acount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                String temp = et_acount.getText().toString().trim();
                et_acount_empty = (temp == null || temp.isEmpty());
                btn_login.setEnabled(!et_acount_empty && !et_password_empty);
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
                btn_login.setEnabled(!et_acount_empty && !et_password_empty);
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
        tv_toRegister = (TextView) findViewById(R.id.tv_register);
        tv_toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //去注册
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
    }

    private void toLogin(String acount, String password){
        //TODO:保存账号密码
        wdb.execSQL("delete from user_table;");
        if (cb_mm.isChecked()){
            wdb.execSQL("insert into user_table(id,password) values('" + acount + "'," + password + ");");
        }
        //发送请求
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
            .add("id", acount)
            .add("password", password)
            .build();
        final Request request = new Request.Builder()
                .url(Url)
                .post(requestBody)
                .build();
        //异步处理
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
                    System.out.println(jSONObject);
                    //获取登录返回状态
                    String ac = jSONObject.getString("ac");
                    if (ac.equals("true")){
                        username = new String(jSONObject.getString("username"));
                        id = new String(acount);
                        mHandler.obtainMessage(SUCCESSFUL_TEXT).sendToTarget();
                    }else{
                        String type = new String(jSONObject.getString("type"));
                        if (type.equals("noUser")) {
                            mHandler.obtainMessage(UNSUCCESSFUL_TEXT).sendToTarget();
                        }else if (type.equals("error")){
                            mHandler.obtainMessage(UNSUCCESSFUL_TEXT).sendToTarget();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}