package com.example.social_practice_activity.discover;

import static com.example.social_practice_activity.Utils.MapUtil.convertToLatLng;
import static com.example.social_practice_activity.Utils.MapUtil.convertToLatLonPoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.example.social_practice_activity.R;
import com.example.social_practice_activity.Utils.BusRouteOverlay;
import com.example.social_practice_activity.Utils.DrivingRouteOverlay;
import com.example.social_practice_activity.Utils.MapUtil;
import com.example.social_practice_activity.Utils.RideRouteOverlay;
import com.example.social_practice_activity.Utils.WalkRouteOverlay;
import com.example.social_practice_activity.bean.myActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends AppCompatActivity implements AMapLocationListener, LocationSource, PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, RouteSearch.OnRouteSearchListener {
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "ActivityServlet";

    private static final int FAIL_TEXT = 2;
    private static final int INITDATA_TEXT = 3;
    private static final int UNUPDATE_TEXT = 5;
    private MapView mapView;
    //输入框
    private EditText etAddress;
    private Spinner s_type;


    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //地图控制器
    private AMap aMap = null;
    //位置更改监听
    private OnLocationChangedListener mListener;

    //定位样式
    private MyLocationStyle myLocationStyle = new MyLocationStyle();

    //定义一个UiSettings对象
    private UiSettings mUiSettings;

    //POI查询对象
    private PoiSearch.Query query;
    //POI搜索对象
    private PoiSearch poiSearch;
    //城市码
    private String cityCode = null;
    //浮动按钮
    private FloatingActionButton fab;

    //地理编码搜索
    private GeocodeSearch geocodeSearch;
    //解析成功标识码
    private static final int PARSE_SUCCESS_CODE = 1000;

    //城市
    private String city;

    private double distance_x, distance_y;
    private String type;
    private Marker marker;
    private List<Marker> markers;
    private String userId;

    private List<myActivity> list = new ArrayList<>();
    private List<myActivity> temp = new ArrayList<>();

    //起点
    private LatLonPoint mStartPoint;
    //终点
    private LatLonPoint mEndPoint;
    //路线搜索对象
    private RouteSearch routeSearch;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITDATA_TEXT:
//                    list.clear();
//                    list.addAll(temp);
                    if (markers != null && markers.size()>0){
                        for (Marker markerItem : markers) {
                            markerItem.remove();
                        }
                    }
                    markers = new ArrayList<Marker>();
                    for (myActivity myActivity : temp){
                        Marker m = aMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myActivity.pos_y, myActivity.pos_x))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.destination))
                                .title("活动标题：" + myActivity.title)
                                .snippet("简介：" + myActivity.introduction + "\n" +
                                "开始时间：" + myActivity.getStart_time() + "\n" +
                                "人数" + myActivity.people));
//                        Animation animation = new RotateAnimation(m.getRotateAngle(), );
//                        long duration = 1000L;
//                        animation.setDuration(duration);
//                        animation.setInterpolator(new LinearInterpolator());

