package com.itcast.whw.activity.search_near;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.itcast.whw.R;
import com.itcast.whw.adapter.MapListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看附近的界面
 */
public class NearMapActivity extends AppCompatActivity implements View.OnClickListener {

    private LocationClient mLocationClient;
    private EditText input_keyboard;
    private Button search_btn;
    private MapView baidu_view;
    private BaiduMap baidu_map;

    private boolean isFirstLocate = true;
    private PoiSearch mPoiSearch;

    private double mCurrentLantitude;

    private double mCurrentLongitude;
    private List<Poi> poiList;
    private BDLocation location;
    private ListView info_list_view;
    private List<PoiInfo> mPoiInfo_list = new ArrayList<>();
    private String current_city;
    private PoiInfo firstPoiInfo;
    private final int NEARSEARCH = 0;
    private final int CITYSEARCH = 1;
    private MyPoiListener mPoiListener;
    private MapListAdapter mapListAdapter;
    private FloatingActionButton back_location;
    private Toolbar map_tool_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_near_map);

        //初始化控件
        initView();

        //动态申请地图相关权限
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(NearMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(NearMapActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(NearMapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(NearMapActivity.this, permissions, 1);
        } else {
            requestLocation();
        }


    }

    //开启定位
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    //配置SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    //地位信息的回调接口
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                return;
            }

            if (isFirstLocate) {
                mCurrentLantitude = bdLocation.getLatitude();
                mCurrentLongitude = bdLocation.getLongitude();
                current_city = bdLocation.getCity();
                poiList = bdLocation.getPoiList();
                if (poiList.size() > 0 && poiList != null) {
                    Log.d("NearMapActivity", "poiList.size():" + poiList.size());
                    for (Poi p : poiList) {
                        Log.d("NearMapActivity", p.getId() + p.getName());
                    }
                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(mCurrentLantitude)
                        .longitude(mCurrentLongitude).build();

                // 设置定位数据
                baidu_map.setMyLocationData(locData);
                LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll, 16);//设置地图中心及缩放级别
                baidu_map.animateMapStatus(update);
                isFirstLocate = false;
                location = bdLocation;
                nearCurrentdata(ll);
            }
        }
    }

    //显示当前位置附近的地点
    private void nearCurrentdata(LatLng ll) {
        GeoCoder geoCoder = GeoCoder.newInstance();
        //
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    Toast.makeText(NearMapActivity.this, "抱歉，未能找到结果",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                ArrayList<PoiInfo> poiList = (ArrayList<PoiInfo>) result.getPoiList();
                if (poiList != null) {
                    mPoiInfo_list = poiList;
                }
                mapListAdapter = new MapListAdapter(mPoiInfo_list, NearMapActivity.this);
                info_list_view.setAdapter(mapListAdapter);
                mapListAdapter.notifyDataSetChanged();

            }

            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
    }

    //关键字城市的检索
    private void search(BDLocation location, String key) {
        /**
         * 搜索城市内POI
         */
        mPoiListener.setSearch_Type(CITYSEARCH);
        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
        citySearchOption.city(current_city);//城市名称,最小到区级单位
        citySearchOption.keyword(key);
        citySearchOption.isReturnAddr(true);//是否返回门址类信息：xx区xx路xx号
        //citySearchOption.pageNum(10);
        citySearchOption.pageCapacity(50);//设置每页查询的个数，默认10个
        mPoiSearch.searchInCity(citySearchOption);//查询
    }

    //关键字周围查询
    private void keyNearSearch(String key, LatLng location) {
        /**
         * 搜索位置点周边POI
         */
        mPoiListener.setSearch_Type(NEARSEARCH);
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        final PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption(); //POI附近检索参数设置类
        nearbySearchOption.location(latLng);//搜索的位置点
        nearbySearchOption.radius(5000);//搜索覆盖半径
        nearbySearchOption.keyword(key);
        nearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);//搜索类型，从近至远
        nearbySearchOption.pageCapacity(50);//设置每页查询的个数，默认10个
        mPoiSearch.searchNearby(nearbySearchOption);

    }

    //兴趣点检索结果回调接口
    private class MyPoiListener implements OnGetPoiSearchResultListener {
        private int search_Type;

        public void setSearch_Type(int search_Type) {
            this.search_Type = search_Type;
        }

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                Toast.makeText(NearMapActivity.this, "未找到结果",
                        Toast.LENGTH_LONG).show();
                return;

            }

            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                List<PoiInfo> allPoi = poiResult.getAllPoi();
                if (allPoi != null) {
                    firstPoiInfo = allPoi.get(0);
                    MyLocationData locData = new MyLocationData.Builder()
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(100).latitude(firstPoiInfo.location.latitude)
                            .longitude(firstPoiInfo.location.longitude).build();
                    if (search_Type == CITYSEARCH) {
                        Log.d("MyPoiListener", "first:" + firstPoiInfo.name);
                        // 设置定位数据
                        baidu_map.setMyLocationData(locData);
                        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(new LatLng(firstPoiInfo.location.latitude, firstPoiInfo.location.longitude));
                        baidu_map.animateMapStatus(update);
                        keyNearSearch(firstPoiInfo.name, firstPoiInfo.location);
                    } else if (search_Type == NEARSEARCH) {
                        Log.d("MyPoiListener", poiResult.getCurrentPageNum() + "----" + poiResult.getTotalPageNum());
                        if (poiResult.getCurrentPageNum() <= poiResult.getTotalPageNum()) {
                            List<PoiInfo> newPageInfo = poiResult.getAllPoi();
                            for (PoiInfo p : newPageInfo) {
                                Log.d("MyPoiListener", "p:" + p);
                            }
                            mPoiInfo_list = newPageInfo;
                        }

                        mapListAdapter = new MapListAdapter(mPoiInfo_list, NearMapActivity.this);
                        info_list_view.setAdapter(mapListAdapter);
                        mapListAdapter.notifyDataSetChanged();
                    }

//                    for (PoiInfo p : allPoi) {
//                        Log.d("NearMapActivity", p.address + "---" + p.name + "---" + p.phoneNum + "---" + p.street_id + "---" + p.type);
//                    }
//                    Log.d("MyPoiListener", "------------------------");
                }
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    }

    //申请权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "拒绝权限将无法正常使用该功能", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                requestLocation();
            } else {
                Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //初始化控件
    private void initView() {

        input_keyboard = (EditText) findViewById(R.id.input_keyboard);
        input_keyboard.setOnClickListener(this);
        search_btn = (Button) findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);
        baidu_view = (MapView) findViewById(R.id.baidu_map);

        //获取百度地图对象
        baidu_map = baidu_view.getMap();
        baidu_map.setMyLocationEnabled(true);

        //获取检索兴趣点对象
        mPoiSearch = PoiSearch.newInstance();
        mPoiListener = new MyPoiListener();
        mPoiListener.setSearch_Type(CITYSEARCH);
        mPoiSearch.setOnGetPoiSearchResultListener(mPoiListener);

        info_list_view = (ListView) findViewById(R.id.info_list_view);

        info_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo poiInfo = mPoiInfo_list.get(position);
                LatLng latLng = poiInfo.location;
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(100f)
                        .direction(100).latitude(latLng.latitude)
                        .longitude(latLng.longitude).build();
                baidu_map.setMyLocationData(locData);
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(latLng, 16);//设置地图中心及缩放级别
                baidu_map.animateMapStatus(update);
            }
        });
        back_location = (FloatingActionButton) findViewById(R.id.back_location);
        back_location.setOnClickListener(this);
        map_tool_bar = (Toolbar) findViewById(R.id.map_tool_bar);

        setSupportActionBar(map_tool_bar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        baidu_view.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        baidu_view.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        baidu_view.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //搜索按钮点击
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                submit();
                break;
            case R.id.back_location:

                LatLng latLng = new LatLng(mCurrentLantitude, mCurrentLongitude);
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(100f)
                        .direction(100).latitude(latLng.latitude)
                        .longitude(latLng.longitude).build();
                baidu_map.setMyLocationData(locData);
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(latLng, 16f);
                baidu_map.animateMapStatus(update);
                break;
        }
    }

    //提交搜索查询
    private void submit() {
        String keyboard = input_keyboard.getText().toString().trim();
        if (TextUtils.isEmpty(keyboard)) {
            Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
            return;
        }

        //关键字搜索
        search(location, keyboard);
    }
}
