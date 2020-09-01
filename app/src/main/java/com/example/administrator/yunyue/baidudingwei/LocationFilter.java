package com.example.administrator.yunyue.baidudingwei;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.administrator.yunyue.AppApplication;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.sc_activity.SjrzActivity;
import com.example.administrator.yunyue.service.LocationService;
import com.example.administrator.yunyue.service.Utils;


import java.util.LinkedList;

/***
 * 定位滤波demo，实际定位场景中，可能会存在很多的位置抖动，此示例展示了一种对定位结果进行的平滑优化处理
 * 实际测试下，该平滑策略在市区步行场景下，有明显平滑效果，有效减少了部分抖动，开放算法逻辑，希望能够对开发者提供帮助
 * 注意：该示例场景仅用于对定位结果优化处理的演示，里边相关的策略或算法并不一定适用于您的使用场景，请注意！！！
 *
 * @author baidu
 *
 */
public class LocationFilter extends Activity {
    private static final String TAG = LocationFilter.class.getSimpleName();
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private Button reset;
    private LocationService locService;
    private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果
    int number = 0;
    private TextView tv_dingwei;
    RequestQueue queue = null;
    GeoCoder geoCoder;
    private Button bt_dw_qr;
    String sXq = "";
    private String sLatitude = "", sLongitude = "";
    private String tjid = "", qu = "", xq = "", sName = "", sPhone = "", sStorename = "", sImage = "", sCategory = "", sType = "",sBeizhu="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.locationfilter);
        Intent intent = getIntent();

        tjid = intent.getStringExtra("tjid");
        qu = intent.getStringExtra("qu");
        xq = intent.getStringExtra("xq");
        sLatitude = intent.getStringExtra("latitude");
        sLongitude = intent.getStringExtra("longitude");
        sName = intent.getStringExtra("name");
        sPhone = intent.getStringExtra("phone");
        sStorename = intent.getStringExtra("storename");
        sImage = intent.getStringExtra("image");
        sCategory = intent.getStringExtra("category");
        sType = intent.getStringExtra("type");
        sBeizhu = intent.getStringExtra("beizhu");

