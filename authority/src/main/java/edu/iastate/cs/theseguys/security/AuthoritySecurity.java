package edu.iastate.cs.theseguys.security;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AuthoritySecurity {

/*	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        AuthoritySecurity a = new AuthoritySecurity();
		System.out.println(a.generateKeyPair());
		byte[] arr = a.generateSignature("Test message", a.getPrivateKey());
		ClientSecurity c = new ClientSecurity();
		System.out.println(c.verifySignature("Test message".getBytes(), arr, a.getPublicKey()));
	}*/

    public boolean generateKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            kpg.initialize(1024, random);
            KeyPair pair = kpg.generateKeyPair();

            try {
                if (!new File("pub.key").isFile() && !new File("priv.key").isFile()) {
                    FileOutputStream keyOutput = new FileOutputStream("pub.key");
                    keyOutput.write(pair.getPublic().getEncoded());
                    keyOutput.close();

                    keyOutput = new FileOutputStream("priv.key");
                    keyOutput.write(pair.getPrivate().getEncoded());
                    keyOutput.close();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte[] generateSignature(String message, PrivateKey priv) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(priv);
            sig.update(message.getBytes("UTF-8"));
            return sig.sign();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;

    }

    public PublicKey getPublicKey() {

        try {
            FileInputStream keyStream = new FileInputStream("pub.key");
            byte[] pubKeyBytes = new byte[keyStream.available()];
            keyStream.read(pubKeyBytes);
            keyStream.close();
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKeyBytes));
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey getPrivateKey() {
        try {
            FileInputStream keyStream = new FileInputStream("priv.key");
            byte[] privKeyBytes = new byte[keyStream.available()];
            keyStream.read(privKeyBytes);
            keyStream.close();
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privKeyBytes));

        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}