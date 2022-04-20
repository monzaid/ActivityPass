package com.example.social_practice_activity.mine;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.Utils.RealPathFromUriUtils;
import com.example.social_practice_activity.bean.myActivity;
import com.example.social_practice_activity.discover.MapActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SandActivity extends AppCompatActivity {

    private final int CAMERA = 1;//事件枚举(可以自定义)
    private final int CHOOSE = 2;//事件枚举(可以自定义)
    String photoPath = "";//要上传的图片路径
    private final int permissionCode = 100;//权限请求码
    HashMap<String, String> fileImage;

    static private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");

    //权限集合，对应在AndroidManifest.xml文件中添加配置
    //    <uses-permission android:name="android.permission.CAMERA" />
    //    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    //    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    //    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    //    <uses-permission android:name="android.permission.INTERNET"/>
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET
    };

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "UserServlet";
    static private final String ActivityUrl = Host[INTERNET] + "ActivityServlet";
    static private final String ActivityUpdateUrl = Host[INTERNET] + "SaveUserImageServlet";
    private Button takePhotoBtn;
    private Button pickPictureBtn;
    private Button cancelBtn;
    private EditText et_title;
    private EditText et_introduction;
    private TextView tv_start_time_text;
    private TextView tv_end_time_text;
    private Button btn_start_time;
    private Button btn_end_time;
    private EditText et_people;
    private EditText et_place;
    private EditText et_username;
    private EditText et_telephone;
    private EditText et_team;
    private EditText et_content;
    private Button btn_create;
    private Button btn_delete;
    private RadioButton rg_yes;
    private RadioButton rg_no;
    private RadioButton rg_man;
    private RadioButton rg_woman;
    private RadioButton rg_no_sex;
    private NumberPicker np_minAge;
    private NumberPicker np_maxAge;
    private Spinner s_place;
    private EditText et_elseAstrict;
    private LinearLayout ll_msgAstrictList;
    private LinearLayout ll_msgContent;
    private ImageButton ib_image;
    private RelativeLayout rl_pos;
    private TextView tv_tips;
    private RelativeLayout rl_Successful;
    private ScrollView sl_table;
    private TextView tv_registerSuccessful;
    private ImageView ib_title_back;
    AlertDialog alertDialog;
    //    布局参数
    private RelativeLayout.LayoutParams params_ll_msgAstrictList;
    private RelativeLayout.LayoutParams params_ll_msgContent;

    private com.example.social_practice_activity.bean.myActivity myActivity;
    private Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private String[] msgs;

    private static final int UPDATE_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int UPDATEUSERDETAIL_TEXT = 4;
    private static final int UNUPDATE_TEXT = 5;
    private static final int DELETE_TEXT = 6;
    private static final int UNDELETE_TEXT = 7;

    private boolean et_title_empty, et_telephone_empty, et_people_empty, et_place_empty, tv_start_time_text_empty, tv_end_time_text_empty, tv_tips_empty;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean ac = false;
            switch (msg.what) {
                case UPDATE_TEXT:
                    Toast.makeText(SandActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case UNUPDATE_TEXT:
                    Toast.makeText(SandActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATEUSERDETAIL_TEXT:
                    et_title.setText(myActivity.title);
                    et_introduction.setText(myActivity.introduction);
                    et_people.setText(String.valueOf(myActivity.people));
                    et_place.setText(myActivity.place);
                    et_username.setText(myActivity.userName);
                    et_telephone.setText(myActivity.telephone);
                    et_team.setText(myActivity.team);
                    et_content.setText(myActivity.content);
                    if (myActivity.pos_y != -1 && myActivity.pos_x != -1){
                        tv_tips.setText("已设置");
                    }
                    tv_start_time_text.setText("20" + myActivity.getStart_time() + ":00");
                    tv_end_time_text.setText("20" + myActivity.getEnd_time() + ":00");
                    if (myActivity.astrict == 1){
                        rg_yes.setChecked(true);
                        if (myActivity.sex == 0){
                            rg_woman.setChecked(true);
                        }else if (myActivity.sex == 1){
                            rg_man.setChecked(true);
                        }
                        if (myActivity.minAge != -1){
                            np_minAge.setValue(myActivity.minAge);
                        }
                        if (myActivity.maxAge != -1){
                            np_maxAge.setValue(myActivity.maxAge);
                        }
                        for (int i = 0; i < msgs.length; i++){
                            if (myActivity.district.equals(msgs[i])){
                                s_place.setSelection(i);
                                break;
                            }
                        }
                        et_elseAstrict.setText(myActivity.elseAstrict);
                    }
                    break;
                case DELETE_TEXT:
                    rl_Successful.setVisibility(View.VISIBLE);
                    sl_table.setVisibility(View.GONE);
                    break;
                case UNDELETE_TEXT:
                    Toast.makeText(SandActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(SandActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_sand);

        myActivity = new myActivity();
        Bundle bundle = this.getIntent().getExtras();
        myActivity.id = Integer.parseInt(bundle.getString("ActivityId"));
        myActivity.t_u_id = bundle.getString("userId");
        myActivity.minAge = -1;
        myActivity.maxAge = -1;
        et_title_empty = false;
        et_telephone_empty = false;
        et_people_empty = false;
        et_place_empty = false;
        tv_start_time_text_empty = false;
        tv_end_time_text_empty = false;
        tv_tips_empty = false;
        msgs = getResources().getStringArray(R.array.astrictPlace);

        ib_title_back = findViewById(R.id.ib_title_back);
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_title = findViewById(R.id.et_title);
        et_introduction = findViewById(R.id.et_introduction);
        et_people = findViewById(R.id.et_people);
        et_place = findViewById(R.id.et_place);
        et_username = findViewById(R.id.et_username);
        et_telephone = findViewById(R.id.et_telephone);
        et_team = findViewById(R.id.et_team);
        et_content = findViewById(R.id.et_content);
        btn_create = findViewById(R.id.btn_create);
        btn_delete = findViewById(R.id.btn_delete);
        ll_msgAstrictList = findViewById(R.id.ll_msgAstrictList);
        ll_msgContent = findViewById(R.id.ll_msgContent);
        params_ll_msgAstrictList = (RelativeLayout.LayoutParams) ll_msgAstrictList.getLayoutParams();
        params_ll_msgContent = (RelativeLayout.LayoutParams) ll_msgContent.getLayoutParams();
        rl_pos = findViewById(R.id.rl_pos);
        rl_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("distance_x", String.valueOf(myActivity.pos_x));
                bundle1.putString("distance_y", String.valueOf(myActivity.pos_y));
                bundle1.putString("type", "setPos");
                Intent intent = new Intent(SandActivity.this, MapActivity.class);
                intent.putExtras(bundle1);
                startActivityForResult(intent, 100);
            }
        });
        tv_tips = findViewById(R.id.tv_tips);
        ib_image = findViewById(R.id.iv_image);
        ib_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPicturePopupWindow();
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCreate();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
        rg_yes = findViewById(R.id.rg_yes);
        rg_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rg_yes.isChecked()){
                    ll_msgAstrictList.setVisibility(View.VISIBLE);
                    params_ll_msgContent.addRule(RelativeLayout.BELOW, R.id.ll_msgAstrictList);
                }
            }
        });
        rg_no = findViewById(R.id.rg_no);
        rg_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rg_no.isChecked()){
                    params_ll_msgContent.addRule(RelativeLayout.BELOW, R.id.ll_msgAstrictList);
                    ll_msgAstrictList.setVisibility(View.GONE);
                }
            }
        });
        rg_man = findViewById(R.id.rg_man);
        rg_woman = findViewById(R.id.rg_woman);
        rg_no_sex = findViewById(R.id.rg_no_sex);
        np_minAge = findViewById(R.id.np_minAge);
        np_minAge.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        np_minAge.setMaxValue(100);
        np_minAge.setMinValue(0);
        np_minAge.setValue(0);
        np_minAge.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                myActivity.minAge = value == 0 ? -1 : value;
                String data;
                if (value == 0) {
                    data = "无限制";
                } else {
                    data = String.valueOf(value);
                }
                return data;
            }
        });

        np_maxAge = findViewById(R.id.np_maxAge);
        np_maxAge.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        np_maxAge.setMaxValue(100);
        np_maxAge.setMinValue(0);
        np_maxAge.setValue(0);
        np_maxAge.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                myActivity.maxAge = value == 0 ? -1 : value;
                String data;
                if (value == 0) {
                    data = "无限制";
                } else {
                    data = String.valueOf(value);
                }
                return data;
            }
        });
        s_place = findViewById(R.id.s_province);
        et_elseAstrict = findViewById(R.id.et_elseAstrict);
        tv_start_time_text = findViewById(R.id.tv_start_time_text);
        tv_end_time_text = findViewById(R.id.tv_end_time_text);
        btn_start_time = findViewById(R.id.btn_start_time);
        btn_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(SandActivity.this,  0, tv_start_time_text, calendar);
            }
        });
        btn_end_time = findViewById(R.id.btn_end_time);
        btn_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(SandActivity.this,  0, tv_end_time_text, calendar);
            }
        });
        rl_Successful = findViewById(R.id.rl_Successful);
        sl_table = findViewById(R.id.sl_table);
        tv_registerSuccessful = findViewById(R.id.tv_registerSuccessful);
        tv_registerSuccessful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", myActivity.t_u_id);
                Intent intent = new Intent(SandActivity.this, ActivitySendList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        alertDialog = new AlertDialog.Builder(this)
            .setTitle("确认要删除这个活动吗？")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    toDelete();
                }
            })

            .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .create();
        et_title.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                String temp = et_title.getText().toString().trim();
                et_title_empty = (temp == null || temp.isEmpty());
                btn_create.setEnabled(!et_title_empty && !et_telephone_empty && !et_people_empty && !et_place_empty && !tv_start_time_text_empty && !tv_end_time_text_empty && !tv_tips_empty);
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
        et_place.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                String temp = et_place.getText().toString().trim();
                et_place_empty = (temp == null || temp.isEmpty());
                btn_create.setEnabled(!et_title_empty && !et_telephone_empty && !et_people_empty && !et_place_empty && !tv_start_time_text_empty && !tv_end_time_text_empty && !tv_tips_empty);
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
        et_people.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                String temp = et_people.getText().toString().trim();
                et_people_empty = (temp == null || temp.isEmpty());
                btn_create.setEnabled(!et_title_empty && !et_telephone_empty && !et_people_empty && !et_place_empty && !tv_start_time_text_empty && !tv_end_time_text_empty && !tv_tips_empty);
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
        et_telephone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                String temp = et_telephone.getText().toString().trim();
                et_telephone_empty = (temp == null || temp.isEmpty());
                btn_create.setEnabled(!et_title_empty && !et_telephone_empty && !et_people_empty && !et_place_empty && !tv_start_time_text_empty && !tv_end_time_text_empty && !tv_tips_empty);
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
        getInitDates();
    }

    public void SelectPicturePopupWindow() {
        View view = LayoutInflater.from(SandActivity.this).inflate(R.layout.layout_popupwindow, null, false);
        takePhotoBtn = (Button) view.findViewById(R.id.picture_selector_take_photo_btn);
        pickPictureBtn = (Button) view.findViewById(R.id.picture_selector_pick_picture_btn);
        cancelBtn = (Button) view.findViewById(R.id.picture_selector_cancel_btn);

        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效

        popWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER | Gravity.BOTTOM, 0, 0);

        popWindow.setFocusable(false); // 点击其他地方隐藏键盘 popWindow
        popWindow.update();

        // 设置按钮监听
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //6.0才用动态权限
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission();
                }
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                takePhoto();
            }
        });
        pickPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //6.0才用动态权限
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission();
                }
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CHOOSE);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        });
    }

    private void takePhoto() {
        //方法一：这样拍照只能取到缩略图（不清晰）
        //intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, CAMERA);


        //方法二：指定加载路径图片路径（保存原图，清晰）
        String SD_PATH = Environment.getExternalStorageDirectory().getPath() + "/activityImage/";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(new Date(System.currentTimeMillis())) + ".JPEG";
        photoPath = SD_PATH + fileName;
        File file = new File(photoPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        //兼容7.0以上的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                ContentValues values = new ContentValues(1);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                values.put(MediaStore.Images.Media.DATA, photoPath);
                Uri tempuri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (tempuri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                }
                startActivityForResult(intent, CAMERA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //指定拍照后的存储路径，保存原图
            startActivityForResult(intent, CAMERA);
        }
    }



    //检查权限
    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }
        if (permissionList.size() <= 0) {
            //说明权限都已经通过，可以做你想做的事情去

        } else {
            //存在未允许的权限
            ActivityCompat.requestPermissions(this, permissions, permissionCode);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 调用照相机拍照
            case CAMERA:
                if (resultCode == RESULT_OK) {
                    //对应方法一：图片未保存，需保存文件到本地
//                    Bundle bundle = data.getExtras();
//                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    String savePath;
//                    String SD_PATH = Environment.getExternalStorageDirectory().getPath() + "/拍照上传示例/";
//                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//                    String fileName = format.format(new Date(System.currentTimeMillis())) + ".JPEG";
//                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                        savePath = SD_PATH;
//                    } else {
//                        Toast.makeText(MainActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    photoPath = savePath + fileName;
//                    File file = new File(photoPath);
//                    try {
//                        if (!file.exists()) {
//                            file.getParentFile().mkdirs();
//                            file.createNewFile();
//                        }
//                        FileOutputStream stream = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                        Toast.makeText(MainActivity.this, "保存成功,位置:" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    //对应方法二：图片已保存，只需读取就行了
                    try {
                        FileInputStream stream = new FileInputStream(photoPath);
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);

                        //预览图片
                        ImageButton image = findViewById(R.id.iv_image);
                        image.setImageBitmap(bitmap);

                        //上传图片（Android 4.0 之后不能在主线程中请求HTTP请求）
                        File file = new File(photoPath);
                        if (file.exists()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //图片字段
                                    fileImage = new HashMap<String, String>();
                                    fileImage.put(RealPathFromUriUtils.getFileNameFromPath(photoPath), photoPath);
                                }
                            }).start();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 选择图片库的图片
            case CHOOSE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        photoPath = RealPathFromUriUtils.getRealPathFromUri(SandActivity.this, uri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                        //压缩图片
                        bitmap = scaleBitmap(bitmap, (float) 0.5);

                        //预览图片
                        ImageButton image = findViewById(R.id.iv_image);
                        image.setImageBitmap(bitmap);

                        //上传图片（Android 4.0 之后不能在主线程中请求HTTP请求）
                        File file = new File(photoPath);
                        if (file.exists()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //图片字段
                                    fileImage = new HashMap<String, String>();
                                    fileImage.put(RealPathFromUriUtils.getFileNameFromPath(photoPath), photoPath);
                                }
                            }).start();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 100:
                if (resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    myActivity.pos_x = Double.parseDouble(bundle.getString("distance_x"));
                    myActivity.pos_y = Double.parseDouble(bundle.getString("distance_y"));
                    System.out.println(myActivity.pos_x + " " + myActivity.pos_y);
                    tv_tips.setText("已设置");
                }
                break;
        }
    }

    //压缩图片
    public Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return newBM;
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
                tv.setText(String.format("%04d-%02d-%02d ", year, (monthOfYear + 1), dayOfMonth));
                showTimePickerDialog(activity,  0, tv, calendar);
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
    public static void showTimePickerDialog(Activity activity,int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog( activity,themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.append(String.format("%02d:%02d:00", hourOfDay, minute));
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true).show();
    }


    private void getInitDates() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "getActivity")
                .add("id", String.valueOf(myActivity.id))
                .build();
        final Request request = new Request.Builder()
                .url(ActivityUrl)
                .post(requestBody)
                .build();
        System.out.println(ActivityUrl);
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
                    JSONObject jo2 = jo1.getJSONObject("activity");
                    myActivity.id = jo2.getInt("id");
                    myActivity.t_u_id = jo2.getString("t_u_id");
                    myActivity.userName = jo2.getString("username");
                    myActivity.content = jo2.getString("content");
                    myActivity.team = jo2.getString("team");
                    myActivity.telephone = jo2.getString("telephone");
                    myActivity.end_time = Timestamp.valueOf(jo2.getString("end_time"));
                    myActivity.image = jo2.getString("image");
                    myActivity.people = jo2.getInt("people");
                    myActivity.title = jo2.getString("title");
                    myActivity.introduction = jo2.getString("introduction");
                    myActivity.place = jo2.getString("place");
                    myActivity.start_time = Timestamp.valueOf(jo2.getString("start_time"));
                    myActivity.people = jo2.getInt("people");
                    myActivity.pos_x = jo2.getDouble("pos_x");
                    myActivity.pos_y = jo2.getDouble("pos_y");
                    myActivity.astrict = jo2.getInt("astrict");
                    myActivity.sex = jo2.getInt("sex");
                    myActivity.maxAge = jo2.getInt("maxAge");
                    myActivity.minAge = jo2.getInt("minAge");
                    myActivity.district = jo2.getString("district");
                    myActivity.elseAstrict = jo2.getString("elseAstrict");
                    System.out.println("news2: " + myActivity.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage(UPDATEUSERDETAIL_TEXT).sendToTarget();
            }
        });
    }

    private void toCreate() {
        if (myActivity.maxAge != -1 && myActivity.minAge != -1 && myActivity.minAge > myActivity.maxAge){
            Toast.makeText(this, "最小年龄大于最大年龄", Toast.LENGTH_SHORT).show();
            return;
        }
        myActivity.title = et_title.getText().toString();
        myActivity.introduction = et_introduction.getText().toString();
        myActivity.start_time = Timestamp.valueOf(tv_start_time_text.getText().toString());
        myActivity.end_time = Timestamp.valueOf(tv_end_time_text.getText().toString());
        myActivity.people = Integer.parseInt(et_people.getText().toString());
        myActivity.place = et_place.getText().toString();
        myActivity.userName = et_username.getText().toString();
        myActivity.telephone = et_telephone.getText().toString();
        myActivity.team = et_team.getText().toString();
        myActivity.astrict = 0;
        myActivity.sex = 2;
        if (rg_yes.isChecked()){
            myActivity.astrict = 1;
        }
        if (rg_man.isChecked()){
            myActivity.sex = 1;
        }else if (rg_woman.isChecked()){
            myActivity.sex = 0;
        }
        myActivity.district = s_place.getSelectedItem().toString();
        myActivity.elseAstrict = et_elseAstrict.getText().toString();
        myActivity.content = et_content.getText().toString();

        if (myActivity.start_time.getTime() >= myActivity.end_time.getTime()){
            Toast.makeText(this, "开始时间大于等于结束时间", Toast.LENGTH_SHORT).show();
            return;
        }

        if (myActivity.elseAstrict.equals("") && myActivity.sex == 2 && myActivity.maxAge == -1 && myActivity.minAge == -1 && myActivity.district.equals("无限制")){
            myActivity.astrict = 0;
        }

        System.out.println("myActivity:" + myActivity);

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("type", "update")
                .addFormDataPart("id", String.valueOf(myActivity.id))
                .addFormDataPart("t_u_id", myActivity.t_u_id)
                .addFormDataPart("title", myActivity.title)
                .addFormDataPart("introduction", myActivity.introduction)
                .addFormDataPart("start_time", myActivity.start_time.toString())
                .addFormDataPart("end_time", myActivity.end_time.toString())
                .addFormDataPart("people", String.valueOf(myActivity.people))
                .addFormDataPart("place", myActivity.place)
                .addFormDataPart("userName", myActivity.userName)
                .addFormDataPart("telephone", myActivity.telephone)
                .addFormDataPart("team", myActivity.team)
                .addFormDataPart("astrict", String.valueOf(myActivity.astrict))
                .addFormDataPart("sex", String.valueOf(myActivity.sex))
                .addFormDataPart("minAge", String.valueOf(myActivity.minAge))
                .addFormDataPart("maxAge", String.valueOf(myActivity.maxAge))
                .addFormDataPart("district", myActivity.district)
                .addFormDataPart("elseAstrict", myActivity.elseAstrict)
                .addFormDataPart("content", myActivity.content)
                .addFormDataPart("pos_x", String.valueOf(myActivity.pos_x))
                .addFormDataPart("pos_y", String.valueOf(myActivity.pos_y));
        if (fileImage != null){
            Iterator iter = fileImage.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String inputName = (String) entry.getKey();
                String inputValue = (String) entry.getValue();
                if (inputValue == null) {
                    continue;
                }
                File file = new File(inputValue);
                String filename = file.getName();
                builder.addFormDataPart("file", filename, RequestBody.create(MEDIA_TYPE_PNG, new File(inputValue)));
            }
        }
        RequestBody requestBody = builder.build();
        final Request request = new Request.Builder()
                .url(ActivityUpdateUrl)
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
                    }else {
                        mHandler.obtainMessage(UPDATE_TEXT).sendToTarget();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toDelete() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "delete")
                .add("id", String.valueOf(myActivity.id))
                .build();
        final Request request = new Request.Builder()
                .url(ActivityUrl)
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
                        mHandler.obtainMessage(UNDELETE_TEXT).sendToTarget();
                    }else {
                        mHandler.obtainMessage(DELETE_TEXT).sendToTarget();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}