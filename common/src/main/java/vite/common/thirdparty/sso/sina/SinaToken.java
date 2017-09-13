package vite.common.thirdparty.sso.sina;

/**
 * Created by trs on 17-9-11.
 */

public class SinaToken {
    public String token;
    public String uid;

    @Override
    public String toString() {
        return "SinaToken{" +
                "token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
