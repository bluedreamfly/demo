package com.amaptest;

import android.util.Log;

import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;

/**
 * Created by zhenhuihuang on 2017/7/10.
 */

public class AmapModule extends ReactContextBaseJavaModule{


    public AmapModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }
    @Override
    public String getName() {
        return "AmapModule";
    }

    @ReactMethod
    public void addPoint(final int tag, final ReadableMap point) {
        final ReactApplicationContext context = getReactApplicationContext();

        final double lng = point.getDouble("lng");
        final double lat = point.getDouble("lat");

        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);

        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                MapView map = (MapView) nvhm.resolveView(tag);

                LatLng latLng = new LatLng(lat,lng);
                final Marker marker = map.getMap().addMarker(new MarkerOptions().position(latLng));

            }
        });
        Log.i("AmapModule ......", "TEST AMAP MODULE");
    }
}
