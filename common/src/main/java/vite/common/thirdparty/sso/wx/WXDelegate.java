package vite.common.thirdparty.sso.wx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import vite.common.BitmapUtil;
import vite.common.thirdparty.ThirdPartyConstants;
import vite.common.thirdparty.sso.LoginListener;
import vite.common.thirdparty.sso.SSODelegate;
import vite.common.thirdparty.sso.ShareListener;
import vite.common.thirdparty.sso.ShareMessage;

import static vite.common.thirdparty.sso.SSOManager.TYPE_WX;
import static vite.common.thirdparty.sso.SSOManager.TYPE_WX_CIRCLE;

/**
 * Created by trs on 17-9-11.
 */

public class WXDelegate implements SSODelegate {

    private static IWXAPI sWXApi;
    private static IWXAPIEventHandler sWXHandler;

    private Context mContext;

    private IWXAPI mWXApi;
    private IWXAPIEventHandler mWXHandler;

    private LoginListener mLoginListener;
    private ShareListener mShareListener;

    private String mLastTransaction;//上次调用的标识，用于标记每个请求的唯一标识
    private int mType;

    public WXDelegate(int type) {
        mType = type;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();

        if (mWXHandler == null) {
            mWXHandler = new IWXAPIEventHandler() {
                @Override
                public void onReq(BaseReq baseReq) {
                }

                @Override
                public void onResp(BaseResp baseResp) {
                    if (mLastTransaction.equals(baseResp.transaction)) {
                        int type = baseResp.getType();
                        switch (type) {
                            case ConstantsAPI.COMMAND_SENDAUTH://登录授权返回
                                verifyLogin((SendAuth.Resp) baseResp);
                                break;
                            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX://分享
                                verifyShare((SendMessageToWX.Resp) baseResp);
                                break;
                        }
                    }
                }
            };
        }

        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), ThirdPartyConstants.WX_APPID);
            mWXApi.registerApp(ThirdPartyConstants.WX_APPID);
        }

        sWXHandler = mWXHandler;
        sWXApi = mWXApi;
    }

    @Override
    public void login(Activity activity, LoginListener listener) {
        init(activity);
        if (!isInstalled(activity)) {
            if (listener != null)
                listener.onNotInstalled(mType);
            return;
        }

        mLoginListener = listener;

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = ThirdPartyConstants.WX_SCOPE;
        req.state = ThirdPartyConstants.WX_STATE;
        req.transaction = mLastTransaction = buildTransaction("login");

        if (!sWXApi.sendReq(req)) {
            if (mLoginListener != null) {
                mLoginListener.onError(mType, "登录失败");
                mLoginListener = null;
            }
        }
    }

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

        WXMediaMessage mediaMessage = new WXMediaMessage();
        String type = "";

        if (!TextUtils.isEmpty(message.mVideoUrl)) {//视频
            type = "video";

            WXVideoObject videoObject = new WXVideoObject();
            videoObject.videoUrl = message.mVideoUrl;

            mediaMessage.mediaObject = videoObject;
            mediaMessage.title = message.mTitle;
            mediaMessage.description = message.mDescription;
            mediaMessage.thumbData = BitmapUtil.toBytes(message.mThumpBitmap);
        } else if (!TextUtils.isEmpty(message.mMusicUrl)) {//音乐
            type = "music";

            WXMusicObject musicObject = new WXMusicObject();
            musicObject.musicUrl = message.mMusicUrl;

            mediaMessage.mediaObject = musicObject;
            mediaMessage.title = message.mTitle;
            mediaMessage.description = message.mDescription;
            mediaMessage.thumbData = BitmapUtil.toBytes(message.mThumpBitmap);
        } else if (!TextUtils.isEmpty(message.mUrl)) {//网页分享
            type = "web";

            WXWebpageObject webpageObject = new WXWebpageObject();
            webpageObject.webpageUrl = message.mUrl;

            mediaMessage.mediaObject = webpageObject;
            mediaMessage.title = message.mTitle;
            mediaMessage.description = message.mDescription;
            mediaMessage.thumbData = BitmapUtil.toBytes(message.mThumpBitmap);
        } else if (message.mThumpBitmap != null && !message.mThumpBitmap.isRecycled()) {//图片
            type = "image";

            //image限制10M
//            imageObject.imageData = BitmapUtil.compressBitmap(BitmapUtil.toBytes(message.mThumpBitmap), 10 * 1024 * 1024);

            mediaMessage.mediaObject = new WXImageObject(message.mThumpBitmap);

            //直接缩放图片
            Bitmap thumb = Bitmap.createScaledBitmap(message.mThumpBitmap, 200, 200, true);
            mediaMessage.thumbData = BitmapUtil.toBytes(message.mThumpBitmap);
            thumb.recycle();
        } else if (!TextUtils.isEmpty(message.mText)) {//文字
            type = "text";

            //text object
            WXTextObject textObject = new WXTextObject();
            textObject.text = message.mText;

            mediaMessage.mediaObject = textObject;
            mediaMessage.description = message.mText;
        } else {
            if (mShareListener != null) {
                mShareListener.onError(mType, "分享失败");
                mShareListener = null;
            }
            return;
        }

        message.recycle();

        //压缩缩略图到32kb
        if (mediaMessage.thumbData != null && mediaMessage.thumbData.length > '耀')//微信sdk里面判断的大小
            mediaMessage.thumbData = BitmapUtil.compressBitmap(mediaMessage.thumbData, '耀');

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = mediaMessage;
        req.transaction = mLastTransaction = buildTransaction(type);

        if (mType == TYPE_WX) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (mType == TYPE_WX_CIRCLE) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }

        if (!sWXApi.sendReq(req)) {
            if (mShareListener != null) {
                mShareListener.onError(mType, "分享失败");
                mShareListener = null;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public boolean isInstalled(Activity activity) {
        return sWXApi.isWXAppInstalled();
    }

    private void verifyLogin(SendAuth.Resp resp) {
        if (mLoginListener == null)
            return;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                mLoginListener.onSuccess(mType, resp.code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                mLoginListener.onCancel(mType);
                break;
            default:
                mLoginListener.onError(mType, resp.errStr);
                break;
        }
        mLoginListener = null;
    }

    private void verifyShare(SendMessageToWX.Resp resp) {
        if (mShareListener == null)
            return;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK://分享成功
                mShareListener.onSuccess(mType);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://分享取消
                mShareListener.onCancel(mType);
                break;
            default:    //分享失败
                mShareListener.onError(mType, resp.errStr);
                break;
        }
        mShareListener = null;
    }

    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * WXCallbackActivity
     *
     * @return
     */
    static IWXAPIEventHandler getEventHandler() {
        return sWXHandler;
    }

    /**
     * WXCallbackActivity
     *
     * @return
     */
    static IWXAPI getWXAPI() {
        return sWXApi;
    }
}
