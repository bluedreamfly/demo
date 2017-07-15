package com.amaptest.zhang;

import android.content.Intent;
import android.os.Bundle;

import javax.annotation.Nullable;
import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;

/**
 * Created by zhenhuihuang on 2017/7/12.
 */

public class BackService extends HeadlessJsTaskService{


    @Nullable
    @Override
    protected HeadlessJsTaskConfig getTaskConfig(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            return new HeadlessJsTaskConfig(
                    "SomeTaskName",
                    Arguments.fromBundle(extras),
                    5000);
        }
        return null;
    }
}
