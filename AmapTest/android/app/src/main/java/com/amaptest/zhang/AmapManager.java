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
        AMap aMap = mapview.getMap();

//        //定位
//        mapview.getMap().setLocationSource(this);
//        aMap.setMyLocationEnabled(true);
        this.initMap();
        return mapview;
    }

    public void initMap() {
//        MyLocationStyle myLocationStyle;
//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
////        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        mapview.getMap().setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        mapview.getMap().setMyLocationEnabled(true);
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
                .build();
    }

}
