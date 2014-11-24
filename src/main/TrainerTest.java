package main;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TrainerTest {
	Classification male;
	Classification female;
	
	
	public void Trainer() {
		male = new Classification("male");
		female = new Classification("female");
	}
		
	public void trainer(Classification type, File file){
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNext()) {
				type.addTrainData(Tools.tokenize(scanner.nextLine()));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		String[] femaleFiles = new File("blogstrain/F").list();
		String[] maleFiles = new File("blogstrain/M").list();
		for(String file : femaleFiles) {
			trainer(female, new File(file));
		}
		for(String file : maleFiles) {
			trainer(male, new File(file));
		}

	}
	
	public static void main(String[] args) {
		TrainerTest test = new TrainerTest();
		test.run();
	}
}
