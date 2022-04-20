package com.example.social_practice_activity.mine;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.Utils.RealPathFromUriUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ImageActivity extends AppCompatActivity {
    private Button btnPhoto;
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
    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    AlertDialog alertDialog;
    private Button takePhotoBtn;
    private Button pickPictureBtn;
    private Button deletePhotoBtn;
    private Button cancelBtn;

    private TextView btn_save;
    private ImageView btn_back;
    private String id;
    private Boolean image_empty;

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "UpFaceServlet";


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
                    ac = true;
                    break;
                case UNUPDATE_TEXT:
                    ac = false;
                    break;
                case FAIL_TEXT:
                    ac = false;
                    break;
                default:
                    break;
            }
            System.out.println(ac);
            Intent intent = new Intent();
            if (ac){
                setResult(RESULT_OK, intent);
            }else{
                setResult(RESULT_CANCELED, intent);
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        image_empty = true;

        //6.0才用动态权限
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }

        btnPhoto = findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPicturePopupWindow();
            }
        });

//        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
//        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.save_title_bar, null);
//        ActionBar actionBar = getActionBar();
//        actionBar.setCustomView(mActionBarView, lp);
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoPath.equals("")){
                    Toast.makeText(ImageActivity.this, "未选择照片！", Toast.LENGTH_SHORT).show();
                    return;
                }
                toUp();
            }
        });
        btn_back = findViewById(R.id.ib_title_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void toUp() {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("id", id);
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
            Toast.makeText(ImageActivity.this, "未选择人脸图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = builder.build();
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
                    System.out.println(jo1);
                    if (jo1.getString("ac").equals("false")){
                        System.out.println("1");
                        mHandler.obtainMessage(UNUPDATE_TEXT).sendToTarget();
                    }else {
                        System.out.println("2");
                        mHandler.obtainMessage(UPDATE_TEXT).sendToTarget();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void SelectPicturePopupWindow() {
        View view = LayoutInflater.from(ImageActivity.this).inflate(R.layout.layout_popupwindow, null, false);
        takePhotoBtn = (Button) view.findViewById(R.id.picture_selector_take_photo_btn);
        pickPictureBtn = (Button) view.findViewById(R.id.picture_selector_pick_picture_btn);
        deletePhotoBtn = (Button) view.findViewById(R.id.picture_selector_delete_picture_btn);
        cancelBtn = (Button) view.findViewById(R.id.picture_selector_cancel_btn);

        deletePhotoBtn.setVisibility(!image_empty ? View.VISIBLE : View.GONE);

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
        deletePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//预览图片
                ImageView image = findViewById(R.id.imageView);
                image.setImageBitmap(null);
                fileImage = new HashMap<String, String>();
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                }
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

    //授权后回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean haspermission = false;
        if (permissionCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    haspermission = true;
                }
            }
            if (haspermission) {
                //跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
//                permissionDialog();
                requestPermission();
            } else {
                //全部权限通过，可以进行下一步操作
            }
        }
    }

    //打开手动设置应用权限
    private void permissionDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示信息")
                    .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            Uri packageURI = Uri.parse("package:" + getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                        }
                    })
                    .create();
        }
        alertDialog.show();
    }

    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {

        if (EasyPermissions.hasPermissions(this, permissions)) {
            //true 有权限 开始定位
//            showMsg("已获得权限，可以定位啦！");
//            mLocationClient.startLocation();
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }

    //用户取消授权
    private void cancelPermissionDialog() {
        alertDialog.cancel();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 调用照相机拍照
            case CAMERA:
                if (resultCode == RESULT_OK) {
                    image_empty = false;
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
                        bitmap = scaleBitmap(bitmap, (float) 0.5, 270);

                        //预览图片
                        ImageView image = findViewById(R.id.imageView);
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
                    image_empty = false;
                    try {
                        Uri uri = data.getData();
                        photoPath = RealPathFromUriUtils.getRealPathFromUri(ImageActivity.this, uri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                        //压缩图片
                        bitmap = scaleBitmap(bitmap, (float) 0.5, 90);

                        //预览图片
                        ImageView image = findViewById(R.id.imageView);
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
        }
    }

    //压缩图片
    public Bitmap scaleBitmap(Bitmap origin, float ratio, int angle) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return newBM;
    }


}