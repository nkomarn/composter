package xyz.nkomarn.composter.util;

import java.security.*;
import java.util.Random;

public class
RSAKeyGen {
    private final KeyPair keyPair;

    public RSAKeyGen() throws NoSuchAlgorithmException {
        this.keyPair = GenerateKeyPair();
    }

    private KeyPair GenerateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        return keyGen.genKeyPair();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public byte[] generateBytes() {
        byte[] b = new byte[4];
        new Random().nextBytes(b);
        return b;
    }

}
