package com.amaptest;

import android.util.Log;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

import static com.facebook.react.common.ApplicationHolder.getApplication;

/**
 * Created by zhenhuihuang on 2017/7/10.
 */

public class AmapManager extends ViewGroupManager<MapView> {

    private MapView mapview;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;


    //声明定位回调监听器
    public AMapLocationListener mLocationListener;

    @Override
    protected MapView createViewInstance(ThemedReactContext reactContext) {

        mapview = new MapView(reactContext);
        mapview.onCreate(reactContext.getCurrentActivity().getIntent().getExtras());

        mLocationClient = new AMapLocationClient(reactContext);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
//        Log.i("fsdfsdfsdfsdfsdf", "sdfsdfsdfsdfsdf");

        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.i("lng getAddress", aMapLocation.getAddress());
            }
        });
        mLocationClient.startLocation();
        this.initMap();
        return mapview;
    }

//    public void test() {
//        Log.i("AmapManager...", "test AmapManager");
//    }


//初始化定位

//设置定位回调监听

    public void initMap() {

        mapview.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(30.277675,120.017964),18,30,0)));
        mapview.getMap().moveCamera(CameraUpdateFactory.zoomTo(16));

    }
    @Override
    public String getName() {
        return "AmapView";
    }


}
