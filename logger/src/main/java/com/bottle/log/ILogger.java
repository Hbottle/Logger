package com.bottle.log;

/**
 * @Description: 一个日志接口，会有多种实现，记录日志的时候会每个实现都记录，如Log，文件等
 */
public interface ILogger {

    /**
     * Priority constant for the println method; use TALogger.v.
     */
    int VERBOSE = 2;

    /**
     * Priority constant for the println method; use TALogger.d.
     */
    int DEBUG = 3;

    /**
     * Priority constant for the println method; use TALogger.i.
     */
    int INFO = 4;

    /**
     * Priority constant for the println method; use TALogger.w.
     */
    int WARN = 5;

    /**
     * Priority constant for the println method; use TALogger.e.
     */
    int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    int ASSERT = 7;

    /**
     * 是否打印日志
     * @param enable true 打印，false 不打印
     */
    void enable(boolean enable);

    /**
     * 退出日志记录，不再接受任何日志
     */
    void close();

    /**
     * 保存日志(Logcat没有保存，文件日志可以保存文件，网络日志可以发送接口等等)
     */
    void save();

    void v(String tag, String msg);

	void d(String tag, String msg);

	void i(String tag, String msg);

	void w(String tag, String msg);

	void e(String tag, String msg);

}
