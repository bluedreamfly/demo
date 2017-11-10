package com.amaptest.zhang.map;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.HashMap;

/**
 * Created by zhenhuihuang on 2017/7/15.
 */

public class QBMapView extends MapView implements AMap.OnMapLoadedListener, AMap.OnMarkerClickListener, AMap.OnCameraChangeListener, LifecycleEventListener{

    final private ThemedReactContext context;
    private ReactApplicationContext appContext;
    private QBMapManager mapManager;
    private AMap map;
    private LifecycleEventListener lifecycleListener;
    final private  QBMapView self = this;
    private Handler myHandler = new Handler();

    private HashMap<Marker, QBMapMarkerView> markerMap = new HashMap<>();

    public QBMapView(ThemedReactContext context, ReactApplicationContext appContext, QBMapManager mapManager) {
        super(context);
        this.context = context;
        this.appContext = appContext;
        this.mapManager = mapManager;
        this.onCreate(context.getCurrentActivity().getIntent().getExtras());
        this.initMapEvent();
    }

    @Override
    public void onMapLoaded() {

        map = this.getMap();
        //通知rn地图加载完成
        mapManager.pushEvent(context, this, "onMapLoaded", new WritableNativeMap());

        Log.i("map", "map loaded......");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        WritableMap event = Arguments.createMap();
        QBMapMarkerView qbMapMarker = markerMap.get(marker);

        double lng = marker.getPosition().longitude;
        double lat = marker.getPosition().latitude;

        event.putDouble("lat", lat);
        event.putDouble("lng", lng);
        mapManager.pushEvent(context, this, "onMarkerPress", event);
        Log.i("onmarkerclick", "lng lat");
        return true;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        double lng = cameraPosition.target.longitude;
        double lat = cameraPosition.target.latitude;

        WritableMap event = Arguments.createMap();
        event.putDouble("lat", lat);
        event.putDouble("lng", lng);

        mapManager.pushEvent(context, this, "onCurrentLocationChange", event);
    }

    //初始化地图上事件
    public void initMapEvent() {
        this.getMap().setOnMapLoadedListener(this);
        this.getMap().setOnMarkerClickListener(this);
        this.getMap().setOnCameraChangeListener(this);

//        lifecycleListener = new LifecycleEventListener() {
//            @Override
//            public void onHostResume() {
////                if (hasPermissions()) {
////                    //noinspection MissingPermission
////                    map.setMyLocationEnabled(showUserLocation);
////                }
//                synchronized (QBMapView.this) {
//                    QBMapView.this.onResume();
////                    paused = false;
//                }
//            }
//
//            @Override
//            public void onHostPause() {
////                if (hasPermissions()) {
////                    //noinspection MissingPermission
////                    map.setMyLocationEnabled(false);
////                }
////                synchronized (QBMapView.this) {
////                    if (!destroyed) {
////                        AirMapView.this.onPause();
////                    }
////                    paused = true;
////                }
//            }
//
//            @Override
//            public void onHostDestroy() {
//                QBMapView.this.doDestroy();
//            }
//        };
//
        context.addLifecycleEventListener(this);
    }

    @Override
    public void onHostResume() {
        Log.i("Lifecycle-mapview", "onHostResume");
    }

    @Override
    public void onHostPause() {
        Log.i("Lifecycle-mapview", "onHostPause");
    }

    @Override
    public void onHostDestroy() {
        Log.i("Lifecycle-mapview", "onHostDestroy");
    }

    //如何添加子元素
    public void addChildView(View child, int index) {
        if (child instanceof QBMapMarkerView) {
            QBMapMarkerView annotation = (QBMapMarkerView) child;
            annotation.addToMap(this.getMap());
//            features.add(index, annotation);
            Marker marker = (Marker) annotation.getFeature();
            markerMap.put(marker, annotation);
        } else {
            ViewGroup children = (ViewGroup) child;
            for (int i = 0; i < children.getChildCount(); i++) {
                addChildView(children.getChildAt(i), index);
            }
        }
    }

    public synchronized void doDestroy() {
//        if (destroyed) {
//            return;
//        }
//        destroyed = true;

        if (lifecycleListener != null && context != null) {
            context.removeLifecycleEventListener(lifecycleListener);
            lifecycleListener = null;
        }
//        if (!paused) {
//            onPause();
//            paused = true;
//        }
        onDestroy();
    }



}
