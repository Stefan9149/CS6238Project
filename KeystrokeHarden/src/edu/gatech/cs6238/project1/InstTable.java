package edu.gatech.cs6238.project1;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class InstTable {
	private static int num_Row;
	private static char[] password;
	private static BigInteger[][] table;
	private static Polynomial poly;

	InstTable() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		num_Row = Initialization.getNumFeature();
		password = Initialization.getPwd();
		poly = Initialization.getPoly();
		table = new BigInteger[num_Row][2];
		setTable();
	}

	//calculate and store values of alfa & beta, using y value of points and HMACSHA1 of Permutation(2i OR 2i+1)
	private static void setTable() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		for(int i = 0; i < num_Row; i++) {
			table[i][0] = (poly.getPointsY(i, 0).add(HMACSHA1_FunctionG.HMAC(Integer.valueOf(2 * (i + 1)).toString(), new String(password))).mod(Initialization.getPrime()));
			//System.out.print(poly.valueOfPoly(BigInteger.valueOf(2 * (i + 1))).toString(16) + "   ");
			table[i][1] = (poly.getPointsY(i, 1).add(HMACSHA1_FunctionG.HMAC(Integer.valueOf(2 * (i + 1) + 1).toString(), new String(password))).mod(Initialization.getPrime()));
			//System.out.print(poly.valueOfPoly(BigInteger.valueOf(2 * (i + 1) + 1)).toString(16) + "\n");
		}
	}

	//print for test
	public void printTable() {
		System.out.println("\nInstruction Table:");
		for(int i = 0; i < num_Row; i++) {
			System.out.println(table[i][0].toString(16) + "   " + table[i][1].toString(16));
		}
	}

	public static BigInteger getElement(int i, int j) {
		return table[i][j];
	}

}