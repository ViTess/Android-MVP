package vite.mvp;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.squareup.leakcanary.LeakCanary;

import vite.api.API;
import vite.common.thirdparty.crash.CrashManager;
import vite.common.thirdparty.push.PushManager;
import vite.common.thirdparty.statistic.StatisticManager;
import vite.common.thirdparty.upgrade.UpgradeManager;
import vite.data.DbManager;

/**
 * Created by trs on 16-10-18.
 */

public class App extends Application {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!setLeakCanary())
            return;

        sApp = this;
        StatisticManager.init(this);//统计
        API.setApplicationContext(this);//网络模块
        DbManager.init(this);//数据库
        Glide.get(this);
        PushManager.init(this);//推送
        CrashManager.init(this);//错误报告
        UpgradeManager.init(this);//更新
    }

    /**
     * @return false - 不同进程，应当退出
     */
    private boolean setLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return false;
        }
        LeakCanary.install(this);
        return true;
    }

    public static Context getAppContext() {
        return sApp;
    }

}
