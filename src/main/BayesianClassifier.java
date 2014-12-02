package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A naïve Bayesian classifier implementation.
 * 
 * @author Rob & Mark
 */
public class BayesianClassifier {
	
	private List<Classification> classifications;
	private double smoothFactor;
	
	public BayesianClassifier(Classification[] classifications, double smoothFactor) {
		this.classifications = new ArrayList<Classification>();
		for (Classification classification : classifications) {
			this.classifications.add(classification);
		}
		this.smoothFactor = smoothFactor;
	}
	
	public BayesianClassifier(List<Classification> classifications, double smoothFactor) {
		this.classifications = new ArrayList<Classification>(classifications);
		this.smoothFactor = smoothFactor;
	}
	
	public List<Classification> getClassifications() {
		return classifications;
	}
	
	public void setClassifications(List<Classification> classifications) {
		this.classifications = classifications;
	}
	
	public double getSmoothFactor() {
		return smoothFactor;
	}
	
	public void setSmoothFactor(double smoothFactor) {
		this.smoothFactor = smoothFactor;
	}
	
	public String classify(String fileName) {
		return classify(new File(fileName));
	}
	
	public String classify(File file) {
		String line;
		try {
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(file).useDelimiter("\\Z");
			line = scan.next();
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		double bestRatio = -Double.MAX_VALUE;
		String bestTypeMatch = null;
		for (Classification classification : classifications) {
			double tempRatio = classification.matchWords(
					Tools.tokenize(line), smoothFactor);
			if (tempRatio > bestRatio) {
				bestTypeMatch = classification.getType();
				bestRatio = tempRatio;
			}
		}		
		return bestTypeMatch;
	}
	
}
