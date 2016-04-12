package edu.iastate.cs.theseguys.security;

import java.io.UnsupportedEncodingException;
import java.security.*;

public class AuthoritySecurity {
	
	private PrivateKey privateKey; //TODO need to store this somewhere once it is created
	private PublicKey publicKey; //TODO need to make this key available to users
	

	public boolean generateKeyPair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			kpg.initialize(1024, random);
			KeyPair pair = kpg.generateKeyPair();
			
			privateKey = pair.getPrivate();
			publicKey = pair.getPublic();
			//TODO store keys
			return true;
			
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
	
	
}
