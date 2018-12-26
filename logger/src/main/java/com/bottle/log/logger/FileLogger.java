package com.bottle.log.logger;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.bottle.log.AbstractLogger;
import com.bottle.log.ILogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @Description: 打印到文件的Log，保存到文件后可以定义接口上传到服务器
 */
public class FileLogger extends AbstractLogger {

    public static final String SPLIT = "|";
    public static final String V = "[V]";
    public static final String D = "[D]";
    public static final String I = "[I]";
    public static final String W = "[W]";
    public static final String E = "[E]";
    public static final String N = "\n";

    private final int MSG_WHAT = 10;
    private final String MSG_TAG = "tag";
    private final String MSG_PRIORITY = "proi";
    private final String MSG_LOG = "msg";
    private final int CACHE_SIZE = 10240; // 10 * 1024B = 10K


	private final SimpleDateFormat TIMESTAMP_FMT;
    private StringBuffer cacheLog;
    private String packageName;
    private HandlerThread logThread;
    private Handler logHandler;
    private String logPath;
    private boolean isDebuggable = true;

	public FileLogger(Context context) {
        TIMESTAMP_FMT = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ");
        cacheLog = new StringBuffer();
        packageName = context.getPackageName();
        logPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Android/data/" + packageName + "/cache/log";
        logThread = new HandlerThread("file-log-thread");
        logThread.start();
        logHandler = new Handler(logThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg == null) {
                    return;
                }
                int what = msg.what;
                if(what == MSG_WHAT) {
                    Bundle bundle = msg.getData();
                    if(bundle == null) {
                        return;
                    }
                    int priority = bundle.getInt(MSG_PRIORITY);
                    String tag = bundle.getString(MSG_TAG);
                    String message = bundle.getString(MSG_LOG);
                    cacheLog(priority, tag, message);
                }
            }
        };
	}

    /**
     * 日志保存在这个目录下，每天一个文件，文件名的格式：yyyyyMMdd.txt，如果需要上报日志，
     * 可以把这个目录下的文件上传到服务器。
     * @return
     */
	public String getLogPath() {
	    return logPath;
    }

    @Override
    public void close() {
        isDebuggable = false;
        if(logThread != null) {
            logThread.quit();
            logThread = null;
            logHandler = null;
        }
    }

    @Override
    public void save() {
        File logDir = new File(logPath);
        if (!logDir.exists()){
            logDir.mkdirs();
        }
        String filePath = logDir.getAbsolutePath() + "/"+ getCurrentTimeString() + ".txt";
        FileWriter fileWriter;
        Writer mWriter = null;
        try{
            File file = new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            String logs = cacheLog.toString();
            fileWriter = new FileWriter(file, true);
            mWriter = new PrintWriter(new BufferedWriter(fileWriter,2028));
            mWriter.write(logs);
            mWriter.flush();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(cacheLog != null) {
                    cacheLog.setLength(0);
                }
                if(mWriter != null) {
                    mWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void enable(boolean enable) {
	    this.isDebuggable = enable;
    }

    @Override
    public void v(String tag, String message) {
        println(VERBOSE, tag, message);
    }

	@Override
	public void d(String tag, String message) {
		// TODO Auto-generated method stub
		println(DEBUG, tag, message);
	}

    @Override
	public void i(String tag, String message) {
		println(INFO, tag, message);
	}

	@Override
	public void w(String tag, String message) {
		println(WARN, tag, message);
	}

    @Override
    public void e(String tag, String message) {
        println(ERROR, tag, message);
    }

    private String getCurrentTimeString() {
        Date now = new Date();
        // 每天保存一个文件
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(now);
    }

    /**
     * 文件消息的格式：[yyyy-MM-dd HH:mm:ss] [D] com.bottle.alive|tag|message
     * @param priority
     * @param tag
     * @param message
     */
	private void println(int priority, String tag, String message) {
	    if( this.isDebuggable == false) {
	        return; // 不打印日志
        }
	    if(logHandler == null) {
	        return;
        }
        Message msg = logHandler.obtainMessage(MSG_WHAT);
        Bundle bundle = new Bundle();
        bundle.putInt(MSG_PRIORITY, priority);
        bundle.putString(MSG_TAG, tag);
        StackTraceElement element = getStackTraceElement(8);
        bundle.putString(MSG_LOG, formatLogMsg(message, element.getFileName(), element.getLineNumber(), element.getMethodName()));
        msg.setData(bundle);
        logHandler.sendMessage(msg);
	}

    private void cacheLog(int priority, String tag, String message) {
        cacheLog.append(TIMESTAMP_FMT.format(new Date()));
        switch (priority){
            case ILogger.VERBOSE:
                cacheLog.append(V);
                break;
            case ILogger.DEBUG:
                cacheLog.append(D);
                break;
            case ILogger.INFO:
                cacheLog.append(I);
                break;
            case ILogger.WARN:
                cacheLog.append(W);
                break;
            case ILogger.ERROR:
                cacheLog.append(E);
                break;
            default:
                break;
        }
        cacheLog.append(SPLIT).append(packageName)
                .append(SPLIT).append(tag)
                .append(SPLIT).append(message)
                .append(N); // 换行
        if(cacheLog.length() > CACHE_SIZE) {
            // 当日志达到一定数量时保存一次,102400B = 10K(按一个字符1Byte计算)
            save();
        }
    }

}
