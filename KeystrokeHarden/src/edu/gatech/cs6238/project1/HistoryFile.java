package edu.gatech.cs6238.project1;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;

public class HistoryFile {
	private static int size;
	private static int numFeature;
	private static double[][]table;
	private static double padding;
	private static int count = 0;
	private static ArrayList<String> staticCipherText;
	private static double[] mean;
	private static double[] Sdeviation;
	private static double[] threshold;
	private static double k;
	
	HistoryFile() {
		size = Initialization.getHistorySize();
		numFeature = Initialization.getNumFeature();
		createTable();
	}
	
	private static void createTable() {
		table = new double[size][numFeature];
		padding = (double) -1;
				//new BigInteger(160, new Random());
		
		for(int i = 0; i < size; i++) {
			for (int j = 0; j < numFeature; j++) {
				table[i][j] = padding;
				//System.out.print(table[i][j]);
			}
			//System.out.println();
		}
	}
	
	public void encryptTable(BigInteger hpwd) {
		staticCipherText = Crypt.encHFile(table, hpwd, Initialization.getUsername());
	}
	public boolean decryptTable(ArrayList<String> ciphertext, BigInteger hpwd, String name, int number) throws IOException {
		return Crypt.decHFile(ciphertext, hpwd, name, number);
	}
	
	public void updateHFile(double[] test) {
		if (test.length == 15 && table.length == 8 && table[0].length == 15) {
			//int count = 0;
			while (count < 8) {
				table[count] = test;
				count++;
				break;
			}
			if (count == 8) {
				count = 0;
			}
			
		} else {
			System.out.println("invalid test");
		}
	}
	
	public void printHistory(){
		for(int i = 0; i < size; i++) {
			for (int j = 0; j < numFeature; j++) {
				//table[i][j] = padding;
				System.out.print(table[i][j]+"  ");
			}
			System.out.println();
		}
	}
	
	
	public double[] getAve() {
		double[] Ave = new double[15];
		if (table.length == 8 && table[0].length == 15) {
			if (isFull()) {
				for (int c = 0; c < 15; c++) {
					double total = 0.0;
					for (int r = 0; r < 8; r++) {
						total += table[r][c];
						//System.out.println("total is"+total);
					}
					Ave[c] = (total / 8);
					//System.out.print(Ave[c] + " ");
				}
				//System.out.println();
			}
		}
		return Ave;
	}

	public double[] getVar(double[] Ave) {
		double[] Var = new double[15];
		if (table.length == 8 && table[0].length == 15) {
			if (isFull()) {
				for (int c = 0; c < 15; c++) {
					double total = 0;
					for (int r = 0; r < 8; r++) {
						total += (Ave[c] - table[r][c])
								* (Ave[c] - table[r][c]);
					}
					Var[c] = (total / 8);
					Var[c] = Math.sqrt(Var[c]);
					//System.out.print(Var[c] + " ");
				}
				//System.out.println();
			}
		}
		return Var;
	}
	
	public static double[][] getTable() {
		return table;
	}

	public static void setTable(double[][] table) {
		HistoryFile.table = table;
	}

	public static double getPadding() {
		return padding;
	}
	
	public static boolean isFull() {
		if(table[7][0] != HistoryFile.getPadding()) {
			return true;
		}
		else return false;
	}
	
	public ArrayList<String> getCipherText() {
		return staticCipherText;
	}
	
	public void Update() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		double[] mean = getAve();
		double[] Sdeviation = getVar(mean);
		threshold = LoginAuth.getT();
		k = 0.9;
		Initialization.createNewHpwd();
		Initialization.createNewPoly(mean, Sdeviation, threshold, k);
		Initialization.createNewInstTable();
	}
	
	public void Update_training() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		Initialization.createNewHpwd();
		Initialization.createNewPoly_training();
		Initialization.createNewInstTable();
	}
}
