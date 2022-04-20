package com.example.social_practice_activity.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.social_practice_activity.R;

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

public class PasswordActivity extends AppCompatActivity {
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "UserServlet";

    private EditText et_oldPassword;
    private EditText et_newPassword;
    private EditText et_confirmPassword;
    private Button btn_modify;
    private ImageView ib_title_back;

    private String id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    private static final int UPDATE_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int UNUPDATE_TEXT = 5;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean ac = false;
            switch (msg.what) {
                case UPDATE_TEXT:
                    Toast.makeText(PasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case UNUPDATE_TEXT:
                    Toast.makeText(PasswordActivity.this, "旧密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(PasswordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_password);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");

        ib_title_back = findViewById(R.id.ib_title_back);
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_oldPassword = findViewById(R.id.et_old_password);
        et_newPassword = findViewById(R.id.et_password);
        et_confirmPassword = findViewById(R.id.et_confirm_password);
        btn_modify = findViewById(R.id.btn_confirm);
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword = et_oldPassword.getText().toString().trim();
                newPassword = et_newPassword.getText().toString().trim();
                confirmPassword = et_confirmPassword.getText().toString().trim();
                if (oldPassword.equals("") || newPassword.equals("") || confirmPassword.equals("")){
                    Toast.makeText(PasswordActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    et_oldPassword.requestFocus();
                }else if (!newPassword.equals(confirmPassword)){
                    Toast.makeText(PasswordActivity.this, "新密码和确认密码不一致！", Toast.LENGTH_SHORT).show();
                    et_newPassword.requestFocus();
                }else if (newPassword.equals(oldPassword)){
                    Toast.makeText(PasswordActivity.this, "旧密码和新密码一致！", Toast.LENGTH_SHORT).show();
                    et_newPassword.requestFocus();
                }else{
                    toModify();
                }
            }
        });
    }

    private void toModify() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "updateUser")
                .add("id", id)
                .add("password",  oldPassword)
                .add("newPassword", newPassword)
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
                    if (jo1.getString("ac").equals("false")){
                        mHandler.obtainMessage(UNUPDATE_TEXT).sendToTarget();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage(UPDATE_TEXT).sendToTarget();
            }
        });
    }
}