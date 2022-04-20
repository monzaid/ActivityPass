package com.example.social_practice_activity.discover;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.social_practice_activity.R;
import com.example.social_practice_activity.Utils.RealPathFromUriUtils;
import com.example.social_practice_activity.bean.myActivity;
import com.example.social_practice_activity.bean.myActivityRegister;
import com.example.social_practice_activity.mine.SandActivity;
import com.example.social_practice_activity.mine.TableActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ActivityDetail extends AppCompatActivity {

    private final int CAMERA = 1;//事件枚举(可以自定义)
    String photoPath = "";//要上传的图片路径
    private final int permissionCode = 100;//权限请求码
    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private double lat, lon;

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

    static String pattern = "yy-MM-dd HH:mm";
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "ActivityServlet";
    static private final String stuteUrl = Host[INTERNET] + "ActivityRegisterServlet";
    static private final String verifyFaceUrl = Host[INTERNET] + "VerifyFaceServlet";

    private ImageView iv_image;
    private TextView tv_title;
    private TextView tv_introduction;
    private TextView tv_team;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private TextView tv_username;
    private TextView tv_telephone;
    private TextView tv_place;
    private TextView tv_people;
    private TextView tv_limit;
    private TextView tv_content;
    private RadioButton rb_map;
//    private RadioButton rb_contact;
    private RadioButton rb_call;
    private RadioGroup rg_main;
    private Button btn_modify;
    private Button btn_join;
    private ImageView ib_title_back;

    private String ActivityId;
    private String userId;
    private myActivity myActivity;
    private myActivityRegister myActivityRegister;
    private StringBuffer sb_astrict = new StringBuffer();
    private int stute = 0;

    private LruCache<String, BitmapDrawable> mImageCache;

    private static final int UPDATE_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int JOIN_TEXT = 3;
    private static final int UNUPDATE_TEXT = 5;

    private static final int UPDATEJOIN_TEXT = 6;
    private static final int UPDATEWAIT_TEXT = 7;
    private static final int UPDATESIGNIN_TEXT = 8;
    private static final int UPDATESIGNOUT_TEXT = 9;
    private static final int UPDATEDONE_TEXT = 10;
    private static final int POSOK_TEXT = 11;
    private static final int POSUNOK_TEXT = 12;
    private static final int UNUPDATEPOS_TEXT = 13;
    private static final int UPDATEPOS_TEXT = 14;
    private static final int UPDATEDELETE_TEXT = 15;
    private static final int UPDATEFACE_TEXT = 16;
    private static final int UNUPDATEFACE_TEXT = 17;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @SuppressLint("ResourceAsColor")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TEXT:
                    // 如果本地已有缓存，就从本地读取，否则从网络请求数据
                    if (mImageCache.get(myActivity.image) != null) {
                        iv_image.setImageDrawable(mImageCache.get(myActivity.image));
                    } else {
                        ImageTask it = new ImageTask();
                        it.execute(myActivity.image);
                    }
                    tv_title.setText(myActivity.title);
                    tv_introduction.setText(myActivity.introduction);
                    tv_team.setText(myActivity.team);
                    tv_start_time.setText(myActivity.getStart_time());
                    tv_end_time.setText(myActivity.getEnd_time());
                    tv_username.setText(myActivity.userName);
                    tv_telephone.setText(myActivity.telephone);
                    tv_place.setText(myActivity.place);
                    tv_people.setText(String.valueOf(myActivity.people));
                    tv_limit.setText(sb_astrict.toString());
                    tv_content.setText(myActivity.content);
                    if (myActivity.t_u_id.equals(userId)){
//                        rb_contact.setVisibility(View.INVISIBLE);
                        rb_call.setVisibility(View.INVISIBLE);
                        btn_modify.setVisibility(View.VISIBLE);
                        stute = 5;
                        btn_join.setText("管理活动");
                    }else {
                        getStute();
                    }
                    break;
                case JOIN_TEXT:
                    Toast.makeText(ActivityDetail.this, "申请成功", Toast.LENGTH_SHORT).show();
                    stute = 1;
                    showBTN();
                    btn_join.setText("等待审核");
//                    btn_join.setBackgroundColor(R.color.gray);
                    btn_join.setClickable(false);
                    break;
                case UNUPDATE_TEXT:
                    Toast.makeText(ActivityDetail.this, "申请失败，这个活动有限制并且未完善个人消息", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(ActivityDetail.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATEJOIN_TEXT:
                    stute = 0;
                    break;
                case UPDATEWAIT_TEXT:
                    stute = 1;
                    btn_join.setText("等待审核");
//                    btn_join.setBackgroundColor(R.color.gray);
                    btn_join.setClickable(false);
                    showBTN();
                    break;
                case UPDATESIGNIN_TEXT:
                    stute = 2;
                    btn_modify.setVisibility(View.VISIBLE);
                    btn_join.setText("签到");
//                    btn_join.setBackgroundColor(R.color.green);
                    showBTN();
                    break;
                case UPDATESIGNOUT_TEXT:
                    stute = 3;
                    btn_join.setText("签退");
//                    btn_join.setBackgroundColor(R.color.green);
                    break;
                case UPDATEDONE_TEXT:
                    stute = 4;
                    btn_join.setText("已完成");
//                    btn_join.setBackgroundColor(R.color.gray);
                    btn_join.setClickable(false);
                    break;
                case POSOK_TEXT:
                    toPos();
                    break;
                case POSUNOK_TEXT:
                    Toast.makeText(ActivityDetail.this, String.format("位置签%s失败，请在活动地点附近500米范围内签到！", stute == 2 ? "到" : "退"), Toast.LENGTH_SHORT).show();
                    break;
                case UPDATEPOS_TEXT:
                    Toast.makeText(ActivityDetail.this, String.format("位置签%s成功\n请完成人脸识别签%s", stute == 2 ? "到" : "退", stute == 2 ? "到" : "退"), Toast.LENGTH_SHORT).show();
                    takePhoto();
                    break;
                case UNUPDATEPOS_TEXT:
                    Toast.makeText(ActivityDetail.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATEDELETE_TEXT:
                    stute = 0;
                    btn_modify.setVisibility(View.INVISIBLE);
                    btn_join.setText("报名参加");
                    btn_join.setClickable(true);
                    Toast.makeText(ActivityDetail.this, "取消成功", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATEFACE_TEXT:
                    stute++;
                    btn_modify.setVisibility(View.INVISIBLE);
                    btn_join.setText(String.format("%s", stute == 3 ? "签退" : "已完成"));
                    Toast.makeText(ActivityDetail.this, "人脸识别成功", Toast.LENGTH_SHORT).show();
                    break;
                case UNUPDATEFACE_TEXT:
                    Toast.makeText(ActivityDetail.this, "人脸识别失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    private void showBTN() {
        btn_modify.setText("取消参加");
        btn_modify.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = this.getIntent().getExtras();
        ActivityId = bundle.getString("id");
        userId = bundle.getString("userId");
        myActivity = new myActivity();
        myActivityRegister = new myActivityRegister();
        ib_title_back = findViewById(R.id.ib_title_back);
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_image = findViewById(R.id.iv_image);
        tv_title = findViewById(R.id.tv_title);
        tv_introduction = findViewById(R.id.tv_introduction);
        tv_team = findViewById(R.id.tv_team);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_username = findViewById(R.id.tv_username);
        tv_telephone = findViewById(R.id.tv_telephone);
        tv_place = findViewById(R.id.tv_place);
        tv_people = findViewById(R.id.tv_people);
        tv_limit = findViewById(R.id.tv_limit);
        tv_content = findViewById(R.id.tv_content);
        rb_map = findViewById(R.id.rb_map);
        rb_call = findViewById(R.id.rb_call);
//        rb_contact = findViewById(R.id.rb_contact);
        rg_main = findViewById(R.id.rg_main);
        btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setVisibility(View.INVISIBLE);
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myActivity.t_u_id.equals(userId)){
                    toModify();
                }else {
                    if (stute == 0 || stute == 1 || (stute == 2 && System.currentTimeMillis() < myActivity.start_time.getTime() - 10 * 60 * 1000)){
                        toDelete();
                    }else {
                        Toast.makeText(ActivityDetail.this, "不能取消参加！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btn_join = findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myActivity.t_u_id.equals(userId)){
                    toManage();
                }else {
                    if (stute == 0){
                        if (myActivity.start_time.getTime() < System.currentTimeMillis()){
                            Toast.makeText(ActivityDetail.this, "当前的时间大于活动开始的时间！", Toast.LENGTH_SHORT).show();
                        }else {
                            toJoin();
                        }
                    }else if (stute == 2 || stute == 3) {
                        toSign();
                    }
                }
            }
        });
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_map:
                        rb_map.setChecked(false);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("distance_x", String.valueOf(myActivity.pos_x));
                        bundle1.putString("distance_y", String.valueOf(myActivity.pos_y));
                        bundle1.putString("type", "navigation");
                        Intent mapIntent =  new Intent(ActivityDetail.this, MapActivity.class);
                        mapIntent.putExtras(bundle1);
                        startActivity(mapIntent);
                        break;
//                    case R.id.rb_contact:
//                        break;
                    case R.id.rb_call:
                        rb_call.setChecked(false);
                        Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + myActivity.telephone));//跳转到拨号界面，同时传递电话号码
                        startActivity(dialIntent);
                        break;
                }
            }
        });
        int maxCache = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxCache / 8;
        mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
        toDetail();
    }

    private void toSign() {
        long newTime = System.currentTimeMillis();
        long ten = 10 * 60 * 1000;
        long sign_p =  0;
        long sign_f = 0;
        if (stute == 2){
            sign_p = myActivity.start_time.getTime() - ten;
            sign_f = myActivity.start_time.getTime() + ten;
        }else {
            sign_p = myActivity.end_time.getTime() - ten;
            sign_f = myActivity.end_time.getTime() + ten;
        }
        if (newTime >= sign_p && newTime <= sign_f){
            if (stute == 2 && !myActivityRegister.sign_in_pos || stute == 3 && !myActivityRegister.sign_out_pos){
                initLocation();
                checkingAndroidVersion();
            }
            if (stute == 2 && myActivityRegister.sign_in_pos && !myActivityRegister.sign_in_face || stute == 3 && myActivityRegister.sign_out_pos && !myActivityRegister.sign_out_face){
                //6.0才用动态权限
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission();
                }
                takePhoto();
            }
        }else{
            Toast.makeText(ActivityDetail.this, String.format("还没到签%s的时间\n签%s的时间：%s ~ %s", stute == 2 ? "到" : "退", stute == 2 ? "到" : "退",
                    simpleDateFormat.format(new Timestamp(sign_p)), simpleDateFormat.format(new Timestamp(sign_f))), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查Android版本
     */
    private void checkingAndroidVersion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Android6.0及以上先获取权限再定位
            requestPermission();
        }else {
            //Android6.0以下直接定位
            mLocationClient.startLocation();
        }
    }


    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            /**
             * 接收异步返回的定位结果
             *
             * @param aMapLocation
             */
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //地址
                        String address = aMapLocation.getAddress();
                        lat = aMapLocation.getLatitude();
                        lon = aMapLocation.getLongitude();
                        double x_f = myActivity.pos_x + 0.005705;
                        double x_p = myActivity.pos_x - 0.005705;
                        double y_f = myActivity.pos_y + 0.004495;
                        double y_p = myActivity.pos_y - 0.004495;
                        if (lon >= x_p && lon <= x_f && lat >= y_p && lat <= y_f){
                            if (stute == 2){
                                myActivityRegister.sign_in_pos = true;
                            }else if (stute == 3){
                                myActivityRegister.sign_out_pos = true;
                            }
                            mHandler.obtainMessage(POSOK_TEXT).sendToTarget();
                        }else {
                            mHandler.obtainMessage(POSUNOK_TEXT).sendToTarget();
                        }
                        mLocationClient.onDestroy();
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }

        });
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制，高精度定位会产生缓存。
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }


    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(this, permissions)) {
            //true 有权限 开始定位
            mLocationClient.startLocation();
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }
    /**
     * 请求权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
                    //对应方法二：图片已保存，只需读取就行了
                    try {
                        FileInputStream stream = new FileInputStream(photoPath);
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);

                        //预览图片
//                        ImageButton image = findViewById(R.id.iv_image);
//                        image.setImageBitmap(bitmap);

                        //上传图片（Android 4.0 之后不能在主线程中请求HTTP请求）
                        File file = new File(photoPath);
                        if (file.exists()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //图片字段
                                    fileImage = new HashMap<String, String>();
                                    fileImage.put(RealPathFromUriUtils.getFileNameFromPath(photoPath), photoPath);
                                    verifyFace();
                                }
                            }).start();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void verifyFace() {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        System.out.println(String.valueOf(myActivityRegister.Id) + "       " + myActivityRegister.userId);
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("activityId", String.valueOf(ActivityId))
                .addFormDataPart("id", userId);
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
        }else{
            Toast.makeText(ActivityDetail.this, "未选择人脸图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = builder.build();
        final Request request = new Request.Builder()
                .url(verifyFaceUrl)
                .post(requestBody)
                .build();
        System.out.println(verifyFaceUrl);
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
                    System.out.println(jo1);
                    if (jo1.getString("ac").equals("false")){
                        System.out.println("1");
                        mHandler.obtainMessage(UNUPDATEFACE_TEXT).sendToTarget();
                    }else {
                        System.out.println("2");
                        mHandler.obtainMessage(UPDATEFACE_TEXT).sendToTarget();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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

    private void takePhoto() {
        System.out.println("vvvv");
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

    private void toModify() {
        Bundle bundle = new Bundle();
        bundle.putString("ActivityId", String.valueOf(ActivityId));
        bundle.putString("userId", userId);
        Intent intent = new Intent(ActivityDetail.this, SandActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void toManage() {
        Bundle bundle = new Bundle();
        bundle.putString("ActivityId", String.valueOf(ActivityId));
        bundle.putString("userId", userId);
        Intent intent = new Intent(ActivityDetail.this, TableActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void toJoin() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "joinActivity")
                .add("activityId", ActivityId)
                .add("userId", userId)
                .build();
        final Request request = new Request.Builder()
                .url(stuteUrl)
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
                    }else{
                        mHandler.obtainMessage(JOIN_TEXT).sendToTarget();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toDetail() {
        //TODO:btn stute
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "getActivity")
                .add("id", ActivityId)
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
                    if (myActivity.astrict == 0){
                        sb_astrict.append("无");
                    }else {
                        boolean ishas = false;
                        myActivity.sex = jo2.getInt("sex");
                        myActivity.maxAge = jo2.getInt("maxAge");
                        myActivity.minAge = jo2.getInt("minAge");
                        myActivity.district = jo2.getString("district");
                        myActivity.elseAstrict = jo2.getString("elseAstrict");
                        if (myActivity.sex != 2){
                            sb_astrict.append(String.format("性别限制：%s\n", myActivity.sex == 0 ? "女" : "男"));
                            ishas = true;
                        }
                        if (myActivity.minAge != -1){
                            sb_astrict.append(String.format("最小年龄：%s\n", myActivity.minAge));
                            ishas = true;
                        }
                        if (myActivity.maxAge != -1){
                            sb_astrict.append(String.format("最大年龄：%s\n", myActivity.maxAge));
                            ishas = true;
                        }
                        if (!myActivity.district.equals("无限制")){
                            sb_astrict.append(String.format("地区限制：活动的%s\n", myActivity.district));
                            ishas = true;
                        }
                        if (!myActivity.elseAstrict.equals("")){
                            sb_astrict.append(String.format("%s%s\n", ishas ? "其他限制：\n" : "", myActivity.elseAstrict));
                        }
                    }
                    System.out.println("news2: " + myActivity.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage(UPDATE_TEXT).sendToTarget();
            }
        });
    }

    private void getStute() {
        //TODO:btn stute
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "getActivityRegister")
                .add("activityId", ActivityId)
                .add("userId", userId)
                .build();
        final Request request = new Request.Builder()
                .url(stuteUrl)
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
                        mHandler.obtainMessage(UPDATEJOIN_TEXT).sendToTarget();
                        return;
                    }
                    myActivityRegister.register = jo1.getInt("register");
                    if (myActivityRegister.register == 1){
                        mHandler.obtainMessage(UPDATEWAIT_TEXT).sendToTarget();
                    }else if (myActivityRegister.register ==2 || myActivityRegister.register == 3){
//                        if (new Timestamp(System.currentTimeMillis()).getTime() < myActivity.end_time.getTime()){
                            myActivityRegister.sign_in_pos = jo1.getBoolean("sign_in_pos");
                            myActivityRegister.sign_in_face = jo1.getBoolean("sign_in_face");
                            myActivityRegister.sign_in_2 = jo1.getBoolean("sign_in_2");
                            myActivityRegister.sign_out_pos = jo1.getBoolean("sign_out_pos");
                            myActivityRegister.sign_out_face = jo1.getBoolean("sign_out_face");
                            myActivityRegister.sing_out_2 = jo1.getBoolean("sing_out_2");
                            if (myActivityRegister.sign_in_face && myActivityRegister.sign_in_pos && myActivityRegister.sign_out_pos && myActivityRegister.sign_out_face){
                                mHandler.obtainMessage(UPDATEDONE_TEXT).sendToTarget();
                            }else if (myActivityRegister.sign_in_face && myActivityRegister.sign_in_pos){
                                mHandler.obtainMessage(UPDATESIGNOUT_TEXT).sendToTarget();
                            }else {
                                mHandler.obtainMessage(UPDATESIGNIN_TEXT).sendToTarget();
                            }
//                        }
                    }else if (myActivityRegister.register == 4){
                        mHandler.obtainMessage(UPDATEDONE_TEXT).sendToTarget();
                    }
                    System.out.println("myActivityRegister: " + myActivityRegister.toString());
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
                .add("activityId", ActivityId)
                .add("userId", userId)
                .build();
        final Request request = new Request.Builder()
                .url(stuteUrl)
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
                        mHandler.obtainMessage(UPDATEJOIN_TEXT).sendToTarget();
                        return;
                    }
                    mHandler.obtainMessage(UPDATEDELETE_TEXT).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toPos() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "update")
                .add("t_a_id", ActivityId)
                .add("t_u_id", userId)
                .add("register", String.valueOf(myActivityRegister.register))
                .add("sign_in_pos", String.valueOf(myActivityRegister.sign_in_pos))
                .add("sign_in_face", String.valueOf(myActivityRegister.sign_in_face))
                .add("sign_in_2", String.valueOf(myActivityRegister.sign_in_2))
                .add("sign_out_pos", String.valueOf(myActivityRegister.sign_out_pos))
                .add("sign_out_face", String.valueOf(myActivityRegister.sign_out_face))
                .add("sing_out_2", String.valueOf(myActivityRegister.sing_out_2))
                .build();
        final Request request = new Request.Builder()
                .url(stuteUrl)
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
                        mHandler.obtainMessage(UNUPDATEPOS_TEXT).sendToTarget();
                        return;
                    }
                    mHandler.obtainMessage(UPDATEPOS_TEXT).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class ImageTask extends AsyncTask<String, Void, BitmapDrawable> {

        private String imageUrl;

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            Bitmap bitmap = downloadImage();
            BitmapDrawable db = new BitmapDrawable(getResources(),
                    bitmap);
            // 如果本地还没缓存该图片，就缓存
            if (mImageCache.get(imageUrl) == null) {
                mImageCache.put(imageUrl, db);
            }
            return db;
        }

        @Override
        protected void onPostExecute(BitmapDrawable result) {
//            // 通过Tag找到我们需要的ImageView，如果该ImageView所在的item已被移出页面，就会直接返回null
//            ImageView iv = (ImageView) listview.findViewWithTag(imageUrl);
//            if (iv != null && result != null) {
                iv_image.setImageDrawable(result);
//            }
        }

        /**
         * 根据url从网络上下载图片
         *
         * @return
         */
        private Bitmap downloadImage() {
            HttpURLConnection con = null;
            Bitmap bitmap = null;
            try {
                URL url = new URL(myActivity.image);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(con.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }

            return bitmap;
        }

    }
}