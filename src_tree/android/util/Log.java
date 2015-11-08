
package android.util;

import edu.buffalo.rtdroid.SystemConfig;

public final class Log {
    public static final int LOG_LEVEL = SystemConfig.LOG_LEVEL;

    public static final int DEBUG__LEVEL = 0;
    public static final int VERBOSE_LEVEL = 1;
    public static final int INFO__LEVEL = 2;
    public static final int WARN_LEVEL = 3;
    public static final int ERROR_LEVEL = 4;

    public static final String VERBOSE = "VERBOSE ";
    public static final String DEBUG = "DEBUG ";
    public static final String INFO = "INFO ";
    public static final String WARN = "WARN ";
    public static final String ERROR = "ERROR ";

    public static final String placeHolder = "%10s, %15s : %s\n";

    public static void i(String tag, String msg) {
        if (LOG_LEVEL <= INFO__LEVEL) {
            // String result = String.format(placeHolder, INFO, tag, msg);
            System.out.print(INFO + ", " + tag + " : " + msg + "\n");
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL <= DEBUG__LEVEL) {
            // String result = String.format(placeHolder, DEBUG, tag, msg);
            System.out.print(DEBUG + ", " + tag + " : " + msg + "\n");
        }
    }

    public static void v(String tag, String msg) {
        if (LOG_LEVEL <= VERBOSE_LEVEL) {
            // String result = String.format(placeHolder, VERBOSE, tag, msg);
            System.out.print(VERBOSE + ", " + tag + " : " + msg + "\n");
        }
    }

    public static void w(String tag, String msg, Exception e) {
        if (LOG_LEVEL <= WARN_LEVEL) {
            // String result = String.format(placeHolder, WARN, tag, msg);
            System.out.print(WARN + ", " + tag + " : " + msg + "\n");
            e.printStackTrace();
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL <= WARN_LEVEL) {
            // String result = String.format(placeHolder, WARN, tag, msg);
            System.out.print(WARN + ", " + tag + " : " + msg + "\n");
        }
    }

    public static void e(String tag, Exception e) {
        if (LOG_LEVEL <= ERROR_LEVEL) {
            // String result = String.format(placeHolder, ERROR, tag,
            // "Exception");
            System.out.print(ERROR + ", " + tag + " : Exception\n");
            e.printStackTrace();
        }
    }

    public static void e(String tag, String msg, Exception e) {
        if (LOG_LEVEL <= ERROR_LEVEL) {
            // String result = String.format(placeHolder, ERROR, tag,
            // "Exception");
            System.out.print(ERROR + ", " + tag + " : " + msg + "\n");
            e.printStackTrace();
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVEL <= ERROR_LEVEL) {
            // String result = String.format(placeHolder, ERROR, tag,
            // "Exception");
            System.out.print(ERROR + ", " + tag + " : " + msg + "\n");
        }
    }

    public static void wtf(String tag, String msg, Throwable t) {
        if (LOG_LEVEL <= ERROR_LEVEL) {
            // String result = String.format(placeHolder, ERROR, tag,
            // "Exception");
            System.out.print(ERROR + ", " + tag + t.getMessage());
        }
    }

    public static void rtdroidLog(Object o, int level, String msg) {
        if (LOG_LEVEL <= level) {
            System.out.println(o.getClass().getName() + " " + msg);
        }
    }
}
