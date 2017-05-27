package vite.mvp;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;

import vite.api.API;

/**
 * Created by trs on 16-10-18.
 */

public class App extends Application {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        API.setApplicationContext(this);
        Glide.get(this);
    }

    public static Context getAppContext() {
        return sApp;
    }
}
