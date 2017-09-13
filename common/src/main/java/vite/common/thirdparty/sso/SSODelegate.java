package vite.common.thirdparty.sso;

import android.app.Activity;
import android.content.Intent;

/**
 * 社会化功能代理(登录、分享)
 * Created by trs on 17-9-11.
 */

public interface SSODelegate {

    void login(Activity activity, LoginListener listener);

    void share(Activity activity, ShareMessage message, ShareListener listener);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);

    boolean isInstalled(Activity activity);
}