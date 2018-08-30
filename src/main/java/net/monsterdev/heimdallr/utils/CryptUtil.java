package net.monsterdev.heimdallr.utils;

import net.monsterdev.heimdallr.exceptions.CryptException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class CryptUtil {
    private static final String PASSPHRASE = "1$%pass%$1phrase";

    private static SecretKeySpec secretKey;

    public static void initialize() {
        MessageDigest sha;

        try {
            byte[] key = PASSPHRASE.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String data) {
        if (secretKey == null)
            initialize();

        String result = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            result = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")));
        } catch (Exception e) {
            // TODO: put error information into log
        }
        return result;
    }

    public static String decrypt(String data) throws CryptException {

        if (secretKey == null)
            initialize();

        String result = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            result = new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        } catch (Exception e) {
            // TODO: put error information into log
            throw new CryptException(e.getMessage());
        }
        return result;
    }
}
