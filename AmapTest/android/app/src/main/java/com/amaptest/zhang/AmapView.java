package com.amaptest.zhang;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.services.route.RouteSearch;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhenhuihuang on 2017/7/10.
 */

public class AmapView extends MapView  {

    private ReactContext context;
    private RouteSearch routeSearch;
    final private AmapView view = this;

    private Polyline curPolyline = null;
    private Marker curLocation = null;
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
//            Log.i("ononMarkerClick ", String.valueOf(marker.getPosition().latitude));
//            Log.i("ononMarkerClick ", String.valueOf(marker.getPosition().longitude));

            double lng = marker.getPosition().longitude;
            double lat = marker.getPosition().latitude;
            HashMap<String, Double> m = new HashMap<String, Double>();
            m.put("lng", lng);
            m.put("lat", lat);

            view.selectMarker(m);
            return true;
        }
    };

    AMap.OnCameraChangeListener cameraChangeListener = new AMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {

        }

        @Override
        public void onCameraChangeFinish(CameraPosition cameraPosition) {

            double lng = cameraPosition.target.longitude;
            double lat = cameraPosition.target.latitude;
            HashMap<String, Double> m = new HashMap<String, Double>();
            m.put("lng", lng);
            m.put("lat", lat);

            view.changeCurPosition(m);
            Log.i("cameraPosition is = ", String.valueOf(cameraPosition.target.longitude) + "," + String.valueOf(cameraPosition.target.latitude));
        }
    };

    public AmapView(Context context) {
        super(context);
        this.getMap().setOnMarkerClickListener(markerClickListener);

        this.getMap().setOnCameraChangeListener(cameraChangeListener);
    }

    public void selectMarker(HashMap m) {
        WritableMap event = Arguments.createMap();
        event.putDouble("lat", (double)m.get("lat"));
        event.putDouble("lng", (double)m.get("lng"));

        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "onCusChange",
                event);


    }

    public void changeCurPosition(HashMap m) {
        WritableMap event = Arguments.createMap();
        event.putDouble("lat", (double)m.get("lat"));
        event.putDouble("lng", (double)m.get("lng"));

        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "onCurLocationChange",
                event);
    }

    public void setCurPolyline(Polyline p) {
        this.curPolyline = p;
    }

    public Polyline getCurPolyline() {
        return curPolyline;
    }


    public void setCurLocation(Marker curLocation) {
        this.curLocation = curLocation;
    }

    public Marker getCurLocation() {
        return curLocation;
    }
}
