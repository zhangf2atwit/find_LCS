package edu.wit.cs.comp2350;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Aligns strings in two text files by matching their longest common substring
 * 
 * Wentworth Institute of Technology COMP 2350 Assignment 7
 * 
 */

public class A7 {
	
	// TODO Document this method	
	/*
	 * findLCSdyn Function is to find the LCS,
	 * It uses a matrix of m by n size.
	 * m is the length of text1 and n is the length of text2. 
	 * A separate matrix, b, is used to store the values of diagonal, up, or left 
	 * which indicate where the current value was obtained from. 
	 */
	public static String[] findLCSdyn(String text1, String text2) {
		// TODO Implement this method
		// length + 1 , to account for row of 0s
		int m = text1.length() + 1;
		int n = text2.length() + 1;

		// populate all first row's elements with zero
		int[][] c = new int[m][n];
		int[][] b = new int[m][n];
		String[] aligned = new String[] { "", "" };
		for (int i = 0; i < m; i++) {
			c[i][0] = 0;
		}
		for (int j = 0; j < n; j++) {
			c[0][j] = 0;
		}
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
					c[i][j] = c[i - 1][j - 1] + 1;
					b[i][j] = 'd';                                          // d for diagonal
				} else if (c[i - 1][j] >= c[i][j - 1]) {
					c[i][j] = c[i - 1][j];
					b[i][j] = 'u';                                          // u for up
				} else {
					c[i][j] = c[i][j - 1];
					b[i][j] = 'l';                                           // l for left
				}
			}
		}

		longest = c[m - 1][n - 1];
		print(b, aligned, text1, text2);

		StringBuffer a0 = new StringBuffer(aligned[0]);
		a0.reverse();

		aligned[0] = a0.toString();
		StringBuffer a1 = new StringBuffer(aligned[1]);
		a1.reverse();

		aligned[1] = a1.toString();
		return aligned;
	}

	private static void print(int b[][], String[] aligned, String text1, String text2) {
		
		int i = text1.length();
		int j = text2.length();
		
		while (i >= 0 && j >= 0) {
			// if i is 0 and j isnt, then chars need to be added to a[1]
			if (i == 0) {
				while (j != 0) {
					aligned[0] += '-';
					aligned[1] += text2.charAt(j - 1);
					j--;
				}
				return;
			} else if (j == 0) {
				while (i != 0) {
					aligned[0] += text1.charAt(i - 1);
					aligned[1] += '-';
					i--;
				}
				return;
			} else {
				if (b[i][j] == 'd') {
					aligned[0] += text1.charAt(i - 1);
					aligned[1] += text2.charAt(j - 1);
					i--;
					j--;
				} else if (b[i][j] == 'u') {
					aligned[0] += text1.charAt(i - 1);
					aligned[1] += '-';
					i--;
				} else {
					aligned[0] += '-';
					aligned[1] += text2.charAt(j - 1);
					j--;
				}
			}
		}
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	private static int longest = -1;

	// recursive helper for DFS
	private static void dfs_solve(int i1, int i2, String s1, String s2, char[] out1, char[] out2, int score,
			int index) {

		if ((i1 >= s1.length()) && (i2 >= s2.length())) {
			if (score > longest) {
				out1[index] = '\0';
				out2[index] = '\0';
				longest = score;
				sol1 = String.valueOf(out1).substring(0, String.valueOf(out1).indexOf('\0'));
				sol2 = String.valueOf(out2).substring(0, String.valueOf(out2).indexOf('\0'));
			}
		} else if ((i1 >= s1.length()) && (i2 < s2.length())) { // at the end of first string
			out1[index] = '-';
			out2[index] = s2.charAt(i2);
			dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index + 1);
		} else if ((i1 < s1.length()) && (i2 >= s2.length())) { // at the end of second string
			out1[index] = s1.charAt(i1);
			out2[index] = '-';
			dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index + 1);
		} else {
			if (s1.charAt(i1) == s2.charAt(i2)) { // matching next character
				out1[index] = s1.charAt(i1);
				out2[index] = s2.charAt(i2);
				dfs_solve(i1 + 1, i2 + 1, s1, s2, out1, out2, score + 1, index + 1);
			}

			out1[index] = '-';
			out2[index] = s2.charAt(i2);
			dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index + 1);

			out1[index] = s1.charAt(i1);
			out2[index] = '-';
			dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index + 1);
		}

	}

	// Used for DFS solution
	private static String sol1, sol2;

	// recursively searches for longest substring, checking all possible alignments
	public static String[] findLCSdfs(String text1, String text2) {
		int max_len = text1.length() + text2.length() + 1;
		char[] out1 = new char[max_len];
		char[] out2 = new char[max_len];

		dfs_solve(0, 0, text1, text2, out1, out2, 0, 0);

		String[] ret = new String[2];
		ret[0] = sol1;
		ret[1] = sol2;
		return ret;
	}

	// returns the length of the longest string
	public static int getLongest() {
		return longest;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1, file2, text1 = "", text2 = "";
		System.out.printf(
				"Enter <text1> <text2> <algorithm>, ([dfs] - depth first search, [dyn] - dynamic programming): ");
		System.out.printf("(e.g: text/a.txt text/b.txt dfs)\n");
		file1 = s.next();
		file2 = s.next();

		try {
			text1 = new String(Files.readAllBytes(Paths.get(file1)));
			text2 = new String(Files.readAllBytes(Paths.get(file2)));
		} catch (IOException e) {
			System.err.println("Cannot open files " + file1 + " and " + file2 + ". Exiting.");
			System.exit(0);
		}

		String algo = s.next();
		String[] result = { "" };

		switch (algo) {
		case "dfs":
			result = findLCSdfs(text1, text2);
			break;
		case "dyn":
			result = findLCSdyn(text1, text2);
			break;
		default:
			System.out.println("Invalid algorithm");
			System.exit(0);
			break;
		}

		s.close();

		System.out.printf("Overlapping characters: %d\nLongest string alignment:\n%s\n\n%s\n", longest, result[0],
				result[1]);
	}
}
