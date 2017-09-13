package vite.common.thirdparty.sso.qq;

import vite.common.thirdparty.sso.ShareMessage;

/**
 * Created by trs on 17-9-13.
 */

public final class QQShareMessageBuilder extends ShareMessage.Builder {
    public QQShareMessageBuilder title(String title) {
        mMsg.mTitle = title;
        return this;
    }

    public QQShareMessageBuilder description(String desc) {
        mMsg.mDescription = desc;
        return this;
    }

    public QQShareMessageBuilder web(String url) {
        mMsg.mUrl = url;
        return this;
    }

    public QQShareMessageBuilder localImage(String path) {
        mMsg.mLocalImagePath = path;
        return this;
    }

    public QQShareMessageBuilder music(String url) {
        mMsg.mMusicUrl = url;
        return this;
    }
}
