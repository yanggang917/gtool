package com.cn.gtool.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Auther: yg
 * @Date: 2019/12/10 11:55
 * @Description:
 */
public class EncryptUtil {

    public static String md5(String str) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }

    public static String sha(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(str.getBytes());
        return new BigInteger(md.digest()).toString(16);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
//        System.out.println(sha("1"));
        System.out.println(md5("1"));
    }
}
