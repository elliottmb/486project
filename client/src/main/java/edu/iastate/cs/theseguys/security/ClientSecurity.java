package edu.iastate.cs.theseguys.security;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.*;

@Component
public class ClientSecurity {

    public boolean verifySignature(String message, byte[] sig, PublicKey publicKey) {
        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(message.getBytes("UTF-8"));
            return signature.verify(sig);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }


}
