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
            hash.update(sharedSecret);
            hash.update(publicKey);
            return hash(hash.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String hash(byte[] digest) {
        try {
            return new BigInteger(digest).toString(16);
        } catch (Error e) {
            throw new RuntimeException(e);
        }
    }
}
