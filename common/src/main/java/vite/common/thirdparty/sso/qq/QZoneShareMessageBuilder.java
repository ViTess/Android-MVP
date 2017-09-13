package vite.common.thirdparty.sso.qq;

import vite.common.thirdparty.sso.ShareMessage;

/**
 * Created by trs on 17-9-13.
 */

public final class QZoneShareMessageBuilder extends ShareMessage.Builder {
    public QZoneShareMessageBuilder title(String title) {
        mMsg.mTitle = title;
        return this;
    }

    public QZoneShareMessageBuilder description(String desc) {
        mMsg.mDescription = desc;
        return this;
    }

    public QZoneShareMessageBuilder web(String url) {
        mMsg.mUrl = url;
        return this;
    }

    public QZoneShareMessageBuilder localImage(String path) {
        mMsg.mLocalImagePath = path;
        return this;
    }
}
