package main;

import java.util.Arrays;
import java.util.List;

public class BayesianClassifier {
	
	private List<Classification> classifications;
	
	public BayesianClassifier(List<Classification> classifications) {
		this.classifications = classifications;
	}
	
	public static void main(String[] args) {
		BayesianClassifier token = new BayesianClassifier(null);
		String str = "Hello, everyone. Do you like the new layout?";
		System.out.println("String:\t\t" + str);	
		String[] tokens = Tools.tokenize(str);
		System.out.println("Tokenized:\t" + Arrays.toString(tokens));	
	}
}

