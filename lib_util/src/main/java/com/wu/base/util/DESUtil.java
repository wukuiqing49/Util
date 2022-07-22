package com.wu.base.util;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import static javax.crypto.Cipher.getInstance;

/**
 * 加密工具
 */
public class DESUtil {

    public static final String DES_KEY = "cnliveIm";
    private static String encoding = "UTF-8";

    /**
     * 加密字符串
     */
    public static String ebotongEncrypto(String str, String key) {
        String result = str;
        if (str != null && str.length() > 0) {
            try {
                byte[] encodeByte = symmetricEncrypto(str.getBytes(encoding), key);
                result = Base64.encodeToString(encodeByte, Base64.DEFAULT);

            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 解密字符串
     */
    public static String ebotongDecrypto(String str, String key) {
        String result = str;
        if (str != null && str.length() > 0) {
            try {
                byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);

                byte[] decoder = symmetricDecrypto(encodeByte, key);
                result = new String(decoder, encoding);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 加密byte[]
     */
    public static byte[] ebotongEncrypto(byte[] str, String key) {
        byte[] result = null;
        if (str != null && str.length > 0) {
            try {
                byte[] encodeByte = symmetricEncrypto(str, key);
                result = Base64.encode(encodeByte, Base64.DEFAULT);

            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 解密byte[]
     */
    public static byte[] ebotongDecrypto(byte[] str, String key) {
        byte[] result = null;
        if (str != null && str.length > 0) {
            try {

                byte[] encodeByte = Base64.decode(new String(str, encoding), Base64.DEFAULT);
                //byte[] encodeByte = base64decoder.decodeBuffer(new String(str));
                byte[] decoder = symmetricDecrypto(encodeByte, key);
                result = new String(decoder).getBytes(encoding);
                result = new String(decoder).getBytes();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 对称加密字节数组并返回
     *
     * @param byteSource 需要加密的数据
     * @return 经过加密的数据
     * @throws Exception
     */
    public static byte[] symmetricEncrypto(byte[] byteSource, String sKey) throws Exception {
        try {
            int mode = Cipher.ENCRYPT_MODE;
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            byte[] keyData = sKey.getBytes();
            DESKeySpec keySpec = new DESKeySpec(keyData);
            Key key = keyFactory.generateSecret(keySpec);
            Cipher cipher = getInstance("DES");
            cipher.init(mode, key);

            return cipher.doFinal(byteSource);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 对称解密字节数组并返回
     *
     * @param byteSource 需要解密的数据
     * @return 经过解密的数据
     * @throws Exception
     */
    public static byte[] symmetricDecrypto(byte[] byteSource, String sKey) throws Exception {
        try {
            int mode = Cipher.DECRYPT_MODE;
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            byte[] keyData = sKey.getBytes();
            DESKeySpec keySpec = new DESKeySpec(keyData);
            Key key = keyFactory.generateSecret(keySpec);
            Cipher cipher = getInstance("DES");
            cipher.init(mode, key);
            return cipher.doFinal(byteSource);
        } catch (Exception e) {
            return null;
        }
    }
}
