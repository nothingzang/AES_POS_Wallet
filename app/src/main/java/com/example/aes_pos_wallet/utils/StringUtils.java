package com.example.aes_pos_wallet.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * Created by Nothing on 2019/7/30.
 */

public class StringUtils {

    public static boolean isPhoneNumber(String number) {
        // TODO: 2019/7/30 By Nothing 校验手机号是否合规
        if (TextUtils.isEmpty(number)) return false;
        return true;
//        return number.length() == 11;
    }

    public static boolean confirmCodeNumber(String codeNumber) {
        // TODO: 2019/7/30 By Nothing  校验验证码是否合规
        if (TextUtils.isEmpty(codeNumber)) return false;
        return true;
//        return codeNumber.length() == 6;
    }

    public static boolean confirmPassWord(String passWord) {
        // TODO: 2019/7/30 By Nothing 校验密码是否合规
        if (TextUtils.isEmpty(passWord)) return false;
        return passWord.length() > 5;
    }


    public static String getMdPassWord(String passWord) {
        String salt = "1a2b3c4d";
        String str = "" + salt.charAt(0) + salt.charAt(2) + passWord + salt.charAt(5) + salt.charAt(4);
        String mdPassword = md5(str);
        return mdPassword;
    }


    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String md5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static String md5(String string, String slat) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + slat).getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getDouble(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }
}
