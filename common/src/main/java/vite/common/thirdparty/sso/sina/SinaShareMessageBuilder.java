package vite.common.thirdparty.sso.sina;

import android.graphics.Bitmap;

import vite.common.thirdparty.sso.ShareMessage;

import java.util.ArrayList;

/**
 * Created by trs on 17-9-13.
 */

public final class SinaShareMessageBuilder extends ShareMessage.Builder {
    public SinaShareMessageBuilder text(String text) {
        mMsg.mText = text;
        return this;
    }

    public SinaShareMessageBuilder singleImage(Bitmap bitmap) {
        mMsg.mThumpBitmap = bitmap;
        return this;
    }

    public SinaShareMessageBuilder multiImage(ArrayList<String> paths) {
        mMsg.mMultiImagePath = paths;
        return this;
    }

    public SinaShareMessageBuilder localVideo(String path) {
        mMsg.mLocalVideoPath = path;
        return this;
    }
}
