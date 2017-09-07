package vite.common.decrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by trs on 17-9-7.
 */

final class AESDecryption implements IDecryption {

    /**
     * "算法/模式/补码方式"
     */
    private static final String AES_WITH_COMPLEMENT = "AES/CBC/PKCS5Padding";
    /**
     * AES
     */
    private static final String AES = "AES";

    private static final String KEY = "kj9T6NgLyGz0ehs6";

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 解密
     *
     * @param
     * @return
     * @throws Exception
     */
    private static String decrypt(String src, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_WITH_COMPLEMENT);
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);// 设置加密Key
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv);// 设置密钥和解密形式
        return new String(cipher.doFinal(hex2byte(src.getBytes())));
    }

    @Override
    public String decrypt(String source) throws Exception {
        return decrypt(source, KEY);
    }
}
