package edu.iastate.cs.theseguys.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class AuthoritySecurity {
	
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
