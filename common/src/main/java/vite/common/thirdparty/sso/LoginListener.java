package vite.common.thirdparty.sso;

/**
 * Created by trs on 17-9-11.
 */

public interface LoginListener {
    /**
     * @param type
     * @param data 登录成功后，返回的数据
     *             WX:code字符串
     *             QQ:QQToken entity
     *             WB:SinaToken entity
     */
    void onSuccess(@SSOManager.Type int type, Object data);

    void onError(@SSOManager.Type int type, String msg);

    void onCancel(@SSOManager.Type int type);

    void onNotInstalled(@SSOManager.Type int type);
}
