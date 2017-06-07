package vite.common;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * 尺寸转换
 * Created by trs on 17-6-7.
 */

public final class DensityUtil {

    private static volatile Screen sScreen;

    /**
     * 获取屏幕长宽
     *
     * @param context
     * @return
     */
    public static Screen getScreenSize(Context context) {
        final Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 17)
            windowManager.getDefaultDisplay().getRealSize(point);
        else
            windowManager.getDefaultDisplay().getSize(point);
        final Screen screen = new Screen(point.x, point.y);
        return screen;
    }

    /**
     * 获取屏幕长宽，第一次获取时将值保存在内存中
     *
     * @param context
     * @return
     */
    public static Screen getScreenSizeOnce(Context context) {
        if (sScreen == null)
            synchronized (DensityUtil.class) {
                if (sScreen == null)
                    sScreen = getScreenSize(context);
            }
        return sScreen;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, float px) {
        return (px / context.getResources().getDisplayMetrics().density);
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context, float px) {
        return (px / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static class Screen {
        final int width, height;

        public Screen(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
