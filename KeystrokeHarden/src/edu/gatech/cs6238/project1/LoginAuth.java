package edu.gatech.cs6238.project1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class LoginAuth {
	private final static double[] threshold = {1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2.0,2.1,2.2,2.3,2.4,2.5};
	private static String username;
	private static double[] featureVector;
	private static int num_feature;
	private static char[] password; //TODO: replace with login password
	private static BigInteger Hpwd_prime;
	private static BigInteger[] pickedInstVector;
	private static BigInteger[] Xlist;
	private static BigInteger[] Ylist;
	private static BigInteger[] lambda;

	LoginAuth(String name, double[] currentLoginFeature, char[] pwd) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		Configuration(name, currentLoginFeature, pwd);
		PointsOfPolyRecovery();
		HardenPwdRecovery();
		//System.out.println("\nRecovered Harden Password:");
		//System.out.println(Hpwd_prime.toString(16));
		//System.out.println(Initialization.getHpwd().toString(16));
	}

	private void Configuration(String name, double[] feature, char[] pwd) {
		num_feature = Initialization.getNumFeature();
		password = pwd;
		//password = Initialization.getPwd();
		username = name;
		featureVector = feature;
		//alfa OR beta picked from Instruction Table
		pickedInstVector = new BigInteger[num_feature];
		//Points X&Y
		Xlist = new BigInteger[num_feature];
		Ylist = new BigInteger[num_feature];		
		lambda = new BigInteger[num_feature];
		//Output Information
		System.out.println("\nAuthenticating.....");
		//System.out.println("\nUser Name:" + username);
		//System.out.print("\nCurrent Login Features:");
		/*for(int i = 0; i < num_feature; i++) {
			System.out.print(featureVector[i]+",");
		}*/
	}

	//Recover m(=15) Points from extracted alfa OR beta
	private void PointsOfPolyRecovery() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		//System.out.println("\n\nPicked alfa OR beta From Instruction Table:\n");	
		PickFromInstTable();
		//printPickedInst();
		//System.out.println("\n\nRecovered Points:\n");	
		//printRecoveredPoints();
	}

	private void HardenPwdRecovery() {
		Hpwd_prime = BigInteger.ZERO;

		for(int i = 0; i < num_feature; i++) {
			lambda[i] = BigInteger.ONE;
			for(int j = 0; j < num_feature; j++) {
				if(j!=i) {
					lambda[i] = lambda[i].multiply(Xlist[j].multiply(((Xlist[j].subtract(Xlist[i]))).modInverse(Initialization.getPrime())));
				}
			}
			//System.out.println(lambda[i].toString(16));
		}
		//Hpwd_prime = Hpwd_prime.add(Ylist[i].multiply(BigInteger.valueOf((int)lambda[i])).mod(Initialization.getPrime()));
		for(int i = 0; i < num_feature; i++) {
			Hpwd_prime = Hpwd_prime.add(Ylist[i].multiply(lambda[i]));
		}
		Hpwd_prime = Hpwd_prime.mod(Initialization.getPrime());
		//System.out.println("Hpwd:" + Hpwd_prime.toString(16));
	}

	//Extract alfa OR beta 
	private void PickFromInstTable() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {

		//System.out.println("\n\nExtracted m points:");
		for(int i = 0; i < num_feature; i++) {
			if(featureVector[i] < threshold[i]) {
				pickedInstVector[i] = InstTable.getElement(i, 0); //f < t, pick alfa
				Xlist[i] = Polynomial.Permutation(BigInteger.valueOf(2*(i+1)));
				Ylist[i] = pickedInstVector[i].subtract(HMACSHA1_FunctionG.HMAC(Integer.valueOf(2 * (i+1)).toString(), new String(password)));
				//System.out.println(Xlist[i].toString(16) + ","+Ylist[i].toString(16));
			}
			else {
				pickedInstVector[i] = InstTable.getElement(i, 1); // f >= t, pick beta
				Xlist[i] = Polynomial.Permutation(BigInteger.valueOf(2*(i+1) + 1));
				Ylist[i] = pickedInstVector[i].subtract(HMACSHA1_FunctionG.HMAC(Integer.valueOf(2 * (i+1) + 1).toString(), new String(password)));
				//System.out.println(Xlist[i].toString(16) + ","+Ylist[i].toString(16));
			}
		}
	}

	//Print
	public void printPickedInst() {
		for(int i = 0; i < num_feature; i++) {
			System.out.println(pickedInstVector[i].toString(16));
		}
	}

	public void printRecoveredPoints() {
		for(int i = 0; i < num_feature; i++) {
			System.out.println(Xlist[i].toString(16) + ","+Ylist[i].toString(16));
		}
	}

	public static BigInteger getHpwdPrime() {
		return Hpwd_prime;
	}
	
	public static double[] getT() {
		return threshold;
	}
}