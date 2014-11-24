package main;

import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Classification {
	
	private String type; //male or female etc.
	private Map<String, Integer> trainData;
	
	public Classification(String type, Map<String, Integer> trainData) {
		this.type = type;
		this.trainData = trainData;
	}

	
	public Classification(String type) {
		this(type, new HashMap<String, Integer>());
	}
	
	/**
	 * Creates a classification from already trained data.
	 * 
	 * @param savedFile the saved train data
	 */
	public Classification(Scanner savedFile) {
		if (savedFile.hasNextLine()) {
			type = savedFile.nextLine();
		} else {
			type = null;
		}
		trainData = new HashMap<String, Integer>();
		while (savedFile.hasNextLine()) {
			String[] wordAndOcc = savedFile.nextLine().split(" ");
			if (wordAndOcc.length == 2) {
				try {
					trainData.put(wordAndOcc[0], 
							Integer.parseInt(wordAndOcc[1]));
				} catch (NumberFormatException e) {}
			}
		}
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Map<String, Integer> getTrainData() {
		return trainData;
	}
	
	public void setTrainData(Map<String, Integer> trainData) {
		this.trainData = trainData;
	}
	
	/**
	 * Add data to train the classification.
	 * @param words
	 */
	public void addTrainData(String[] words) {
		for (String word : words) {
			if (trainData.containsKey(word)) {
				trainData.put(word, trainData.get(word) + 1);
			} else {
				trainData.put(word, 1);
			}
		}
	}
	
	/**
	 * Get the String representation of the train data to write it to a 
	 * file.
	 * 
	 * @return the string to write
	 */
	public String toWriteableString() {
		StringBuilder sb = new StringBuilder();
		for (String word : trainData.keySet()) {
			sb.append(word);
			sb.append(" ");
			sb.append(trainData.get(word));
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
