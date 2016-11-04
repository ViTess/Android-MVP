package vite.mvp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by trs on 16-10-18.
 */

public class NetworkUtil {

    public static final boolean isNetworkConnecting(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }

}
