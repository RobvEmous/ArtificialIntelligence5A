package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BayesianClassifier {
	
	private Classification male;
	private Classification female;
	
	public BayesianClassifier(Classification[] classifications) {
		this.male = classifications[0];
		this.female = classifications[1];
		test();
	}
	
	public String classified(File file){
		String toReturn = null;
		try {
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(file).useDelimiter("\\Z");
			String line = scan.next();
			//System.out.println(line);
			double chanceMale = male.matchWords(Tools.tokenize(line), 0.001);
			double chanceFemale = female.matchWords(Tools.tokenize(line), 0.001);
			System.out.println("Male: " + chanceMale + " Female: " + chanceFemale);
			if (chanceMale > chanceFemale) {
				toReturn = "Male";
			} else {
				toReturn = "Female";
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	public void test() {
		String femalePath = "blogstest/F/";
		String malePath = "blogstest/M/";
		String[] femaleFiles = new File(femalePath).list();
		String[] maleFiles = new File(malePath).list();
		ArrayList<String> toReturn = new ArrayList<String>();
		for(String file : femaleFiles) {
			//toReturn.add(classified(new File(femalePath + file)) + " " + file);
			toReturn.add(classified(new File(femalePath + file)));
		}
		for(String file : maleFiles) {
			toReturn.add(classified(new File(malePath + file)));
		}
		System.out.println(toReturn);
	}
	
	/*public static void main(String[] args) {
		BayesianClassifier token = new BayesianClassifier(null);
		String str = "Hello, everyone. Do you like the new layout?";
		System.out.println("String:\t\t" + str);	
		String[] tokens = Tools.tokenize(str);
		System.out.println("Tokenized:\t" + Arrays.toString(tokens));	
	}*/
}

