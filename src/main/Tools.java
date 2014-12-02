package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tools {
	
	/**
	 * Turns the text into a list of words and removes all non-letter or 
	 * -number tokens.
	 * 
	 * @param input the text
	 * @return a list of words
	 */
	public static String[] tokenize(String input) {
		return input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
	}
	
	public static String[] AddBaseToNames(String basePath, String[] names) {
		String[] baseandNames = new String[names.length];
		for (int i = 0; i < names.length; i++) {
			baseandNames[i] = basePath + names[i];
		}
		return baseandNames;
	}
	
	/**
	 * Creates a scanner to read the file.
	 * 
	 * @param file to read
	 * @return the scanner
	 */
	public static Scanner createScanner(File file) {
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return scan;
	}
	
}
