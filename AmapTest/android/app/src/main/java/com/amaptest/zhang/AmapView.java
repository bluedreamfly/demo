package com.amaptest.zhang;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
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
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.i("ononMarkerClick ", String.valueOf(marker.getPosition().latitude));
            Log.i("ononMarkerClick ", String.valueOf(marker.getPosition().longitude));

            double lng = marker.getPosition().longitude;
            double lat = marker.getPosition().latitude;
            HashMap<String, Double> m = new HashMap<String, Double>();
            m.put("lng", lng);
            m.put("lat", lat);

            view.selectMarker(m);
//            double lng = marker.getPosition().longitude;
//            double lat = marker.getPosition().latitude;
//
//            mPointClickCallback.invoke(lng, lat);
            return true;
        }
    };

    public AmapView(Context context) {
        super(context);
        context = context;

        this.getMap().setOnMarkerClickListener(markerClickListener);
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

    public void setCurPolyline(Polyline p) {
        this.curPolyline = p;
    }

    public Polyline getCurPolyline() {
        return curPolyline;
    }
}
