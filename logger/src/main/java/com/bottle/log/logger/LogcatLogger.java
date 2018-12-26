package com.bottle.log.logger;

import android.util.Log;

import com.bottle.log.AbstractLogger;

/**
 * 一个 Logcat 日志，可以在Logcat中点击直达这打印log的一行代码
 */
public class LogcatLogger extends AbstractLogger {

    private boolean isDebuggable = true;

    public LogcatLogger() {

    }

    @Override
    public void enable(boolean enable) {
        this.isDebuggable = enable;
    }

    @Override
    public void close() {
        isDebuggable = false;
    }

    @Override
    public void save() {

    }

    @Override
    public void v(String tag, String msg) {
        if (isDebuggable == false) {
            return;
        }
        StackTraceElement element = getStackTraceElement(7);
        Log.v(tag, formatLogMsg(msg, element.getFileName(), element.getLineNumber(), element.getMethodName()));
    }

    @Override
    public void d(String tag, String msg) {
        if (isDebuggable == false) {
            return;
        }
        StackTraceElement element = getStackTraceElement(7);
        Log.d(tag, formatLogMsg(msg, element.getFileName(), element.getLineNumber(), element.getMethodName()));
    }

    @Override
    public void i(String tag, String msg) {
        if (isDebuggable == false) {
            return;
        }
        StackTraceElement element = getStackTraceElement(7);
        Log.i(tag, formatLogMsg(msg, element.getFileName(), element.getLineNumber(), element.getMethodName()));
    }

    @Override
    public void w(String tag, String msg) {
        if (isDebuggable == false) {
            return;
        }
        StackTraceElement element = getStackTraceElement(7);
        Log.w(tag, formatLogMsg(msg, element.getFileName(), element.getLineNumber(), element.getMethodName()));
    }

    @Override
    public void e(String tag, String msg) {
        if (isDebuggable == false) {
            return;
        }
        StackTraceElement element = getStackTraceElement(7);
        Log.e(tag, formatLogMsg(msg, element.getFileName(), element.getLineNumber(), element.getMethodName()));
    }

}
