package com.example.qtp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Patterns;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;

public class Utils {
    static final String QR_KEY = "QTP";
    static final String DELIMITER = ":";

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    public static boolean isContactValid(String contact) {
        return contact.length() == 10;
    }

    public static boolean isMpinValid(String mpin) {
        return mpin.length() == 4;
    }

    public static ProgressDialog getProgressDialog(Context context, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static byte[] getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptMPIN(String mpin, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(mpin.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            sb.append(DELIMITER).append(base64Encode(salt));
            generatedPassword = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static boolean isMpinAuthentic(String mpin, String secureMPIN) {
        String splitMpin[] = secureMPIN.split(DELIMITER);

        String salt = splitMpin[1];
        byte[] saltInByte = base64Decode(salt);

        return secureMPIN.equals(encryptMPIN(mpin, saltInByte));
    }


    public static boolean isValidQRContent(String content) {
        if (content.startsWith(QR_KEY)) {
            content = content.replace(QR_KEY, "");

            String[] splitContents = content.split(DELIMITER);
            long expiryTime = Long.parseLong(splitContents[0]);

            long now = Calendar.getInstance().getTimeInMillis();

            return expiryTime > now;
        } else
            return false;
    }

    public static boolean isQRContentStartsWithKey(String content) {
        return content.startsWith(AESHelper.seed);
    }

    public static String[] furnishQRContent(String content) {

        content = content.replace(QR_KEY, "");

        String[] splitContents = content.split(DELIMITER);
        String uid = splitContents[1];
        String amount = splitContents[2];

        return new String[]{uid, amount};
    }

    public static String getDebitInfo(String uid, String amount) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 1);

        long expiryTime = now.getTimeInMillis();

        StringBuilder sb = new StringBuilder()
                .append(QR_KEY)
                .append(expiryTime)
                .append(DELIMITER)
                .append(uid)
                .append(DELIMITER)
                .append(amount);


        return sb.toString();
    }

    private static String base64Encode(byte[] bytes) {

        return Base64.encodeToString(bytes, Base64.NO_WRAP | Base64.URL_SAFE | Base64.NO_PADDING);
    }

    private static byte[] base64Decode(String s) {
        return Base64.decode(s, Base64.NO_WRAP | Base64.URL_SAFE | Base64.NO_PADDING);
    }
}
