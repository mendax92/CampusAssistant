package com.ming.base.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {

	/**
     * 加密--把加密后的byte数组先进行二进制转16进制在进行base64编码
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) { //密钥不能为空
            throw new IllegalArgumentException("Argument sKey is null.");
        }
        if (sKey.length() < 16) { //密钥长度必须为16位
            throw new IllegalArgumentException("Argument sKey'length is not 16.");
        }
        byte[] raw = sKey.getBytes("ASCII"); //将密钥以ASCII转成二进制
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES/ECB/PKCS7Padding");//生成加密密钥对象

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");//创建AES加密对象
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);//初始化AES加密对象
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));//进行AES加密

        String tempStr = parseByte2HexStr(encrypted);//将AES加密后的二进制转成十六进制文本

        return Base64.encodeToString(tempStr.getBytes("UTF-8"), Base64.NO_WRAP);//将数据Base64加密
    }

    public static String simpleEncrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) { //密钥不能为空
            throw new IllegalArgumentException("Argument sKey is null.");
        }
        if (sKey.length() < 16) { //密钥长度必须为16位
            throw new IllegalArgumentException("Argument sKey'length is not 16.");
        }
        byte[] raw = sKey.getBytes("ASCII"); //将密钥以ASCII转成二进制
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES/ECB/PKCS7Padding");//生成加密密钥对象

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");//创建AES加密对象
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);//初始化AES加密对象
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));//进行AES加密

        //String tempStr = parseByte2HexStr(encrypted);//将AES加密后的二进制转成十六进制文本

        return Base64.encodeToString(encrypted, Base64.NO_WRAP);//将数据Base64加密
    }

    public static String simpleDecrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            throw new IllegalArgumentException("Argument sKey is null.");
        }
        if (sKey.length() < 16) {
            throw new IllegalArgumentException("Argument sKey'length is not 16.");
        }

        byte[] raw = sKey.getBytes("ASCII"); //获取密钥ASCII编码的二进制
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES");//生成AES解密对象
        cipher.init(Cipher.DECRYPT_MODE, skeySpec); //初始AES解密对象
        byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);//将解密的字符串Base64解密
        byte[] original = cipher.doFinal(encrypted1);//AES解密
        String originalString = new String(original, "utf-8");//界面结果转UTF-8编码文本
        return originalString;
    }

    /**
     * 解密--先 进行base64解码，在进行16进制转为2进制然后再解码
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            throw new IllegalArgumentException("Argument sKey is null.");
        }
        if (sKey.length() < 16) {
            throw new IllegalArgumentException("Argument sKey'length is not 16.");
        }

        byte[] raw = sKey.getBytes("ASCII"); //获取密钥ASCII编码的二进制
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES");//生成AES解密对象
        cipher.init(Cipher.DECRYPT_MODE, skeySpec); //初始AES解密对象

        byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);//将解密的字符串Base64解密


        String tempStr = new String(encrypted1, "utf-8");//Base64解密后取UTF-8编码文本
        encrypted1 = parseHexStr2Byte(tempStr); //十六进制转二进制
        byte[] original = cipher.doFinal(encrypted1);//AES解密
        String originalString = new String(original, "utf-8");//界面结果转UTF-8编码文本
        return originalString;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