//                        m.setAnimation(animation);
//                        m.startAnimation();
                        markers.add(m);
                    }
                    break;
                case UNUPDATE_TEXT:
                    Toast.makeText(MapActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_TEXT:
                    Toast.makeText(MapActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle bundle = this.getIntent().getExtras();
        distance_x = Double.parseDouble(bundle.getString("distance_x"));
        distance_y = Double.parseDouble(bundle.getString("distance_y"));
        type = bundle.getString("type");
        System.out.println(distance_x + " " + distance_y);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        fab = findViewById(R.id.fab);
        if (type.equals("navigation")){
            fab.setImageResource(R.mipmap.destination_pos);
        }else if (type.equals("setPos")){
            Toast.makeText(MapActivity.this, "点击地图来设置签到和签退的位置", Toast.LENGTH_LONG).show();
            fab.setImageResource(R.mipmap.save);
        }else if (type.equals("search")){
            fab.setImageResource(R.mipmap.search_pos);
            userId = bundle.getString("id");
        }
        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * 浮动按钮点击跳的目的地
             * @param view
             */
            public void onClick(View view) {
                if (type.equals("navigation")) {
                    updateMapCenter(new LatLng(distance_y, distance_x));
                }else if (type.equals("setPos")){
                    Bundle bundle = new Bundle();
                    bundle.putString("distance_x", String.valueOf(distance_x));
                    bundle.putString("distance_y", String.valueOf(distance_y));
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    if (distance_y == -1 || distance_x == -1){
                        setResult(RESULT_CANCELED, intent);
                    }else{
                        setResult(RESULT_OK, intent);
                    }
                    finish();
                }else if (type.equals("search")){
                    getActivityDate();
                }
            }
        });
        etAddress = findViewById(R.id.et_address);
        etAddress.setOnKeyListener(new View.OnKeyListener() {
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
                if (type.equals("navigation")){
                    return false;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    //获取输入框的值
                    String address = etAddress.getText().toString().trim();
                    if (address == null || address.isEmpty()) {
                        showMsg("请输入地址");
                    }else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        //隐藏软键盘
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
                        GeocodeQuery query = new GeocodeQuery(address,city);
                        geocodeSearch.getFromLocationNameAsyn(query);
                    }
                    return true;
                }
                return false;
            }

        });
        s_type = findViewById(R.id.s_type);

        s_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!type.equals("navigation")){
                    return;
                }
                String SpItemSelected = s_type.getSelectedItem().toString();
                System.out.println("---" + SpItemSelected);
                if (SpItemSelected.equals("步行")){
                    startRouteSearch();
                }else if (SpItemSelected.equals("骑行")){
                    startRouteSearch();
                }else if (SpItemSelected.equals("驾车")){
                    startRouteSearch();
                }else if (SpItemSelected.equals("公交")){
                    startRouteSearch();
                }else {
                    aMap.clear();// 清理地图上的所有覆盖物
                    aMap.addMarker(new MarkerOptions().position(new LatLng(distance_y, distance_x))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.destination))
                            .snippet("终点"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (type.equals("navigation")){
            s_type.setVisibility(View.VISIBLE);
            etAddress.setVisibility(View.GONE);
        }else {
            s_type.setVisibility(View.GONE);
            etAddress.setVisibility(View.VISIBLE);
        }

        //构造 GeocodeSearch 对象
        geocodeSearch = new GeocodeSearch(MapActivity.this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                //解析result获取地址描述信息
                if(rCode == PARSE_SUCCESS_CODE){
                    RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                    //显示解析后的地址
                    showMsg("地址："+regeocodeAddress.getFormatAddress());
                }else {
                    showMsg("获取地址失败");
                }

            }


            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
                if (rCode == PARSE_SUCCESS_CODE) {
                    List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
                    if(geocodeAddressList!=null && geocodeAddressList.size()>0){
                        LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
                        //显示解析后的坐标
                        showMsg("坐标：" + latLonPoint.getLongitude()+"，"+latLonPoint.getLatitude());
                        updateMapCenter(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                    }

                } else {
                    showMsg("获取坐标失败");
                }
            }

        });

        initLocation();

        initMap(savedInstanceState);

        checkingAndroidVersion();

        initRoute();
    }

    private void getActivityDate() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "pageByPos")
                .add("pos_x", String.valueOf(distance_x))
                .add("pos_y", String.valueOf(distance_y))
                .add("distance", "0.051")//5km
                .add("pageNo", "1")
                .add("pageSize", "9999")
                .build();
        final Request request = new Request.Builder()
                .url(Url)
                .post(requestBody)
                .build();
        System.out.println(distance_x + " " + distance_y);
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
                    JSONArray jsonArray = jo1.getJSONArray("data");
                    if (jsonArray.length() == 0){
                        mHandler.obtainMessage(UNUPDATE_TEXT).sendToTarget();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        myActivity news = new myActivity();
                        news.id = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                        news.title = jsonArray.getJSONObject(i).getString("title");
                        news.introduction = jsonArray.getJSONObject(i).getString("introduction");
                        news.start_time = Timestamp.valueOf(jsonArray.getJSONObject(i).getString("start_time"));
                        news.people = Integer.parseInt(jsonArray.getJSONObject(i).getString("people"));
                        news.pos_x = Double.parseDouble(jsonArray.getJSONObject(i).getString("pos_x"));
                        news.pos_y = Double.parseDouble(jsonArray.getJSONObject(i).getString("pos_y"));
                        temp.add(news);
                        System.out.println("news1: " + temp.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage(INITDATA_TEXT).sendToTarget();
            }
        });
    }

    /**
     * 通过经纬度获取地址
     * @param latLng
     */
    private void latlonToAddress(LatLng latLng) {
        //位置点  通过经纬度进行构建
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        //逆编码查询  第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 20, GeocodeSearch.AMAP);
        //异步获取地址信息
        geocodeSearch.getFromLocationAsyn(query);
    }


    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
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
     * 初始化地图
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mapView = findViewById(R.id.map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mapView.getMap();

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (type.equals("navigation")){
                    return;
                }
//                showMsg("点击了地图，经度："+latLng.longitude+"，纬度："+latLng.latitude);
                latlonToAddress(latLng);
                //添加标点
                addNarker(latLng);
            }
        });

        aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (type.equals("navigation")){
                    return;
                }
