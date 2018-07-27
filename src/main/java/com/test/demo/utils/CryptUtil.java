package com.test.demo.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {

    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

    public static byte[] encryptAES(byte[] input,byte[] key){
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(new SecretKeySpec(key, "AES").getEncoded(), "AES"));// 初始化
            return cipher.doFinal(input); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptAES(byte[] input,byte[] key){
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(new SecretKeySpec(key, "AES").getEncoded(), "AES"));// 初始化
            return cipher.doFinal(input); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
