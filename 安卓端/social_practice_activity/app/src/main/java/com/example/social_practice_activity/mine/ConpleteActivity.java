package com.example.social_practice_activity.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.bean.myUserDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConpleteActivity extends AppCompatActivity {
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "UserServlet";

    private EditText et_username;
    private RadioGroup rg_sex;
    private RadioButton rg_man;
    private RadioButton rg_woman;
    private EditText et_age;
    private EditText et_telephone;
    private Spinner s_province;
    private Spinner s_city;
    private Spinner s_district;
    private Button btn_complete;
    private RelativeLayout rl_face;
    private TextView tv_content;
    private ArrayAdapter<String> adapter1, adapter2;
    private ImageView ib_title_back;

    private String[] msgs1, msgs2, msgs0;
    private String id;
    private myUserDetail myUserDetail = new myUserDetail();
    private String defaultPlace = "-请选择-";
    private String city, district;


    private String key = "7a012a416031e7c4582ba799f10efbb4";
    private String mapUrl = "https://restapi.amap.com/v3/config/district";


    private static final int UPDATE1_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int UPDATE2_TEXT = 3;
    private static final int UPDATEUSERDETAIL_TEXT = 4;
    private static final int UNUPDATE_TEXT = 5;
    private static final int UPDATEFAIL_TEXT = 6;
    private static final int UPDATEOK_TEXT = 7;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean ac = false;
            switch (msg.what) {
                case UPDATE1_TEXT:
                    adapter1.clear();
                    adapter2.clear();
                    adapter2.add(defaultPlace);
                    for (String s : msgs1){
                        adapter1.add(s);
                    }
                    for (int i = 0; i < msgs1.length; i++){
                        if (msgs1[i].equals(myUserDetail.city)){
                            s_city.setSelection(i);
                            ac = true;
                            break;
                        }
                    }
                    if (!ac){
                        s_city.setSelection(0);
                    }
                    s_district.setSelection(0);
                    break;
                case UPDATE2_TEXT:
                    adapter2.clear();
                    for (String s : msgs2){
                        adapter2.add(s);
                    }
                    for (int i = 0; i < msgs2.length; i++){
                        if (msgs2[i].equals(myUserDetail.district)){
                            s_district.setSelection(i);
                            ac = true;
                            break;
                        }
                    }
                    if (!ac){
                        s_district.setSelection(0);
                    }
                    break;
                case UPDATEUSERDETAIL_TEXT:
                    et_username.setText(myUserDetail.name);
                    if (myUserDetail.sex == 1){
                        rg_man.setChecked(true);
                    }else if (myUserDetail.sex == 0){
                        rg_woman.setChecked(true);
                    }else{
                        rg_man.setChecked(false);
                        rg_woman.setChecked(false);
                    }
                    if (myUserDetail.age != 0){
                        et_age.setText(String.valueOf(myUserDetail.age));
                    }
                    et_telephone.setText(String.valueOf(myUserDetail.telephone));

                    for (int i = 0; i < msgs0.length; i++){
                        if (msgs0[i].equals(myUserDetail.province)){
                            s_province.setSelection(i);
                            break;
                        }
                    }
                    if (!myUserDetail.face.equals("未认证")){
                        if (System.currentTimeMillis() - myUserDetail.face_last_upload_time.getTime() <= 30 * 24 * 60 * 60 * 1000){
                            myUserDetail.face = "本月还可以上传0次";
                        }else {
                            myUserDetail.face = "本月还可以上传1次";
                        }
                    }
                    tv_content.setText(myUserDetail.face);
                    break;
                case UPDATEFAIL_TEXT:
                    Toast.makeText(ConpleteActivity.this, "保存失败·", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATEOK_TEXT:
                    Toast.makeText(ConpleteActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    break;
                case UNUPDATE_TEXT:
                    Toast.makeText(ConpleteActivity.this, "服务器脱机了", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(ConpleteActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_conplete);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");

        ib_title_back = findViewById(R.id.ib_title_back);
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_username = findViewById(R.id.et_username);
        rg_sex = findViewById(R.id.rg_sex);
        rg_man = findViewById(R.id.rg_man);
        rg_woman = findViewById(R.id.rg_woman);
        et_age = findViewById(R.id.et_age);
        et_telephone = findViewById(R.id.et_telephone);
        tv_content = findViewById(R.id.tv_content);

        myUserDetail.city = defaultPlace;
        myUserDetail.district = defaultPlace;

        msgs0 = getResources().getStringArray(R.array.province);
        s_province = findViewById(R.id.s_province);
        s_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    return;
                String SpItemSelected = s_province.getSelectedItem().toString();
                System.out.println("---" + SpItemSelected);
                toGetNext(SpItemSelected, 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_city = findViewById(R.id.s_city);
        s_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    return;
                String SpItemSelected = s_city.getSelectedItem().toString();
                System.out.println("------" + SpItemSelected);
                toGetNext(SpItemSelected, 2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter1 = new ArrayAdapter<String>(this,R.layout.myspinner);
        msgs1 = new String[]{defaultPlace};
        adapter1.add(defaultPlace);
        s_city.setAdapter(adapter1);

        s_district = findViewById(R.id.s_district);
        adapter2 = new ArrayAdapter<String>(this,R.layout.myspinner);
        msgs2 = new String[]{defaultPlace};
        adapter2.add(defaultPlace);
        s_district.setAdapter(adapter2);

        btn_complete = findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPost();
            }
        });
        rl_face = findViewById(R.id.rl_face);
        rl_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myUserDetail.face.equals("本月还可以上传0次")){
                    Toast.makeText(ConpleteActivity.this, "本月的上传次数用完了，请下个月再上传", Toast.LENGTH_SHORT).show();
                }else{
                    toFace();
                }
            }
        });

        getDetail();
    }

    private void getDetail() {

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "find")
                .add("id", id)
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
                    myUserDetail.id = id;
                    myUserDetail.name = jo1.getString("username");
                    myUserDetail.sex = jo1.getInt("sex");
                    myUserDetail.age = jo1.getInt("age");
                    myUserDetail.province = jo1.getString("province");
                    myUserDetail.city = jo1.getString("city");
                    myUserDetail.district = jo1.getString("district");
                    myUserDetail.face = jo1.getString("face");
                    myUserDetail.telephone = jo1.getString("telephone");
                    myUserDetail.face_last_upload_time = Timestamp.valueOf(jo1.getString("face_last_upload_time"));
                    System.out.println("myUserDetail: " + myUserDetail.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage(UPDATEUSERDETAIL_TEXT).sendToTarget();
            }
        });
    }

    private void toGetNext(String place, int id) {

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(mapUrl + "?key=" + key + "&keywords=" + place)
                .build();
        System.out.println(mapUrl + "?key=" + key + "&keywords=" + place);
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
                    System.out.println(jo1.toString());
                    if (jo1.getString("status").equals("0")){
                        mHandler.obtainMessage(UNUPDATE_TEXT).sendToTarget();
                        return;
                    }
//                    JSONArray jo3 = jo1.getJSONArray("districts");
//                    System.out.println(jo3.toString());
                    JSONArray jo2 = jo1.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");
                    System.out.println(jo2.toString());
                    if (id == 1){
                        msgs1 = new String[jo2.length() + 1];
                        msgs1[0] = defaultPlace;
                    }else{
                        msgs2 = new String[jo2.length() + 1];
                        msgs2[0] = defaultPlace;
                    }
                    for (int i = 0; i < jo2.length(); i++){
                        if (id == 1){
                            msgs1[i+1] = new String(jo2.getJSONObject(i).getString("name"));
                            System.out.println(msgs1[i+1]);
                        }else{
                            msgs2[i+1] = new String(jo2.getJSONObject(i).getString("name"));
                            System.out.println(msgs2[i+1]);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (id == 1){
                    mHandler.obtainMessage(UPDATE1_TEXT).sendToTarget();
                }else{
                    mHandler.obtainMessage(UPDATE2_TEXT).sendToTarget();
                }
            }
        });
    }

    private void toFace() {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        Intent intent = new Intent(ConpleteActivity.this, ImageActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 10);
    }

    private void toPost() {
        myUserDetail.name = et_username.getText().toString();
        myUserDetail.sex = 2;
        if (rg_man.isChecked()){
            myUserDetail.sex = 1;
        }else if (rg_woman.isChecked()){
            myUserDetail.sex = 0;
        }
        System.out.println(myUserDetail.sex);
        myUserDetail.age = Integer.parseInt(et_age.getText().toString().equals("") ? "2" : et_age.getText().toString());
        myUserDetail.province = s_province.getSelectedItem().toString();
        myUserDetail.city = s_city.getSelectedItem().toString();
        myUserDetail.district = s_district.getSelectedItem().toString();
        myUserDetail.telephone = et_telephone.getText().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "updateDetails")
                .add("id", id)
                .add("name",  myUserDetail.name)
                .add("sex", String.valueOf(myUserDetail.sex))
                .add("age", String.valueOf(myUserDetail.age))
                .add("province", myUserDetail.province)
                .add("city", myUserDetail.city)
                .add("district", myUserDetail.district)
                .add("telephone", myUserDetail.telephone)
                .add("face", myUserDetail.face)
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
                        mHandler.obtainMessage(UPDATEFAIL_TEXT).sendToTarget();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage(UPDATEOK_TEXT).sendToTarget();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(ConpleteActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    if (myUserDetail.face.equals("未认证")){
                        myUserDetail.face = "本月还可以上传1次";
                    }else{
                        myUserDetail.face = "本月还可以上传0次";
                    }
                    tv_content.setText(myUserDetail.face);
                }else {
                    Toast.makeText(ConpleteActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}