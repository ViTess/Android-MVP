package vite.mvp;

import android.app.Application;
import android.content.Context;

import vite.mvp.api.Api;

/**
 * Created by trs on 16-10-18.
 */

public class App extends Application{

    private static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        Api.setApplicationContext(this);
    }

    public static Context getAppContext() {
        return mApp;
    }

}
