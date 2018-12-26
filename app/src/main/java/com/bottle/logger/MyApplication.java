package com.bottle.logger;

import android.app.Application;

import com.bottle.log.Log;
import com.bottle.log.logger.FileLogger;
import com.bottle.log.logger.LogcatLogger;

import static android.content.ContentValues.TAG;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.addLogger(new LogcatLogger());
        Log.addLogger(new FileLogger(this));
        Log.d(TAG, "init log completed ");
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory");
        Log.save();
        super.onTrimMemory(level);
    }
}