//                showMsg("长按了地图，经度："+latLng.longitude+"，纬度："+latLng.latitude);
                latlonToAddress(latLng);
                //添加标点
                addNarker(latLng);
            }
        });

//        // 自定义定位蓝点图标
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.local_pos));
//        // 自定义精度范围的圆形边框颜色  都为0则透明
//        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
//        // 自定义精度范围的圆形边框宽度  0 无宽度
//        myLocationStyle.strokeWidth(0);
//        // 设置圆形的填充颜色  都为0则透明
//        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
//
//        //设置定位蓝点的Style
//        aMap.setMyLocationStyle(myLocationStyle);
//
//        //设置最小缩放等级为14 ，缩放级别范围为[3, 20]
//        aMap.setMinZoomLevel(14);
//        //室内地图
//        aMap.showIndoorMap(true);

        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);

        aMap.setOnMarkerClickListener(this);

        aMap.setOnInfoWindowClickListener(this);

        //实例化UiSettings类对象
        mUiSettings = aMap.getUiSettings();
//        //隐藏缩放按钮
//        mUiSettings.setZoomControlsEnabled(false);

        //显示比例尺 默认不显示
        mUiSettings.setScaleControlsEnabled(true);

        // 是否显示定位按钮（据我所知不能更改，知道的麻烦告我一声）
        mUiSettings.setMyLocationButtonEnabled(true);
        //添加指南针
//        mUiSettings.setCompassEnabled(true);
//
//        aMap.getCameraPosition(); //方法可以获取地图的旋转角度


        //管理缩放控件
//        mUiSettings.setZoomControlsEnabled(true);
        //设置logo位置，左下，底部居中，右下（隐藏logo：settings.setLogoLeftMargin(9000)）
        mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);

        //添加标点
        if (type.equals("navigation")){
            aMap.addMarker(new MarkerOptions().position(new LatLng(distance_y, distance_x))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.destination))
                    .snippet("终点"));
            mEndPoint = convertToLatLonPoint(new LatLng(distance_y, distance_x));
        }else{
            addNarker(new LatLng(distance_y, distance_x));
        }

    }

    public void addNarker(LatLng latLng){
        if (marker != null){
            marker.remove();
        }
        marker = aMap.addMarker(new MarkerOptions().position(latLng));
        distance_x = latLng.longitude;
        distance_y = latLng.latitude;
        updateMapCenter(new LatLng(distance_y, distance_x));
    }

    /**
     * Marker点击事件
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        //showMsg("点击了标点");
        //显示InfoWindow
        if (!marker.isInfoWindowShown()) {
            //显示
            marker.showInfoWindow();
        } else {
            //隐藏
            marker.hideInfoWindow();
        }
        return true;
    }

    /**
     * InfoWindow点击事件
     *
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
//        showMsg("弹窗内容：标题：" + marker.getTitle() + "\n片段：" + marker.getSnippet());
        int id = temp.get(markers.indexOf(marker)).id;
        Bundle bundle = new Bundle();
        bundle.putString("id", String.valueOf(id));
        bundle.putString("userId", userId);
        Intent intent = new Intent(MapActivity.this, ActivityDetail.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
//            showMsg("已获得权限，可以定位啦！");
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

    /**
     * Toast提示
     * @param msg 提示内容
     */
    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 接收异步返回的定位结果
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
//                StringBuffer sb = new StringBuffer();
                double lat = aMapLocation.getLatitude();
                double lon = aMapLocation.getLongitude();
                //地址
