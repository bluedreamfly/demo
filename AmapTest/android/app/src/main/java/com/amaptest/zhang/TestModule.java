package com.amaptest.zhang;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by zhenhuihuang on 2017/7/10.
 */

public class TestModule extends ReactContextBaseJavaModule {


    public TestModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "TestModule";
    }

    @ReactMethod
    public void TestShow() {
        Log.i("This is my test", "you call testshow");
    }


}
