package vite.common;

import android.util.Log;

/**
 * Created by trs on 17-5-27.
 */

public class LogUtil {
    private static final boolean DEBUGGABLE = BuildConfig.DEBUG;

    public static void d(String tag, String msg) {
        if (DEBUGGABLE)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUGGABLE)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUGGABLE)
            Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUGGABLE)
            Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUGGABLE)
            Log.i(tag, msg);
    }
}
