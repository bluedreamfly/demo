package com.amaptest.zhang;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;

import static com.facebook.react.common.ApplicationHolder.getApplication;

/**
 * Created by zhenhuihuang on 2017/7/10.
 */


public class AmapManager extends ViewGroupManager<AmapView>  {

    private AmapView mapview;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    private ThemedReactContext context;


    //声明定位回调监听器
    public AMapLocationListener mLocationListener;

    @Override
    protected AmapView createViewInstance(ThemedReactContext reactContext) {
        context = reactContext;
        mapview = new AmapView(reactContext);
        mapview.onCreate(reactContext.getCurrentActivity().getIntent().getExtras());

//        Log.i("经度", mapview.g)
        this.initMap();
        return mapview;
    }

    public void initMap() {
//        mapview.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(30.277675,120.017964),18,30,0)));
        mapview.getMap().moveCamera(CameraUpdateFactory.zoomTo(16));
    }

    @Override
    public String getName() {
        return "AmapView";
    }


    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("onCusChange", MapBuilder.of("registrationName", "onCusChange"))//registrationName 后的名字,RN中方法也要是这个名字否则不执行
                .put("onCurLocationChange", MapBuilder.of("registrationName", "onCurLocationChange"))
                .build();
    }

}
