package com.amaptest.zhang;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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


    //地图定位 client
    public AMapLocationClient mLocationClient = null;
    //地图定位 client option
    public AMapLocationClientOption mLocationOption = null;
    //位置变化监听器
    private OnLocationChangedListener mListener;
    //路径搜索对象
    private RouteSearch routeSearch;
    //定位回调
    private  Callback mLocationCallback;
    //react 应用上下文
    private ReactApplicationContext context;



    public AmapModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }
    @Override
    public String getName() {
        return "AmapModule";
    }

    /**
     * 画标记点
     * @param tag
     * @param point  点坐标
     */
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
    }

    /**
     * 增加点集合
     * @param tag
     * @param points
     */
    @ReactMethod
    public void addPoints(final int tag, final ReadableArray points) {
        final ReactApplicationContext context = getReactApplicationContext();


//        mPointClickCallback = callback;
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);

        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                AmapView mapView = (AmapView) nvhm.resolveView(tag);

                List<Marker> markers = new ArrayList<Marker>();
                if (mapView.getBikes().size() > 0) {
                    mapView.clearBikes();
//                    if(mapView.getCurPolyline()!= null) {
//                        mapView.getCurPolyline().remove();
//                    }
                }
                for(int i = 0; i < points.size(); i++) {
                    final double lng = points.getMap(i).getDouble("lng");
                    final double lat = points.getMap(i).getDouble("lat");
                    LatLng latLng = new LatLng(lat,lng);

                    MarkerOptions markerOptions = new MarkerOptions();

                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(context.getResources(),R.mipmap.ic_bike)));

                    final Marker marker = mapView.getMap().addMarker(markerOptions.position(latLng));

                    markers.add(marker);
                }

                mapView.setBikes(markers);
            }
        });
    }

    @ReactMethod
    public void startTestServive() {
        final ReactApplicationContext context = getReactApplicationContext();
        Intent intent1 = new Intent(context, BackService.class);
        context.startService(intent1);
    }

    @ReactMethod
    public void cusLog() {
        Log.i("HELLO WORLD", "THIS IS A TEST SERVICE");
    }

    @ReactMethod
    public void addCurLocation(final int tag, final ReadableMap point) {

        final ReactApplicationContext context = getReactApplicationContext();

        final double lng = point.getDouble("lng");
        final double lat = point.getDouble("lat");

        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);

        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                AmapView mapView = (AmapView) nvhm.resolveView(tag);

                LatLng latLng = new LatLng(lat,lng);

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(context.getResources(),R.mipmap.ic_location))).zIndex(100000);
                Marker marker =  mapView.getCurLocation();
                if (mapView.getCurLocation() == null) {
                    marker = mapView.getMap().addMarker(markerOptions);
                }
                marker.setPositionByPixels(mapView.getWidth() / 2, mapView.getHeight() / 2);
//                marker.setPositionByPixels(, mapView.getHeight() / 2);

            }
        });
    }

    /**
     * 定位
     * @param tag   引用的地图视图id
     * @param callback  定位完成的回调
     */
    @ReactMethod
    public void locate(final int tag, Callback callback) {
        final ReactApplicationContext context = getReactApplicationContext();
        final AmapModule self = this;
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        mLocationCallback = callback;
        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                AmapView mapView = (AmapView) nvhm.resolveView(tag);
                mapView.getMap().setLocationSource(self);
                mapView.getMap().setMyLocationEnabled(true);
            }
        });

    }

    @ReactMethod
    public void remove(final int tag) {
        final ReactApplicationContext context = getReactApplicationContext();

        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);

        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                AmapView mapView = (AmapView) nvhm.resolveView(tag);
                mapView.getMap().setOnCameraChangeListener(null);
                mapView.getMap().setOnMarkerClickListener(null);
//                isRemove = true;
                mapView.onDestroy();
            }
        });
    }

    /**
     * 搜索起点到终点的路径
     * @param start
     * @param end
     */

    @ReactMethod
    public void getRoutePath(final ReadableMap start, final ReadableMap end) {
        routeSearch = new RouteSearch(context);
        routeSearch.setRouteSearchListener(this);

        LatLonPoint from =  new LatLonPoint(start.getDouble("lat"), start.getDouble("lng"));
        LatLonPoint to =  new LatLonPoint(end.getDouble("lat"), end.getDouble("lng"));

        RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(from, to));

        routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
    }

    /**
     * 画折线
     * @param tag  地图视图的tag
     * @param points 折线上的点
     */
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
                if (mapView.getCurPolyline() != null) {
                    mapView.getCurPolyline().remove();
                }
                Polyline p = mapView.getMap().addPolyline(new PolylineOptions().
                addAll(list).width(10).color(Color.argb(255, 1, 1, 1)));
                mapView.setCurPolyline(p);
            }
        });
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    /**
     * 行走路径搜索
     * @param walkRouteResult  搜索路径获得的结果
     * @param i
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

        List<WalkPath> paths = walkRouteResult.getPaths();

        WritableArray writableArray = new WritableNativeArray();

        for(int index = 0;index < paths.size(); index++) {
            List<WalkStep> steps = paths.get(index).getSteps();
            for (int j = 0; j < steps.size();j++) {
                List<LatLonPoint> points = steps.get(j).getPolyline();
                for(int t = 0; t < points.size(); t++) {
                    LatLonPoint point = points.get(t);
                    writableArray.pushString(String.valueOf(point.getLatitude()) + "," + String.valueOf(point.getLongitude()));
                }
            }
        }

        WritableMap w = Arguments.createMap();

        w.putArray("list", writableArray);
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("routeFinish", w);
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

                mLocationCallback.invoke(aMapLocation.getLongitude(), aMapLocation.getLatitude());
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

}


