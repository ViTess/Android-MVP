package vite.common.thirdparty.sso;

/**
 * Created by trs on 17-9-11.
 */

public interface ShareListener {
    void onSuccess(@SSOManager.Type int type);

    void onError(@SSOManager.Type int type, String err);

    void onCancel(@SSOManager.Type int type);

    void onNotInstalled(@SSOManager.Type int type);
}
