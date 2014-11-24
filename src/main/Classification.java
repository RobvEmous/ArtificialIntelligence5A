package main;

import java.util.Map;

public class Classification {
	
	private String type; //male or female etc.
	private Map<String, Integer> trainData;
	
	public Classification(String type, Map<String, Integer> trainData) {
		this.type = type;
		this.trainData = trainData;
	}
	
	public Classification(String type) {
		this(type, null);
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
	
	public void addTrainData(String[] words) {
		for (String word : words) {
			if (trainData.containsKey(word)) {
				trainData.put(word, trainData.get(word) + 1);
			} else {
				trainData.put(word, 1);
			}
		}
	}
	
	
}
