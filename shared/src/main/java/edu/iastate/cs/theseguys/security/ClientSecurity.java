package edu.iastate.cs.theseguys.security;

import java.io.UnsupportedEncodingException;
import java.security.*;

public class ClientSecurity {
	
	private PrivateKey privateKey;
	private PublicKey publicKey; //TODO need to send this key to the central authority
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		ClientSecurity sec = new ClientSecurity();
		sec.generateKeyPair();
		String message = "test message";
		byte[] arr = sec.generateSignature(message);
		AuthoritySecurity auth = new AuthoritySecurity();
		boolean ver = auth.verifySignature(message.getBytes("UTF-8"), arr, sec.publicKey);
		System.out.println(ver);
	}

	public boolean generateKeyPair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			kpg.initialize(1024, random);
			KeyPair pair = kpg.generateKeyPair();
			
			privateKey = pair.getPrivate();
			publicKey = pair.getPublic();
			return true;
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;	
	}
	
	public byte[] generateSignature(String message) {
		try {
			Signature sig = Signature.getInstance("SHA256withRSA");
			sig.initSign(privateKey);
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
