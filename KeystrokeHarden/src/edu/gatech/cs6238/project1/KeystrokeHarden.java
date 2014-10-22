package edu.gatech.cs6238.project1;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;


public class KeystrokeHarden {
	private static char[] loginPwd;
	private static String username;
	private static String name;
	private static double[] feature;
	private static int sequence;
	public static void main(String[] args) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, IOException {
		//********Initialization**********
		Initialization init = new Initialization();
		name = new String();
		HistoryFile histFile = new HistoryFile();
		histFile.encryptTable(Initialization.getHpwd());
		System.out.println("Successfully registered!! However, the harden Password system needs several times of successful login for training...");
		
		//********Log-in Attempts(Test file loading)*********
		System.out.println("**********Request for login***********");
		BufferedReader br = new BufferedReader(new FileReader("testfile.txt"));
		String line;	
		InputUsername();
		//username = "shaolei";   //static input for test
		System.out.println("(To speed up, input password for once, then the login attempts will automatically run:)");
		InputPwd();
		//loginPwd = "12345678".toCharArray();  //static input for test
		System.out.println("Start Login Attempts......");
		while ((line = br.readLine()) != null) {
			String[] string = line.split(" ");
			System.out.println("\nLogin Attempt: No."+string[0]);
			sequence = Double.valueOf(string[0]).intValue();
			name = string[1];
			if(!name.equals(username)){
				System.out.println("Wrong username!");
			}
			else {
				feature = new double[Initialization.getNumFeature()];
				for(int i = 0; i < 15; i++) {
					feature[i] = Double.valueOf(string[i+2]);
				}
				//*********Login Authentication + Upate&Training**********
				LoginAttempt(histFile, feature, histFile.getCipherText(), name);
			}
		}
		br.close();
		
	}
	
	private static void LoginAttempt(HistoryFile histFile, double[] feature, ArrayList<String> cipher_text, String name) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, IOException {

		//**********Login Authentication************
		LoginAuth login = new LoginAuth(name, feature, loginPwd);
		//System.out.println(cipher_text.get(1).length());
		boolean success = histFile.decryptTable(cipher_text,LoginAuth.getHpwdPrime(), name, sequence);
		
		//**********Update & Training***************
		if(success) {
			//Update if success
			histFile.updateHFile(feature);
			System.out.println("Updated history file.");
			//histFile.printHistory();
			if(HistoryFile.isFull()) {
				//System.out.println("Now Full, Start calculation!");
				histFile.Update();
				histFile.encryptTable(Initialization.getHpwd());
			}
			else {
				System.out.println("History file is not full, system still need training.");
				histFile.Update_training();
				histFile.encryptTable(Initialization.getHpwd());
			}
		}
		else {
			System.out.println("Loggin failed");
		}
		
	}
	
	private static void InputPwd() {      
	    Console console = System.console();
	    if (console == null) {
	        System.out.println("Couldn't get Console instance");
	        System.exit(0);
	    }
	    //digit check
	    loginPwd = console.readPassword("Please enter your secret password(8 digits): ");
	    while(loginPwd.length != 8) {
	    	loginPwd = console.readPassword("Password should be 8-digits, please re-enter your secret password: ");
	    }
	}
	
	private static void InputUsername() {
		Console console = System.console();
	    if (console == null) {
	        System.out.println("Couldn't get Console instance");
	        System.exit(0);
	    }
	    char[] usr;
	    usr = console.readPassword("To sign up an account, please enter your username: ");
	    username = new String(usr);
	}
}