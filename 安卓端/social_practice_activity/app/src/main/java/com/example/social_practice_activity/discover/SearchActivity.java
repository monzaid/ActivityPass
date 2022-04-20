package com.example.social_practice_activity.discover;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.bean.myActivity;
import com.example.social_practice_activity.myAdaper.UpdataListViewAdaper;
import com.example.social_practice_activity.myView.UpdataListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements UpdataListView.OnRefreshListener {
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "ActivityServlet";
    static private final String[] action = new String[]{"pageByTitle", "pageByTeam", "pageByPlace", "pageByPos", "pageByTime"};

    private Spinner s_type;
    private EditText et_search;
    private TextView tv_back;
    private TextView tv_time;
    private Button btn_time;
    private UpdataListView updataListView;

    private String[] msg;
    private static final int UPDATE_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int INITDATA_TEXT = 3;
    private static final int REINITDATA_TEXT = 4;
    private static final int UNUPDATE_TEXT = 5;

    private List<myActivity> list = new ArrayList<>();
    private List<myActivity> temp = new ArrayList<>();
    private UpdataListViewAdaper adapter;

    private int page = 1;
    private int MaxPage = 1;
    private int pageSize = 10;

    private String userId;
    private String content;
    Calendar calendar= Calendar.getInstance(Locale.CHINA);

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
                        Toast.makeText(SearchActivity.this, "没有查询结果", Toast.LENGTH_SHORT).show();
                    }
//                    if (list.size() == 0){
//                        fab.setVisibility(View.GONE);
//                        break;
//                    }
                    list.clear();
                    list.addAll(temp);
                    fab.setVisibility(View.VISIBLE);
                    adapter = new UpdataListViewAdaper(list);
                    updataListView.setAdapter(adapter);
                    updataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            myActivity msg = (myActivity) adapterView.getItemAtPosition(position);
                            Bundle bundle = new Bundle();
                            bundle.putString("activityId", String.valueOf(msg.id));
                            bundle.putString("userId", userId);
                            Intent intent = new Intent(SearchActivity.this, ActivityDetail.class);
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
                    Toast.makeText(SearchActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(SearchActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_search);

        msg = getResources().getStringArray(R.array.type);
        Bundle bundle = this.getIntent().getExtras();
        userId = new String(bundle.getString("id"));

        s_type = findViewById(R.id.s_type);
        s_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String SpItemSelected = s_type.getSelectedItem().toString();
                System.out.println("---" + SpItemSelected);
                if (SpItemSelected.equals("地图")){
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", userId);
                    bundle1.putString("distance_x", "-1");
                    bundle1.putString("distance_y", "-1");
                    bundle1.putString("type", "search");
                    Intent intent = new Intent(SearchActivity.this, MapActivity.class);
                    intent.putExtras(bundle1);
                    startActivityForResult(intent, 100);
                }else if (SpItemSelected.equals("时间")){
                    et_search.setVisibility(View.GONE);
                    tv_time.setVisibility(View.VISIBLE);
                    btn_time.setVisibility(View.VISIBLE);
                }else {
                    et_search.setVisibility(View.VISIBLE);
                    tv_time.setVisibility(View.GONE);
                    btn_time.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        et_search = findViewById(R.id.et_search);
        et_search.setOnKeyListener(new View.OnKeyListener() {
            /**
             * 键盘点击
             *
             * @param v
             * @param keyCode
             * @param event
             * @return
             */
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    //获取输入框的值
                    content = et_search.getText().toString().trim();
                    if (content == null || content.isEmpty()) {
                        Toast.makeText(SearchActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    }else {
                        initDatas(INITDATA_TEXT, s_type.getSelectedItemPosition());
                    }
                    return true;
                }
                return false;
            }

        });
        tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_time = findViewById(R.id.tv_time);
        tv_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                content = tv_time.getText().toString();
                initDatas(INITDATA_TEXT, s_type.getSelectedItemPosition());
            }
        });
        btn_time = findViewById(R.id.btn_time);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(SearchActivity.this,  0, tv_time, calendar);
            }
        });
        updataListView = findViewById(R.id.u);
        updataListView.setOnRefreshListener(this);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= 8) {
                    updataListView.smoothScrollToPosition(0);
                } else {
                    updataListView.setSelection(0);
                }
            }
        });
    }

    /**
     * 日期选择
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                String s = String.format("%04d-%02d-%02d ", year, (monthOfYear + 1), dayOfMonth);
                showTimePickerDialog(activity,  0, tv, calendar, s);
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 时间选择
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showTimePickerDialog(Activity activity,int themeResId, final TextView tv, Calendar calendar, String s) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog( activity,themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.setText(s + String.format("%02d:%02d:00", hourOfDay, minute));
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true).show();
    }


    private void initDatas(int msg, int pos) {
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
                .add("action", action[pos])
                .add("content", content)
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
                    System.out.println(jo1);
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
        updataListView.setEnabled(false);
        //handler设置刷新延时效果
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取最新数据
                initDatas(REINITDATA_TEXT, s_type.getSelectedItemPosition());
                //通知listView刷新数据完毕
                updataListView.completeRefresh();
                updataListView.setEnabled(true);
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
                initDatas(UPDATE_TEXT, s_type.getSelectedItemPosition());
                //通知listView刷新数据完毕
                updataListView.completeRefresh();
            }
        }, 2000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            s_type.setSelection(0);
        }
    }
}