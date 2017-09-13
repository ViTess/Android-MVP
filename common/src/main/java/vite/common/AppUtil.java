package vite.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by trs on 17-9-12.
 */

public final class AppUtil {

    /**
     * 获取渠道名
     *
     * @param context
     * @return
     */
    public static String getUmengChannel(Context context) {
        return getChannel(context, "UMENG_CHANNEL", "none");
    }

    /**
     * 获取渠道名
     *
     * @param context
     * @param key     meta-data中渠道名的key
     * @param def     默认返回值
     * @return
     */
    public static String getChannel(Context context, String key, String def) {
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            ApplicationInfo info = null;
            try {
                info = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (info != null && info.metaData != null)
                return info.metaData.getString(key);
        }
        return def;
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version = "";
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            PackageInfo info = null;
            try {
                info = pm.getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (info != null)
                version = info.versionName;
        }
        return version;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int version = 0;
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            PackageInfo info = null;
            try {
                info = pm.getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (info != null)
                version = info.versionCode;
        }
        return version;
    }

    /**
     * 获取包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    /**
     * 获取进程号对应的进程名
     *
     * @return
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
