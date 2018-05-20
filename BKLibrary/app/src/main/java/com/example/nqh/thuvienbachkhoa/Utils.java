package com.example.nqh.thuvienbachkhoa;

import android.text.TextUtils;
import android.util.Patterns;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}
