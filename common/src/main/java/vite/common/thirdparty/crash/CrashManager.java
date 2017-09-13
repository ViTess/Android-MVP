package vite.common.thirdparty.crash;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import vite.common.AppUtil;
import vite.common.thirdparty.ThirdPartyConstants;

/**
 * 第三方错误报告
 * Created by trs on 17-9-12.
 */

public final class CrashManager {

    /**
     * 目前只使用bugly，因此注意：bugly的初始化最好保持在最后，确保不被其他Crash handler影响
     *
     * @param context
     */
    public static void init(Context context) {
        initBugly(context);
    }

    /**
     * 如果使用了MultiDex，建议通过Gradle的“multiDexKeepFile”配置等方式把Bugly的类放到主Dex，另外建议在Application类的"attachBaseContext"方法中主动加载非主dex：
     * <p>
     * public class MyApplication extends SomeOtherApplication {
     * <p>
     * protected void attachBaseContext(Context base) {
     * super.attachBaseContext(context);
     * Multidex.install(this);
     * }
     * }
     */
    private static void initBugly(Context context) {
        Beta.autoInit = false;//设置upgrade自动初始化为false，方便将该逻辑全部放置在UpgradeManager调用

        final Context appContext = context.getApplicationContext();
        final String packageName = AppUtil.getPackageName(context);
        final String processName = AppUtil.getProcessName(android.os.Process.myPid());

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(appContext);
        strategy.setAppChannel(AppUtil.getUmengChannel(appContext));
        strategy.setAppVersion(AppUtil.getVersionName(appContext));
        strategy.setAppReportDelay(10000);//初始化延迟，默认为10s，看需求配置

        //设置只在主进程下上报数据
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //由于使用了加入升级的包，Bugly替换CrashReport
        Bugly.init(appContext, ThirdPartyConstants.BUGLY_APPID, false, strategy);
    }
}
