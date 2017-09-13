package vite.common.thirdparty.sso.wx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by trs on 17-9-11.
 */

public class WXCallbackActivity extends Activity implements IWXAPIEventHandler {

//    private WXDelegate mWXDelegate;
//    private WXDelegate mWXCircleDelegate;

    private IWXAPI mIWXAPI;
    private IWXAPIEventHandler mIWXAPIHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (mIWXAPI != null)
            mIWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//        init();
        if (mIWXAPI != null)
            mIWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        if (mIWXAPIHandler != null)
            mIWXAPIHandler.onReq(baseReq);

        this.finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp != null) {
            if (mIWXAPIHandler != null)
                mIWXAPIHandler.onResp(baseResp);
        }
        finish();
    }

    private void init() {
        mIWXAPI = WXDelegate.getWXAPI();
        mIWXAPIHandler = WXDelegate.getEventHandler();
    }
}
