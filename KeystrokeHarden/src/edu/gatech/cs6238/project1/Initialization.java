package edu.gatech.cs6238.project1;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Random;

public class Initialization {
	private static BigInteger bigPrime;
	private static BigInteger hpwd;
	private static int historySize;
	private static int numFeature;
	private static String username;
	private static char[] pwd;
	private static Polynomial p;
	private static InstTable instTable;
	
	Initialization() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, IOException {
		System.out.println("**********Initialization**********");
		numFeature = 15;
		historySize = 8;
		bigPrime = primeGenerator(160); 	//select a 160 bits value prime => bigPrime
		hpwd = hpwdGenerator(bigPrime);		//randomly produce a hpwd < bigPrime
		p = new Polynomial();				//create a Polynomial, hidden Hpwd into it.

		//set username & password from command line
		setUsername();
		setPwd();
		//static setting for test
		//setUsername("shaolei");
		//setPwd("12345678");
		
		//create Instruction table
		instTable = new InstTable();
		//instTable.printTable();
	}
	
	//Generate a big prime with defined bit length
	private static BigInteger primeGenerator(int length) {
		BigInteger primeNumber;
		primeNumber = BigInteger.probablePrime(length, new Random());
		return primeNumber;
	}
	
	//Generate the hpwd < a big prime
	private static BigInteger hpwdGenerator(BigInteger n) {
		Random rand = new Random();
		BigInteger result = new BigInteger(n.bitLength(), rand);
		while(result.compareTo(n) >= 0) {
			result = new BigInteger(n.bitLength(), rand);
		}
		return result;
	}
	
	//statically set username & pwd, for test
	private void setPwd(String s) {
		pwd = s.toCharArray();
	}
	
	private void setUsername(String n) {
		username = n;
	}  
	
	//user input username & pwd
	static Console console = System.console();
	private static void setUsername() {    
	    if (console == null) {
	        System.out.println("Couldn't get Console instance");
	        System.exit(0);
	    }
	    char[] usr;
	    usr = console.readPassword("To sign up an account, please enter your username: ");
	    username = new String(usr);
	}
	
	private static void setPwd() {    
	    if (console == null) {
	        System.out.println("Couldn't get Console instance");
	        System.exit(0);
	    }
	    
	    pwd = console.readPassword("Please enter your secret password(8 digits): ");
	    if(pwd.length != 8) {
	    	pwd = console.readPassword("Password should be 8 digits, please re-enter your secret password: ");
	    }

	}
	
	public static String getUsername() {
		return username;
	}
	public static char[] getPwd() {
		return pwd;
	}
	
	public static int getHistorySize() {
		return historySize;
	}
	
	public static BigInteger getHpwd() {
		return hpwd;
	}
	
	public static BigInteger getPrime() {
		return bigPrime;
	}
	
	public static int getNumFeature() {
		return numFeature;
	}
	
	
	public static Polynomial getPoly() {
		return p;
	}
	
	//create Polynomial when still in training procedure(history file not full)
	public static void createNewPoly_training() {
		p = new Polynomial();
	}
	//create Polynomial when having calculated mean & standard deviation
	public static void createNewPoly(double[] mean, double[] Sdeviation, double[] threshold, double k) {
		p = new Polynomial(mean, Sdeviation, threshold, k);
	}
	//create new instruction table based on new polynomial
	public static void createNewInstTable() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		instTable = new InstTable();
		//instTable.printTable();
	}
	//create new Hpwd 
	public static void createNewHpwd() {
		bigPrime = primeGenerator(160); 	
		hpwd = hpwdGenerator(bigPrime);
	}
}

