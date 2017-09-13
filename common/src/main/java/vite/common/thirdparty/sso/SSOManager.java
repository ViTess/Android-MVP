package vite.common.thirdparty.sso;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntDef;

import vite.common.thirdparty.sso.qq.QQDelegate;
import vite.common.thirdparty.sso.sina.SinaDelegate;
import vite.common.thirdparty.sso.wx.WXDelegate;

/**
 * 第三方登录、分享
 * <p>
 * Created by trs on 17-9-11.
 */

public final class SSOManager {

    public static final int TYPE_WX = 1;
    public static final int TYPE_WX_CIRCLE = 2;
    public static final int TYPE_QQ = 3;
    public static final int TYPE_QZONE = 4;
    public static final int TYPE_SINA = 5;

    @IntDef({TYPE_WX, TYPE_WX_CIRCLE, TYPE_QQ, TYPE_QZONE, TYPE_SINA})
    public @interface Type {
    }

//    private static Singleton<SSODelegate, Void> sWXDelegate = new Singleton<SSODelegate, Void>() {
//        @Override
//        protected SSODelegate newInstance(Void... args) {
//            return new WXDelegate(TYPE_WX);
//        }
//    };
//
//    private static Singleton<SSODelegate, Void> sWXCircleDelegate = new Singleton<SSODelegate, Void>() {
//        @Override
//        protected SSODelegate newInstance(Void... args) {
//            return new WXDelegate(TYPE_WX_CIRCLE);
//        }
//    };
//
//    private static Singleton<SSODelegate, Void> sQQDelegate = new Singleton<SSODelegate, Void>() {
//        @Override
//        protected SSODelegate newInstance(Void... args) {
//            return new QQDelegate(TYPE_QQ);
//        }
//    };
//
//    private static Singleton<SSODelegate, Void> sQZoneDelegate = new Singleton<SSODelegate, Void>() {
//        @Override
//        protected SSODelegate newInstance(Void... args) {
//            return new QQDelegate(TYPE_QZONE);
//        }
//    };
//
//    private static Singleton<SSODelegate, Void> sWBDelegate = new Singleton<SSODelegate, Void>() {
//        @Override
//        protected SSODelegate newInstance(Void... args) {
//            return new SinaDelegate();
//        }
//    };

    public static SSODelegate create(@Type int type) {
        switch (type) {
            case TYPE_WX:
                return new WXDelegate(TYPE_WX);
            case TYPE_WX_CIRCLE:
                return new WXDelegate(TYPE_WX_CIRCLE);
            case TYPE_QQ:
                return new QQDelegate(TYPE_QQ);
            case TYPE_QZONE:
                return new QQDelegate(TYPE_QZONE);
            case TYPE_SINA:
                return new SinaDelegate();
        }
        return new NullDelegate();
    }

    private static final class NullDelegate implements SSODelegate {
        @Override
        public void login(Activity activity, LoginListener listener) {

        }

        @Override
        public void share(Activity activity, ShareMessage message, ShareListener listener) {

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

        }

        @Override
        public void onNewIntent(Intent intent) {

        }

        @Override
        public boolean isInstalled(Activity activity) {
            return false;
        }
    }
}
