package com.amaptest.zhang.map;

import android.util.Log;
import android.view.View;

import com.amap.api.maps2d.MapView;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;

/**
 * Created by zhenhuihuang on 2017/7/15.
 */

public class QBMapManager extends ViewGroupManager<QBMapView>{

    private static final String REACT_CLASS = "QBMap";
    private ReactApplicationContext appContext;
    private QBMapView mapView;


    public QBMapManager(ReactApplicationContext reactContext) {
        appContext = reactContext;
    }

    @Override
    protected QBMapView createViewInstance(ThemedReactContext reactContext) {

        mapView = new QBMapView(reactContext, appContext, this);

        return mapView;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public void pushEvent(ThemedReactContext context, View view, String name, WritableMap data) {
        context.getJSModule(RCTEventEmitter.class)
                .receiveEvent(view.getId(), name, data);
    }

    //回调转换
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("onMapLoaded", MapBuilder.of("registrationName", "onMapLoaded"))
                .put("onMarkerPress", MapBuilder.of("registrationName", "onMarkerPress"))
                .put("onCurrentLocationChange", MapBuilder.of("registrationName", "onCurrentLocationChange"))
                .build();
    }

    @Override
    public void addView(QBMapView parent, View child, int index) {
        parent.addChildView(child, index);
//        super.addView(parent, child, index);
//        Log.i("addView", "addView");
    }
}
