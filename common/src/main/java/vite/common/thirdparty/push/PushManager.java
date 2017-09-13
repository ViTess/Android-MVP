package vite.common.thirdparty.push;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;

/**
 * 第三方推送
 * Created by trs on 17-9-12.
 */

public final class PushManager {
    public static void init(Context context) {
        initJPush(context);
    }

    private static void initJPush(Context context) {
//        JPushInterface.setDebugMode(true);//调试模式
        JPushInterface.init(context);
    }
}
