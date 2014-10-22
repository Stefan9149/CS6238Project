package edu.gatech.cs6238.project1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;

public class Crypt {
    public static String iniHashValue = null;
	private static IvParameterSpec IV = new IvParameterSpec(new byte[16]);

	public Crypt() {
	}

	private static Key enableKey(BigInteger hpwd) {
		
		byte[] keyArray = hpwd.toByteArray();

		SecretKeySpec key = new SecretKeySpec(keyArray, 0, 16, "AES");
		return key;
	}

	private static String encrypt(String plaintext, BigInteger BIkey) {
		Key key = enableKey(BIkey);

		Cipher encryptCipher = null;
		try {
			encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			try {
				try {
					encryptCipher.init(Cipher.ENCRYPT_MODE, key, IV);
					// encryptCipher.init(Cipher.ENCRYPT_MODE, key,new
					// IvParameterSpec(new byte[16]));
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] stringBytes = null;
		// stringBytes = plaintext.getBytes();
		stringBytes = plaintext.getBytes(Charset.forName("UTF-8"));

		// encrypt using the cipher
		byte[] encrypted = null;
		try {
			// encrypted = encryptCipher.doFinal(stringBytes);
			if(stringBytes!=null){
			encrypted = encryptCipher.doFinal(stringBytes);
			}
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			return "Wrong Key!";
		}
		String cipherText = null;
		if(encrypted!=null){
		cipherText = new String(encrypted);
		}
		// BigInteger cipherText = new BigInteger(encrypted);

		return cipherText;

	}

	private static String decrypt(String cipherText, BigInteger BIkey) {
		Key key = enableKey(BIkey);
		Cipher decryptCipher = null;
		try {
			decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			// System.out.println("key for decrypt is "+ key);

			try {
				try {
					// decryptCipher.init(Cipher.DECRYPT_MODE, key,new
					// IvParameterSpec(new byte[16]));
					decryptCipher.init(Cipher.DECRYPT_MODE, key, IV);
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println("key for decrypt is "+ key);

			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] stringBytes = null;
		stringBytes = cipherText.getBytes();

		byte[] decrypted = null;

		try {
			if(stringBytes!=null){
			decrypted = decryptCipher.doFinal(stringBytes);
			}
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			return "Wrong Key!";
		}
		String plainText = null;
		if(decrypted!= null){
		 plainText = new String(decrypted);
		}
		// BigInteger plainText = new BigInteger(decrypted);

		return plainText;

	}

	private static String hashHFile(String HFile) {

		// String sTBuffer = new String(tBuffer);
		String hashValue = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(HFile.getBytes());
			byte[] output = md.digest();
			hashValue = new String(output);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("hashValue is " + hashValue);
		return hashValue;

	}

	public static boolean checkHash(String newHashValue) {
		if (newHashValue.equals(iniHashValue)) {
			System.out.println("Login success!!");
			return true;
		}
		System.out.println("Login fail..");
		return false;
	}

	public static ArrayList<String> encHFile(double[][] table, BigInteger hpwd, String username) {
		StringBuffer hashBuffer = new StringBuffer();

		ArrayList<String> cipherText = new ArrayList();
		for (int i = 0; i < 8; i++) {
			StringBuffer plainText = new StringBuffer();
			for (int j = 0; j < 15; j++) {
				plainText.append(table[i][j]);
				plainText.append(',');

			}
			hashBuffer.append(plainText);
			String plain = new String(plainText);

			cipherText.add(encrypt(plain, hpwd));
		}
		
		//System.out.println(cipherText.size());
		//System.out.println(cipherText.get(1).length());

		String hashString = new String(hashBuffer);
		iniHashValue = hashHFile(hashString);

		
		try {
			PrintWriter writer = new PrintWriter(username + "_history_encrypted.txt", "UTF-8");
			System.out.println("Created encrypted history file: " + username + "_history_encrypted.txt");
			Iterator i = cipherText.iterator();
			while (i.hasNext()) {

				writer.println(i.next());
				
			}
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cipherText;
	}

	public static boolean decHFile(ArrayList<String> cipherText,
			BigInteger hpwd, String name, int number) 
	{
		StringBuffer dHashBuffer = new StringBuffer();
		ArrayList<String> plainText = new ArrayList<String>();
		Iterator ii = cipherText.iterator();
		while (ii.hasNext()) {
			String ciphertext = (String) ii.next();
			String test = decrypt(ciphertext, hpwd);
			if(test == "Wrong Key!") return false;
			dHashBuffer.append(test);
			// System.out.println("test decrypt is "+ test);
			plainText.add(decrypt(ciphertext, hpwd));
			// plainText.add(test);
		}
		//test decrypted file
		/*
		PrintWriter writer;
		try {
			writer = new PrintWriter("Decrypted_History_"+name+"_"+number+".txt", "UTF-8");
			System.out.println("create decrypted history file");
			Iterator it = plainText.iterator();
			while (it.hasNext()) {
				writer.println(it.next());
			}
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		String dHString = new String(dHashBuffer);

		return checkHash(hashHFile(dHString));

	}

}
