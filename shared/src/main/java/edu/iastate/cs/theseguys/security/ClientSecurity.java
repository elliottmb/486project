package edu.iastate.cs.theseguys.security;

import java.security.*;

public class ClientSecurity {

    public boolean verifySignature(byte[] message, byte[] sig, PublicKey publicKey) {
        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(message);
            return signature.verify(sig);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }


}
