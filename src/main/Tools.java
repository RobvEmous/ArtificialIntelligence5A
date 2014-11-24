package main;

public class Tools {
	
	public static String[] tokenize(String input) {
		return input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
	}
	
}