        geoCoder = GeoCoder.newInstance();
        queue = Volley.newRequestQueue(LocationFilter.this);
        mMapView = (MapView) findViewById(R.id.bmapView);
        tv_dingwei = (TextView) findViewById(R.id.tv_dingwei);
        bt_dw_qr = (Button) findViewById(R.id.bt_dw_qr);
        reset = (Button) findViewById(R.id.clear);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setOnMapStatusChangeListener(changeListener);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
        locService = ((AppApplication) getApplication()).locationService;
        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mOption.setCoorType("bd09ll");
        locService.setLocationOption(mOption);
        locService.registerListener(listener);
        locService.start();
        bt_dw_qr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationFilter.this, SjrzActivity.class);
                intent.putExtra("tjid", tjid);
                intent.putExtra("qu", tv_dingwei.getText().toString());
                intent.putExtra("xq", sXq);
                intent.putExtra("latitude", sLatitude);
                intent.putExtra("longitude", sLongitude);
                intent.putExtra("name", sName);
                intent.putExtra("phone", sPhone);
                intent.putExtra("storename", sStorename);
                intent.putExtra("image", sImage);
                intent.putExtra("category", sCategory);
                intent.putExtra("type", sType);
                intent.putExtra("beizhu", sBeizhu);
                startActivity(intent);
                finish();
            }
        });
    }

    private BaiduMap.OnMapStatusChangeListener changeListener = new BaiduMap.OnMapStatusChangeListener() {

        @Override
        public void onMapStatusChangeStart(MapStatus arg0) {
        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

        }

        @Override
        public void onMapStatusChange(MapStatus arg0) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus arg0) {
            int[] location = new int[2];
            mMapView.getLocationOnScreen(location);
            Point p = new Point(location[0] + mMapView.getWidth() / 2, location[1] + mMapView.getHeight() / 2);
            //TODO 已经获取到屏幕中心经纬度，可上传或者地理转码
            LatLng latLng = mBaiduMap.getProjection().fromScreenLocation(p);

            sLatitude = String.valueOf(latLng.latitude);
            sLongitude = String.valueOf(latLng.longitude);

            SjrzActivity.sLatitude = sLatitude;
            SjrzActivity.sLongitude = sLongitude;

            Log.i("location", latLng.toString());
            //  tv_dingwei.setText(latLng.toString());
            latlngToAddress(latLng);
        }
    };

    private void latlngToAddress(LatLng latlng) {
        // 设置反地理经纬度坐标,请求位置时,需要一个经纬度
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
        //设置地址或经纬度反编译后的监听,这里有两个回调方法,
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            //经纬度转换成地址
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //  Toast.makeText(this, "找不到该地址!", Toast.LENGTH_SHORT).show();
                    tv_dingwei.setText("找不到该地址!");
                }
                tv_dingwei.setText(result.getAddress());
                sXq = result.getSematicDescription();
                //  SjrzActivity.fuzi(result.getAddress(),sXq);
                // tv_address.setText("地址:" + result.getAddress());
            }

            //把地址转换成经纬度
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                // 详细地址转换在经纬度
                String address = result.getAddress();
            }
        });
    }


    /***
     * 定位结果回调，在此方法中处理定位结果
     */
    BDAbstractLocationListener listener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
                Message locMsg = locHander.obtainMessage();
                Bundle locData;
                locData = Algorithm(location);
                if (locData != null) {
                    locData.putParcelable("loc", location);
                    locMsg.setData(locData);
                    locHander.sendMessage(locMsg);
                }
            }
        }

    };

    /***
     * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
     * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
     * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
     *
     * @param location
     * @return Bundle
     */
    private Bundle Algorithm(BDLocation location) {
        Bundle locData = new Bundle();
        double curSpeed = 0;
        if (locationList.isEmpty() || locationList.size() < 2) {
            LocationEntity temp = new LocationEntity();
            temp.location = location;
            temp.time = System.currentTimeMillis();
            locData.putInt("iscalculate", 0);
            locationList.add(temp);

        } else {
            if (locationList.size() > 5)
                locationList.removeFirst();
            double score = 0;
            for (int i = 0; i < locationList.size(); ++i) {
                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
                        locationList.get(i).location.getLongitude());
                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
                score += curSpeed * Utils.EARTH_WEIGHT[i];
                if (number == 1) {
                    latlngToAddress(lastPoint);
                    number = 2;
                }

            }
            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
                location.setLongitude(
                        (locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
                                / 2);
                location.setLatitude(
                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
                                / 2);
                locData.putInt("iscalculate", 1);
            } else {
                locData.putInt("iscalculate", 0);
            }
            LocationEntity newLocation = new LocationEntity();
            newLocation.location = location;
            newLocation.time = System.currentTimeMillis();
            locationList.add(newLocation);

        }
        return locData;
    }

    /***
     * 接收定位结果消息，并显示在地图上
     */
    private Handler locHander = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                BDLocation location = msg.getData().getParcelable("loc");
                int iscal = msg.getData().getInt("iscalculate");
                if (location != null) {
                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                    // 构建Marker图标
                    BitmapDescriptor bitmap = null;
                    if (iscal == 0) {
                        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark); // 非推算结果
                    } else {
                        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark); // 推算结果
                    }
                    mBaiduMap.clear();
                    mBaiduMap.hideInfoWindow();
                    // 构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
                    // 在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(option);
                    if (number == 0) {
                        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
                        number = 1;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        locService.unregisterListener(listener);
        locService.stop();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        reset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mBaiduMap != null)
                    mBaiduMap.clear();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

    }

    /**
     * 封装定位结果和时间的实体类
     *
     * @author baidu
     */
    class LocationEntity {
        BDLocation location;
        long time;
    }

    private void back() {
        Intent intent = new Intent(LocationFilter.this, SjrzActivity.class);
        intent.putExtra("qu", qu);
        intent.putExtra("xq", xq);
        intent.putExtra("latitude", sLatitude);
        intent.putExtra("longitude", sLongitude);
        intent.putExtra("name", sName);
        intent.putExtra("phone", sPhone);
        intent.putExtra("storename", sStorename);
        intent.putExtra("image", sImage);
        intent.putExtra("category", sCategory);
        intent.putExtra("type", sType);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                //   showDialog();
                back();
                break;
            /*
            * Home键是系统事件，不能通过KeyDown监听
            * 此处log不会打印
            */
            case KeyEvent.KEYCODE_HOME:
                Log.d(TAG, "onKeyDown KEYCODE_HOME");
                break;
            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG, "onKeyDown KEYCODE_MENU");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
