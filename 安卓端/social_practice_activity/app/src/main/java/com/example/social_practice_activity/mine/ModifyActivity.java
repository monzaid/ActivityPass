package com.example.social_practice_activity.mine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.Utils.RealPathFromUriUtils;
import com.example.social_practice_activity.bean.mySettings;
import com.example.social_practice_activity.myAdaper.SettingsAdaper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyActivity extends AppCompatActivity {
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "UserServlet";
    static private final String saveUrl = Host[INTERNET] + "SaveUserImageServlet";
    static private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");

    String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int REQUEST_PICK_IMAGE = 11101;


    private ListView listView;
    private String[] modify;
    private List<mySettings> mySettings = new ArrayList<>();
    private TextView tv_name;
    private String id;
    private String username;

    private static final int UPDATE_TEXT = 1;
    private static final int FAIL_TEXT = 2;
    private static final int UPDATEIMAGE_TEXT = 3;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            switch (msg.what) {
                case UPDATE_TEXT:
                    tv_name.setText(username);
                    Toast.makeText(ModifyActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                    Bundle bundle1 =  new Bundle();
                    setResult(RESULT_OK, intent);
                    break;
                case UPDATEIMAGE_TEXT:
                    Toast.makeText(ModifyActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, intent);
                case FAIL_TEXT:
                    Toast.makeText(ModifyActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        listView = findViewById(R.id.u);
        Bundle bundle = this.getIntent().getExtras();
        username = new String(bundle.getString("username"));
        id = new String(bundle.getString("id"));

        modify = getResources().getStringArray(R.array.modify);
        mySettings.add(new mySettings(modify[0], null));
        mySettings.add(new mySettings(modify[1], username));
        SettingsAdaper adaper = new SettingsAdaper(mySettings);
        listView.setAdapter(adaper);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mySettings settings = (mySettings) adapterView.getItemAtPosition(position);
                String action = settings.name;
                if (action.equals("头像")){
                    ActivityCompat.requestPermissions(ModifyActivity.this, mPermissionList, 100);
                }else if (action.equals("用户名")){
                    tv_name = view.findViewById(R.id.tv_content);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "更改用户名");
                    bundle.putString("text", username);
                    bundle.putString("tips", "好的名字可以让人眼前一亮。");
                    Intent intent = new Intent(ModifyActivity.this, SaveActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
                Log.e(this.getClass().getName(), "imageUri:" + realPathFromUri);
                toUpdateImage(realPathFromUri);
            } else {
                Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == 1 && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String text = bundle.getString("text").trim();
            Log.e(this.getClass().getName(), "username:" + text);
            toUpdateUsername(text);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean writeExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && writeExternalStorage && readExternalStorage) {
                    getImage();
                } else {
                    Toast.makeText(this, "请设置必要权限", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void getImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    2);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        }
    }


    private void toUpdateImage(String path) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", id + ".png", RequestBody.create(MEDIA_TYPE_PNG, new File(path)))
                .addFormDataPart("id", id)
                .build();
        final Request request = new Request.Builder()
                .url(saveUrl)
                .post(requestBody)
                .build();
        System.out.println(saveUrl + "  ssss ");
        client.newCall(request).enqueue(new Callback() {
            @Override
            //请求失败
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(FAIL_TEXT).sendToTarget();
            }

            @Override
            //请求成功
            public void onResponse(Call call, final Response response) throws IOException {
                mHandler.obtainMessage(UPDATEIMAGE_TEXT).sendToTarget();
            }
        });
    }

    private void toUpdateUsername(String text) {
        //TODO:保存
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "update")
                .add("id", id)
                .add("username", text)
                .build();
        //TODO:password
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
                username = new String(text);
                mHandler.obtainMessage(UPDATE_TEXT).sendToTarget();
            }
        });
    }

    /**
     * 根据图片的Uri获取图片的绝对路径(已经适配多种API)
     * @return 如果Uri对应的图片存在,那么返回该图片的绝对路径,否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) {
            // SDK < Api11
            return getRealPathFromUri_BelowApi11(context, uri);
        }
        if (sdkVersion < 19) {
            // SDK > 11 && SDK < 19
            return getRealPathFromUri_Api11To18(context, uri);
        }
        // SDK > 19
        return getRealFilePath(context, uri);
    }


    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    @SuppressLint("Range")
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA};

        CursorLoader loader = new CursorLoader(context, uri, projection, null,
                null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    @SuppressLint("Range")
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * @param context
     * @param uri
     * @return 文件绝对路径或者null
     */
    private static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}