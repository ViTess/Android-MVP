package vite.mvp;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.squareup.leakcanary.LeakCanary;

import vite.api.API;
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
        API.setApplicationContext(this);
        DbManager.init(this);
        Glide.get(this);
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
