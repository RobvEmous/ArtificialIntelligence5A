package main;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Trainer {
	private static Classification male;
	private static Classification female;
	
	
	public Trainer() {
		male = new Classification("male");
		female = new Classification("female");
	}
		
	public void trainer(Classification type, File file){
		try {
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(file).useDelimiter("\\Z");
			String line = scan.next();
			type.addTrainData(Tools.tokenize(line));
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void createModels() {
		String femalePath = "blogstrain/F/";
		String malePath = "blogstrain/M/";
		String[] femaleFiles = new File(femalePath).list();
		String[] maleFiles = new File(malePath).list();
		for(String file : femaleFiles) {
			trainer(female, new File(femalePath + file));
		}
		for(String file : maleFiles) {
			trainer(male, new File(malePath + file));
		}
		male.cleanData(30);
		female.cleanData(30);
		try {
			PrintWriter maleWriter = new PrintWriter("male.txt", "UTF-8");
			PrintWriter femaleWriter = new PrintWriter("female.txt", "UTF-8");
			maleWriter.println(male.toWriteableString());
			femaleWriter.println(female.toWriteableString());
			maleWriter.close();
			femaleWriter.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		System.out.println(male.toWriteableString());
	}
	
	public static Scanner createScanner(File file) {
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return scan;
	}
	
	public static void main(String[] args) {
		Trainer test = new Trainer();
		File femaleModel = new File("female.txt");
		File maleModel = new File("male.txt");
		//if(!femaleModel.exists() && !maleModel.exists()) { 
			male = new Classification("male");
			female = new Classification("female");
			test.createModels();
		/*} else {
			male = new Classification("male", createScanner(maleModel));
			female = new Classification("female", createScanner(femaleModel));
		}*/
		;
		Classification[] classificationObjects = new Classification[]{male,female};
		new BayesianClassifier(classificationObjects);
		
	}
}
