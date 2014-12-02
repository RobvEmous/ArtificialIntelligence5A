package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * A classification of data in a certain type.
 * 
 * @author Rob
 */
public class Classification {
	
	private String type; //male or female etc.
	private Map<String, Integer> trainData;
	private int wordCounter = 0;
	
	public Classification(String type, Map<String, Integer> trainData) {
		this.type = type;
		this.trainData = trainData;
	}

	/**
	 * Creates a new classification.
	 * 
	 * @param type the type/name of the classification
	 */
	public Classification(String type) {
		this(type, new HashMap<String, Integer>());
	}
	
	/**
	 * Creates a classification object from already trained data.
	 * 
	 * @param savedFile the saved train data
	 */
	public Classification(String type, Scanner savedFile) {
		if (savedFile.hasNextLine()) {
			wordCounter = Integer.parseInt(savedFile.nextLine());
		} else {
			wordCounter = -1;
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
	
	/**
	 * Gets the type/name of the classification.
	 * 
	 * @return the type/name
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type/name of the classification.
	 * 
	 * @param type the type/name
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Gets the number of words this classifier has encountered while 
	 * training.
	 * 
	 * @return the number of words
	 */
	public int getWordCounter() {
		return wordCounter;
	}
	
	/**
	 * Gets the words and their number of occurrences combinations.
	 * 
	 * @return the combinations
	 */	
	public Map<String, Integer> getTrainData() {
		return trainData;
	}
	
	/**
	 * Sets the words and their number of occurrences combinations.
	 * 
	 * @param trainData the combinations
	 */	
	public void setTrainData(Map<String, Integer> trainData) {
		this.trainData = trainData;
	}
	
	/**
	 * Adds data to train the classification.
	 * 
	 * @param words the tokenized words
	 */
	public void addTrainData(String[] words) {
		for (String word : words) {
			if (word.equals("") || word.length() <= 2) {
				continue;
			}
			wordCounter++;
			if (trainData.containsKey(word)) {
				trainData.put(word, trainData.get(word) + 1);
			} else {
				trainData.put(word, 1);
			}
		}
	}
		
	/**
	 * Gets the ratio the list of words match with
	 * this classification.<br>
	 * A value of 0 means a total match, otherwise the value is lower.
	 * 
	 * @param words to test
	 * @param smoothFactor the factor to smooth the results with
	 * @return the match ratio
	 */
	public double matchWords(String[] words, double smoothFactor) {
		double chance = 0d;
		for (String word : words) {
			chance += Math.log10(matchWord(word, smoothFactor)) 
						/ Math.log10(2);
		}
		return chance;
	}
	
	/**
	 * The percentage the word matches the classification.
	 * 
	 * @param word to test
	 * @param smoothFactor the factor to smooth the results with
	 * @return the match percentage
	 */
	private double matchWord(String word, double smoothFactor) {
		int occurence;
		if (trainData.containsKey(word)) {
			occurence = trainData.get(word);
		} else {
			occurence = 0;
		}
		return (double)(occurence + smoothFactor) / 
				(double)(wordCounter + smoothFactor * trainData.size());
	}
	
	/**
	 * Removes all words which do not occur a certain amount of times.<br>
	 * Only call this after done training.
	 * 
	 * @param minOccurence the amount of times
	 */
	public void cleanData(int minOccurence) {
		List<String> toBeRemoved = new ArrayList<String>();
		for (String word : trainData.keySet()) {
			if (trainData.get(word) < minOccurence) {
				toBeRemoved.add(word);
			}
		}
		for (String word : toBeRemoved) {
			trainData.remove(word);
		}
	}
	
	/**
	 * Get the String representation of the classification (the train 
	 * data) to write it to a file.
	 * 
	 * @return the string to write
	 */
	public String toWriteableString() {
		StringBuilder sb = new StringBuilder();
		sb.append(wordCounter);
		sb.append(System.lineSeparator());
		for (String word : trainData.keySet()) {
			sb.append(word);
			sb.append(" ");
			sb.append(trainData.get(word));
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
