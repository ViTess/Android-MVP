package vite.common.thirdparty.sso.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import vite.common.thirdparty.ThirdPartyConstants;
import vite.common.thirdparty.sso.LoginListener;
import vite.common.thirdparty.sso.SSODelegate;
import vite.common.thirdparty.sso.ShareListener;
import vite.common.thirdparty.sso.ShareMessage;

import java.io.File;
import java.util.ArrayList;

import static vite.common.thirdparty.sso.SSOManager.TYPE_SINA;

/**
 * Created by trs on 17-9-11.
 */

public class SinaDelegate implements SSODelegate {

    private Context mContext;

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private WbShareHandler mShareHandler;
    private WbAuthListener mAuthListener;
    private WbShareCallback mShareCallBack;

    private LoginListener mLoginListener;
    private ShareListener mShareListener;

    private void init(Context context) {
        mContext = context.getApplicationContext();

        if (mAuthInfo == null) {
            mAuthInfo = new AuthInfo(mContext, ThirdPartyConstants.SINA_APPKEY, ThirdPartyConstants.REDIRECT_URL, ThirdPartyConstants.SINA_SCOPE);
            WbSdk.install(mContext, mAuthInfo);
        }

        if (mAuthListener == null) {
            mAuthListener = new WbAuthListener() {
                @Override
                public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                    if (mLoginListener == null)
                        return;

                    if (oauth2AccessToken.isSessionValid()) {
                        SinaToken token = new SinaToken();
                        token.token = oauth2AccessToken.getToken();
                        token.uid = oauth2AccessToken.getUid();
                        mLoginListener.onSuccess(TYPE_SINA, token);
                    } else {
                        mLoginListener.onError(TYPE_SINA, "登录失败");
                    }
                    mLoginListener = null;//置空避免泄露
                }

                @Override
                public void cancel() {
                    if (mLoginListener != null) {
                        mLoginListener.onCancel(TYPE_SINA);
                        mLoginListener = null;//置空避免泄露
                    }
                }

                @Override
                public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                    String msg = "errcode:" + wbConnectErrorMessage.getErrorCode() + " , errmsg:" + wbConnectErrorMessage.getErrorMessage();
                    Log.e("SinaDelegate", msg);
                    if (mLoginListener != null) {
                        mLoginListener.onError(TYPE_SINA, wbConnectErrorMessage.getErrorMessage());
                        mLoginListener = null;//置空避免泄露
                    }
                }
            };
        }

        if (mShareCallBack == null) {
            mShareCallBack = new WbShareCallback() {
                @Override
                public void onWbShareSuccess() {
                    if (mShareListener != null) {
                        mShareListener.onSuccess(TYPE_SINA);
                        mShareListener = null;
                    }
                }

                @Override
                public void onWbShareCancel() {
                    if (mShareListener != null) {
                        mShareListener.onCancel(TYPE_SINA);
                        mShareListener = null;
                    }
                }

                @Override
                public void onWbShareFail() {
                    if (mShareListener != null) {
                        mShareListener.onError(TYPE_SINA, "分享失败");
                        mShareListener = null;
                    }
                }
            };
        }
    }

    @Override
    public void login(Activity activity, LoginListener listener) {
        init(activity);

        mLoginListener = listener;

        //每次使用重新构造，不然无法重复调起微博
        mSsoHandler = new SsoHandler(activity);

        mSsoHandler.authorize(mAuthListener);
    }

    @Override
    public void share(Activity activity, ShareMessage message, ShareListener listener) {
        init(activity);

        if (message == null) {
            if (listener != null)
                listener.onError(TYPE_SINA, "分享内容为空");
            return;
        }

        mShareListener = listener;

        if (mShareHandler == null) {
            mShareHandler = new WbShareHandler(activity);
            mShareHandler.registerApp();
        }

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (!TextUtils.isEmpty(message.mText)) {
            TextObject textObject = new TextObject();
            textObject.text = message.mText;
            weiboMessage.textObject = textObject;
        }
        if (message.mThumpBitmap != null && !message.mThumpBitmap.isRecycled()) {
            ImageObject imageObject = new ImageObject();
            imageObject.setImageObject(message.mThumpBitmap);
            weiboMessage.imageObject = imageObject;
        }

        //低版本微博不支持多图
        if (message.mMultiImagePath != null && message.mMultiImagePath.size() > 0) {
            if (!WbSdk.supportMultiImage(activity)) {
                if (mShareListener != null) {
                    mShareListener.onError(TYPE_SINA, "当前微博版本不支持分享多图");
                    mShareListener = null;
                    return;
                }
            }

            ArrayList<Uri> pathList = new ArrayList<>();
            for (String path : message.mMultiImagePath) {
                pathList.add(Uri.fromFile(new File(path)));
            }
            MultiImageObject multiImageObject = new MultiImageObject();
            multiImageObject.setImageList(pathList);
            weiboMessage.multiImageObject = multiImageObject;
        }

        if (!TextUtils.isEmpty(message.mLocalVideoPath)) {
            VideoSourceObject videoSourceObject = new VideoSourceObject();
            videoSourceObject.videoPath = Uri.fromFile(new File(message.mLocalVideoPath));
            weiboMessage.videoSourceObject = videoSourceObject;
        }

        mShareHandler.shareMessage(weiboMessage, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            mSsoHandler = null;//内部强引用activity，在此处置空避免内存泄漏
        }
    }

    @Override
    public boolean isInstalled(Activity activity) {
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (mShareHandler != null) {
            mShareHandler.doResultIntent(intent, mShareCallBack);
            mShareHandler = null;//内部强引用activity，在此处置空避免内存泄漏
        }
    }
}
