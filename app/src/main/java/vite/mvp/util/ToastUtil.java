package vite.mvp.util;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

import vite.mvp.App;

/**
 * Created by trs on 17-5-27.
 */

public final class ToastUtil {
    public static Toast sToast;

    private static final void show(Object o, int timeType) {
        if (sToast == null)
            if (o instanceof Integer)
                sToast = Toast.makeText(App.getAppContext(), (int) o, Toast.LENGTH_SHORT);
            else
                sToast = Toast.makeText(App.getAppContext(), o.toString(), Toast.LENGTH_SHORT);
        else {
            if (o instanceof Integer)
                sToast.setText((int) o);
            else
                sToast.setText(o.toString());
            sToast.setDuration(timeType);
        }
        sToast.show();
    }

    public static final void showShort(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public static final void showShort(@NonNull String s) {
        show(s, Toast.LENGTH_SHORT);
    }


    public static final void showLong(@StringRes int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    public static final void showLong(@NonNull String s) {
        show(s, Toast.LENGTH_LONG);
    }
}
