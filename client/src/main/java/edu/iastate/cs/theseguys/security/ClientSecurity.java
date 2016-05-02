package edu.iastate.cs.theseguys.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.*;

@Component
/**
 * Class used for verifying a given message signature using a public key, message, and byte[] signature
 * @author Alex
 *
 */
public class ClientSecurity {
    private static final Logger log = LoggerFactory.getLogger(ClientSecurity.class);

    /**
     * Method used to check whether or not the message can be verified with the public key
     * @param message
     * @param sig
     * @param publicKey
     * @return true if verified
     */
    public boolean verifySignature(String message, byte[] sig, PublicKey publicKey) {
        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(message.getBytes("UTF-8"));
            return signature.verify(sig);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.warn(e.toString());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            log.warn(e.toString());
        } catch (SignatureException e) {
            e.printStackTrace();
            log.warn(e.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
        return false;
    }


}
