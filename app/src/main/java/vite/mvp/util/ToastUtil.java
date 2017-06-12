package vite.mvp.util;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import vite.mvp.App;

/**
 * Created by trs on 17-5-27.
 */

public final class ToastUtil {
    public static Toast sToast;

    private static final void show(Object o, int timeType) {
        CharSequence text = o instanceof Integer ?
                App.getAppContext().getResources().getText((Integer) o) :
                o.toString();
        if (sToast == null)
            sToast = Toast.makeText(App.getAppContext(), text, Toast.LENGTH_SHORT);
        else {
            sToast.setText(text);
            sToast.setDuration(timeType);
        }
        sToast.show();
    }

    public static final void showShort(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public static final void showShort(@NonNull String s) {
        if (!TextUtils.isEmpty(s))
            show(s, Toast.LENGTH_SHORT);
    }


    public static final void showLong(@StringRes int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    public static final void showLong(@NonNull String s) {
        if (!TextUtils.isEmpty(s))
            show(s, Toast.LENGTH_LONG);
    }
}
