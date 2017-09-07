package vite.common.decrypt;

import android.support.annotation.IntDef;

/**
 * Created by trs on 17-9-7.
 */

public final class DecryptionFactory {

    public static final int AES = 1;

    @IntDef({AES})
    public @interface Type {
    }

    public static IDecryption create(@Type int type) {
        switch (type) {
            case AES:
                return new AESDecryption();
            default:
                return new NoneDecryption();
        }
    }

    private static final class NoneDecryption implements IDecryption {
        @Override
        public String decrypt(String source) {
            return source;
        }
    }
}
