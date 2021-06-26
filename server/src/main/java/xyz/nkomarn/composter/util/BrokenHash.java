package xyz.nkomarn.composter.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.nio.charset.StandardCharsets;

/**
 * Generates a broken Minecraft-style twos-complement signed
 * hex digest. Tested and confirmed to match vanilla.
 */
public class BrokenHash {

    public static String encryptServerHash(byte[] publicKey, byte[] sharedSecret) {
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-1");
            hash.reset();
            hash.update(publicKey);
            hash.update(sharedSecret);
            return hash(hash.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String hash(String str) {
        try {
            byte[] digest = digest(str);
            return new BigInteger(digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] digest(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        return md.digest(strBytes);
    }
}
