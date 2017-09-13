package vite.common.thirdparty.sso.wx;

import android.graphics.Bitmap;

import vite.common.thirdparty.sso.ShareMessage;


/**
 * Created by trs on 17-9-13.
 */

public final class WXShareMessageBuilder extends ShareMessage.Builder {

    /**
     * 分享文字
     *
     * @param text
     * @return
     */
    public WXShareMessageBuilder text(String text) {
        mMsg.mText = text;
        return this;
    }

    /**
     * 分享内容的标题
     *
     * @param title
     * @return
     */
    public WXShareMessageBuilder title(String title) {
        mMsg.mTitle = title;
        return this;
    }

    /**
     * 分享内容的描述
     *
     * @param desc
     * @return
     */
    public WXShareMessageBuilder description(String desc) {
        mMsg.mDescription = desc;
        return this;
    }

    /**
     * 分享网页（可加image）
     *
     * @param url
     * @return
     */
    public WXShareMessageBuilder web(String url) {
        mMsg.mUrl = url;
        return this;
    }

    /**
     * 分享图片
     *
     * @param bitmap
     * @return
     */
    public WXShareMessageBuilder image(Bitmap bitmap) {
        mMsg.mThumpBitmap = bitmap;
        return this;
    }

    /**
     * 分享音乐（可加image）
     *
     * @param url
     * @return
     */
    public WXShareMessageBuilder music(String url) {
        mMsg.mMusicUrl = url;
        return this;
    }

    /**
     * 分享视频（可加image）
     *
     * @param url
     * @return
     */
    public WXShareMessageBuilder video(String url) {
        mMsg.mVideoUrl = url;
        return this;
    }
}
