package com.amaptest.zhang.map;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by zhenhuihuang on 2017/7/15.
 */

public class QBMapMarkerManager extends ViewGroupManager<QBMapMarkerView> {

    private static final String REACT_CLASS = "QBMapMarker";
    @Override
    protected QBMapMarkerView createViewInstance(ThemedReactContext reactContext) {
        return new QBMapMarkerView(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = "coordinate")
    public void setCoordinate(QBMapMarkerView view, ReadableMap map) {
        view.setCoordinate(map);
    }

    @ReactProp(name = "draggable", defaultBoolean = false)
    public void setDraggable(QBMapMarkerView view, Boolean draggable) {
        view.setDraggable(draggable);
    }

    @ReactProp(name = "title")
    public void setTitle(QBMapMarkerView view, String title) {
        view.setTitle(title);
    }

    @ReactProp(name ="image")
    public void setImage(QBMapMarkerView view, String uri) {
        view.setImage(uri);
    }


}