//                String address = aMapLocation.getAddress();
//                sb.append("维度:" + lat + "\n");
//                sb.append("经度:" + lon + "\n");
//                sb.append("地址:" + address + "\n");
//                System.out.println(sb.toString());
//                tv.setText(address == null ? "无地址" : address);
//                showMsg(address);

                if (type.equals("setPos") || type.equals("search")){
                    if (distance_x == -1 || distance_y == -1) {
                        distance_x = lon;
                        distance_y = lat;
                    }
                    //添加标点
                    addNarker(new LatLng(distance_y, distance_x));
                }

                //城市赋值
                city = aMapLocation.getCity();

                //停止定位后，本地定位服务并不会被销毁
                mLocationClient.stopLocation();

                //显示地图定位结果
                if (mListener != null){
                    //显示系统图标
                    mListener.onLocationChanged(aMapLocation);
                }

                //显示浮动按钮
                fab.show();
                //赋值
                cityCode = aMapLocation.getCityCode();
                if (type.equals("navigation")) {
                    //移动地图
                    updateMapCenter(new LatLng(distance_y, distance_x));
                }

                //设置起点
                mStartPoint = convertToLatLonPoint(new LatLng(lat, lon));

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
//                if (isFirstLoc) {
//
//                    //设置缩放级别（缩放级别为4-20级）
////                    aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomlevel));
//                    //将地图移动到定位点
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
//                    //点击定位按钮 能够将地图的中心移动到定位点
//                    mListener.onLocationChanged(aMapLocation);
//                    //获取定位信息
//                    StringBuffer buffer = new StringBuffer();
//                    buffer.append(aMapLocation.getCountry() + ""
//                            + aMapLocation.getProvince() + ""
//                            + aMapLocation.getCity() + ""
//                            + aMapLocation.getProvince() + ""
//                            + aMapLocation.getDistrict() + ""
//                            + aMapLocation.getStreet() + ""
//                            + aMapLocation.getStreetNum());
//                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
//                    isFirstLoc = false;
//                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁定位客户端，同时销毁本地定位服务。
        mLocationClient.onDestroy();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * POI搜索返回
     *
     * @param poiResult POI所有数据
     * @param i
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        //解析result获取POI信息

        //获取POI组数列表
        ArrayList<PoiItem> poiItems = poiResult.getPois();
        for (PoiItem poiItem : poiItems) {
            Log.d("MainActivity", " Title：" + poiItem.getTitle() + " Snippet：" + poiItem.getSnippet());
        }
    }

    /**
     * POI中的项目搜索返回
     *
     * @param poiItem 获取POI item
     * @param i
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 改变地图中心位置
     * @param latLng 位置
     */
    private void updateMapCenter(LatLng latLng) {
        // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
        // CameraPosition 第二个参数： 目标可视区域的缩放级别
        // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
        // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
        CameraPosition cameraPosition = new CameraPosition(latLng, aMap.getCameraPosition().zoom, aMap.getCameraPosition().tilt, aMap.getCameraPosition().bearing);
        //位置变更
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//        //改变位置
//        aMap.moveCamera(cameraUpdate);
        //带动画的移动
        aMap.animateCamera(cameraUpdate);

    }

    /**
     * 初始化路线
     */
    private void initRoute() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
    }

    /**
     * 公交规划路径结果
     *
     * @param busRouteResult 结果
     * @param code           结果码
     */
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int code) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (code == AMapException.CODE_AMAP_SUCCESS) {
            if (busRouteResult != null && busRouteResult.getPaths() != null) {
                if (busRouteResult.getPaths().size() > 0) {
                    final BusPath busPath = busRouteResult.getPaths().get(0);
                    if (busPath == null) {
                        return;
                    }
                    BusRouteOverlay busRouteOverlay = new BusRouteOverlay(
                            this, aMap, busPath,
                            busRouteResult.getStartPos(),
                            busRouteResult.getTargetPos());
                    busRouteOverlay.removeFromMap();
                    busRouteOverlay.addToMap();
                    busRouteOverlay.zoomToSpan();

                    int dis = (int) busPath.getDistance();
                    int dur = (int) busPath.getDuration();
                    String des = MapUtil.getFriendlyTime(dur) + "(" + MapUtil.getFriendlyLength(dis) + ")";
                    Log.d("Map_TAG", des);
                } else if (busRouteResult.getPaths() == null) {
                    showMsg("对不起，没有搜索到相关数据！");
                }
            } else {
                showMsg("对不起，没有搜索到相关数据！");
            }
        } else {
            showMsg("错误码；" + code);
        }
    }



    /**
     * 驾车规划路径结果
     *
     * @param driveRouteResult 结果
     * @param code            结果码
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int code) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (code == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    final DrivePath drivePath = driveRouteResult.getPaths()
                            .get(0);
                    if(drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            driveRouteResult.getStartPos(),
                            driveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();

                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = MapUtil.getFriendlyTime(dur)+"("+MapUtil.getFriendlyLength(dis)+")";
                    Log.d("Map_TAG", des);
                } else if (driveRouteResult.getPaths() == null) {
                    showMsg("对不起，没有搜索到相关数据！");
                }
            } else {
                showMsg("对不起，没有搜索到相关数据！");
            }
        } else {
            showMsg("错误码；" + code);
        }
    }


    /**
     * 步行规划路径结果
     *
     * @param walkRouteResult 结果
     * @param code            结果码
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int code) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (code == AMapException.CODE_AMAP_SUCCESS) {
            if (walkRouteResult != null && walkRouteResult.getPaths() != null) {
                if (walkRouteResult.getPaths().size() > 0) {
                    final WalkPath walkPath = walkRouteResult.getPaths().get(0);
                    if (walkPath == null) {
                        return;
                    }
                    //绘制路线
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            walkRouteResult.getStartPos(),
                            walkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();

                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = MapUtil.getFriendlyTime(dur) + "(" + MapUtil.getFriendlyLength(dis) + ")";
                    Log.d("Map_TAG", des);

//                    //显示步行花费时间
//                    tvTime.setText(des);
//                    bottomLayout.setVisibility(View.VISIBLE);
//                    //跳转到路线详情页面
//                    bottomLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(MapActivity.this,
//                                    MapDetailActivity.class);
//                            intent.putExtra("type",0);
//                            intent.putExtra("path", walkPath);
//                            startActivity(intent);
//                        }
//                    });


                } else if (walkRouteResult.getPaths() == null) {
                    showMsg("对不起，没有搜索到相关数据！");
                }
            } else {
                showMsg("对不起，没有搜索到相关数据！");
            }
        } else {
            showMsg("错误码；" + code);
        }
    }


    /**
     * 骑行规划路径结果
     *
     * @param rideRouteResult 结果
     * @param code            结果码
     */
    @Override
    public void onRideRouteSearched(final RideRouteResult rideRouteResult, int code) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (code == AMapException.CODE_AMAP_SUCCESS) {
            if (rideRouteResult != null && rideRouteResult.getPaths() != null) {
                if (rideRouteResult.getPaths().size() > 0) {
                    final RidePath ridePath = rideRouteResult.getPaths()
                            .get(0);
                    if(ridePath == null) {
                        return;
                    }
                    RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(
                            this, aMap, ridePath,
                            rideRouteResult.getStartPos(),
                            rideRouteResult.getTargetPos());
                    rideRouteOverlay.removeFromMap();
                    rideRouteOverlay.addToMap();
                    rideRouteOverlay.zoomToSpan();

                    int dis = (int) ridePath.getDistance();
                    int dur = (int) ridePath.getDuration();
                    String des = MapUtil.getFriendlyTime(dur)+"("+MapUtil.getFriendlyLength(dis)+")";
                    Log.d("Map_TAG", des);

                } else if (rideRouteResult.getPaths() == null) {
                    showMsg("对不起，没有搜索到相关数据！");
                }
            } else {
                showMsg("对不起，没有搜索到相关数据！");
            }
        } else {
            showMsg("错误码；" + code);
        }
    }



    /**
     * 开始路线搜索
     */
    private void startRouteSearch() {
        //在地图上添加起点Marker
        aMap.addMarker(new MarkerOptions()
                .position(convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_pos))
                .snippet("起点"));

        //搜索路线 构建路径的起终点
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (s_type.getSelectedItemPosition() == 1) {
            //构建步行路线搜索对象
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
            // 异步路径规划步行模式查询
            routeSearch.calculateWalkRouteAsyn(query);
        }else if (s_type.getSelectedItemPosition() == 2) {
            //构建骑行路线搜索对象
            RouteSearch.RideRouteQuery rideQuery = new RouteSearch.RideRouteQuery(fromAndTo, RouteSearch.WalkDefault);
            //骑行规划路径计算
            routeSearch.calculateRideRouteAsyn(rideQuery);
        }else if (s_type.getSelectedItemPosition() == 3) {
            //构建驾车路线搜索对象  剩余三个参数分别是：途经点、避让区域、避让道路
            RouteSearch.DriveRouteQuery driveQuery = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.WalkDefault, null, null, "");
            //驾车规划路径计算
            routeSearch.calculateDriveRouteAsyn(driveQuery);
        }else if (s_type.getSelectedItemPosition() == 4) {
            //构建驾车路线搜索对象 第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算,1表示计算
            RouteSearch.BusRouteQuery busQuery = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusLeaseWalk, city, 0);
            //公交规划路径计算
            routeSearch.calculateBusRouteAsyn(busQuery);
        }
    }

}