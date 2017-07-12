package com.amaptest.zhang;

import com.amap.api.maps2d.CameraUpdateFactory;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import java.util.Map;

/**
 * Created by zhenhuihuang on 2017/7/10.
 */


public class AmapManager extends ViewGroupManager<AmapView>  {

    //地图视图对象
    private AmapView mapview;
    //react上下文
    private ThemedReactContext context;
    @Override
    protected AmapView createViewInstance(ThemedReactContext reactContext) {
        context = reactContext;
        mapview = new AmapView(reactContext);
        mapview.onCreate(reactContext.getCurrentActivity().getIntent().getExtras());

//        Log.i("经度", mapview.g)
        this.initMap();
        return mapview;
    }

    //初始化项目
    public void initMap() {
        mapview.getMap().getUiSettings().setZoomControlsEnabled(false);
//        mapview.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(30.277675,120.017964),18,30,0)));
        mapview.getMap().moveCamera(CameraUpdateFactory.zoomTo(16));
    }


    @Override
    public String getName() {
        return "AmapView";
    }

    //回调转换
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("onCusChange", MapBuilder.of("registrationName", "onCusChange"))//registrationName 后的名字,RN中方法也要是这个名字否则不执行
                .put("onCurLocationChange", MapBuilder.of("registrationName", "onCurLocationChange"))
                .build();
    }

}
