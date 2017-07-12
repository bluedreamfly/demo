package com.amaptest.zhang;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenhuihuang on 2017/7/10.
 */

public class AmapModule extends ReactContextBaseJavaModule implements LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener{


    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener;
    private RouteSearch routeSearch;
    private  Callback mCallback;

//    private  Callback mPointClickCallback;

    private ReactApplicationContext context;
    public AMapLocationListener mLocationListener;

    final  AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.i("ononMarkerClick ", String.valueOf(marker.getPosition().latitude));
            Log.i("ononMarkerClick ", String.valueOf(marker.getPosition().longitude));

//            double lng = marker.getPosition().longitude;
//            double lat = marker.getPosition().latitude;
//
//            mPointClickCallback.invoke(lng, lat);
            return true;
        }
    };



    public AmapModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
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
//        mPointClickCallback = callback;
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);

        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                MapView map = (MapView) nvhm.resolveView(tag);

                LatLng latLng = new LatLng(lat,lng);

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(context.getResources(),R.mipmap.ic_bike)));

                final Marker marker = map.getMap().addMarker(markerOptions.position(latLng));


            }
        });
        Log.i("AmapModule ......", "TEST AMAP MODULE");
    }

    @ReactMethod
    public void locate(final int tag, Callback callback) {
        final ReactApplicationContext context = getReactApplicationContext();
        final AmapModule self = this;
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        mCallback = callback;
        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                MapView mapView = (MapView) nvhm.resolveView(tag);
                mapView.getMap().setLocationSource(self);
                mapView.getMap().setMyLocationEnabled(true);
            }
        });

    }

    @ReactMethod
    public void getRoutePath(final ReadableMap start, final ReadableMap end) {
        routeSearch = new RouteSearch(context);
        routeSearch.setRouteSearchListener(this);

        LatLonPoint from =  new LatLonPoint(start.getDouble("lat"), start.getDouble("lng"));
        LatLonPoint to =  new LatLonPoint(end.getDouble("lat"), end.getDouble("lng"));

        RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(from, to));

        routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
    }


    @ReactMethod
    public void drawPolyline(final int tag, final ReadableArray points) {

        final ReactApplicationContext context = getReactApplicationContext();
        final AmapModule self = this;
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);

        final List<LatLng> list = new ArrayList<>();

        for(int i = 0; i < points.size();i++) {
            String pointStr = points.getString(i);
            String[] ps = pointStr.split(",");
            list.add(new LatLng(Double.parseDouble(ps[0]), Double.parseDouble(ps[1])));
        }

        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                AmapView mapView = (AmapView) nvhm.resolveView(tag);
//                mapView.setCurPolyline();
                if (mapView.getCurPolyline() != null) {
                    mapView.getCurPolyline().remove();
                }
//                mapView.getMap().clear();
                Polyline p = mapView.getMap().addPolyline(new PolylineOptions().
                addAll(list).width(10).color(Color.argb(255, 1, 1, 1)));

                mapView.setCurPolyline(p);



            }
        });

    }


    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

        List<WalkPath> paths = walkRouteResult.getPaths();
//        List<LatLng> latLngs = new ArrayList<LatLng>();

        WritableArray writableArray = new WritableNativeArray();

        for(int index = 0;index < paths.size(); index++) {
            List<WalkStep> steps = paths.get(index).getSteps();
            for (int j = 0; j < steps.size();j++) {
                List<LatLonPoint> points = steps.get(j).getPolyline();
                for(int t = 0; t < points.size(); t++) {
                    LatLonPoint point = points.get(t);

                    writableArray.pushString(String.valueOf(point.getLatitude()) + "," + String.valueOf(point.getLongitude()));
//                    latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));

//                    Log.i("this is point", String.valueOf(point.getLatitude()) + "--" + String.valueOf(point.getLongitude()));
                }
            }
        }

        WritableMap w = Arguments.createMap();
        w.putArray("list", writableArray);

                context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("routeFinish", w);

    }



    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(context);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();//启动定位
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null&&aMapLocation != null) {
            if (aMapLocation != null
                    &&aMapLocation.getErrorCode() == 0) {
                mCallback.invoke(aMapLocation.getLongitude(), aMapLocation.getLatitude());
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

}
