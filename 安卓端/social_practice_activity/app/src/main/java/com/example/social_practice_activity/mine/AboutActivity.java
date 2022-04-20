package com.example.social_practice_activity.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class AboutActivity extends AppCompatActivity {
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "VersionServlet";

    private TextView tv_version;
    private ImageView ib_title_back;

    private String url;
    private String version;

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
                    Toast.makeText(AboutActivity.this, "最新版本" + version + "\n请去 " + url + "下载", Toast.LENGTH_LONG).show();
                    break;
                case UNUPDATE_TEXT:
                    Toast.makeText(AboutActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(AboutActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_about);
        ib_title_back = findViewById(R.id.ib_title_back);
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_version = findViewById(R.id.tv_update);
        tv_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toUpdate();
            }
        });
    }

    private void toUpdate() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "updateUser")
                .add("version", getResources().getString(R.string.version))
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
                    if (jo1.getString("ac").equals("true")){
                        mHandler.obtainMessage(UNUPDATE_TEXT).sendToTarget();
                        return;
                    }
                    version = new String(jo1.getString("version"));
                    url = new String(jo1.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage(UPDATE_TEXT).sendToTarget();
            }
        });
    }
}