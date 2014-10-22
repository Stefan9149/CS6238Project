package edu.gatech.cs6238.project1;

import java.math.BigInteger;
import java.util.Random;

public class Polynomial {
	private static int exp;
	private static BigInteger[] coefficients;
	private static BigInteger Harden_password;
	private static BigInteger[][] Xlist;
	private static BigInteger[][] Ylist;
	
	//for initialization or training
	Polynomial() {
		exp = Initialization.getNumFeature();
		coefficients = new BigInteger[exp - 1];
		Harden_password = Initialization.getHpwd();
		//2*m points
		Xlist = new BigInteger[exp][2];
		Ylist = new BigInteger[exp][2];
		//create polynomial random coefficients 
		createCoefficients();
		producePolyPoints();
		//printPolyPoints();
		//System.out.println("\nRandom Polynomial Constants:");
		//printContantArray();
	}
	
	//for trained system
	Polynomial(double[] mean, double[] Sdeviation, double[] threshold, double k) {
		exp = Initialization.getNumFeature();
		coefficients = new BigInteger[exp - 1];
		Harden_password = Initialization.getHpwd();
		Xlist = new BigInteger[exp][2];
		Ylist = new BigInteger[exp][2];
		createCoefficients();
		producePolyPoints(mean, Sdeviation, threshold, k);
	}

	//produce random constants for Polynomial
	private static void createCoefficients() {  
		for(int i = 0; i < exp -1; i++) {
			coefficients[i] = new BigInteger(160, new Random());
		}
	}
	
	//print for test
	private void printContantArray() { 
		for(int i = 0; i < exp -1; i++) {
			System.out.print(coefficients[i].toString(16) + "\n");
		}
		System.out.println();
	}
	
	//for initialization or training
	private void producePolyPoints() {
		for(int i = 0; i < exp; i++) {
			Xlist[i][0] = Permutation(BigInteger.valueOf(2*(i+1)));
			Ylist[i][0] = valueOfPoly(Xlist[i][0]);
			Xlist[i][1] = Permutation(BigInteger.valueOf(2*(i+1) + 1));
			Ylist[i][1] = valueOfPoly(Xlist[i][1]);
		}
	}
	
	//for trained system
	private void producePolyPoints(double[] mean, double[] Sdeviation, double[] threshold, double k) {
		for(int i = 0; i < exp; i++) {
			if(Math.abs(mean[i] - threshold[i]) > (k * Sdeviation[i])) {
				if(mean[i] < threshold[i]) {
					//System.out.println("mean[i] < threshold[i]");
					Xlist[i][0] = Permutation(BigInteger.valueOf(2*(i+1)));
					Ylist[i][0] = valueOfPoly(Xlist[i][0]);
					Xlist[i][1] = Permutation(BigInteger.valueOf(2*(i+1) + 1));
					Ylist[i][1] = (new BigInteger(160, new Random())).mod(Initialization.getPrime());
					while(Ylist[i][1] == valueOfPoly(Xlist[i][1])) {
						Ylist[i][1] = (new BigInteger(160, new Random())).mod(Initialization.getPrime());
					}
				}
				else {
					//System.out.println("mean[i] > threshold[i]");
					Xlist[i][0] = Permutation(BigInteger.valueOf(2*(i+1)));
					Ylist[i][0] = (new BigInteger(160, new Random())).mod(Initialization.getPrime());
					while(Ylist[i][0] == valueOfPoly(Xlist[i][0])) {
						Ylist[i][0] = (new BigInteger(160, new Random())).mod(Initialization.getPrime());
					}
					Xlist[i][1] = Permutation(BigInteger.valueOf(2*(i+1) + 1));
					Ylist[i][1] = valueOfPoly(Xlist[i][1]);
				}
			}
			else {
				//System.out.println("still need to train");
				System.out.println("Features are not distringuishing, system still need training.");
				Xlist[i][0] = Permutation(BigInteger.valueOf(2*(i+1)));
				Ylist[i][0] = valueOfPoly(Xlist[i][0]);
				Xlist[i][1] = Permutation(BigInteger.valueOf(2*(i+1) + 1));
				Ylist[i][1] = valueOfPoly(Xlist[i][1]);
			}
		}
	}

	//print out points
	private void printPolyPoints() {
		System.out.println("\nShow produced 2m points:");
		for(int i = 0; i < exp; i++) {
			System.out.print(Xlist[i][0].toString(16) + "," + Ylist[i][0].toString(16) + "    " + Xlist[i][1].toString(16) + "," + Ylist[i][1].toString(16) + "\n");
		}
	}

	//calculate value of polynomial based on given "x" and "hpwd"
	public BigInteger valueOfPoly(BigInteger x) {
		BigInteger y = Harden_password;
		for(int i = 0; i < exp -1; i++) {
			BigInteger temp = x.modPow(BigInteger.valueOf(i+1), Initialization.getPrime()).multiply(coefficients[i]);
			y = y.add(temp);
		}
 		return y.mod(Initialization.getPrime());
	} 

	//Permutation
	public static BigInteger Permutation(BigInteger bv) {        
	    BigInteger fa = BigInteger.ONE;      
	    for(BigInteger inte = BigInteger.ONE; inte.compareTo(bv) <= 0; inte = inte.add(BigInteger.ONE)){            
	        fa = fa.multiply(inte);
	    }        
	    return fa.mod(Initialization.getPrime());
	}

	public BigInteger getPointsX(int i, int j) {
		return Xlist[i][j];
	}

	public BigInteger getPointsY(int i, int j) {
		return Ylist[i][j];
	}
}