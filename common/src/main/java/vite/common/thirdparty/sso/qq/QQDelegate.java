package vite.common.thirdparty.sso.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import vite.common.LogUtil;
import vite.common.thirdparty.ThirdPartyConstants;
import vite.common.thirdparty.sso.LoginListener;
import vite.common.thirdparty.sso.SSODelegate;
import vite.common.thirdparty.sso.ShareListener;
import vite.common.thirdparty.sso.ShareMessage;

import org.json.JSONObject;

import java.util.ArrayList;

import static vite.common.thirdparty.ThirdPartyConstants.QQ_APPID;
import static vite.common.thirdparty.sso.SSOManager.TYPE_QQ;

/**
 * Created by trs on 17-9-11.
 */

public class QQDelegate implements SSODelegate {

    private Context mContext;

    private Tencent mTencent;
    private IUiListener mLoginUiListener;

    private LoginListener mLoginListener;
    private ShareListener mShareListener;

    private int mType;

    public QQDelegate(int type) {
        mType = type;
    }

    private void init(Context context) {
        mContext = context.getApplicationContext();
        mTencent = Tencent.createInstance(QQ_APPID, mContext);
        if (mLoginUiListener == null) {
            mLoginUiListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    if (mLoginListener == null)
                        return;

                    if (o == null) {
                        mLoginListener.onError(mType, "返回为空");
                        mLoginListener = null;//置空避免泄露
                        return;
                    }

                    JSONObject json = (JSONObject) o;
                    QQToken token = new QQToken();
                    try {
                        token.token = json.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                        token.expires = json.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                        token.openId = json.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);

                        mTencent.setAccessToken(token.token, token.expires);
                        mTencent.setOpenId(token.openId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mLoginListener.onSuccess(mType, token);
                    mLoginListener = null;//置空避免泄露
                }

                @Override
                public void onError(UiError uiError) {
                    String errmsg = "errcode=" + uiError.errorCode + " errmsg=" + uiError.errorMessage + " errdetail=" + uiError.errorDetail;
                    LogUtil.e("QQDelegate", errmsg);
                    if (mLoginListener != null) {
                        mLoginListener.onError(mType, uiError.errorMessage);
                        mLoginListener = null;//置空避免泄露
                    }
                }

                @Override
                public void onCancel() {
                    if (mLoginListener != null) {
                        mLoginListener.onCancel(mType);
                        mLoginListener = null;//置空避免泄露
                    }
                }
            };
        }
    }

    @Override
    public void login(Activity activity, LoginListener listener) {
        init(activity);
        if (!isInstalled(activity)) {
            mLoginListener.onNotInstalled(mType);
            return;
        }

        mLoginListener = listener;

        mTencent.login(activity, ThirdPartyConstants.QQ_SCOPE, mLoginUiListener);
    }

    /**
     * qq的相关分享需要在主线程操作
     *
     * @param activity
     * @param message
     * @param listener
     */
    @Override
    public void share(Activity activity, ShareMessage message, ShareListener listener) {
        init(activity);
        if (!isInstalled(activity)) {
            if (listener != null)
                listener.onNotInstalled(mType);
            return;
        }

        if (message == null) {
            if (listener != null)
                listener.onError(mType, "分享内容为空");
            return;
        }

        mShareListener = listener;

        Bundle bundle;
        if (mType == TYPE_QQ) {
            bundle = buildShareToQQ(message, listener);
            mTencent.shareToQQ(activity, bundle, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    if (mShareListener != null) {
                        mShareListener.onSuccess(mType);
                        mShareListener = null;
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    if (mShareListener != null) {
                        mShareListener.onError(mType, uiError.errorMessage);
                        mShareListener = null;
                    }
                }

                @Override
                public void onCancel() {
                    if (mShareListener != null) {
                        mShareListener.onCancel(mType);
                        mShareListener = null;
                    }
                }
            });
        } else {
            bundle = buildShareToQZone(message, listener);
            mTencent.shareToQzone(activity, bundle, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    if (mShareListener != null) {
                        mShareListener.onSuccess(mType);
                        mShareListener = null;
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    if (mShareListener != null) {
                        mShareListener.onError(mType, uiError.errorMessage);
                        mShareListener = null;
                    }
                }

                @Override
                public void onCancel() {
                    if (mShareListener != null) {
                        mShareListener.onCancel(mType);
                        mShareListener = null;
                    }
                }
            });
        }

//        message.recycle();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, null);
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public boolean isInstalled(Activity activity) {
        return mTencent.isSupportSSOLogin(activity);
    }

    private Bundle buildShareToQQ(ShareMessage message, ShareListener listener) {
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);//默认图文并存
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, message.mTitle);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, message.mDescription);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, message.mLocalImagePath);

        if (!TextUtils.isEmpty(message.mMusicUrl)) {//分享音乐
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, message.mMusicUrl);
            bundle.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, message.mMusicUrl);
        } else if (!TextUtils.isEmpty(message.mUrl)) {//分享网页
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, message.mUrl);
        } else if (!TextUtils.isEmpty(message.mLocalImagePath)) {//纯分享图片
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        }
        return bundle;
    }

    private Bundle buildShareToQZone(ShareMessage message, ShareListener listener) {
        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);//默认图文并存
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, message.mTitle);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, message.mDescription);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, message.mUrl);

//        if (TextUtils.isEmpty(message.mTitle) && TextUtils.isEmpty(message.mDescription)
//                && TextUtils.isEmpty(message.mUrl)) {//纯分享图片
//            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_NO_TYPE);
//        }

        ArrayList<String> image = new ArrayList<>();
        image.add(message.mLocalImagePath);
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, image);

        return bundle;
    }
}
