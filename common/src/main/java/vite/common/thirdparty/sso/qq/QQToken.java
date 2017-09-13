package vite.common.thirdparty.sso.qq;

/**
 * Created by trs on 17-9-11.
 */

public final class QQToken {
    public String token;
    public String expires;
    public String openId;

    @Override
    public String toString() {
        return "QQToken{" +
                "token='" + token + '\'' +
                ", expires='" + expires + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }
}
