package mockstock.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class encrypts a given string to MD5 hash value
 * @author Kenny Lienhard
 */
public class PasswordEncrypter {

    public static String encryptPassword(String password) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes(), 0, password.length());
            hash = new BigInteger(1, m.digest()).toString(16);
            //if the returned md5 value has a 0 in front it will be trunked
            while (hash.length() < 32) {
                hash = "0" + hash;
            }
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        return hash;
    }
}
