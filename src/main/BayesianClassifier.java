package main;

import java.util.Arrays;

public class BayesianClassifier {
	
	public static void main(String[] args) {
		BayesianClassifier token = new BayesianClassifier();
		String str = "Hello, everyone. Do you like the new layout?";
		System.out.println("String:\t\t" + str);	
		String[] tokens = token.tokenizer(str);
		System.out.println("Tokenized:\t" + Arrays.toString(tokens));	
	}
}
