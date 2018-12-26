package com.bottle.log;

/**
 * 这是LogcatLogger和FileLogger的父类
 */
public abstract class AbstractLogger implements ILogger{

    // 控制日志格式的一些常量
    public static String LEFT_BRACKET = "(";
    public static String RIGHT_BRACKET = ")";
    public static String SHARP = "#";
    public static String COLON = ":";

    /**
     * 格式化日志
     * @param msg log内容
     * @param fileName 类文件名
     * @param lineNumber 代码行数
     * @param methodName 方法名
     * @return
     */
    protected String formatLogMsg(String msg, String fileName, int lineNumber, String methodName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(LEFT_BRACKET);
        buffer.append(fileName)
                .append(COLON).append(lineNumber).append(RIGHT_BRACKET)
                .append(SHARP).append(methodName)
                .append(COLON).append(msg);
        return buffer.toString();
    }

    /**
     * 用于获取类文件名、代码行数、方法名
     * @param index
     * @return
     */
    protected StackTraceElement getStackTraceElement(int index) {
        StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
        StackTraceElement element = traceElements[index];
        return element;
    }
}
