package vite.common.thirdparty.sso;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by trs on 17-9-11.
 */

public class ShareMessage {

    public String mText;
    public String mTitle;
    public String mDescription;
    public String mUrl;
    public String mVideoUrl;
    public String mMusicUrl;
    public Bitmap mThumpBitmap;//weixin
    public String mLocalImagePath;//QQ
    public String mLocalVideoPath;//Weibo
    public ArrayList<String> mMultiImagePath;//Weibo

    public ShareMessage() {
    }

//    public void text(String text) {
//        mText = text;
//    }
//
//    public void title(String title) {
//        mTitle = title;
//    }
//
//    public void description(String description) {
//        mDescription = description;
//    }
//
//    public void url(String url) {
//        mUrl = url;
//    }
//
//    public void videoUrl(String url) {
//        mVideoUrl = url;
//    }
//
//    public void musicUrl(String url) {
//        mMusicUrl = url;
//    }
//
//    public void thumbBitmap(Bitmap bitmap) {
//        mThumpBitmap = bitmap;
//    }
//
//    public void localImagePath(String localPath) {
//        mLocalImagePath = localPath;
//    }
//
//    public void localVideoPath(String path){
//        mLocalVideoPath = path;
//    }
//
//    public void multiLocalImage(ArrayList<String> path){
//        mMultiImagePath = path;
//    }

    public void recycle() {
        if (mThumpBitmap != null && !mThumpBitmap.isRecycled())
            mThumpBitmap.recycle();
        mThumpBitmap = null;
    }

    public static abstract class Builder {
        protected final ShareMessage mMsg = new ShareMessage();

        public ShareMessage build() {
            return mMsg;
        }
    }
}
