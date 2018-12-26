package com.bottle.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @Description: 是一个日志打印类 
 */
public class Log{

	private static boolean logAble = true;

	private static HashMap<String, ILogger> loggerHashMap = new HashMap<String, ILogger>();

	public static boolean addLogger(ILogger logger) {
		String loggerName = logger.getClass().getName();
		boolean isSuccess = false;
		if (!loggerHashMap.containsKey(loggerName)){
			loggerHashMap.put(loggerName, logger);
			isSuccess = true;
		}
		return isSuccess;
	}

	public static void removeLogger(ILogger logger){
		String loggerName = logger.getClass().getName();
		if (loggerHashMap.containsKey(loggerName)){
		    try {
                logger.close();
                logger.save(); // 退出前先保存日记(需要保存的Logger)
                loggerHashMap.remove(loggerName);
            } catch (Exception e) {
		        e.printStackTrace();
            }
		}
	}

    public static void setLogAble(boolean logAble) {
        Log.logAble = logAble;
    }

    /**
     * @param logger 关闭某一个logger
     */
	public static void save(ILogger logger) {
	    if(logger != null) {
	        try {
                logger.save();
            } catch (Exception e) {
	            e.printStackTrace();
            }
        }
    }

    /**
     * 关闭所有的logger
     */
    public static void save() {
	    if(loggerHashMap == null || loggerHashMap.size() == 0) {
	        return;
        }
        for(String key : loggerHashMap.keySet()) {
			save(loggerHashMap.get(key));
        }
    }

    public static void v(Object object, String message){
        printLogger(ILogger.VERBOSE, object, message);
    }

	public static void d(Object object, String message){
		printLogger(ILogger.DEBUG, object, message);
	}

	public static void i(Object object, String message){
		printLogger(ILogger.INFO, object, message);
	}

	public static void w(Object object, String message){
		printLogger(ILogger.WARN, object, message);
	}

    public static void w(Object tag, Throwable ex){
        printLogger(ILogger.WARN, tag, dumpThrowable(ex));
    }

    public static void e(Object object, String message){
        printLogger(ILogger.ERROR, object, message);
    }

	public static void e(Object object, Throwable ex){
		printLogger(ILogger.ERROR, object, dumpThrowable(ex));
	}

    public static void v(String tag, String message){
        printLogger(ILogger.VERBOSE, tag, message);
    }

	public static void d(String tag, String message){
		printLogger(ILogger.DEBUG, tag, message);
	}

	public static void i(String tag, String message){
		printLogger(ILogger.INFO, tag, message);
	}

	public static void w(String tag, String message){
		printLogger(ILogger.WARN, tag, message);
	}

    public static void w(String tag, Throwable ex){
        printLogger(ILogger.WARN, tag, dumpThrowable(ex));
    }

    public static void e(String tag, String message){
        printLogger(ILogger.ERROR, tag, message);
    }

	public static void e(String tag, Throwable ex){
		printLogger(ILogger.ERROR, tag, dumpThrowable(ex));
	}

	private static String dumpThrowable(Throwable ex){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}

	public static void println(int priority, String tag, String message){
		printLogger(priority, tag, message);
	}

	private static void printLogger(int priority, Object object , String message){
		Class<?> cls = object.getClass();
		String tag = cls.getSimpleName();
		printLogger(priority, tag, message);
	}

	private static void printLogger(int priority, String tag, String message){
		if (logAble == false){
			return;
		}
        if (message == null) {
            message = "";
        }
        Iterator<Entry<String, ILogger>> iterator = loggerHashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Entry<String, ILogger> entry = iterator.next();
            ILogger logger = entry.getValue();
            if (logger != null){
                printLogger(logger, priority, tag, message);
            }
        }
	}

	private static void printLogger(ILogger logger, int priority, String tag, String message){
		switch (priority){
		case ILogger.VERBOSE:
			logger.v(tag, message);
			break;
		case ILogger.DEBUG:
			logger.d(tag, message);
			break;
		case ILogger.INFO:
			logger.i(tag, message);
			break;
		case ILogger.WARN:
			logger.w(tag, message);
			break;
		case ILogger.ERROR:
			logger.e(tag, message);
			break;
		default:
			break;
		}
	}

}
