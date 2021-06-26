package xyz.nkomarn.composter.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Random;

/**
 * Generates RSA Public and Private keys for client
 * and server auth
 */

public class RSA {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final byte[] token;

    public RSA() throws NoSuchAlgorithmException {
        KeyPair keyPair = GenerateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        this.token = generateBytes();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getToken() { return token; }

    public byte[] generateBytes() {
        byte[] b = new byte[4];
        new Random().nextBytes(b);
        return b;
    }

    private KeyPair GenerateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        return keyGen.genKeyPair();
    }

    public byte[] decrypt(byte[] buffer) {
        try {
            Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.DECRYPT_MODE, privateKey);
            return rsa.doFinal(buffer);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
